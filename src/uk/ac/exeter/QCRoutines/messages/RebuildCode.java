package uk.ac.exeter.QCRoutines.messages;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RebuildCode {
	
	private static final int CODE_INDEX_CLASS_NAME = 0;
	
	private static final int CODE_INDEX_LINE_NUMBER = 1;
	
	private static final int CODE_INDEX_COLUMN_INDEX = 2;
	
	private static final int CODE_INDEX_COLUMN_NAME = 3;
	
	private static final int CODE_INDEX_FLAG_VALUE = 4;
	
	private static final int CODE_INDEX_FIELD_VALUE = 5;
	
	private static final int CODE_INDEX_VALID_VALUE = 6;

	private Class<? extends Message> messageClass;
	
	private int lineNumber;
	
	private TreeSet<Integer> columnIndices;
	
	private TreeSet<String> columnNames;
	
	private int flagValue;
	
	String fieldValue;
	
	String validValue;
	
	public RebuildCode(Message message) throws MessageException {
		messageClass = message.getClass();
		lineNumber = message.getLineNumber();
		columnIndices = message.getColumnIndices();
		columnNames = message.getColumnNames();
		flagValue = message.getFlag().getFlagValue();
		fieldValue = message.getFieldValue();
		validValue = message.getValidValue();
		
		Message.checkBasicConstructor(messageClass);
	}
	
	@SuppressWarnings("unchecked")
	public RebuildCode(String code) throws RebuildCodeException {
			
		String[] codeComponents = code.split("_");
		if (codeComponents.length != 7) {
			throw new RebuildCodeException("Incorrect number of elements");
		} else {
			
			try {
				messageClass = (Class<? extends Message>) Class.forName(codeComponents[CODE_INDEX_CLASS_NAME]);
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
				columnIndices = new TreeSet<Integer>();
				
				String[] indices = codeComponents[CODE_INDEX_COLUMN_INDEX].split("|");
				for (String indexString : indices) {
					int columnIndex = Integer.parseInt(indexString);
					if (columnIndex < 0) {
						throw new RebuildCodeException("Invalid column index value");
					}

					columnIndices.add(columnIndex);
				}
				
			} catch (NumberFormatException e) {
				throw new RebuildCodeException("Unparseable column index value");
			}
			
			
			columnNames = new TreeSet<String>(Arrays.asList(codeComponents[CODE_INDEX_COLUMN_NAME].split("|")));
			
			try {
				flagValue = Integer.parseInt(codeComponents[CODE_INDEX_FLAG_VALUE]);
				if (!Flag.isValidFlagValue(flagValue)) {
					throw new RebuildCodeException("Invalid flag value");
				}
			} catch (NumberFormatException e) {
				throw new RebuildCodeException("Unparseable flag value");
			}
			
			fieldValue = codeComponents[CODE_INDEX_FIELD_VALUE];
			validValue = codeComponents[CODE_INDEX_VALID_VALUE];
		}
	}
	
	public String getCode() {
		StringBuffer result = new StringBuffer();
		result.append(messageClass.getName());
		result.append('_');
		result.append(lineNumber);
		result.append('_');
		
		int indexCount = 0;
		for (int columnIndex : columnIndices) {
			indexCount++;
			result.append(columnIndex);
			if (indexCount < columnIndices.size()) {
				result.append('|');
			}
		}
		
		result.append('_');

		int nameCount = 0;
		for (String columnName : columnNames) {
			nameCount++;
			result.append(columnName);
			if (nameCount < columnNames.size()) {
				result.append('|');
			}
		}
		
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
			Constructor<?> messageConstructor = messageClass.getConstructor(int.class, Set.class, Set.class, Flag.class, String.class, String.class);
			return (Message) messageConstructor.newInstance(lineNumber, columnIndices, columnNames, new Flag(flagValue), fieldValue, validValue);
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
