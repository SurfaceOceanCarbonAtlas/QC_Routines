package uk.ac.exeter.QCRoutines.routines.FixedValue;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class ValueNotFixedMessage extends Message {

	public ValueNotFixedMessage(int lineNumber, DataColumn dataColumn, Flag flag, String validValue) {
		super(lineNumber, dataColumn, flag, validValue);
	}

	@Override
	protected String getFullMessage() {
		return columnName + " value is '" + fieldValue + "' - should be fixed as '" + validValue + "'";
	}

	@Override
	public String getShortMessage() {
		return columnName + " value should be fixed";
	}

}
