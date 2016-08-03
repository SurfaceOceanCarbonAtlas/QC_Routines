package uk.ac.exeter.QCRoutines.messages;

public class InvalidFlagException extends Exception {

	private static final long serialVersionUID = 1787207060024257945L;

	public InvalidFlagException(int flagValue) {
		super("Invalid flag value " + flagValue);
	}
	
	public InvalidFlagException(String message) {
		super(message);
	}
}
