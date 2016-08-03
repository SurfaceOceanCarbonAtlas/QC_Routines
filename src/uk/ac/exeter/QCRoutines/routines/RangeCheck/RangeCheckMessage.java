package uk.ac.exeter.QCRoutines.routines.RangeCheck;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class RangeCheckMessage extends Message {

	public RangeCheckMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public RangeCheckMessage(int lineNumber, DataColumn dataColumn, Flag flag, double fieldValue, double rangeMin, double rangeMax) throws MessageException {
		super(lineNumber, dataColumn, flag, String.valueOf(fieldValue), rangeMin + " - " + rangeMax);
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " value is " + fieldValue + ": should be in the range " + validValue;
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " is out of range";
	}
}
