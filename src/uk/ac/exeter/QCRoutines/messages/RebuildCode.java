package uk.ac.exeter.QCRoutines.messages;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class RebuildCode {
	
	private static final int CODE_INDEX_CLASS_NAME = 0;
	
	private static final int CODE_INDEX_LINE_NUMBER = 1;
	
	private static final int CODE_INDEX_COLUMN_INDEX = 2;
	
	private static final int CODE_INDEX_COLUMN_NAME = 3;
	
	private static final int CODE_INDEX_FLAG_VALUE = 4;
	
	private static final int CODE_INDEX_FIELD_VALUE = 5;
	
	private static final int CODE_INDEX_VALID_VALUE = 6;

	private Class<?> messageClass;
	
	private int lineNumber;
	
	private int columnIndex;
	
	private String columnName;
	
	private int flagValue;
	
	String fieldValue;
	
	String validValue;
	
	public RebuildCode(Message message) throws MessageException {
		messageClass = message.getClass();
		lineNumber = message.getLineNumber();
		columnIndex = message.getColumnIndex();
		columnName = message.getColumnName();
		flagValue = message.getFlag().getFlagValue();
		fieldValue = message.getFieldValue();
		validValue = message.getValidValue();
		validateMessageClass();
	}
	
	public RebuildCode(String code) throws RebuildCodeException {
			
		String[] codeComponents = code.split("_");
		if (codeComponents.length != 7) {
			throw new RebuildCodeException("Incorrect number of elements");
		} else {
			
			try {
				messageClass = Class.forName(codeComponents[CODE_INDEX_CLASS_NAME]);
			} catch (ClassNotFoundException e) {
				throw new RebuildCodeException("Cannot find message class '" + codeComponents[0] + "'");
			}
			
			try {
				lineNumber = Integer.parseInt(codeComponents[CODE_INDEX_LINE_NUMBER]);
				if (lineNumber < 1) {
					throw new RebuildCodeException("Invalid line number");
				}
			} catch (NumberFormatException e) {
				throw new RebuildCodeException("Unparseable line number value");
			}
			
			try {
				columnIndex = Integer.parseInt(codeComponents[CODE_INDEX_COLUMN_INDEX]);
				if (columnIndex < 0) {
					throw new RebuildCodeException("Invalid column index value");
				}
			} catch (NumberFormatException e) {
				throw new RebuildCodeException("Unparseable column index value");
			}
			
			columnName = codeComponents[CODE_INDEX_COLUMN_NAME];
			
			try {
				flagValue = Integer.parseInt(codeComponents[CODE_INDEX_FLAG_VALUE]);
				if (!Flag.isValidFlagValue(flagValue)) {
					throw new RebuildCodeException("Invalid severity value");
				}
			} catch (NumberFormatException e) {
				throw new RebuildCodeException("Unparseable severity value");
			}
			
			fieldValue = codeComponents[CODE_INDEX_FIELD_VALUE];
			validValue = codeComponents[CODE_INDEX_VALID_VALUE];
		}
	}
	
	private void validateMessageClass() throws MessageException {
		// Message classes must have a constructor that takes
		// int columnIndex, String columnName, Flag flag, int lineNumber, String fieldValue, String validValue
		
		try {
			messageClass.getConstructor(int.class, int.class, String.class, Flag.class, String.class, String.class);
		} catch (Exception e) {
			throw new RebuildCodeException("Message class does not have the required constructor"); 
		}
	}
	
	public String getCode() {
		StringBuffer result = new StringBuffer();
		result.append(messageClass.getName());
		result.append('_');
		result.append(lineNumber);
		result.append('_');
		result.append(columnIndex);
		result.append('_');
		result.append(columnName);
		result.append('_');
		result.append(flagValue);
		result.append('_');
		result.append(fieldValue);
		result.append('_');
		result.append(validValue);
		result.append(';');
		
		return result.toString();
	}
	
	public String toString() {
		return getCode();
	}
	
	public Message getMessage() throws MessageException {
		try {
			Constructor<?> messageConstructor = messageClass.getConstructor(int.class, int.class, String.class, Flag.class, String.class, String.class);
			return (Message) messageConstructor.newInstance(lineNumber, columnIndex, columnName, new Flag(flagValue), fieldValue, validValue);
		} catch (Exception e) {
			throw new MessageException("Error while constructing message object from rebuild code", e);
		}
	}
	
	public static List<Message> getMessagesFromRebuildCodes(String codes) throws MessageException {

		List<Message> messages = new ArrayList<Message>();

		String codeString = codes.trim();
		if (codeString.length() > 0) {
			String[] splitCodes = codeString.split(";");
			for (int i = 0; i < splitCodes.length; i++) {
				messages.add(new RebuildCode(splitCodes[i]).getMessage());
			}
		}
		
		return messages;
	}
	
	public static String getRebuildCodes(List<Message> messages) throws MessageException {
		StringBuffer codes = new StringBuffer();
		for (int i = 0; i < messages.size(); i++) {
			codes.append(new RebuildCode(messages.get(i)).getCode());
		}
		
		return codes.toString();
	}
}
