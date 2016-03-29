package uk.ac.exeter.QCRoutines.messages;

public abstract class Message {
	
	public static final int DATE_TIME_COLUMN_INDEX = -1;
	
	public static final String DATE_TIME_COLUMN_NAME = "Date/Time";
	
	public static final int SHIP_SPEED_COLUMN_INDEX = -2;
	
	public static final String SHIP_SPEED_COLUMN_NAME = "Lon/Lat/Date/Time";
	
	public static final int NO_COLUMN_INDEX = -999;
	
	public static final int NO_LINE_NUMBER = -999;
	
	/**
	 * Token identifying the placeholder for the column name
	 */
	public static final String COLUMN_NAME_IDENTIFIER = "%c%";
	
	/**
	 * Token identifying the placeholder for a field value
	 */
	public static final String FIELD_VALUE_IDENTIFIER = "%f%";
	
	/**
	 * Token identifying the placeholder for a valid value
	 */
	public static final String VALID_VALUE_IDENTIFIER = "%v%";
	
	/**
	 * String to be used if tokens are in the message string, but
	 * no values are supplied to fill them.
	 */
	private static final String MISSING_VALUE_STRING = "MISSING_VALUE";

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
	
	public String getMessageString() {
		
		StringBuffer result = new StringBuffer();
		result.append(flag.toString());
		result.append(": LINE ");
		result.append(lineNumber);
		result.append(": ");
		result.append(getRecordMessage(columnName, fieldValue, validValue));
		return result.toString();
	}
		
	protected abstract String getFullMessage();
	
	public abstract String getShortMessage();
	
	/**
	 * Generate the long form error message for this error type, substituting in
	 * the supplied field and valid values.
	 * 
	 * If the message requires these values but they are not provided, the message will still
	 * be generated but the values will be replaced with {@link #MISSING_VALUE_STRING}.
	 * 
	 * @param fieldValue The field value from the data file
	 * @param validValue The expected valid value(s)
	 * @return The message string
	 */
	public String getRecordMessage(String columnName, String fieldValue, String validValue) {
		
		String columnReplaceValue = MISSING_VALUE_STRING;
		if (null != columnName && columnName.trim().length() > 0) {
			columnReplaceValue = columnName.trim();
		}

		String fieldReplaceValue = MISSING_VALUE_STRING;
		if (null != fieldValue && fieldValue.trim().length() > 0) {
			fieldReplaceValue = fieldValue.trim();
		}
		
		String validReplaceValue = MISSING_VALUE_STRING;
		if (null != validValue && validValue.trim().length() > 0) {
			validReplaceValue = validValue.trim();
		}

		String result = getFullMessage().replaceAll(COLUMN_NAME_IDENTIFIER, columnReplaceValue);
		result = result.replaceAll(FIELD_VALUE_IDENTIFIER, fieldReplaceValue);
		result = result.replaceAll(VALID_VALUE_IDENTIFIER, validReplaceValue);

		return result;
	}
	
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
