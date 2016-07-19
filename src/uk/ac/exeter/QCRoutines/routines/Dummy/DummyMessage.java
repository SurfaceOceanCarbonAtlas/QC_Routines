package uk.ac.exeter.QCRoutines.routines.Dummy;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class DummyMessage extends Message {

	public DummyMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public DummyMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue) throws MessageException {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "This is a dummy message for column '" + getColumnNamesAsString() + "' on line " + lineNumber;
	}

	@Override
	public String getShortMessage() {
		return "Dummy message";
	}
}
