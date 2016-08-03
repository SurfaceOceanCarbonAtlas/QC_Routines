package uk.ac.exeter.QCRoutines.data;

public class DataRecordException extends Exception {

	private static final long serialVersionUID = 794200486951764768L;

	/**
	 * The line number of the input file that was being processed when the error occurred.
	 */
	protected int lineNumber;
	
	protected DataColumn column = null;

	public DataRecordException(int lineNumber, String message) {
		super(message);
		this.lineNumber = lineNumber;
	}
	
	public DataRecordException(int lineNumber, String message, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}
	
	public DataRecordException(int lineNumber, DataColumn column, String message) {
		super(message);
		this.lineNumber = lineNumber;
		this.column = column;
	}
	
	public DataRecordException(int lineNumber, DataColumn column, String message, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
		this.column = column;
	}

	public String getMessage() {
		return getMessageStub() + super.getMessage();
	}

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
