package uk.ac.exeter.QCRoutines.data;

import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.config.FlagCascade;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.util.RoutineUtils;

public class DataColumn {

	private ColumnConfigItem columnConfig;
	
	private String value;
	
	private Flag flag = Flag.GOOD;
	
	private DataRecord parent;
	
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig) {
		this.parent = parent;
		this.columnConfig = columnConfig;
		this.value = null;
	}
	
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig, String value) throws InvalidDataException {
		this.parent = parent;
		this.columnConfig = columnConfig;
		this.value = value;
		validateDataType();
	}
	
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig, String value, Flag flag) throws InvalidDataException {
		this.parent = parent;
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
		validateDataType(value);
	}
	
	private void validateDataType(String value) throws InvalidDataException {
		
		if (null != value) {
			
			switch(columnConfig.getDataType()) {
			case ColumnConfig.TYPE_NUMERIC: {
				if (!RoutineUtils.isNumeric(value)) {
					throw new InvalidDataException(parent.getLineNumber(), this);
				}
				break;
			}
			case ColumnConfig.TYPE_BOOLEAN: {
				if (!RoutineUtils.isBoolean(value)) {
					throw new InvalidDataException(parent.getLineNumber(), this);
				}
				break;
			}
			}
		}
	}
	
	public int getColumnIndex() {
		return columnConfig.getIndex();
	}
	
	public String getDataType() {
		return columnConfig.getDataType();
	}
	
	public void resetFlag() {
		flag = Flag.GOOD;
	}
	
	public void setFlag(Flag flag) throws NoSuchColumnException {
		if (flag.moreSignificantThan(this.flag)) {
			this.flag = flag;

			if (flag.equals(Flag.QUESTIONABLE) || flag.equals(Flag.BAD)) {
				List<FlagCascade> cascades = columnConfig.getFlagCascades();
				for (FlagCascade cascade : cascades) {
					cascade.apply(parent, flag);
				}
			}
		}
	}
	
	public void setValue(String value) throws InvalidDataException {
		validateDataType(value);
		this.value = value;
	}
	
	public Flag getFlag() {
		return flag;
	}
}
