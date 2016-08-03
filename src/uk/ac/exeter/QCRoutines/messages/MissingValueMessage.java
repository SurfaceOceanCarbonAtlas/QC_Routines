package uk.ac.exeter.QCRoutines.messages;

import java.util.TreeSet;

public class MissingValueMessage extends Message {

	public MissingValueMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public MissingValueMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag) throws MessageException {
		super(lineNumber, columnIndices, columnNames, flag, null, null);
	}

	public MissingValueMessage(int lineNumber, int columnIndex, String columnNames, Flag flag) throws MessageException {
		super(lineNumber, columnIndex, columnNames, flag, null, null);
	}

	@Override
	public String getFullMessage() {
		return "Missing value for column '" + getColumnNamesAsString() + "' on line " + lineNumber;
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + "missing";
	}
}
