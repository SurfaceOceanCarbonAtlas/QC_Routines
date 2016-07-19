package uk.ac.exeter.QCRoutines.routines.FixedValue;

import java.text.ParseException;
import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;
import uk.ac.exeter.QCRoutines.util.RoutineUtils;

public class FixedValueRoutine extends Routine {

	private String columnName;
	
	private boolean ignoreMissing = false;
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		if (parameters.size() != 2) {
			throw new RoutineException("Incorrect number of parameters. Must be <columnName>,<ignoreMissing?>");
		}
		
		columnName = parameters.get(0);
		if (!columnConfig.hasColumn(columnName)) {
			throw new RoutineException("Column '" + columnName + "' does not exist");
		}

		try {
			ignoreMissing = RoutineUtils.parseBoolean(parameters.get(1));
		} catch (ParseException e) {
			throw new RoutineException("ignoreMissing parameter is not a recognised boolean value");
		}
		
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {
		
		try {
			String firstValue = records.get(0).getValue(columnName);
			int firstRecord = 0;
			
			
			if (ignoreMissing) {
				while (RoutineUtils.isEmpty(firstValue) && firstRecord < records.size()) {
					firstRecord++;
					firstValue = records.get(firstRecord).getValue(columnName);
				}
			}
			
			for (int recordIndex = firstRecord + 1; recordIndex < records.size(); recordIndex++) {
				DataRecord record = records.get(recordIndex);
				String recordValue = record.getValue(columnName);
				
				if (!(ignoreMissing && RoutineUtils.isEmpty(recordValue))) {
					if (!recordValue.equals(firstValue)) {
						try {
							addMessage(new ValueNotFixedMessage(record.getLineNumber(), record.getColumn(columnName), Flag.BAD, firstValue), record);
						} catch (DataRecordException e) {
							throw new RoutineException("Error while adding message", e);
						} catch (MessageException e) {
							throw new RoutineException("Error while generating QC message", e);
						}
					}
				}
			}
			
			
			
		} catch (NoSuchColumnException e) {
			throw new RoutineException("Could not find column '" + columnName + "' in record", e);
		}
		
		
		
	}

}
