package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class UnparseableNumberMessage extends Message {

	public UnparseableNumberMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public UnparseableNumberMessage(int lineNumber, int columnIndex, String columnName, String fieldValue) throws MessageException {
		super(lineNumber, columnIndex, columnName, Flag.BAD, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "The value '" + fieldValue + "' in column '" + getColumnNamesAsString() + "' could not be parsed  - it should be numeric";
	}

	@Override
	public String getShortMessage() {
		return "Non-numeric value in column '" + getColumnNamesAsString() + "'";
	}

}
