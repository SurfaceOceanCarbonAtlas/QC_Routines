package uk.ac.exeter.QCRoutines.data;

/**
 * Exception raised when a specified column does not exist in a data record
 * @author Steve Jones
 *
 */
public class NoSuchColumnException extends DataRecordException {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = 3997426626030321279L;

	/**
	 * Constructor for a column referenced by index that doesn't exist
	 * @param lineNumber The line number on which the error was raised
	 * @param columnIndex The column index
	 */
	public NoSuchColumnException(int lineNumber, int columnIndex) {
		super(lineNumber, "There is no column with index " + columnIndex);
	}
	
	/**
	 * Constructor for a column referenced by name that doesn't exist
	 * @param lineNumber The line number on which the error was raised
	 * @param columnName The column name
	 */
	public NoSuchColumnException(int lineNumber, String columnName) {
		super(lineNumber, "There is no column with the name '" + columnName + "'");
	}
	
}
