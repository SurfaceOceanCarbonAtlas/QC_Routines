package uk.ac.exeter.QCRoutines.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.messages.InvalidFlagException;

/**
 * Holds details of the configuration for a single SOCAT column
 * @see ColumnConfig
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
	 * The data type of the column. One of S, N, or B.
	 * @see ColumnConfig
	 */
	private String dataType;
	
	/**
	 * Indicates whether or not this column is required
	 */
	private boolean required;
	
	/**
	 * The flag cascade configuration String
	 * @see ColumnConfig
	 */
	private String flagCascadeConfig;
	
	/**
	 * The list of flag cascades for this column
	 */
	private List<FlagCascade> flagCascades = new ArrayList<FlagCascade>();

	/**
	 * Creates an empty column configuration item ready to be
	 * populated by {@link ColumnConfig#parseLine}.
	 * @param configFileLine The line number in the config file
	 * @param columnIndex The column's index in the data file
	 */
	public ColumnConfigItem(int configFileLine, int columnIndex) {
		this.configFileLine = configFileLine;
		this.columnIndex = columnIndex;
	}
	
	/**
	 * Returns the name of the column that this configuration item refers to.
	 * @return The column name
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * Determines whether or not this column is required to have a value.
	 * @return {@code true} if the column must contain a value; {@code false} otherwise. 
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
	 * Parse the flag cascade configuration string. This must not be called
	 * until all the entire {@link ColumnConfig} has been parsed, because otherwise
	 * the cascade may reference columns that have not yet been configured.
	 * @param columnConfig The complete column configuration
	 * @throws ConfigException If the cascade configuration is invalid
	 * @see ColumnConfig
	 */
	protected void parseFlagCascade(ColumnConfig columnConfig) throws ConfigException {
		if (null != flagCascadeConfig && flagCascadeConfig.length() > 0) {
			List<String> cascades = Arrays.asList(flagCascadeConfig.split(";"));
			for (String cascade : cascades) {
				List<String> cascadeFields = Arrays.asList(cascade.split("\\|"));
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
	
	/**
	 * Returns the set of flag cascade specifications for this column
	 * @return The flag cascades
	 */
	public List<FlagCascade> getFlagCascades() {
		return flagCascades;
	}
	
	/**
	 * Determines whether or not this column is numeric
	 * @return {@code true} if the column is numeric; {@code false} if it is not.
	 */
	public boolean isNumeric() {
		return (dataType.equals(TYPE_NUMERIC));
	}
	
	/**
	 * Determines whether or not this column is Boolean
	 * @return {@code true} if the column is Boolean; {@code false} if it is not.
	 */
	public boolean isBoolean() {
		return (dataType.equals(TYPE_BOOLEAN));
	}
	
	/**
	 * Set the column name for this column
	 * @param columnName The column name
	 */
	protected void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Set the data type for this column. The type must be one of
	 * {@link #TYPE_STRING}, {@link #TYPE_NUMERIC}, or {@link #TYPE_BOOLEAN}.
	 * @param dataType The data type
	 * @throws InvalidDataTypeException If an invalid data type is specified
	 */
	protected void setDataType(String dataType) throws InvalidDataTypeException {
		if (!isValidDataType(dataType)) {
			throw new InvalidDataTypeException(dataType);
		} else {
			this.dataType = dataType;
		}
	}
	
	/**
	 * Specifies whether this column is required
	 * @param required {@code true} if the column is required; {@code false} if it is not.
	 */
	protected void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * Set the flag cascade configuration.
	 * @param flagCascadeConfig The flag cascade configuration.
	 */
	protected void setFlagCascadeConfig(String flagCascadeConfig) {
		this.flagCascadeConfig = flagCascadeConfig;
	}
	
	/**
	 * Check that a data type string is valid
	 * @param type The data type string
	 * @return {@code true} if the string is valid; {@code false} if it is not.
	 */
	private boolean isValidDataType(String type) {
		return (type.equals(TYPE_STRING) || type.equals(TYPE_NUMERIC) || type.equals(TYPE_BOOLEAN)); 
	}
}