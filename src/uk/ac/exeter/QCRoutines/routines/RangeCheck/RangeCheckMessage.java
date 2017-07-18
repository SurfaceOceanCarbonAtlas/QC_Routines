package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

/**
 * Message raised when a value is out of range
 * @author Steve Jones
 *
 */
public class RangeCheckMessage extends Message {

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
	public RangeCheckMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Constructor for the {@link RangeCheckRoutine}
	 * @param lineNumber The line to which this message applies
	 * @param dataColumn The column in which the outlier was detected
	 * @param flag The flag for the message
	 * @param fieldValue The value from the line that caused the message to be triggered
	 * @param rangeMin The minimum value of the accepted range
	 * @param rangeMax The maximum value of the accepted range
	 * @throws MessageException If the message cannot be created
	 */
	public RangeCheckMessage(int lineNumber, DataColumn dataColumn, Flag flag, double fieldValue, double rangeMin, double rangeMax) throws MessageException {
		super(lineNumber, dataColumn, flag, String.valueOf(fieldValue), rangeMin + " - " + rangeMax);
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " value is " + fieldValue + ": should be in the range " + validValue;
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " is out of range";
	}
}
