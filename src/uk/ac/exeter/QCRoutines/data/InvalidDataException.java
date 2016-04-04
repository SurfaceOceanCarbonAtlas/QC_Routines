package uk.ac.exeter.QCRoutines.data;

import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;

public class InvalidDataException extends DataRecordException {

	private static final long serialVersionUID = -3199169220532474396L;

	public InvalidDataException(int lineNumber, DataColumn sourceColumn) {
		super(lineNumber, sourceColumn, "");
	}
	
	public String getMessage() {
		StringBuffer message = new StringBuffer(getMessageStub());
		message.append("Value '");
		message.append(column.getValue());
		message.append("' is not ");
		
		switch(column.getDataType()) {
		case ColumnConfigItem.TYPE_BOOLEAN: {
			message.append("boolean");
			break;
		}
		case ColumnConfigItem.TYPE_NUMERIC: {
			message.append("numeric");
			break;
		}
		default: {
			message.append("valid");
		}
		}
		
		return message.toString();
	}
}