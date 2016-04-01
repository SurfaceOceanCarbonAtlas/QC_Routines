package uk.ac.exeter.QCRoutines.routines.Outlier;

import java.util.ArrayList;
import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

public class OutlierRoutine extends Routine {

	String columnName;
	
	double stdevLimit;
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
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
	public void processRecords(List<DataRecord> records) throws RoutineException {
		
		int valueCount = 0;
		List<RecordValue> recordValues = new ArrayList<RecordValue>();
		double mean = 0.0;
		double stdev = 0.0;
		
		for (DataRecord record : records) {
			try {
				double value = Double.parseDouble(record.getValue(columnName));
				if (!Double.isNaN(value)) {
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
				}
			}
		}

	}
	
	/**
	 * A simple object to store each record and its column value, to simplify
	 * checking after the mean and stdev have been calculated.
	 */
	private class RecordValue {
		private DataRecord record;
		private double value;
		
		private RecordValue(DataRecord record, double value) {
			this.record = record;
			this.value = value;
		}
	}
}
