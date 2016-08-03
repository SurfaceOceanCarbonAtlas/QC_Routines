package uk.ac.exeter.QCRoutines.config;

public class InvalidDataTypeException extends Exception {

	private static final long serialVersionUID = 1717382421306145234L;

	public InvalidDataTypeException(String dataType) {
		super("The data type '" + dataType + "' is invalid");
	}
	
}
