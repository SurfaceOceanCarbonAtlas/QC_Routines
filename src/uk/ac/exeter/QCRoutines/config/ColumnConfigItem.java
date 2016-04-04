package uk.ac.exeter.QCRoutines.config;

import java.util.Arrays;
import java.util.List;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.InvalidFlagException;

/**
 * Holds details of the configuration for a single SOCAT column
 */
public class ColumnConfigItem {
		
	/**
	 * The String data type identifier
	 */
	public static final String TYPE_STRING = "S";
	
	/**
	 * The Numeric data type identifier
	 */
	public static final String TYPE_NUMERIC = "N";
	
	/**
	 * The Boolean data type identifier
	 */
	public static final String TYPE_BOOLEAN = "B";
	
	/**
	 * The name of the column
	 */
	private String columnName;
	
	/**
	 * The index of the column in the data file
	 */
	private int columnIndex;
	
	/**
	 * The line in the configuration file that defines this column
	 */
	private int configFileLine;
	
	/**
	 * The data type of the column
	 */
	private String dataType;
	
	/**
	 * Indicates whether or not this column is required
	 */
	private boolean required;
	
	/**
	 * The flag cascade configuration String, which must be parsed
	 * before flag cascades will work
	 */
	private String flagCascadeConfig;
	
	/**
	 * The list of flag cascades for this column
	 */
	private List<FlagCascade> flagCascades = null;

	
	public ColumnConfigItem(int configFileLine, int columnIndex) {
		this.configFileLine = configFileLine;
		this.columnIndex = columnIndex;
	}
	
	/**
	 * Returns the name of the output SOCAT column that this configuration item refers to.
	 * @return
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * Determines whether or not this column is required to have a value.
	 * If the column is part of a required group, it is deemed to be required
	 * in this function.
	 * 
	 * @return {@code} true if the column must contain a value; {@code false} otherwise. 
	 */
	public boolean getRequired() {
		return required;
	}
	
	/**
	 * Returns the index of this column in the data file
	 * @return The index of this column in the data file
	 */
	public int getColumnIndex() {
		return columnIndex;
	}
	
	/**
	 * Returns the data type of this column
	 * @return The data type
	 */
	public String getDataType() {
		return dataType;
	}
	
	/**
	 * Parse the flag cascade configuration string.
	 * @param columnConfig The complete column configuration
	 * @throws ConfigException If the configuration is invalid
	 */
	protected void parseFlagCascade(ColumnConfig columnConfig) throws ConfigException {
		if (null != flagCascadeConfig && flagCascadeConfig.length() > 0) {
			List<String> cascades = Arrays.asList(flagCascadeConfig.split(";"));
			for (String cascade : cascades) {
				List<String> cascadeFields = Arrays.asList(cascade.split("|"));
				if (cascadeFields.size() != 3) {
					throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Invalid flag cascade string");
				} else {
					
					try {
						String column = cascadeFields.get(0);
						int questionableFlagCascade = Integer.parseInt(cascadeFields.get(1));
						int badFlagCascade = Integer.parseInt(cascadeFields.get(2));
						
						if (!columnConfig.hasColumn(column)) {
							throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Flag cascade destination column '" + column + "' does not exist");
						}
						
						if (!Flag.isValidFlagValue(questionableFlagCascade)) {
							throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Invalid questionable cascade value for destionation column '" + column + "'");
						}
						
						if (!Flag.isValidFlagValue(badFlagCascade)) {
							throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Invalid bad cascade value for destionation column '" + column + "'");
						}
						
						flagCascades.add(new FlagCascade(column, new Flag(questionableFlagCascade), new Flag(badFlagCascade)));
						
						
					} catch (NumberFormatException e) {
						throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Flag values for flag cascade must be numeric");
					} catch (InvalidFlagException e) {
						throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Flag values for flag cascade are invalid");
					}
				}
			}

		}
	}
	
	public List<FlagCascade> getFlagCascades() {
		return flagCascades;
	}
	
	public boolean isNumeric() {
		return (dataType.equals(TYPE_NUMERIC));
	}
	
	public boolean isBoolean() {
		return (dataType.equals(TYPE_BOOLEAN));
	}
	
	protected void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	protected void setDataType(String dataType) throws InvalidDataTypeException {
		if (!isValidDataType(dataType)) {
			throw new InvalidDataTypeException(dataType);
		} else {
			this.dataType = dataType;
		}
	}
	
	protected void setRequired(boolean required) {
		this.required = required;
	}
	
	protected void setFlagCascadeConfig(String flagCascadeConfig) {
		this.flagCascadeConfig = flagCascadeConfig;
	}
	
	/**
	 * Check that a data type string is valid
	 * @param type The dat type string
	 * @return {@code true} if the string is valid; {@code false} if it is not.
	 */
	private boolean isValidDataType(String type) {
		return (type.equals(TYPE_STRING) || type.equals(TYPE_NUMERIC) || type.equals(TYPE_BOOLEAN)); 
	}
}