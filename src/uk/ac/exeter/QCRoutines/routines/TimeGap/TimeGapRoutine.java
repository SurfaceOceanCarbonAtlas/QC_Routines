package uk.ac.exeter.QCRoutines.routines.TimeGap;

import java.util.List;

import org.joda.time.DateTime;

import uk.ac.exeter.QCRoutines.Routine;
import uk.ac.exeter.QCRoutines.RoutineException;
import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;

public class TimeGapRoutine extends Routine {

	private int gapLimit;

	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		if (parameters.size() != 1) {
			throw new RoutineException("Incorrect number of parameters - should be <gapLimit>");
		}
		
		try {
			gapLimit = Integer.parseInt(parameters.get(0));
		} catch (NumberFormatException e) {
			throw new RoutineException("Gap Limit parameter must be numeric");
		}
		
		if (gapLimit <= 0) {
			throw new RoutineException("Gap limit must be greater than zero");
		}
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {
		DateTime lastTime = null;
		
		for (DataRecord record : records) {
			if (null != lastTime) {
				DateTime recordTime = record.getTime();
				if (null != recordTime) {
					double gap = calcDayDiff(lastTime, recordTime);
					
					if (gap > gapLimit) {
						try {
							addMessage(new TimeGapMessage(record.getLineNumber(), gap, gapLimit), record);
						} catch (DataRecordException e) {
							throw new RoutineException("Error while adding message", e);
						}
					}
				}
			}
		
			// Record date ready for next record
			lastTime = record.getTime();
		}
	}
	
	/**
	 * Calculate the difference between two times in days
	 * @param time1 The first time
	 * @param time2 The second time
	 * @return The difference between the two times
	 */
	private double calcDayDiff(DateTime time1, DateTime time2) {
		long difference = time2.getMillis() - time1.getMillis();
		return (double) (difference / 3600000.0) / 24;
	}
}
