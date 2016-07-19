package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

public class RangeCheckRoutine extends Routine {

	String columnName;

	double questionableMin = 0.0;
	
	double questionableMax = 0.0;
	
	double badMin = 0.0;
	
	double badMax = 0.0;
	
	boolean hasQuestionableRange = false;
	
	boolean hasBadRange = false;
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		if (parameters.size() != 5) {
			throw new RoutineException("Incorrect number of parameters. Must be <columnName>,<questionable_range_min>,<questionable_range_max>,<bad_range_min>,<bad_range_max>");
		}
		
		columnName = parameters.get(0);
		if (!columnConfig.hasColumn(columnName)) {
			throw new RoutineException("Column '" + columnName + "' does not exist");
		}
		
		ColumnConfigItem column = columnConfig.getColumnConfig(columnName);
		if (!column.isNumeric()) {
			throw new RoutineException("Column '" + columnName + "' must be numeric");
		}
		
		if (parameters.get(1).trim().length() > 0 || parameters.get(2).trim().length() > 0) {
			hasQuestionableRange = true;
			try {
				questionableMin = Double.parseDouble(parameters.get(1));
				questionableMax = Double.parseDouble(parameters.get(2));
			} catch(NumberFormatException e) {
				throw new RoutineException("Questionable range parameters must be numeric", e);
			}
		}
		
		if (parameters.get(3).trim().length() > 0 || parameters.get(4).trim().length() > 0) {
			hasBadRange = true;
			try {
				badMin = Double.parseDouble(parameters.get(3));
				badMax = Double.parseDouble(parameters.get(4));
			} catch(NumberFormatException e) {
				throw new RoutineException("Bad range parameters must be numeric", e);
			}
		}
		
		if (hasQuestionableRange && hasBadRange) {
			if (badMin > questionableMin || badMax < questionableMax) {
				throw new RoutineException("Bad range must be larger than questionable range");
			}
		}
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {

		for (DataRecord record : records) {
			try {
				double value = Double.parseDouble(record.getValue(columnName));
				if (!Double.isNaN(value)) {
					if (hasBadRange && (value < badMin || value > badMax)) {
						addMessage(new RangeCheckMessage(record.getLineNumber(), record.getColumn(columnName), Flag.BAD, value, badMin, badMax), record);
					} else if (hasQuestionableRange && (value < questionableMin || value > questionableMax)) {
						addMessage(new RangeCheckMessage(record.getLineNumber(), record.getColumn(columnName), Flag.QUESTIONABLE, value, questionableMin, questionableMax), record);
					}
				}
			} catch(NumberFormatException e) {
				throw new RoutineException("All record values must be numeric", e);
			} catch (NoSuchColumnException e) {
				throw new RoutineException("Could not find column '" + columnName + "' in record", e);
			} catch (DataRecordException e) {
				throw new RoutineException ("Error while adding message", e);
			} catch (MessageException e) {
				throw new RoutineException("Error while generating QC message", e);
			}
		}
	}
}
