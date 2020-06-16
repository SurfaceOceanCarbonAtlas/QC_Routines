package uk.ac.exeter.QCRoutines.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores a summary of all {@link Message}s attached to a data record.
 * 
 * <p>
 *   Messages are grouped by a combination of the message class (indicating
 *   the type of problem raised) and the column indices to which those messages
 *   are applied. Note that different combinations of column indices are considered
 *   unique, so a message of a given class raised for columns 1 and 5 will be considered
 *   separately to message of the same class raised for column 1 alone.
 * </p>
 * 
 * <p>
 *   A complete message summary can be created from a set of messages by calling
 *   {@link #getMessageSummaries(List)}.
 * </p>
 * 
 * @author Steve Jones
 * @see Message
 * @see MessageKey
 */
public class MessageSummary {

	/**
	 * The short version of the message text
	 * @see Message#getShortMessage()
	 */
	private String message;
	
	/**
	 * The number of {@link Flag#QUESTIONABLE} flags raised in this message group
	 */
	private int questionableCount = 0;

	/**
	 * The number of {@link Flag#BAD} flags raised in this message group
	 */
	private int badCount = 0;
	
	/**
	 * Create a message summary for a specific message string.
	 * @param message The message
	 */
	private MessageSummary(String message) {
		this.message = message;
	}
	
	/**
	 * Construct a complete message summary for a set of messages
	 * @param messages The messages for which a summary must be constructed
	 * @return The message summary
	 */
	public static List<MessageSummary> getMessageSummaries(List<Message> messages) {
		
		Map<MessageKey, MessageSummary> summaryMap = new HashMap<MessageKey, MessageSummary>();
		
		for (Message message : messages) {
			MessageKey key = message.generateMessageKey();
			if (!summaryMap.containsKey(key)) {
				summaryMap.put(key, new MessageSummary(message.getShortMessage()));
			}
			
			summaryMap.get(key).addInstance(message.getFlag());
		}
		
		return new ArrayList<MessageSummary>(summaryMap.values());
	}
	
	/**
	 * Get the message for a given message group
	 * @return The message
	 */
	public String getMessageString() {
		return message;
	}
	
	/**
	 * Get the number of {@link Flag#QUESTIONABLE} flags raised in this message group
	 * @return The number of {@link Flag#QUESTIONABLE} flags raised in this message group
	 */
	public int getQuestionableCount() {
		return questionableCount;
	}
	
	/**
	 * Get the number of {@link Flag#BAD} flags raised in this message group
	 * @return The number of {@link Flag#BAD} flags raised in this message group
	 */
	public int getBadCount() {
		return badCount;
	}
	
	/**
	 * Add a flag instance to this message group within the summary.
	 * Only {@link Flag#QUESTIONABLE} and {@link Flag#BAD} flags
	 * are added - all others are ignored. The relevant flag count
	 * is incremented.
	 * @param flag The flag to be added.
	 */
	private void addInstance(Flag flag) {
		if (flag.equals(Flag.BAD)) {
			badCount++;
		} else if (flag.equals(Flag.QUESTIONABLE)) {
			questionableCount++;
		}
	}
}
