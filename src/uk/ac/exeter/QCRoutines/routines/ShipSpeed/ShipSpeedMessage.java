package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

/**
 * Message raised when a ship is travelling too fast
 * @author Steve Jones
 *
 */
public class ShipSpeedMessage extends Message {

	/**
	 * The generic constructor for a Message object.
	 * @param lineNumber The line to which this message applies
	 * @param columnIndices The index(es) of the column(s) to which this message applies
	 * @param columnNames The name(s) of the column(s) to which this message applies
	 * @param flag The flag for the message
	 * @param fieldValue The value from the line that caused the message to be triggered
	 * @param validValue An example of a valid value indicating what the line should contain
	 * @see Message#Message(int, TreeSet, TreeSet, Flag, String, String)
	 */
	public ShipSpeedMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	/**
	 * Constructor for the {@link ShipSpeedRoutine}
	 * @param record The record in which the excessive speed was found
	 * @param flag The flag for the message
	 * @param shipSpeed The ship's speed
	 * @param maxShipSpeed The maximum allowed ship speed
	 * @throws NoSuchColumnException If the ship speed columns (position, date/time) cannot be reconciled
	 */
	public ShipSpeedMessage(DataRecord record, Flag flag, String shipSpeed, String maxShipSpeed) throws NoSuchColumnException {
		super(record.getLineNumber(), getShipSpeedColumnIndices(record), record.getColumnNames(getShipSpeedColumnIndices(record)), flag, shipSpeed, maxShipSpeed);
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
	
	/**
	 * Get the set of columns used for calculating ship speed:
	 * date/time and position
	 * @param record A data record
	 * @return The set of columns
	 */
	private static TreeSet<Integer> getShipSpeedColumnIndices(DataRecord record) {
		@SuppressWarnings("unchecked")
		TreeSet<Integer> columnIndices = (TreeSet<Integer>) record.getDateTimeColumns().clone();
		columnIndices.add(record.getLongitudeColumn());
		columnIndices.add(record.getLatitudeColumn());
		return columnIndices;
	}

}
