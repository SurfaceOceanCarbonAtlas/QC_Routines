package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class UnparseableNumberMessage extends Message {

	public UnparseableNumberMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public UnparseableNumberMessage(int lineNumber, int columnIndex, String columnName, String fieldValue) {
		super(lineNumber, columnIndex, columnName, Flag.BAD, fieldValue, null);
	}

	@Override
	protected String getFullMessage() {
		return "The value '" + fieldValue + "' in column '" + columnName + "' could not be parsed  - it should be numeric";
	}

	@Override
	public String getShortMessage() {
		return "Non-numeric value in column '" + columnName + "'";
	}

}
