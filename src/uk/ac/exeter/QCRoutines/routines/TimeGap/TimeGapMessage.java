package uk.ac.exeter.QCRoutines.routines.TimeGap;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class TimeGapMessage extends Message {

	public static final int DATE_TIME_COLUMN_INDEX = -1;
	
	public static final String DATE_TIME_COLUMN_NAME = "Date/Time";
	
	public TimeGapMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public TimeGapMessage(int lineNumber, double gap, double gapLimit) {
		super(lineNumber, DATE_TIME_COLUMN_INDEX, DATE_TIME_COLUMN_NAME, Flag.BAD, String.valueOf(gap), String.valueOf(gapLimit));
	}

	@Override
	public String getFullMessage() {
		return "Gap between measurements is " + fieldValue + " days - limit is " + validValue + " days";
	}

	@Override
	public String getShortMessage() {
		return "Too much time between measurements";
	}

}
