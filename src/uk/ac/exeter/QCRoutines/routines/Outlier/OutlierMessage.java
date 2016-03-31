package uk.ac.exeter.QCRoutines.routines.Outlier;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class OutlierMessage extends Message {

	public OutlierMessage(int lineNumber, DataColumn dataColumn, double recordStdev, double stdevLimit) {
		super(lineNumber, dataColumn, Flag.BAD, String.valueOf(recordStdev), String.valueOf(stdevLimit));
	}

	@Override
	protected String getFullMessage() {
		return columnName + " standard deviation is " + fieldValue + " - limit is " + validValue;
	}

	@Override
	public String getShortMessage() {
		return columnName + " standard deviation is too large";
	}

}
