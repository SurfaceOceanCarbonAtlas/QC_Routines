package uk.ac.exeter.QCRoutines.config;

public class FlagCascade {

	private String destinationColumn;
	
	private int questionableCascadeFlag;
	
	private int badCascadeFlag;
	
	public FlagCascade(String destinationColumn, int questionableCascadeFlag, int badCascadeFlag) {
		this.destinationColumn = destinationColumn;
		this.questionableCascadeFlag = questionableCascadeFlag;
		this.badCascadeFlag = badCascadeFlag;
	}
}
