package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public abstract class DateTimeMessage extends Message {

	public DateTimeMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public static final int DATE_TIME_COLUMN_INDEX = -1;
	
	public static final String DATE_TIME_COLUMN_NAME = "Date/Time";
}
