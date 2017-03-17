package uk.ac.exeter.QCRoutines.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.exeter.QCRoutines.data.DataColumn;
import uk.ac.exeter.QCRoutines.data.DataRecord;
import uk.ac.exeter.QCRoutines.messages.Flag;
import uk.ac.exeter.QCRoutines.util.RoutineUtils;

/**
 * Holds the configuration of columns details for a {@link DataRecord}.
 * The configuration stores details of the column names, types, and aspects
 * of the behaviour of {@link Flag} objects attached to them.
 * 
 * <p>
 *   The configuration is stored in a CSV file, with columns in the same order
 *   as they are in the data file. Each line of the file contains the following values:
 * <p>
 * 
 * <ol>
 *   <li>
 *     Column Name
 *   </li>
 *   <li>
 *     Data Type. One of:
 *     <ul>
 *       <li>
 *         {@code S} - String. Times should be set as Strings; they will be parsed automatically.
 *       </li>
 *       <li>
 *         {@code N} - Numeric
 *       </li>
 *       <li>
 *         {@code B} - Boolean
 *       </li>
 *     </ul>
 *   </li>
 *   <li>
 *     Required. {@code true} if a value is required, {@code false} otherwise.
 *     If a Boolean column is empty it is assumed false.
 *   </li>
 *   <li>
 *     Flag Cascade: Flags placed on a column can cascade to other columns. (See below.)
 *   </li>
 * </ol>
 * 
 * <p>
 *   <b>Flag Cascading</b>
 * </p>
 * 
 * <p>
 *   When a {@link Flag} is set on a column, it can 'cascade' so that flags
 *   are set on other columns. For example, a questionable sea surface temperature
 *   value will mean that the calculated fCO<sub>2 is also questionable.
 * </p>
 * 
 * <p>
 *   The specification is as follows:
 * </p>
 * 
 * <p>
 *   {@code <Target column name>|<Flag to set on Questionable>|<Flag to set on Bad>}
 * <p>
 * 
 * <p>
 *   Multiple cascade columns can be specified, separated by semi-colons.
 * </p>
 *
 * <p>
 *   An example configuration line is below;
 * </p>
 * 
 * <p>
 *   {@code Salinity 1,N,Y,Mean Salinity|3|4;fCO2|2|3}
 * </p>
 * 
 * <p>
 *   This is the entry for a Salinity column. If its flag is set to Questionable,
 *   then the Mean Salinity column is also assigned a Questionable flag, while the fCO2 column
 *   is assigned a Good flag. For a Bad Salinity, Mean Salinity is set to Bad, and fCO2 is set to Questionable.
 * </p>
 * 
 * <p>
 *   Note that these cascades will only set flags if they are 'worse' than the flag that is already set, i.e.
 *   Good -> Questionable -> Bad. A Questionable flag cascading to a column that already has a Bad flag will have no effect.
 * </p>
 * 
 * @see Flag
 */
public class ColumnConfig {
	
	/**
	 * The index of the column containing the column name
	 */
	private static final int COL_NAME = 0;
	
	/**
	 * The index of the column that indicates the data type
	 */
	private static final int COL_TYPE = 1;
	
	/**
	 * The index of the column that indicates whether this entry is required
	 */
	private static final int COL_REQUIRED = 2;
	
	/**
	 * The index of the column that contains the flag cascade configuration
	 */
	private static final int COL_FLAG_CASCADE = 3;
	
	/**
	 * The list of columns in the order in which they appear in the config file
	 */
	private List<String> columnNames;
	
	/**
	 * The set of configuration items for the columns
	 */
	private Map<String, ColumnConfigItem> columnConfig;
	
	/**
	 * The location of the metadata config file.
	 * Must be set via {@link #init(String)} before calling
	 * {@link #getInstance()}.
	 */
	private static String configFilename = null;

	/**
	 * The singleton instance of this class.
	 */
	private static ColumnConfig instance = null;

	/**
	 * Set the required data for building the singleton instance of this class
	 * 
	 * @param filename The name of the file containing the configuration
	 * @throws ConfigException If the configuration is invalid
	 */
	public static void init(String filename) throws ConfigException {
		configFilename = filename;
		instance = new ColumnConfig();
	}

	/**
	 * Initialises the column configuration config.
	 * This cannot be called until after {@link #init(String)} has been called.
	 * @throws ConfigException If the configuration cannot be loaded
	 */
	protected ColumnConfig() throws ConfigException {
		if (configFilename == null) {
			throw new ConfigException(null, "ColumnConfig filename has not been set - must run init first");
		}
		
		columnNames = new ArrayList<String>();
		columnNames.add(null); // Column indices are 1-based, so add an empty entry here
		columnConfig = new HashMap<String, ColumnConfigItem>();
		readFile();
	}
	
	/**
	 * Retrieve the singleton instance of this class, creating it if
	 * it doesn't exist.
	 * 
	 * @return The singleton instance of this class
	 * @throws ConfigException If the configuration is invalid
	 */
	public static ColumnConfig getInstance() throws ConfigException {
		if (instance == null) {
			instance = new ColumnConfig();
		}
		
		return instance;
	}
	
	/**
	 * Destroys the singleton instance of this class
	 */
	public static void destroy() {
		instance = null;
	}
	
	/**
	 * Reads and parses the contents of the column config file
	 */
	private void readFile() throws ConfigException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configFilename));
			try {
				String line = reader.readLine();
				int lineCount = 1;
				int entryCount = 0;				
				
				while (null != line) {
					if (!RoutineUtils.isComment(line)) {
						entryCount++;
						
						List<String> fields = Arrays.asList(line.split(","));
						fields = RoutineUtils.trimList(fields);
						
						ColumnConfigItem configItem = new ColumnConfigItem(lineCount, entryCount);
						parseLine(lineCount, fields, configItem);
						columnNames.add(configItem.getColumnName());
						columnConfig.put(configItem.getColumnName(), configItem);
					}

					line = reader.readLine();
					lineCount++;
				}
			} finally {
				reader.close();
			}

			for (ColumnConfigItem columnConfigItem : columnConfig.values()) {
				columnConfigItem.parseFlagCascade(this);
			}

		} catch (IOException e) {
			throw new ConfigException(configFilename, "I/O Error while reading file", e);
		}
	}
	
	/**
	 * Parse a line of the configuration file, and populates a {@link ColumnConfigItem}.
	 * 
	 * @param lineCount The line number in the file
	 * @param fields The list of fields from the line
	 * @param columnConfigItem The config item to be populated
	 * @throws ConfigException If the line cannot be parsed
	 */
	private void parseLine(int lineCount, List<String> fields, ColumnConfigItem columnConfigItem) throws ConfigException {

		if (fields.size() < 3) {
			throw new ConfigException(configFilename, lineCount, "Column config must contain at least 3 entries (name, type, required)");
		}
		
		String columnName = fields.get(COL_NAME);

		if (columnNames.contains(columnName)) {
			throw new ConfigException(configFilename, columnName, lineCount, "Column is configured more than once");
		} else if (columnName.indexOf('_') >= 0) {
			throw new ConfigException(configFilename, lineCount, "Column names cannot contain the underscore character");
		} else {
			columnConfigItem.setColumnName(columnName);
		}
		
		String dataType = fields.get(COL_TYPE);
		try {
			columnConfigItem.setDataType(dataType);
		} catch (InvalidDataTypeException e) {
			throw new ConfigException(configFilename, columnName, lineCount, "Invalid Data Type", e);
		}
		
		boolean required;
		try {
			required = RoutineUtils.parseBoolean(fields.get(COL_REQUIRED));
			columnConfigItem.setRequired(required);
		} catch (ParseException e) {
			throw new ConfigException(configFilename, columnName, lineCount, "Invalid boolean value");
		}
		
		if (fields.size() > 3) {
			String cascadeConfig = fields.get(COL_FLAG_CASCADE);
			columnConfigItem.setFlagCascadeConfig(cascadeConfig);
		}
	}
	
	/**
	 * Returns a list of the configured data field names in file order.
	 * @return The list of data field names
	 */
	public List<String> getColumnList() {
		return columnNames;
	}
	
	/**
	 * Returns the column configuration for the specified column
	 * @param columnName The name of the column
	 * @return The column configuration for the specified column
	 */
	public ColumnConfigItem getColumnConfig(String columnName) {
		return columnConfig.get(columnName);
	}
	
	/**
	 * Retrieves the column name for a given column index in the data file
	 * @param columnIndex The index of the column
	 * @return The name of the column
	 */
	public String getColumnName(int columnIndex) {
		
		String columnName = null;
		
		for (String searchColumn : columnConfig.keySet()) {
			
			ColumnConfigItem columnConfigItem = columnConfig.get(searchColumn);
			if (columnConfigItem.getColumnIndex() == columnIndex) {
				columnName = searchColumn;
				break;
			}
		}
		
		return columnName;
	}
	
	/**
	 * Returns the name of the configuration file
	 * @return The name of the configuration file
	 */
	protected String getConfigFilename() {
		return configFilename;
	}
	
	/**
	 * Determines whether or not the named column exists in this configuration
	 * @param column The column
	 * @return {@code true} if the column exists; {@code false} if it does not
	 */
	public boolean hasColumn(String column) {
		return columnConfig.keySet().contains(column);
	}
	
	/**
	 * Create a set of {@link DataColumn} objects for a given {@link DataRecord}.
	 * @param record The record for which the {@link DataColumn} objects should be created
	 * @return The set of {@link DataColumn} objects
	 */
	public List<DataColumn> getDataColumns(DataRecord record) {
		List<DataColumn> result = new ArrayList<DataColumn>(columnNames.size());
		for (int i = 0; i < columnNames.size(); i++) {
			result.add(null);
		}
		
		for (int i = 1; i < columnNames.size(); i++) {
			ColumnConfigItem columnConfigItem = getColumnConfig(columnNames.get(i));
			result.set(columnConfigItem.getColumnIndex(), new DataColumn(record, columnConfigItem));
		}
		
		return result;
	}
	
	/**
	 * Returns the number of columns configured.
	 * @return The number of columns
	 */
	public int getColumnCount() {
		// The first column name is null because the indices are 1-based, so
		// the count reduced by 1.
		return columnNames.size() - 1;
	}
}
