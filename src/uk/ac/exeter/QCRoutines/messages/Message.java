package uk.ac.exeter.QCRoutines.messages;

import uk.ac.exeter.QCRoutines.data.DataColumn;

public abstract class Message {
	
	public static final int NO_COLUMN_INDEX = -999;
	
	public static final int NO_LINE_NUMBER = -999;

	protected int columnIndex;
	
	protected String columnName;
	
	private Flag flag;
	
	protected int lineNumber;
	
	protected String fieldValue;
	
	protected String validValue;
	
	public Message(int lineNumber, int columnIndex, String columnName, Flag flag, String fieldValue, String validValue) {
		this.lineNumber = lineNumber;
		this.columnIndex = columnIndex;
		this.columnName = columnName;
		this.flag = flag;
		this.fieldValue = fieldValue;
		this.validValue = validValue;
	}
	
	public Message(int lineNumber, DataColumn dataColumn, Flag flag, String validValue) {
		this.lineNumber = lineNumber;
		this.columnIndex = dataColumn.getColumnIndex();
		this.columnName = dataColumn.getName();
		this.flag = flag;
		this.fieldValue = dataColumn.getValue();
		this.validValue = validValue;
	}
	
	public Message(int lineNumber, DataColumn dataColumn, Flag flag, String fieldValue, String validValue) {
		this.lineNumber = lineNumber;
		this.columnIndex = dataColumn.getColumnIndex();
		this.columnName = dataColumn.getName();
		this.flag = flag;
		this.fieldValue = fieldValue;
		this.validValue = validValue;
	}
	
	/**
	 * Returns the line number for which this message was raised.
	 * @return The line number for which this message was raised.
	 */
	public int getLineNumber() {
		return lineNumber;
	}
	
	/**
	 * Returns the column index for which this message was raised.
	 * @return The column index for which this message was raised.
	 */
	public int getColumnIndex() {
		return columnIndex;
	}

	/**
	 * @return the name of the column for which this message was raised.
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * Returns the flag of the message
	 * @return The flag of the message
	 */
	public Flag getFlag() {
		return flag;
	}
	
	/**
	 * Create the {@link MessageKey} object for this message,
	 * to be used in storing the message.
	 * 
	 * @return The {@link MessageKey} object for this message 
	 */
	public MessageKey generateMessageKey() {
		return new MessageKey(columnIndex, getClass());
	}
	
	protected abstract String getFullMessage();
	
	public abstract String getShortMessage();
	
	public RebuildCode getRebuildCode() throws MessageException {
		return new RebuildCode(this);
	}
	
	public String getFieldValue() {
		return fieldValue;
	}
	
	public String getValidValue() {
		return validValue;
	}
	
	public String toString() {
		return getFullMessage();
	}
	
	public boolean equals(Object o) {
		boolean equals = true;
		
		if (!(o instanceof Message)) {
			equals = false;
		} else {
			Message compare = (Message) o;
			if (compare.columnIndex != columnIndex ||
					!compare.columnName.equals(columnName) ||
					!compare.flag.equals(flag) ||
					compare.lineNumber != lineNumber ||
					!compare.fieldValue.equals(fieldValue) ||
					!compare.validValue.equals(validValue)) {
				equals = false;
			}
		}
		
		return equals;
	}
}
