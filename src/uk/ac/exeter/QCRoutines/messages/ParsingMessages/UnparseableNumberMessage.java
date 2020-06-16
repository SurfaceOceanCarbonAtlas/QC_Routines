package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

/**
 * Message for a numeric value that cannot be parsed as such
 * @author Steve Jones
 *
 */
public class UnparseableNumberMessage extends Message {

	/**
	 * The generic constructor for a Message object.
	 * @param lineNumber The line to which this message applies
	 * @param columnIndices The index(es) of the column(s) to which this message applies
	 * @param columnNames The name(s) of the column(s) to which this message applies
	 * @param flag The flag for the message
	 * @param fieldValue The value from the line that caused the message to be triggered
	 * @param validValue An example of a valid value indicating what the line should contain
	 * @see Message#Message(int, TreeSet, TreeSet, Flag, String, String)
	 */
	public UnparseableNumberMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * The generic constructor for a Message object.
	 * @param lineNumber The line to which this message applies
	 * @param columnIndex The index of the column to which this message applies
	 * @param columnName The name of the column to which this message applies
	 * @param fieldValue The value from the line that caused the message to be triggered
	 * @throws MessageException If the message cannot be created
	 * @see Message#Message(int, TreeSet, TreeSet, Flag, String, String)
	 */
	public UnparseableNumberMessage(int lineNumber, int columnIndex, String columnName, String fieldValue) throws MessageException {
		super(lineNumber, columnIndex, columnName, Flag.BAD, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "The value '" + fieldValue + "' in column '" + getColumnNamesAsString() + "' could not be parsed  - it should be numeric";
	}

	@Override
	public String getShortMessage() {
		return "Non-numeric value in column '" + getColumnNamesAsString() + "'";
	}

}
