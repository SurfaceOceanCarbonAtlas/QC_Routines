package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import java.util.List;

import org.joda.time.DateTime;

import uk.ac.exeter.QCRoutines.Routine;
import uk.ac.exeter.QCRoutines.RoutineException;
import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.data.DataRecordException;
import uk.ac.exeter.QCRoutines.messages.Flag;

public class ShipSpeedRoutine extends Routine {

	/**
	 * The radius of the earth in kilometres
	 */
	private static final double EARTH_RADIUS = 6367.5;
	
	private double badSpeedLimit = 0;
	
	private double questionableSpeedLimit = 0;
	
	@Override
	public void initialise(List<String> parameters, ColumnConfig columnConfig) throws RoutineException {
		if (parameters.size() < 2) {
			throw new RoutineException("Must supply two parameters: Questionable Speed Limit and Bad Speed Limit");
		}
		
		try {
			questionableSpeedLimit = Double.parseDouble(parameters.get(0));
			badSpeedLimit = Double.parseDouble(parameters.get(1));
			
		} catch(NumberFormatException e) {
			throw new RoutineException("All speed parameters must be numeric");
		}
		
		if (questionableSpeedLimit > badSpeedLimit) {
			throw new RoutineException("Bad speed limit must be >= Questionable speed limit"); 
		}
	}

	@Override
	public void processRecords(List<DataRecord> records) throws RoutineException {
		
		try {
			DataRecord lastRecord = null;
			
			for (DataRecord currentRecord : records) {
				if (null != lastRecord) {
					double lastLon = lastRecord.getLongitude();
					double lastLat = lastRecord.getLatitude();
					DateTime lastTime = lastRecord.getTime();
					
					double thisLon = currentRecord.getLongitude();
					double thisLat = currentRecord.getLatitude();
					DateTime thisTime = currentRecord.getTime();
					
					if (null != lastTime && null != thisTime) {
					
						double distance = calcDistance(lastLon, lastLat, thisLon, thisLat);
						double hourDiff = calcHourDiff(lastTime, thisTime);
						
						if (hourDiff <= 0.0) {
							addMessage(new BackwardsTimeMessage(currentRecord.getLineNumber()), currentRecord);
						} else if (calcSecondsDiff(lastTime, thisTime) > 1) {
							double speed = distance / hourDiff;
							if (speed > badSpeedLimit) {
								addMessage(new ShipSpeedMessage(currentRecord.getLineNumber(), Flag.BAD, String.valueOf(speed), String.valueOf(badSpeedLimit)), currentRecord);
							} else if (speed > questionableSpeedLimit) {
								addMessage(new ShipSpeedMessage(currentRecord.getLineNumber(), Flag.QUESTIONABLE, String.valueOf(speed), String.valueOf(questionableSpeedLimit)), currentRecord);
							}
						}
					}
				}
				
				lastRecord = currentRecord;
			}
		} catch (DataRecordException e) {
			throw new RoutineException("Error while setting record message", e);
		}
		
	}

	/**
	 * Calculate the distance between two points in kilometres
	 * @param lon1 The longitude of the first point
	 * @param lat1 The latitude of the first point
	 * @param lon2 The longitude of the second point
	 * @param lat2 The latitude of the second point
	 * @return The distance between the two points
	 */
	private double calcDistance(double lon1, double lat1, double lon2, double lat2) {
		
		double lon1Rad = calcRadians(lon1);
		double lat1Rad = calcRadians(lat1);
		double lon2Rad = calcRadians(lon2);
		double lat2Rad = calcRadians(lat2);
		
		double deltaLon = lon2Rad - lon1Rad;
		double deltaLat = lat2Rad - lat2Rad;
		
		double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(deltaLon / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		
		return c * EARTH_RADIUS;
	}
	
	/**
	 * Convert degrees to radians
	 * @param degrees The degrees value to be converted
	 * @return The radians value
	 */
	private double calcRadians(double degrees) {
		return degrees * (Math.PI / 180);
	}
	
	/**
	 * Calculate the difference between two times in hours
	 * @param time1 The first time
	 * @param time2 The second time
	 * @return The difference between the two times
	 */
	private double calcHourDiff(DateTime time1, DateTime time2) {
		long difference = time2.getMillis() - time1.getMillis();
		return (double) difference / 3600000.0;
	}
	
	/**
	 * Calculate the difference between two times in seconds
	 * @param time1 The first time
	 * @param time2 The second time
	 * @return The difference between the two times
	 */
	private double calcSecondsDiff(DateTime time1, DateTime time2) {
		long difference = time2.getMillis() - time1.getMillis();
		return (double) difference / 1000.0;
	}
}
