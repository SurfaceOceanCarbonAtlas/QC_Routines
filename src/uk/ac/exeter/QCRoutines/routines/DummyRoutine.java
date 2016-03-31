package uk.ac.exeter.QCRoutines.routines;

import java.util.List;

import uk.ac.exeter.QCRoutines.Routine;
import uk.ac.exeter.QCRoutines.RoutineException;
import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.messages.Flag;

public class DummyRoutine extends Routine {

	public DummyRoutine() {
		super();
	}
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		System.out.println("Dummy routine initialised with parameter '" + parameters.get(0) + "'");
		
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {
		
		System.out.println("Dummy QC Routine was passed " + records.size() + " records");

		// We make messages for groups of records, assuming they exist.
		// The first five records get warnings on the 1st column. The second five get errors on the 2nd column.
		// The third five get both.
		
		for (int i = 1; i <= 5; i++) {
			
			if (i < records.size()) {
				try {
					addMessage(new DummyMessage(records.get(i).getLineNumber(), 1, "Col1", Flag.QUESTIONABLE, records.get(i).getValue(1)), records.get(i));
				} catch (Exception e) {
					throw new RoutineException("Error while checking records", e);
				}
			}
		}

		for (int i = 6; i <= 10; i++) {
			if (i < records.size()) {
				try {
					addMessage(new DummyMessage(records.get(i).getLineNumber(), 2, "Col2", Flag.BAD, records.get(i).getValue(1)), records.get(i));
				} catch (Exception e) {
					throw new RoutineException("Error while checking records", e);
				}
			}
		}

		for (int i = 1; i <= 15; i++) {
			if (i < records.size()) {
				try {
					addMessage(new DummyMessage(records.get(i).getLineNumber(), 1, "Col1", Flag.QUESTIONABLE, records.get(i).getValue(1)), records.get(i));
					addMessage(new DummyMessage(records.get(i).getLineNumber(), 2, "Col2", Flag.BAD, records.get(i).getValue(1)), records.get(i));
				} catch (Exception e) {
					throw new RoutineException("Error while checking records", e);
				}
			}
		}

	}
}
