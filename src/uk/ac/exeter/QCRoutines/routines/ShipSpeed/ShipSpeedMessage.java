package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class ShipSpeedMessage extends Message {

	public static final int SHIP_SPEED_COLUMN_INDEX = -1;
	
	public static final String SHIP_SPEED_COLUMN_NAME = "Lon/Lat/Date/Time";
	
	public ShipSpeedMessage(int columnIndex, String columnName, Flag flag, int lineNumber, String fieldValue,
			String validValue) {
		super(columnIndex, columnName, flag, lineNumber, fieldValue, validValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getFullMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getShortMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
