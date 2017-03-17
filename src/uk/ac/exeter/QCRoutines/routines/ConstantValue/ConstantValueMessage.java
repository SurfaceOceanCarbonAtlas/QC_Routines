package uk.ac.exeter.QCRoutines.routines.ConstantValue;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

/**
 * Class for messages generated by the {@link ConstantValueRoutine}.
 * @author Steve Jones
 */
public class ConstantValueMessage extends Message {

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
	public ConstantValueMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * The main message constructor. The flag for the message is always {@link Flag#BAD}.
	 * @param lineNumber The line number for which the message was raised
	 * @param dataColumn The column to which the message relates
	 * @param constantTime The time for which the column was constant
	 * @param maxConstantTime The maximum time for which the column is allowed to be constant
	 * @throws MessageException If an error occurs while constructing the message
	 */
	public ConstantValueMessage(int lineNumber, DataColumn dataColumn, double constantTime, double maxConstantTime) throws MessageException {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(constantTime), String.valueOf(constantTime));
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " is constant for " + fieldValue + " minutes - limit is " + validValue + " minutes";
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " constant for too long";
	}

}
