package uk.ac.exeter.QCRoutines.data;

/**
 * Exception class for errors encountered within Data Records.
 * 
 * <p>
 *   Messages from these exceptions are prefixed with the line number and
 *   (if applicable) the column in which the error occurred.
 * </p>
 * 
 * @author Steve Jones
 * @see DataRecord
 */
public class DataRecordException extends Exception {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 794200486951764768L;

	/**
	 * The line number of the input file that was being processed when the error occurred.
	 */
	protected int lineNumber;
	
	/**
	 * The column in which the error occurred
	 */
	protected DataColumn column = null;

	/**
	 * Constructs an exception for an error that occurred on in a record, but
	 * is not related to a specific column.
	 * @param lineNumber The line number of the record
	 * @param message The error message
	 */
	public DataRecordException(int lineNumber, String message) {
		super(message);
		this.lineNumber = lineNumber;
	}
	
	/**
	 * Constructs an exception for an error that occurred on in a record, but
	 * is not related to a specific column.
	 * @param lineNumber The line number of the record
	 * @param message The error message
	 * @param cause The underlying cause of the error
	 */
	public DataRecordException(int lineNumber, String message, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}
	
	/**
	 * Constructs an exception for an error relating to a specific column
	 * @param lineNumber The line number of the record
	 * @param column The column in which the error occurred
	 * @param message The error message
	 */
	public DataRecordException(int lineNumber, DataColumn column, String message) {
		super(message);
		this.lineNumber = lineNumber;
		this.column = column;
	}
	
	/**
	 * Constructs an exception for an error relating to a specific column
	 * @param lineNumber The line number of the record
	 * @param column The column in which the error occurred
	 * @param message The error message
	 * @param cause The underlying cause of the error
	 */
	public DataRecordException(int lineNumber, DataColumn column, String message, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
		this.column = column;
	}

	/**
	 * Returns the error message, prefixed by the line number and (if supplied)
	 * column in which the error occurred.
	 * @see #getMessageStub()
	 */
	@Override
	public String getMessage() {
		return getMessageStub() + super.getMessage();
	}

	/**
	 * Creates the line number/column prefix for the error message.
	 * @return The error message prefix
	 * @see #getMessage()
	 */
	protected String getMessageStub() {
		StringBuffer message = new StringBuffer();
		message.append("Line ");
		message.append(lineNumber);
		
		if (null != column) {
			message.append(", Column ");
			message.append(column.getName());
			message.append(" (");
			message.append(column.getColumnIndex());
			message.append(')');
		}
		
		message.append(": ");

		return message.toString();
	}
}
