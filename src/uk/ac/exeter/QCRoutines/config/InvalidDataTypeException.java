package uk.ac.exeter.QCRoutines.config;

/**
 * Exception thrown when a data type specified in the QC Routines
 * configuration is not recognised.
 * 
 * @author Steve Jones
 *
 */
public class InvalidDataTypeException extends Exception {

	/**
	 * The Serial Version UID
	 */
	private static final long serialVersionUID = 1717382421306145234L;

	/**
	 * Simple constructor
	 * @param dataType The invalid data type
	 */
	public InvalidDataTypeException(String dataType) {
		super("The data type '" + dataType + "' is invalid");
	}
	
}
