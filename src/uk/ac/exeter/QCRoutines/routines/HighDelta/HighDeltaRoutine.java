package uk.ac.exeter.QCRoutines.routines.HighDelta;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

public class HighDeltaRoutine extends Routine {

	private static final double NO_VALUE = -99999.9;

	private String columnName;
	
	private double maxDelta;
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {

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
	public void processRecords(List<DataRecord> records) throws RoutineException {
		double lastValue = NO_VALUE;
		DateTime lastTime = null;
		
		for (DataRecord record : records) {
		
			try {
				if (lastValue == NO_VALUE) {
					lastValue = Double.parseDouble(record.getValue(columnName));
					lastTime = record.getTime();
				} else {
					
					// Calculate the change between this record and the previous one
					double thisValue = Double.parseDouble(record.getValue(columnName));
					DateTime thisTime = record.getTime();
					
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
					
					
					lastValue = thisValue;
					lastTime = thisTime;
				}
			} catch (NumberFormatException e) {
				throw new RoutineException("Cannot check non-numeric values", e);
			} catch (NoSuchColumnException e) {
				throw new RoutineException("Could not find column '" + columnName + "' in record", e);
			}
		}
	}
}
