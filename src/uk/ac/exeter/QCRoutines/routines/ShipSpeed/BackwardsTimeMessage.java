package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class BackwardsTimeMessage extends Message {

	public static final int DATE_TIME_COLUMN_INDEX = -1;
	
	public static final String DATE_TIME_COLUMN_NAME = "Date/Time";
	
	public BackwardsTimeMessage(int lineNumber) {
		super(lineNumber, DATE_TIME_COLUMN_INDEX, DATE_TIME_COLUMN_NAME, Flag.BAD, null, null);
	}

	@Override
	protected String getFullMessage() {
		return "This record is either at the same time as or before the previous record";
	}

	@Override
	public String getShortMessage() {
		return "Record time goes backwards";
	}

}
