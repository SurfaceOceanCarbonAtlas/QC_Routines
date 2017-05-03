package uk.ac.exeter.QCRoutines.data;

import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;

/**
 * An exception that is thrown when the data in a column is
 * not of the type in its configuration.
 * @author Steve Jones
 *
 */
public class InvalidDataException extends DataRecordException {

	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = -3199169220532474396L;

	/**
	 * Main constructor
	 * @param lineNumber The line number on which the error was found
	 * @param sourceColumn The column whose data is of the incorrect type
	 */
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