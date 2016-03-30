package uk.ac.exeter.QCRoutines.routines.ShipSpeed;

import java.util.List;

import uk.ac.exeter.QCRoutines.Routine;
import uk.ac.exeter.QCRoutines.RoutineException;
import uk.ac.exeter.QCRoutines.data.DataRecord;

public class ShipSpeedRoutine extends Routine {

	private double badSpeedLimit = 0;
	
	private double questionableSpeedLimit = 0;
	
	@Override
	public void initialise(List<String> parameters) throws RoutineException {
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
		// TODO Auto-generated method stub
		
	}

}
