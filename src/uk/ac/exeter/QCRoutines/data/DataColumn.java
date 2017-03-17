package uk.ac.exeter.QCRoutines.data;

import java.util.List;

import uk.ac.exeter.QCRoutines.config.ColumnConfig;
import uk.ac.exeter.QCRoutines.config.ColumnConfigItem;
import uk.ac.exeter.QCRoutines.config.FlagCascade;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.util.RoutineUtils;

/**
 * A class to hold a data value in a {@link DataRecord}.
 * Provides useful methods to interrogate its properties and
 * set flags.
 * @author Steve Jones
 */
public class DataColumn {

	/**
	 * The configuration for the column
	 */
	protected ColumnConfigItem columnConfig;
	
	/**
	 * The column value
	 */
	private String value;
	
	/**
	 * The flag set on the record
	 */
	private Flag flag = Flag.GOOD;
	
	/**
	 * The {@link DataRecord} to which this column value belongs
	 */
	private DataRecord parent;
	
	/**
	 * Creates a placeholder for a data value in a given column, without setting a value or {@link Flag}.
	 * @param parent The parent data record
	 * @param columnConfig The configuration for the column
	 */
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig) {
		this.parent = parent;
		this.columnConfig = columnConfig;
		this.value = null;
	}
	
	/**
	 * Creates a data value for a column with the default {@link Flag#GOOD} flag.
	 * @param parent The parent data record
	 * @param columnConfig The configuration for the column
	 * @param value The data value
	 * @throws InvalidDataException If the data value is invalid
	 */
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig, String value) throws InvalidDataException {
		this.parent = parent;
		this.columnConfig = columnConfig;
		this.value = value;
		validateDataType();
	}
	
	/**
	 * Creates a data value for a column with a specified flag.
	 * @param parent The parent data record
	 * @param columnConfig The configuration for the column
	 * @param value The data value
	 * @param flag The flag
	 * @throws InvalidDataException If the data value is of the wrong data type
	 */
	public DataColumn(DataRecord parent, ColumnConfigItem columnConfig, String value, Flag flag) throws InvalidDataException {
		this.parent = parent;
		this.columnConfig = columnConfig;
		this.value = value;
		this.flag = flag;
		validateDataType();
	}
	
	/**
	 * Returns the name of the column
	 * @return The column name
	 */
	public String getName() {
		return columnConfig.getColumnName();
	}
	
	/**
	 * Returns the data value
	 * @return The data value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Validate the stored data value to make sure it is valid for the column's data type
	 * @throws InvalidDataException If the value is invalid
	 * @see #value
	 */
	private void validateDataType() throws InvalidDataException {
		validateDataType(value);
	}
	
	/**
	 * Validate a data value to make sure it is valid for the column's data type
	 * @param value The value to be checked
	 * @throws InvalidDataException If the value is invalid
	 */
	private void validateDataType(String value) throws InvalidDataException {
		if (null != value) {
			
			switch(columnConfig.getDataType()) {
			case ColumnConfigItem.TYPE_NUMERIC: {
				if (!RoutineUtils.isNumeric(value)) {
					throw new InvalidDataException(parent.getLineNumber(), this);
				}
				break;
			}
			case ColumnConfigItem.TYPE_BOOLEAN: {
				if (!RoutineUtils.isBoolean(value)) {
					throw new InvalidDataException(parent.getLineNumber(), this);
				}
				break;
			}
			}
		}
	}
	
	/**
	 * Returns the column index (1-based)
	 * @return The column index
	 */
	public int getColumnIndex() {
		return columnConfig.getColumnIndex();
	}
	
	/**
	 * Returns the data type of the column
	 * @return The data type
	 */
	public String getDataType() {
		return columnConfig.getDataType();
	}
	
	/**
	 * Reset the flag to the default {@link Flag#GOOD}.
	 */
	public void resetFlag() {
		flag = Flag.GOOD;
	}
	
	/**
	 * Set the flag for the data value. Any flag cascades are also triggered.
	 * @param flag The flag
	 * @throws NoSuchColumnException If any cascade columns are missing
	 * @see ColumnConfig
	 */
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
	
	/**
	 * Sets the column value
	 * @param value The value
	 * @throws InvalidDataException If the value is of the wrong data type
	 */
	public void setValue(String value) throws InvalidDataException {
		validateDataType(value);
		this.value = value;
	}
	
	/**
	 * Returns the flag set on the column
	 * @return The flag
	 */
	public Flag getFlag() {
		return flag;
	}
	
	/**
	 * Determines whether or not this value is empty.
	 * @return {@code true} if the value is empty; {@code false} if it contains a value
	 * @see RoutineUtils#isEmpty(String...)
	 */
	public boolean isEmpty() {
		return RoutineUtils.isEmpty(value);
	}
	
	/**
	 * Determines whether or not a value is required for this column
	 * @return {@code true} if a value is required; {@code false} if it is not
	 */
	public boolean isRequired() {
		return columnConfig.getRequired();
	}
}
