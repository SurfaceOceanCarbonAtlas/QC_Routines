package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class BackwardsTimeMessage extends Message {

	public BackwardsTimeMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public BackwardsTimeMessage(DataRecord record) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, null, null);
	}

	@Override
	public String getFullMessage() {
		return "This record is either at the same time as or before the previous record";
	}

	@Override
	public String getShortMessage() {
		return "Record time goes backwards";
	}

}
