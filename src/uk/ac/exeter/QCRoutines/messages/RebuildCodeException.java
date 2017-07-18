package uk.ac.exeter.QCRoutines.messages;

/**
 * Exception thrown from invalid rebuild code strings
 * @author Steve Jones
 * @see RebuildCode
 */
public class RebuildCodeException extends MessageException {

	/**
	 * The serial version UID
	 */
	private static final long serialVersionUID = -8843114125586814644L;

	/**
	 * Basic constructor
	 * @param message The error message
	 */
	public RebuildCodeException(String message) {
		super("Invalid message rebuild code: " + message);
	}
}
