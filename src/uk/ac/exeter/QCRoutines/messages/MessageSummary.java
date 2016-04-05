package uk.ac.exeter.QCRoutines.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageSummary {

	private String message;
	
	private int questionableCount = 0;

	private int badCount = 0;
	
	private MessageSummary(String message) {
		this.message = message;
	}
	
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
	
	public String getMessageString() {
		return message;
	}
	
	public int getQuestionableCount() {
		return questionableCount;
	}
	
	public int getBadCount() {
		return badCount;
	}
	
	private void addInstance(Flag flag) {
		if (flag.equals(Flag.BAD)) {
			badCount++;
		} else if (flag.equals(Flag.QUESTIONABLE)) {
			questionableCount++;
		}
	}
}
