package uk.ac.exeter.QCRoutines.routines.TimeGap;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

/**
 * Message raised when the time elapsed between two measurements is too large
 * @author Steve Jones
 *
 */
public class TimeGapMessage extends Message {

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
	public TimeGapMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Constructor for the Time Gap Routine
	 * @param record The record after the large gap
	 * @param gap The gap between the two records
	 * @param gapLimit The limit of the gap
	 * @throws NoSuchColumnException If the date/time columns cannot be established
	 * @see TimeGapRoutine
	 */
	public TimeGapMessage(DataRecord record, double gap, double gapLimit) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, String.valueOf(gap), String.valueOf(gapLimit));
	}

	@Override
	public String getFullMessage() {
		return "Gap between measurements is " + fieldValue + " days - limit is " + validValue + " days";
	}

	@Override
	public String getShortMessage() {
		return "Too much time between measurements";
	}

}
