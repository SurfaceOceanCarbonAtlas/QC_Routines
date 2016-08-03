package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;

public class UnparseableDateMessage extends DateTimeMessage {

	public UnparseableDateMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public UnparseableDateMessage(DataRecord record, String fieldValue) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, fieldValue, null);
	}

	@Override
	public String getFullMessage() {
		return "The date and/or time value '" + fieldValue + "' could not be parsed";
	}

	@Override
	public String getShortMessage() {
		return "Unparseable date/time";
	}

}
