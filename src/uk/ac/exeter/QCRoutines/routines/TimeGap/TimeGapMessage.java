package uk.ac.exeter.QCRoutines.routines.TimeGap;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class TimeGapMessage extends Message {

	public TimeGapMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public TimeGapMessage(DataRecord record, double gap, double gapLimit) throws NoSuchColumnException {
		super(record.getLineNumber(), record.getDateTimeColumns(), record.getColumnNames(record.getDateTimeColumns()), Flag.BAD, String.valueOf(gap), String.valueOf(gapLimit));
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
