package uk.ac.exeter.QCRoutines.routines.Outlier;

import java.util.ArrayList;
import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.config.RoutinesConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

/**
 * Routine to detect outliers.
 *
 * <p>
 *   The routine is configured with a column name and a standard deviation limit.
 *   The mean and standard deviation of all the values in that column are calculated.
 *   Each record is assessed to determine how many standard deviations away from the
 *   mean it is. If it is further away than the configured standard deviation limit,
 *   a message will be raised.
 * </p>
 * <p>
 * 	 All outliers are given a {@link Flag#BAD} status.
 * </p>
 * @author Steve Jones
 *
 */
public class OutlierRoutine extends Routine {

	/**
	 * The name of the column whose values are to be checked
	 */
	private String columnName;

	/**
	 * The maximum number of standard deviations away from the mean a
	 * value can be before it is considered an outlier.
	 */
	private double stdevLimit;

	@Override
	protected void processParameters(List<String> parameters) throws RoutineException {
		if (parameters.size() != 2) {
			throw new RoutineException("Incorrect number of parameters. Must be <columnName>,<stdevLimit>");
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
			stdevLimit = Double.parseDouble(parameters.get(1));
		} catch (NumberFormatException e) {
			throw new RoutineException("Standard deviation limit parameter must be numeric");
		}

		if (stdevLimit <= 0) {
			throw new RoutineException("Standard deviation limit must be greater than zero");
		}

	}

	@Override
	public void doRecordProcessing(List<DataRecord> records) throws RoutineException {

		int valueCount = 0;
		List<RecordValue> recordValues = new ArrayList<RecordValue>();
		double mean = 0.0;
		double stdev = 0.0;

		for (DataRecord record : records) {
			try {
				String valueString = record.getValue(columnName);
				if (null != valueString) {
					double value = Double.parseDouble(valueString);
					if (!Double.isNaN(value) && value != RoutinesConfig.NO_VALUE) {
						valueCount++;
						recordValues.add(new RecordValue(record, value));

						if (valueCount == 1) {
							mean = value;
						} else {
							double d = value - mean;
							stdev += (valueCount - 1)*d*d/valueCount;
							mean += d/valueCount;
						}
					}
				}
			} catch(NumberFormatException e) {
				throw new RoutineException("All record values must be numeric", e);
			} catch (NoSuchColumnException e) {
				throw new RoutineException("Could not find column '" + columnName + "' in record", e);
			}
		}

		// Finalise the stdev calculation
		stdev = Math.sqrt(stdev / valueCount);

		// Check all values to see if they're outside the limit
		for (RecordValue recordValue : recordValues) {
			double diffFromMean = Math.abs(recordValue.value - mean);

			if (diffFromMean > (stdev * stdevLimit)) {
				try {
					DataRecord record = recordValue.record;
					addMessage(new OutlierMessage(record.getLineNumber(), record.getColumn(columnName), stdev, stdevLimit), record);
				} catch (DataRecordException e) {
					throw new RoutineException ("Error while adding message", e);
				} catch (MessageException e) {
					throw new RoutineException("Error while generating QC message", e);
				}
			}
		}

	}

	/**
	 * A simple object to store each record and its column value, to simplify
	 * checking after the mean and stdev have been calculated.
	 */
	private class RecordValue {

		/**
		 * The data record
		 */
		private DataRecord record;

		/**
		 * The extracted value
		 */
		private double value;

		/**
		 * Simple constructor
		 * @param record The data record
		 * @param value The extracted value
		 */
		private RecordValue(DataRecord record, double value) {
			this.record = record;
			this.value = value;
		}
	}

  @Override
  public Class<? extends Message> getMessageClass() {
    return OutlierMessage.class;
  }
}
