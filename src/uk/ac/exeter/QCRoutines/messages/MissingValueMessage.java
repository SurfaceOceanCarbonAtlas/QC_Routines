package uk.ac.exeter.QCRoutines.messages;

import java.util.TreeSet;

/**
 * Message class for missing values in records
 * @author Steve Jones
 *
 */
public class MissingValueMessage extends Message {

	/**
	 * Constructor for reconstructing a message object from a {@link RebuildCode}.
	 * @param lineNumber The line number for which the message was raised
	 * @param columnIndices The column indices to which the the message relates
	 * @param columnNames The column names to which the message relates
	 * @param flag The flag for the message
	 * @param fieldValue The field value(s) that triggered the message
	 * @param validValue An example of a valid value 
	 * @see RebuildCode#getMessage()
	 */
	public MissingValueMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Constructor for a message covering multiple columns
	 * @param lineNumber The line number for which the message was raised
	 * @param columnIndices The column indices to which the the message relates
	 * @param columnNames The column names to which the message relates
	 * @param flag The flag for the message
	 * @throws MessageException If the message cannot be created
	 */
	public MissingValueMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag) throws MessageException {
		super(lineNumber, columnIndices, columnNames, flag, null, null);
	}

	/**
	 * Constructor for a message covering a single column
	 * @param lineNumber The line number for which the message was raised
	 * @param columnIndex The column index to which the message relates
	 * @param columnName The name of the column to which the message relates
	 * @param flag The flag for the message
	 * @throws MessageException If the message cannot be created
	 */
	public MissingValueMessage(long lineNumber, int columnIndex, String columnName, Flag flag) throws MessageException {
		super(lineNumber, columnIndex, columnName, flag, null, null);
	}

	@Override
	public String getFullMessage() {
		return "Missing value for column '" + getColumnNamesAsString() + "' on line " + lineNumber;
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " missing";
	}
}
