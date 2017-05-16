package uk.ac.exeter.QCRoutines.routines.Dummy;

import java.util.List;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

/**
 * A dummy QC routine that will always generate messages. This is useful for
 * testing message handling without having to create data to trigger a 'normal'
 * routine.
 * 
 * <p>
 *   Messages are created as follows:
 * </p>
 * <ul>
 *   <li>
 *     The first 500 records are given messages with {@link Flag#QUESTIONABLE} flags.
 *   </li>
 *   <li>
 *     Records 501 to 1003 are given messages with {@link Flag#BAD} flags.
 *   </li>
 *   <li>
 *     The first 15 records are given messages with both {@link Flag#QUESTIONABLE} and {@link Flag#BAD} flags.
 *   </li>
 * </ul> 
 * 
 * @author Steve Jones
 * @see DummyMessage
 */
public class DummyRoutine extends Routine {

	/**
	 * Constructor - no action needed
	 */
	public DummyRoutine() {
		super();
	}
	
	@Override
	protected void processParameters(List<String> parameters) throws RoutineException {
		// Nothing to do
	}

	@Override
	protected void doRecordProcessing(List<DataRecord> records) throws RoutineException {

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
