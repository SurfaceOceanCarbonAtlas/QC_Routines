package uk.ac.exeter.QCRoutines.config;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;

/**
 * Sometimes when a flag is set on one column, a flag should
 * also be set automatically on another column. For example,
 * if SST is marked as Bad, fCO<sub>2</sub> should also be marked bad.
 * 
 * <p>
 *   This class describes this 'cascade' of one flag to another column. A
 *   cascade is defined in terms of the destination column of the cascade,
 *   and the flag to set on that column if the source column is given a
 *   {@link Flag#QUESTIONABLE} or {@link Flag#BAD} flag (the destination flag may not mirror the source
 *   flag in all cases).
 * </p>
 * 
 * @author Steve Jones
 * @see ColumnConfigItem
 */
public class FlagCascade {

	/**
	 * The name of the column whose flag must be set when the
	 * source column's flag is set.
	 */
	private String destinationColumn;
	
	/**
	 * The flag to set on the {@link #destinationColumn} when the
	 * source column's flag is set to {@link Flag#QUESTIONABLE}.
	 */
	private Flag questionableCascadeFlag;
	
	/**
	 * The flag to set on the {@link #destinationColumn} when the
	 * source column's flag is set to {@link Flag#BAD}.
	 */
	private Flag badCascadeFlag;
	
	/**
	 * Constructor for a flag cascade
	 * @param destinationColumn The destination column
	 * @param questionableCascadeFlag The flag to set when the source column is {@link Flag#QUESTIONABLE}
	 * @param badCascadeFlag The flag to set when the source column is {@link Flag#BAD}
	 */
	public FlagCascade(String destinationColumn, Flag questionableCascadeFlag, Flag badCascadeFlag) {
		this.destinationColumn = destinationColumn;
		this.questionableCascadeFlag = questionableCascadeFlag;
		this.badCascadeFlag = badCascadeFlag;
	}
	
	/**
	 * Applies this flag cascade to a data record based on the specified source column flag.
	 * The rules for setting flags are the same as those used in {@link DataColumn#setFlag(Flag)}.
	 * 
	 * @param record The record to be edited
	 * @param sourceFlag The flag set on the source column
	 * @throws NoSuchColumnException If the destination column does not exist in the specified record
	 * @see DataColumn#setFlag(Flag)
	 */
	public void apply(DataRecord record, Flag sourceFlag) throws NoSuchColumnException {
		DataColumn column = record.getColumn(destinationColumn);
		if (sourceFlag.equals(Flag.QUESTIONABLE)) {
			column.setFlag(questionableCascadeFlag);
		} else if (sourceFlag.equals(Flag.BAD)) {
			column.setFlag(badCascadeFlag);
		}
	}
}
