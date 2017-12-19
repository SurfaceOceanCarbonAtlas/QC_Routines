package uk.ac.exeter.QCRoutines.routines.Monotonic;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

/**
 * Message class for records that fail the monotonic check
 * @author Steve Jones
 *
 */
public class MonotonicMessage extends Message {

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
	public MonotonicMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}
	
	/**
	 * Creates a new {@code MonotonicMessage} for a data record. The flag to use can be set by the caller.
	 * @param record The data record that failed the monotonic check
	 * @param flag The flag to set for the record
	 * @throws NoSuchColumnException If an error occurs while determining the date/time columns in the record
	 */
	public MonotonicMessage(DataRecord record, Flag flag) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), flag, null, null);
	}

	@Override
	public String getFullMessage() {
		return getShortMessage();
	}

	@Override
	public String getShortMessage() {
		return "The date and time are not monotonic";
	}
}
