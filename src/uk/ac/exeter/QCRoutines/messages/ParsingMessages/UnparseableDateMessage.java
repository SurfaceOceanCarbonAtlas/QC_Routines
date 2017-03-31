package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

/**
 * Message for a date/time that cannot be parsed due to an invalid value
 * @author Steve Jones
 *
 */
public class UnparseableDateMessage extends Message {

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
	public UnparseableDateMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Main constructor. 
	 * @param record The record for which the message was raised
	 * @param fieldValue The invalid value that caused parsing to fail
	 * @throws NoSuchColumnException If any of the date/time columns are missing (only possible through mis-configuration)
	 */
	public UnparseableDateMessage(DataRecord record, String fieldValue) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "The date and/or time value '" + fieldValue + "' could not be parsed";
	}

	@Override
	public String getShortMessage() {
		return "Unparseable date/time";
	}

}
