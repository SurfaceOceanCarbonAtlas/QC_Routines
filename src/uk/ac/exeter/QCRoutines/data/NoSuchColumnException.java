package uk.ac.exeter.QCRoutines.data;

public class NoSuchColumnException extends DataRecordException {

	private static final long serialVersionUID = 3997426626030321279L;

	public NoSuchColumnException(int lineNumber, int columnIndex) {
		super(lineNumber, "There is no column with index " + columnIndex);
	}
	
	public NoSuchColumnException(int lineNumber, String columnName) {
		super(lineNumber, "There is no column with the name '" + columnName + "'");
	}
	
}
