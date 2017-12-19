package uk.ac.exeter.QCRoutines.routines.Outlier;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

/**
 * Message raised when an outlier is detected
 * @author Steve Jones
 *
 */
public class OutlierMessage extends Message {

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
	public OutlierMessage(long lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Constructor for the {@link OutlierRoutine}
	 * @param lineNumber The line to which this message applies
	 * @param dataColumn The column in which the outlier was detected
	 * @param recordStdev The standard deviation of the value from the mean of the data
	 * @param stdevLimit The standard deviation limit
	 * @throws MessageException If the message cannot be created
	 */
	public OutlierMessage(long lineNumber, DataColumn dataColumn, double recordStdev, double stdevLimit) throws MessageException {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(recordStdev), String.valueOf(stdevLimit));
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " standard deviation is " + fieldValue + " - limit is " + validValue;
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " standard deviation is too large";
	}

}
