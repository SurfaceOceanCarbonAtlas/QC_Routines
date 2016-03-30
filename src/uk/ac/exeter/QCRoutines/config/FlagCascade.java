package uk.ac.exeter.QCRoutines.config;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.NoSuchColumnException;
import uk.ac.exeter.QCRoutines.messages.Flag;

public class FlagCascade {

	private String destinationColumn;
	
	private Flag questionableCascadeFlag;
	
	private Flag badCascadeFlag;
	
	public FlagCascade(String destinationColumn, Flag questionableCascadeFlag, Flag badCascadeFlag) {
		this.destinationColumn = destinationColumn;
		this.questionableCascadeFlag = questionableCascadeFlag;
		this.badCascadeFlag = badCascadeFlag;
	}
	
	public void apply(DataRecord record, Flag sourceFlag) throws NoSuchColumnException {
		DataColumn column = record.getColumn(destinationColumn);
		if (sourceFlag.equals(Flag.QUESTIONABLE)) {
			column.setFlag(questionableCascadeFlag);
		} else if (sourceFlag.equals(Flag.BAD)) {
			column.setFlag(badCascadeFlag);
		}
	}
}
