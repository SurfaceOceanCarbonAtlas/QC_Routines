package uk.ac.exeter.QCRoutines.routines.ConstantValue;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class ConstantValueMessage extends Message {

	public ConstantValueMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public ConstantValueMessage(int lineNumber, DataColumn dataColumn, double constantTime, double maxConstantTime) throws MessageException {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(constantTime), String.valueOf(constantTime));
	}

	@Override
	public String getFullMessage() {
		return columnName + " is constant for " + fieldValue + " minutes - limit is " + validValue + " minutes";
	}

	@Override
	public String getShortMessage() {
		return columnName + " constant for too long";
	}

}
