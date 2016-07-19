package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class RangeCheckMessage extends Message {

	public RangeCheckMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public RangeCheckMessage(int lineNumber, DataColumn dataColumn, Flag flag, double fieldValue, double rangeMin, double rangeMax) throws MessageException {
		super(lineNumber, dataColumn, flag, String.valueOf(fieldValue), rangeMin + " - " + rangeMax);
	}

	@Override
	public String getFullMessage() {
		return columnName + " value is " + fieldValue + ": should be in the range " + validValue;
	}

	@Override
	public String getShortMessage() {
		return columnName + " is out of range";
	}
}
