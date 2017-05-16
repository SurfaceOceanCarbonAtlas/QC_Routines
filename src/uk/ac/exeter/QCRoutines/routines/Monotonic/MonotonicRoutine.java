package uk.ac.exeter.QCRoutines.routines.Monotonic;

import java.util.List;

import org.joda.time.DateTime;

import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;

/**
 * Ensures that all records in a file are monotonic, i.e. arranged in ascending date/time order
 * @author Steve Jones
 *
 */
public class MonotonicRoutine extends Routine {

	/**
	 * The flag to set on messages generated for failed monotonic checks
	 */
	private Flag failureFlag;
	
	@Override
	protected void processParameters(List<String> parameters) throws RoutineException {

		boolean paramsOK = true;
		
		String param = parameters.get(0);
		if (null == param) {
			paramsOK = false;
		} else if (param.equalsIgnoreCase("bad") || param.equals(Flag.VALUE_BAD)) {
			failureFlag = Flag.BAD;
		} else if (param.equalsIgnoreCase("fatal") || param.equals(Flag.VALUE_FATAL)) {
			failureFlag = Flag.FATAL;
		} else {
			paramsOK = false;
		}
		
		if (!paramsOK) {
			throw new RoutineException("You must specify a parameter of Bad or Fatal for records that fail the monotonic check");
		}
	}

	@Override
	protected void doRecordProcessing(List<DataRecord> records) throws RoutineException {
		try {
			DataRecord lastRecord = null;
			
			for (DataRecord currentRecord : records) {
				if (null != lastRecord) {
					double hourDiff = calcHourDiff(lastRecord, currentRecord);
					
					if (hourDiff <= 0.0) {
						addMessage(new MonotonicMessage(currentRecord, failureFlag), currentRecord);
					}
				}
				
				lastRecord = currentRecord;
			}
		} catch (DataRecordException e) {
			throw new RoutineException("Error while setting record message", e);
		} catch (MissingTimeException e) {
			try {
				addMessage(new MissingTimeMessage(e.getRecord()), e.getRecord());
			} catch (Exception e2) {
				throw new RoutineException("Error while adding message to record", e2);
			}
		}
	}

	/**
	 * Calculate the time difference (in hours) between two records. A positive result
	 * indicates that the second record is after the first record.
	 * 
	 * <p>
	 * Differences of portions of an hour are returned as decimal fractions. e.g. a difference
	 * of 90 minutes gives a result of {@code 1.5}.
	 * </p>
	 * 
	 * @param firstRecord The first record
	 * @param secondRecord The second record
	 * @return The time difference between the records
	 * @throws DataRecordException If there is an error while getting the date/time from a record
	 * @throws MissingTimeException If one of the records does not have a date/time
	 */
	public static double calcHourDiff(DataRecord firstRecord, DataRecord secondRecord) throws DataRecordException, MissingTimeException {
		DateTime firstTime = firstRecord.getTime();
		DateTime secondTime = secondRecord.getTime();

		double result;
		
		if (null == firstTime) {
			throw new MissingTimeException(firstRecord);
		} else if (null == secondTime) {
			throw new MissingTimeException(secondRecord);
		} else {
			long difference = secondTime.getMillis() - firstTime.getMillis();
			result = (double) difference / 3600000.0;
		}
		
		return result;
	}
}
