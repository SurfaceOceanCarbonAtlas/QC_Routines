package uk.ac.exeter.QCRoutines.routines.FixedValue;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

/**
 * Message raised when a value changes when it should be fixed
 * @author Steve Jones
 *
 */
public class ValueNotFixedMessage extends Message {

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
	public ValueNotFixedMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * The generic constructor for a Message object.
	 * @param lineNumber The line to which this message applies
	 * @param dataColumn The column in which the changed value was found
	 * @param flag The flag for the message
	 * @param validValue An example of a valid value indicating what the line should contain
	 * @throws MessageException If the message cannot be created
	 * @see Message#Message(int, TreeSet, TreeSet, Flag, String, String)
	 */
	public ValueNotFixedMessage(long lineNumber, DataColumn dataColumn, Flag flag, String validValue) throws MessageException {
		super(lineNumber, dataColumn, flag, validValue);
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " value is '" + fieldValue + "' - should be fixed as '" + validValue + "'";
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " value should be fixed";
	}

}
