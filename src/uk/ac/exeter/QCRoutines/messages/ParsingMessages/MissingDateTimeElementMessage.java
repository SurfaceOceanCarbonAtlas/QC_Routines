package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import uk.ac.exeter.QCRoutines.messages.Flag;

public class MissingDateTimeElementMessage extends DateTimeMessage {

	public MissingDateTimeElementMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public MissingDateTimeElementMessage(int lineNumber) {
		super(lineNumber, DATE_TIME_COLUMN_INDEX, DATE_TIME_COLUMN_NAME, Flag.BAD, null, null);
	}

	@Override
	public String getFullMessage() {
		return "One or more date/time elements are missing";
	}

	@Override
	public String getShortMessage() {
		return "One or more date/time elements missing";
	}

}
