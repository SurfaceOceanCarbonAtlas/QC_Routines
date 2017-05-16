package uk.ac.exeter.QCRoutines.routines.HighDelta;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.config.RoutinesConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

/**
 * QC Routine to check whether a given data value is changing at an
 * unacceptably fast rate.
 * 
 * <p>
 *   There are limits on how quickly a given measured value should change over time. For example,
 *   sea surface temperatures should not change by 20°C between measurements, given the normal operating speed
 *   of a ship. This routine checks the values between consecutive records and calculates the delta
 *   in terms of units per minute. If this delta exceeds the configured threshold, a message will be generated. 
 * </p>
 * 
 * @author Steve Jones
 *
 */
public class HighDeltaRoutine extends Routine {

	/**
	 * The name of the columns whose values are to be checked.
	 */
	private String columnName;
	
	/**
	 * The maximum delta between values, in units per minute
	 */
	private double maxDelta;
	
	@Override
	protected void processParameters(List<String> parameters) throws RoutineException {

		if (parameters.size() != 2) {
			throw new RoutineException("Incorrect number of parameters. Must be <columnName>,<maxDelta>");
		}
		
		columnName = parameters.get(0);
		if (!columnConfig.hasColumn(columnName)) {
			throw new RoutineException("Column '" + columnName + "' does not exist");
		}

		ColumnConfigItem column = columnConfig.getColumnConfig(columnName);
		if (!column.isNumeric()) {
			throw new RoutineException("Column '" + columnName + "' must be numeric");
		}

		try {
			maxDelta = Double.parseDouble(parameters.get(1));
		} catch (NumberFormatException e) {
			throw new RoutineException("Max delta parameter must be numeric");
		}
		
		if (maxDelta <= 0) {
			throw new RoutineException("Max duration must be greater than zero");
		}
	}

	@Override
	protected void doRecordProcessing(List<DataRecord> records) throws RoutineException {
		double lastValue = RoutinesConfig.NO_VALUE;
		DateTime lastTime = null;
		
		for (DataRecord record : records) {
		
			try {
				if (lastValue == RoutinesConfig.NO_VALUE) {
					String lastValueString = record.getValue(columnName);
					if (null != lastValueString) {
						lastValue = Double.parseDouble(lastValueString);
						lastTime = record.getTime();
					}
				} else {
					
					// Calculate the change between this record and the previous one
					String thisStringValue = record.getValue(columnName);
					if (null != thisStringValue) {
						double thisValue = Double.parseDouble(thisStringValue);
						DateTime thisTime = record.getTime();
						if (thisValue != RoutinesConfig.NO_VALUE) {
							
							double minutesDifference = Seconds.secondsBetween(lastTime, thisTime).getSeconds() / 60.0;
							double valueDelta = Math.abs(thisValue - lastValue);
							
							double deltaPerMinute = valueDelta / minutesDifference;
							if (deltaPerMinute > maxDelta) {
								try {
									addMessage(new HighDeltaMessage(record.getLineNumber(), record.getColumn(columnName), deltaPerMinute, maxDelta), record);
								} catch (DataRecordException e) {
									throw new RoutineException ("Error while adding message", e);
								}
							}
						}
					
					
						lastValue = thisValue;
						lastTime = thisTime;
					}
				}
			} catch (NumberFormatException e) {
				throw new RoutineException("Cannot check non-numeric values", e);
			} catch (NoSuchColumnException e) {
				throw new RoutineException("Could not find column '" + columnName + "' in record", e);
			} catch (DataRecordException e) {
				throw new RoutineException("Error while retrieving data", e);
			} catch (MessageException e) {
				throw new RoutineException("Error while generating QC message", e);
			}
		}
	}
}
