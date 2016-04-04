package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;

public class RangeCheckMessage extends Message {

	double rangeMin;
	
	double rangeMax;
	
	public RangeCheckMessage(int lineNumber, DataColumn dataColumn, Flag flag, double fieldValue, double rangeMin, double rangeMax) {
		super(lineNumber, dataColumn, flag, String.valueOf(fieldValue), null);
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
	}

	@Override
	protected String getFullMessage() {
		return columnName + " value is " + fieldValue + ": should be between " + rangeMin + " and " + rangeMax;
	}

	@Override
	public String getShortMessage() {
		return columnName + " is out of range";
	}
}
