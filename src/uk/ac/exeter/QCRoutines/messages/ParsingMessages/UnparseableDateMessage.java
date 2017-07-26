package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

/**
 * Message for a date/time that cannot be parsed due to an invalid value
 * @author Steve Jones
 *
 */
public class UnparseableDateMessage extends Message {

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
