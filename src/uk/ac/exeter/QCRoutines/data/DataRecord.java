package uk.ac.exeter.QCRoutines.data;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.joda.time.DateTime;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

/**
 * Holds a complete data record for use with QC routines.
 * Each data record represents one line in a data file.
 * 
 * <p>
 *   This is an abstract class because each type of {@code DataRecord}
 *   must explicitly specify how the date/time, longitude and latitude
 *   should be read from the record.
 * </p>
 * 
 * @author Steve Jones
 */
public abstract class DataRecord {

	/**
	 * The QC messages generated for this record
	 */
	protected List<Message> messages;
	
	/**
	 * The line number of this record in the original file
	 */
	protected int lineNumber;
	
	/**
	 * The record data
	 */
	protected List<DataColumn> data;
	
	/**
	 * Creates an empty {@code DataRecord} with the specified columns, ready to be
	 * populated with data.
	 * @param lineNumber The record's line number
	 * @param columnConfig The column configuration
	 */
	public DataRecord(int lineNumber, ColumnConfig columnConfig) {
		this.messages = new ArrayList<Message>();
		this.lineNumber = lineNumber;
		this.data = columnConfig.getDataColumns(this);
	}
	
	/**
	 * Creates a {@code DataRecord} with the specified columns and populates it
	 * with the specified data values.
	 * 
	 * <p>
	 *   The entries in {@code dataFields} are assumed to have data in the same
	 *   column order as the {@code columnConfig}.
	 * </p>
	 * 
	 * @param lineNumber The record's line number
	 * @param columnConfig The column configuration
	 * @param dataFields The data values for the record
	 * @throws DataRecordException If the {@code dataFields} do not match the {@code columnConfig}
	 */
	public DataRecord(int lineNumber, ColumnConfig columnConfig, List<String> dataFields) throws DataRecordException {
		this.messages = new ArrayList<Message>();
		this.lineNumber = lineNumber;
		this.data = columnConfig.getDataColumns(this);

		// Populate all the basic data columns
		setDataValues(dataFields);
	}
	
	/**
	 * Returns the date/time of this record as a single object.
	 * @return The date/time of this record.
	 * @throws DataRecordException If the date/time is mis-formatted or otherwise cannot be obtained
	 */
	public abstract DateTime getTime() throws DataRecordException;

	/**
	 * Returns the column indices that hold the date and time values in the record
	 * @return The indices of the date/time column(s)
	 */
	public abstract TreeSet<Integer> getDateTimeColumns();
	
	/**
	 * Returns the longitude of this record
	 * @return The longitude of this record
	 * @throws DataRecordException If the longitude is invalid
	 */
	public abstract double getLongitude() throws DataRecordException;
	
	/**
	 * Returns the index of the longitude column
	 * @return The longitude column index
	 */
	public abstract int getLongitudeColumn();
	
	/**
	 * Returns the latitude of this record
	 * @return The latitude of this record
	 * @throws DataRecordException If the latitude is invalid
	 */
	public abstract double getLatitude() throws DataRecordException;
	
	/**
	 * Returns the index of the latitude column
	 * @return The latitude column index
	 */
	public abstract int getLatitudeColumn();

	/**
	 * Returns the list of date/time column names
	 * @return The list of date/time column names
	 * @throws NoSuchColumnException If the column names cannot be found
	 */
	public TreeSet<String> getDateTimeColumnNames() throws NoSuchColumnException {
		return getColumnNames(getDateTimeColumns());
	}
	
	/**
	 * Returns the name of the longitude column
	 * @return The name of the longitude column
	 * @throws NoSuchColumnException If the column name cannot be found
	 */
	public String getLongitudeColumnName() throws NoSuchColumnException {
		return getColumnName(getLongitudeColumn());
	}
	
	/**
	 * Returns the name of the latitude column
	 * @return The name of the latitude column
	 * @throws NoSuchColumnException If the column name cannot be found
	 */
	public String getLatitudeColumnName() throws NoSuchColumnException {
		return getColumnName(getLatitudeColumn());
	}
	
	/**
	 * Populate the column values in this record with the specified values.
	 * 
 	 * <p>
	 *   The entries in {@code dataFields} are assumed to have data in the same
	 *   column order as the column configuration for the record.
	 * </p>
	 * @param dataFields The input data fields
	 * @throws DataRecordException If the data fields do not match the columns/data types of the record
	 * @see DataRecord#DataRecord(int, ColumnConfig, List) 
	 */
	protected void setDataValues(List<String> dataFields) throws DataRecordException {
		for (int i = 1; i < dataFields.size(); i++) {
			
			DataColumn column = data.get(i);
			if (null == column) {
				throw new NoSuchColumnException(lineNumber, i);
			}
			
			column.setValue(dataFields.get(i));
		}
	}
	
	/**
	 * Returns the value of a named column
	 * @param columnName The name of the column
	 * @return The value of that column
	 * @throws NoSuchColumnException If the named column does not exist
	 */
	public String getValue(String columnName) throws NoSuchColumnException {
		return data.get(getColumnIndex(columnName)).getValue();
	}
	
	/**
	 * Returns the value held in the specified column
	 * @param columnIndex The 1-based column index
	 * @return The value of that column
	 * @throws NoSuchColumnException If the column does not exist
	 */
	public String getValue(int columnIndex) throws NoSuchColumnException {
		DataColumn column = data.get(columnIndex);
		if (null == column) {
			throw new NoSuchColumnException(lineNumber, columnIndex);
		}
		
		return column.getValue();
	}
	
	/**
	 * Returns the name of the column corresponding to the specified column index
	 * @param columnIndex The 1-based column index
	 * @return The column name
	 * @throws NoSuchColumnException If the column does not exist
	 */
	public String getColumnName(int columnIndex) throws NoSuchColumnException {
		DataColumn column = data.get(columnIndex);
		if (null == column) {
			throw new NoSuchColumnException(lineNumber, columnIndex);
		}

		return column.getName();
	}
	
	/**
	 * Returns the column names of each of the specified (1-based) column indices.
	 * @param columnIndices The column indices
	 * @return The column names
	 * @throws NoSuchColumnException If any of the column indices does not exist
	 */
	public TreeSet<String> getColumnNames(TreeSet<Integer> columnIndices) throws NoSuchColumnException {
		TreeSet<String> columnNames = new TreeSet<String>();
		for (int columnIndex : columnIndices) {
			columnNames.add(getColumnName(columnIndex));
		}
		return columnNames;
	}
	
	/**
	 * Returns the 1-based index of the named column
	 * @param columnName The column name
	 * @return The 1-based column index
	 * @throws NoSuchColumnException If the column does not exist
	 */
	public int getColumnIndex(String columnName) throws NoSuchColumnException {
		
		int result = -1;
		
		// The zeroth column is always empty
		for (int i = 1; i < data.size(); i++) {
			DataColumn column = data.get(i);
			if (column.getName().equals(columnName)) {
				result = column.getColumnIndex();
				break;
			}
		}
		
		if (result == -1) {
			throw new NoSuchColumnException(lineNumber, columnName);
		}
		
		return result;
	}
	
	/**
	 * Indicates whether or not questionable flags were raised during the processing of this
	 * data record
	 * @return {@code true} if questionable flags were raised; {@code false} otherwise.
	 */
	public boolean hasQuestionable() {
		return hasMessageWithFlag(Flag.QUESTIONABLE);
	}
	
	/**
	 * Indicates whether or not bad flags were raised during the processing of this
	 * data record
	 * @return {@code true} if bad flags were raised; {@code false} otherwise.
	 */
	public boolean hasBad() {
		return hasMessageWithFlag(Flag.BAD);
	}
	
	/**
	 * Indicates whether or not any messages with the specified flag were raised
	 * during the processing of this record
	 * @param flag The flag to be searched for
	 * @return {@code true} if flags of the specified type were raised; {@code false} otherwise.
	 */
	private boolean hasMessageWithFlag(Flag flag) {
		boolean result = false;
		
		for (Message message : messages) {
			if (message.getFlag().equals(flag)) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the list of all messages created during processing of this record
	 * @return The list of messages
	 */
	public List<Message> getMessages() {
		return messages;
	}
	
	/**
	 * Returns the line number in the original data file that this record came from
	 * @return The line number
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Adds a message to the set of messages assigned to this record,
	 * and optionally updates the record's flag to match
	 * @param message The message
	 * @throws NoSuchColumnException If the column specified in the message does not exist
	 */
	public void addMessage(Message message) throws NoSuchColumnException {
		
		for (int columnIndex : message.getColumnIndices()) {
			if (columnIndex != Message.NO_COLUMN_INDEX) {
				DataColumn column = data.get(columnIndex);
				if (null == column) {
					throw new NoSuchColumnException(lineNumber, columnIndex);
				}
				column.setFlag(message.getFlag());
			}
		}

		messages.add(message);
	}

	/**
	 * Replace all the messages for this record with the supplied list of messages.
	 * Optionally, the record's flags will also be reset according to the flags on the messages.
	 * @param messages The set of messages
	 * @throws NoSuchColumnException If any column specified in the messages does not exist 
	 */
	public void setMessages(List<Message> messages) throws NoSuchColumnException {
		clearMessages();
		for (Message message : messages) {
			addMessage(message);
		}
	}
	
	/**
	 * Replace all the messages for this record with the supplied message codes.
	 * The record's flags will also be reset according to the flags on the messages.
	 * @param codes The message codes
	 * @throws MessageException If a message cannot be rebuilt from a message code
	 * @throws NoSuchColumnException If any column specified in the messages does not exist
	 * @see RebuildCode 
	 */
	public void setMessages(String codes) throws MessageException, NoSuchColumnException {
		setMessages(RebuildCode.getMessagesFromRebuildCodes(codes));
	}
	
	/**
	 * Clear all messages from the record, and reset the flags to the default
	 * {@link Flag#GOOD}.
	 */
	private void clearMessages() {
		messages = new ArrayList<Message>();
		for (int i = 1; i < data.size(); i++) {
			data.get(i).resetFlag();
		}
	}

	/**
	 * Return a string containing the summary for all messages in this record
	 * @return The messages summary string
	 */
	public String getMessageSummaries() {
		StringBuffer summaries = new StringBuffer();
		for (int i = 0; i < messages.size(); i++) {
			summaries.append(messages.get(i).getShortMessage());
			if (i < messages.size() - 1) {
				summaries.append("; ");
			}
		}
		
		return summaries.toString();
	}
	
	/**
	 * Returns the complete {@code DataColumn} object for the named column
	 * @param columnName The column name
	 * @return The column's {@code DataColumn} object
	 * @throws NoSuchColumnException If the column does not exist
	 */
	public DataColumn getColumn(String columnName) throws NoSuchColumnException {
		return data.get(getColumnIndex(columnName));
	}
	
	/**
	 * Determines whether or not a named column exists in this record
	 * @param columnName The column name
	 * @return {@code true} if the column exists; {@code false} if it does not.
	 */
	public boolean columnExists(String columnName) {
		boolean result = false;
		
		for (DataColumn column : data) {
			if (column.getName().equalsIgnoreCase(columnName)) {
				result = true;
				break;
			}
		}
			
		return result;
	}
}
