package uk.ac.exeter.QCRoutines.routines.FixedValue;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class ValueNotFixedMessage extends Message {

	public ValueNotFixedMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public ValueNotFixedMessage(int lineNumber, DataColumn dataColumn, Flag flag, String validValue) throws MessageException {
		super(lineNumber, dataColumn, flag, validValue);
	}

	@Override
	public String getFullMessage() {
		return columnName + " value is '" + fieldValue + "' - should be fixed as '" + validValue + "'";
	}

	@Override
	public String getShortMessage() {
		return columnName + " value should be fixed";
	}

}
