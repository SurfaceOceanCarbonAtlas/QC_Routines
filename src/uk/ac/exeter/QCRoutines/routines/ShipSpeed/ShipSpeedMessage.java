package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class ShipSpeedMessage extends Message {

	public ShipSpeedMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public ShipSpeedMessage(DataRecord record, Flag flag, String fieldValue, String validValue) throws NoSuchColumnException {
		super(record.getLineNumber(), getShipSpeedColumnIndices(record), record.getColumnNames(getShipSpeedColumnIndices(record)), flag, fieldValue, validValue);
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
	
	private static TreeSet<Integer> getShipSpeedColumnIndices(DataRecord record) {
		@SuppressWarnings("unchecked")
		TreeSet<Integer> columnIndices = (TreeSet<Integer>) record.getDateTimeColumns().clone();
		columnIndices.add(record.getLongitudeColumn());
		columnIndices.add(record.getLatitudeColumn());
		return columnIndices;
	}

}
