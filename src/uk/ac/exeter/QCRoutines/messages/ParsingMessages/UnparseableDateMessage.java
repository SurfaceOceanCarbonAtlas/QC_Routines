package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import uk.ac.exeter.QCRoutines.messages.Flag;

public class UnparseableDateMessage extends DateTimeMessage {

	public UnparseableDateMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public UnparseableDateMessage(int lineNumber, String fieldValue) {
		super(lineNumber, DATE_TIME_COLUMN_INDEX, DATE_TIME_COLUMN_NAME, Flag.BAD, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "The date and/or time value '" + fieldValue + "' could not be parsed";
	}

	@Override
	public String getShortMessage() {
		return "Unparseable date/time";
	}

}
