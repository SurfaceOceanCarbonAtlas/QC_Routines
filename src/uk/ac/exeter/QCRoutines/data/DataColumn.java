package uk.ac.exeter.QCRoutines.data;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.util.RoutineUtils;

public class DataColumn {

	private ColumnConfigItem columnConfig;
	
	private String value;
	
	private Flag flag = Flag.GOOD;
	
	public DataColumn(ColumnConfigItem columnConfig) {
		this.columnConfig = columnConfig;
		this.value = null;
	}
	
	public DataColumn(ColumnConfigItem columnConfig, String value) throws InvalidDataException {
		this.columnConfig = columnConfig;
		this.value = value;
		validateDataType();
	}
	
	public DataColumn(ColumnConfigItem columnConfig, String value, Flag flag) throws InvalidDataException {
		this.columnConfig = columnConfig;
		this.value = value;
		this.flag = flag;
		validateDataType();
	}
	
	public String getName() {
		return columnConfig.getColumnName();
	}
	
	public String getValue() {
		return value;
	}
	
	private void validateDataType() throws InvalidDataException {
		
		if (null != value) {
			
			switch(columnConfig.getDataType()) {
			case ColumnConfig.TYPE_NUMERIC: {
				if (!RoutineUtils.isNumeric(value)) {
					throw new InvalidDataException(this, "Value '" + value + "' is not numeric");
				}
				break;
			}
			case ColumnConfig.TYPE_BOOLEAN: {
				if (!RoutineUtils.isBoolean(value)) {
					throw new InvalidDataException(this, "Value '" + value + "' is not a boolean");
				}
				break;
			}
			}
		}
	}
}
