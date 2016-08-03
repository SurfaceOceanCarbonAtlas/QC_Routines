package uk.ac.exeter.QCRoutines.routines.ConstantValue;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class ConstantValueMessage extends Message {

	public ConstantValueMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

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
