package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

/**
 * Message raised when a date/time cannot be parsed because one or more date/time
 * columns are empty.
 * @author Steve Jones
 *
 */
public class MissingDateTimeElementMessage extends Message {

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
	public MissingDateTimeElementMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * The main message constructor
	 * @param record The data record from which the message was raised
	 * @throws NoSuchColumnException If any of the date/time columns are missing (only possible through mis-configuration)
	 */
	public MissingDateTimeElementMessage(DataRecord record) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, null, null);
	}

	@Override
	public String getFullMessage() {
		return "One or more date/time elements are missing";
	}

	@Override
	public String getShortMessage() {
		return "One or more date/time elements missing";
	}

}
