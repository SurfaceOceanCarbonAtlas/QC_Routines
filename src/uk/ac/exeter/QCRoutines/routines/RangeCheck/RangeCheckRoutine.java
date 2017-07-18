package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.config.RoutinesConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

/**
 * Routine for checking whether values are within an allowable range.
 * <p>
 *   Three ranges are allowed - the narrowest range will trigger a {@link Flag#QUESTIONABLE},
 *   the middle range will trigger a {@link Flag#BAD}, and the widest range will trigger a
 *   {@link Flag#FATAL}.
 * </p>
 * @author Steve Jones
 *
 */
public class RangeCheckRoutine extends Routine {

	/**
	 * The name of the column whose values are to be checked
	 */
	private String columnName;

	/**
	 * The minimum value of the range that will trigger a {@link Flag#QUESTIONABLE}.
	 */
	private double questionableMin = 0.0;
	
	/**
	 * The maximum value of the range that will trigger a {@link Flag#QUESTIONABLE}.
	 */
	private double questionableMax = 0.0;
	
	/**
	 * The minimum value of the range that will trigger a {@link Flag#BAD}.
	 */
	private double badMin = 0.0;
	
	/**
	 * The maximum value of the range that will trigger a {@link Flag#BAD}.
	 */
	private double badMax = 0.0;
	
	/**
	 * The minimum value of the range that will trigger a {@link Flag#FATAL}.
	 */
	private double fatalMin = 0.0;
	
	/**
	 * The maximum value of the range that will trigger a {@link Flag#FATAL}.
	 */
	private double fatalMax = 0.0;
	
	/**
	 * Indicates whether or not this range checker has a Questionable range configured
	 */
	private boolean hasQuestionableRange = false;
	
	/**
	 * Indicates whether or not this range checker has a Bad range configured
	 */
	private boolean hasBadRange = false;
	
	/**
	 * Indicates whether or not this range checker has a Fatal range configured
	 */
	private boolean hasFatalRange = false;
	
	@Override
	public void processParameters(List<String> parameters) throws RoutineException {
		if (parameters.size() != 5 && parameters.size() != 7) {
			throw new RoutineException("Incorrect number of parameters. Must be <columnName>,<questionable_range_min>,<questionable_range_max>,<bad_range_min>,<bad_range_max>[,<fatal_range_min>,<fatal_range_max>]");
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
		
		if (parameters.size() == 7) {
			if (parameters.get(5).trim().length() > 0 || parameters.get(6).trim().length() > 0) {
				hasFatalRange = true;
				try {
					fatalMin = Double.parseDouble(parameters.get(3));
					fatalMax = Double.parseDouble(parameters.get(4));
				} catch(NumberFormatException e) {
					throw new RoutineException("Fatal range parameters must be numeric", e);
				}
			}
			
			if (hasBadRange && hasFatalRange) {
				if (fatalMin > badMin || fatalMax < fatalMax) {
					throw new RoutineException("Fatal range must be larger than bad range");
				}
			}
		}
	}

	@Override
	protected void doRecordProcessing(List<DataRecord> records) throws RoutineException {

		for (DataRecord record : records) {
			try {
				String valueString = record.getValue(columnName);
				
				if (null != valueString) {
					double value = Double.parseDouble(valueString);
					if (!Double.isNaN(value) && value != RoutinesConfig.NO_VALUE) {
						if (hasFatalRange && (value < fatalMin || value > fatalMax)) {
							addMessage(new RangeCheckMessage(record.getLineNumber(), record.getColumn(columnName), Flag.FATAL, value, fatalMin, fatalMax), record);
						} else if (hasBadRange && (value < badMin || value > badMax)) {
							addMessage(new RangeCheckMessage(record.getLineNumber(), record.getColumn(columnName), Flag.BAD, value, badMin, badMax), record);
						} else if (hasQuestionableRange && (value < questionableMin || value > questionableMax)) {
							addMessage(new RangeCheckMessage(record.getLineNumber(), record.getColumn(columnName), Flag.QUESTIONABLE, value, questionableMin, questionableMax), record);
						}
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
