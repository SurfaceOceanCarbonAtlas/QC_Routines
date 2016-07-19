package uk.ac.exeter.QCRoutines.routines.HighDelta;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class HighDeltaMessage extends Message {

	public HighDeltaMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public HighDeltaMessage(int lineNumber, DataColumn dataColumn, double recordDelta, double maxDelta) throws MessageException {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(recordDelta), String.valueOf(maxDelta));
	}

	@Override
	public String getFullMessage() {
		return columnName + " changed by " + fieldValue + " per minute - max is " + validValue + " per minute";
	}

	@Override
	public String getShortMessage() {
		return columnName + " changes too quickly";
	}

}
