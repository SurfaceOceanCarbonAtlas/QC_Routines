package uk.ac.exeter.QCRoutines.routines.Outlier;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class OutlierMessage extends Message {

	public OutlierMessage(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndex, columnName, flag, fieldValue, validValue);
	}

	public OutlierMessage(int lineNumber, DataColumn dataColumn, double recordStdev, double stdevLimit) throws MessageException {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(recordStdev), String.valueOf(stdevLimit));
	}

	@Override
	public String getFullMessage() {
		return columnName + " standard deviation is " + fieldValue + " - limit is " + validValue;
	}

	@Override
	public String getShortMessage() {
		return columnName + " standard deviation is too large";
	}

}
