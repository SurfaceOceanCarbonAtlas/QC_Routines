package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;

public class MissingDateTimeElementMessage extends DateTimeMessage {

	public MissingDateTimeElementMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public MissingDateTimeElementMessage(DataRecord record) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, null, null);
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
