package uk.ac.exeter.QCRoutines.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.exeter.QCRoutines.messages.Flag;

/**
 * Holds details of the configuration for a single SOCAT column
 */
public class ColumnConfigItem {
	
	/**
	 * The name of the column
	 */
	private String columnName;
	
	/**
	 * The index of the column in the data file
	 */
	private int index;
	
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

	
	/**
	 * The constructor for the output column configuration
	 * @param columnName The column name
	 * @param index The column index (1-based)
	 * @param dataType The column data type
	 * @param required Is the field required?
	 * @param flagCascadeConfig The flag cascade configuration string
	 */
	public ColumnConfigItem(int configFileLine, String columnName, int index, String dataType, boolean required, String flagCascadeConfig) {
		this.configFileLine = configFileLine;
		this.columnName = columnName;
		this.index = index;
		this.dataType = dataType;
		this.required = required;
		this.flagCascadeConfig = flagCascadeConfig;
		flagCascades = new ArrayList<FlagCascade>();
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
	public int getIndex() {
		return index;
	}
	
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
						
						flagCascades.add(new FlagCascade(column, questionableFlagCascade, badFlagCascade));
						
						
					} catch (NumberFormatException e) {
						throw new ConfigException(columnConfig.getConfigFilename(), columnName, configFileLine, "Flag values for flag cascade must be numeric");
					}
				}
			}

		}
	}
}