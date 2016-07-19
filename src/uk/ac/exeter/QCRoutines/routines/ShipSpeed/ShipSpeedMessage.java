package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class ShipSpeedMessage extends Message {

	public static final int SHIP_SPEED_COLUMN_INDEX = -1;
	
	public static final String SHIP_SPEED_COLUMN_NAME = "Lon/Lat/Date/Time";
	
	public ShipSpeedMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public ShipSpeedMessage(int lineNumber, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, SHIP_SPEED_COLUMN_INDEX, SHIP_SPEED_COLUMN_NAME, flag, fieldValue, validValue);
	}

	@Override
	public String getFullMessage() {
		StringBuffer message = new StringBuffer();
		message.append("Ship speed between this measurement and the last is too high. Was ");
		message.append(fieldValue);
		message.append("km/h, threshold is ");
		message.append(validValue);
		message.append("km/h");
		
		return message.toString();
	}

	@Override
	public String getShortMessage() {
		return "Ship speed too high";
	}

}
