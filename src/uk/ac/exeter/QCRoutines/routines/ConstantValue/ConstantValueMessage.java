package uk.ac.exeter.QCRoutines.routines.ConstantValue;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class ConstantValueMessage extends Message {

	public ConstantValueMessage(int lineNumber, DataColumn dataColumn, double constantTime, double maxConstantTime) {
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
