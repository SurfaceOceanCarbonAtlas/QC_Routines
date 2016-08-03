package uk.ac.exeter.QCRoutines.routines.Dummy;

import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

public class DummyRoutine extends Routine {

	public DummyRoutine() {
		super();
	}
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		// Nothing to do
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {

		// We make messages for groups of records, assuming they exist.
		
		for (int i = 1; i <= 500; i++) {
			
			if (i < records.size()) {
				try {
					addMessage(new DummyMessage(records.get(i).getLineNumber(), 1, "Col1", Flag.QUESTIONABLE, records.get(i).getValue(1)), records.get(i));
				} catch (Exception e) {
					throw new RoutineException("Error while checking records", e);
				}
			}
		}

		for (int i = 501; i <= 1003; i++) {
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
