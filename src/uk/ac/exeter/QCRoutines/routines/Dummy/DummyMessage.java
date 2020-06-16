package uk.ac.exeter.QCRoutines.routines.Dummy;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

/**
 * Messages raised by the dummy QC routine (used for testing)
 * @author Steve Jones
 * @see DummyRoutine
 */
public class DummyMessage extends Message {

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
	public DummyMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Main constructor
	 * @param lineNumber The line number of the record where the message was raised
	 * @param columnIndex The index of the column where the message was raised
	 * @param columnName The name of the column where the message was raised
	 * @param flag The flag for the message
	 * @param fieldValue The field value that caused the message to be raised
	 * @throws MessageException If an error occurs while constructing the message
	 */
	public DummyMessage(long lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue) throws MessageException {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "This is a dummy message for column '" + getColumnNamesAsString() + "' on line " + lineNumber;
	}

	@Override
	public String getShortMessage() {
		return "Dummy message";
	}
}
