package uk.ac.exeter.QCRoutines.routines.Monotonic;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MissingValueMessage;

/**
 * Message for records with missing times. These are always fatal
 * @author Steve Jones
 *
 */
public class MissingTimeMessage extends MissingValueMessage {

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
	public MissingTimeMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}
	
	/**
	 * Basic constructor
	 * @param record The record that is missing a date/time
	 * @throws NoSuchColumnException If an error occurs while determining the date/time columns from the record
	 */
	public MissingTimeMessage(DataRecord record) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.FATAL, null, null);
	}

	@Override
	public String getFullMessage() {
		return "Missing Date/Time";
	}

	@Override
	public String getShortMessage() {
		return "Missing Date/Time";
	}
}
