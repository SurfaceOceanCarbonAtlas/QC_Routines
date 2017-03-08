package uk.ac.exeter.QCRoutines.routines.Monotonic;

import uk.ac.exeter.QCRoutines.data.DataRecord;

/**
 * An exception for records that are missing the date/time
 * @author Steve Jones
 *
 */
public class MissingTimeException extends Exception {
	
	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 322454013241170306L;
	
	/**
	 * The record that is missing a date/time
	 */
	private DataRecord record;
	
	/**
	 * Exception constructor
	 * @param record The record that is missing a date/time
	 */
	public MissingTimeException(DataRecord record) {
		super("The record for line " + record.getLineNumber() + " has no time");
		this.record = record;
	}

	/**
	 * Get the record that is missing a date/time
	 * @return The record
	 */
	public DataRecord getRecord() {
		return record;
	}
}
