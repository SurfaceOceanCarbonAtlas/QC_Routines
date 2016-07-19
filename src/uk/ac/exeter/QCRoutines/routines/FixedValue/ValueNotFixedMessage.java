package uk.ac.exeter.QCRoutines.routines.FixedValue;

import java.util.TreeSet;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;

public class ValueNotFixedMessage extends Message {

	public ValueNotFixedMessage(int lineNumber, TreeSet<Integer> columnIndices, TreeSet<String> columnNames, Flag flag, String fieldValue, String validValue) {
		super(lineNumber, columnIndices, columnNames, flag, fieldValue, validValue);
	}

	public ValueNotFixedMessage(int lineNumber, DataColumn dataColumn, Flag flag, String validValue) throws MessageException {
		super(lineNumber, dataColumn, flag, validValue);
	}

	@Override
	public String getFullMessage() {
		return getColumnNamesAsString() + " value is '" + fieldValue + "' - should be fixed as '" + validValue + "'";
	}

	@Override
	public String getShortMessage() {
		return getColumnNamesAsString() + " value should be fixed";
	}

}
