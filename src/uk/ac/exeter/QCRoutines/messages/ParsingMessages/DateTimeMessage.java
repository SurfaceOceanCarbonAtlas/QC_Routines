package uk.ac.exeter.QCRoutines.messages.ParsingMessages;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public abstract class DateTimeMessage extends Message {

	public DateTimeMessage(int lineNumber, DataRecord record, Flag flag, String fieldValue, String validValue) throws NoSuchColumnException {
		super(lineNumber, record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), flag, fieldValue, validValue);
	}

	public DateTimeMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}
}
