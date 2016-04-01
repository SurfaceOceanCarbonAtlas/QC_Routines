package uk.ac.exeter.QCRoutines.routines.Dummy;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class DummyMessage extends Message {

	public DummyMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public DummyMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, null);
	}

	@Override
	protected String getFullMessage() {
		return "This is a dummy message for column '" + columnName + "' on line " + lineNumber;
	}

	@Override
	public String getShortMessage() {
		return "Dummy message";
	}
}
