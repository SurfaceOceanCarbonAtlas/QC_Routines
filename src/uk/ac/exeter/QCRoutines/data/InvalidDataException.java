package uk.ac.exeter.QCRoutines.data;

public class InvalidDataException extends Exception {

	private static final long serialVersionUID = -3199169220532474396L;

	private DataColumn sourceColumn;
	
	public InvalidDataException(DataColumn sourceColumn, String message) {
		super(message);
		this.sourceColumn = sourceColumn;
	}
	
	@Override
	public String getMessage() {
		StringBuffer fullMessage = new StringBuffer();
		fullMessage.append("Column '");
		fullMessage.append(sourceColumn.getName());
		fullMessage.append(": ");
		fullMessage.append(super.getMessage());
		
		return fullMessage.toString();
	}
}
