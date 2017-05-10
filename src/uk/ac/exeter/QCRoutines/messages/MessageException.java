package uk.ac.exeter.QCRoutines.messages;

/**
 * Exception for errors when working with messages and their rebuild codes.
 * @author Steve Jones
 *
 */
public class MessageException extends Exception {

	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = -1501643116718246827L;

	/**
	 * Simple constructor with an error message
	 * @param message The error message
	 */
	public MessageException(String message) {
		super(message);
	}
	
	/**
	 * Constructor for an error with an underlying cause
	 * @param message The error message
	 * @param cause The underlying cause
	 */
	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
