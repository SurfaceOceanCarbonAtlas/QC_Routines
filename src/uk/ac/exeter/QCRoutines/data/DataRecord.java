package uk.ac.exeter.QCRoutines.data;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.Message;
import uk.ac.exeter.QCRoutines.messages.MessageException;
import uk.ac.exeter.QCRoutines.messages.RebuildCode;

public abstract class DataRecord {

	/**
	 * The output messages generated for this line, if any
	 */
	protected List<Message> messages;
	
	/**
	 * The line of the input file that this record came from
	 */
	protected int lineNumber;
	
	/**
	 * The record data
	 */
	private List<DataColumn> data;
	
	
	public DataRecord(int lineNumber, ColumnConfig columnConfig) {
		this.messages = new ArrayList<Message>();
		this.lineNumber = lineNumber;
		this.data = columnConfig.getDataColumns(this);
	}
	
	/**
	 * Builds a complete record object
	 * @param dataFields The set of data values for the record, in the order specified by the column specification
	 * @param lineNumber The line number of the record
	 */
	public DataRecord(int lineNumber, ColumnConfig columnConfig, List<String> dataFields) throws DataRecordException {
		this.messages = new ArrayList<Message>();
		this.lineNumber = lineNumber;

		// Populate all the basic data columns
		setDataValues(dataFields);
	}
	
	/**
	 * Returns the date/time of this record as a single object.
	 * @return The date/time of this record.
	 */
	public abstract DateTime getTime();

	/**
	 * Returns the longitude of this record
	 * @return The longitude of this record
	 */
	public abstract double getLongitude();
	
	/**
	 * Returns the latitude of this record
	 * @return The latitude of this record
	 */
	public abstract double getLatitude();
	
	/**
	 * Populate all fields whose values are taken directly from the input data
	 * @param dataFields The input data fields
	 * @throws DataRecordException If the data fields do not match the columns/data types of the record 
	 */
	protected void setDataValues(List<String> dataFields) throws DataRecordException {
		for (int i = 0; i < dataFields.size(); i++) {
			
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
	 * @throws DataRecordException If the named column does not exist
	 */
	public String getValue(String columnName) throws NoSuchColumnException {
		return data.get(getColumnIndex(columnName)).getValue();
	}
	
	/**
	 * Returns the value held in the specified column
	 * @param columnIndex The 1-based column index
	 * @return The value of that column
	 * @throws DataRecordException If the column does not exist
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
	 * @throws DataRecordException If the column does not exist
	 */
	public String getColumnName(int columnIndex) throws NoSuchColumnException {
		DataColumn column = data.get(columnIndex);
		if (null == column) {
			throw new NoSuchColumnException(lineNumber, columnIndex);
		}

		return column.getName();
	}
	
	/**
	 * Returns the index of the named column
	 * @param columnIndex The column name
	 * @return The 1-based column index
	 * @throws DataRecordException If the column does not exist
	 */
	public int getColumnIndex(String columnName) throws NoSuchColumnException {
		
		int result = -1;
		
		for (DataColumn column : data) {
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
		return hasMessageWithFlag(Flag.BAD);	}
	
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
	 * @param updateFlag {@code true} if the record's flag should be updated; {@code false} if it should not.
	 * @throws NoSuchColumnException 
	 */
	public void addMessage(Message message) throws NoSuchColumnException {
		DataColumn column = data.get(message.getColumnIndex());
		if (null == column) {
			throw new NoSuchColumnException(lineNumber, message.getColumnIndex());
		}
		messages.add(message);
		column.setFlag(message.getFlag());
	}

	/**
	 * Replace all the messages for this record with the supplied list of messages.
	 * Optionally, the record's flags will also be reset according to the flags on the messages.
	 * @param messages The set of messages
	 * @param setFlag Indicates whether or not the record's flag is to be updated
	 * @throws NoSuchColumnException 
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
	 * @param messages The set of message codes
	 * @throws NoSuchColumnException 
	 */
	public void setMessages(String codes) throws MessageException, NoSuchColumnException {
		setMessages(RebuildCode.getMessagesFromRebuildCodes(codes));
	}
	
	/**
	 * Clear all messages from the record, and reset the flags to the default
	 * 'good' state.
	 */
	private void clearMessages() {
		messages = new ArrayList<Message>();
		for (DataColumn column : data) {
			column.resetFlag();
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
	
	public DataColumn getColumn(String columnName) throws NoSuchColumnException {
		return data.get(getColumnIndex(columnName));
	}
}
