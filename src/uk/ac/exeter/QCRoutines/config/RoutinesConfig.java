package uk.ac.exeter.QCRoutines.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.exeter.QCRoutines.routines.Routine;
import uk.ac.exeter.QCRoutines.routines.RoutineException;


/**
 * Represents the configuration of the routines to be run against
 * input files.
 */
public class RoutinesConfig {
	
	/**
	 * Indicates an empty value
	 */
	public static final double NO_VALUE = -99999.9;

	/**
	 * The name for the default configuration.
	 * This is used if only one unnamed instance is used.
	 */
	private static final String DEFAULT_CONFIG_NAME = "default";
	
	/**
	 * The name of the package in which all routine classes will be stored
	 */
	private static final String ROUTINE_CLASS_ROOT = "uk.ac.exeter.QCRoutines.routines.";

	/**
	 * All routine class names must end with the same text
	 */
	private static final String ROUTINE_CLASS_TAIL = "Routine";

	/**
	 * The list of routine objects.
	 */
	private List<CheckerInitData> routineClasses;
	
	/**
	 * The name of the configuration file for the routines
	 */
	private String configFilename;
	
	/**
	 * The instances of this class
	 */
	private static Map<String, RoutinesConfig> instances = new HashMap<String, RoutinesConfig>();
	
	/**
	 * Initialises a {@code RoutinesConfig} instance. This is called by the
	 * {@code init} methods.
	 * @param configFilename The full path to the configuration file
	 * @throws ConfigException If the configuration is invalid
	 * @see #init(String)
	 * @see #init(String, String)
	 */
	private RoutinesConfig(String configFilename) throws ConfigException {
		if (configFilename == null) {
			throw new ConfigException(null, "RoutinesConfig filename has cannot be null");
		}
		this.configFilename = configFilename;
		
		routineClasses = new ArrayList<CheckerInitData>();
		readFile();
	}
	
	/**
	 * Initialises an unnamed {@code RoutinesConfig} instance.
	 * This can be used if only one instance is needed in the application.
	 * @param filename The full path to the configuration file
	 * @throws ConfigException If the configuration is invalid
	 */
	public static void init(String filename) throws ConfigException {
		init(DEFAULT_CONFIG_NAME, filename);
	}
	
	/**
	 * Initialises a named {@code RoutinesConfig} instance.
	 * @param instanceName The instance name
	 * @param filename The full path to the configuration file
	 * @throws ConfigException If the configuration is invalid
	 */
	public static void init(String instanceName, String filename) throws ConfigException {
		instances.put(instanceName, new RoutinesConfig(filename));
	}
	
	/**
	 * Retrieves a named {@code RoutinesConfig} instance
	 * @return The requested instance
	 * @throws ConfigException If the configuration cannot be loaded
	 */

	/**
	 * Retrieves a named {@code RoutinesConfig} instance
	 * @param instanceName The instance name
	 * @return The requested instance
	 * @throws ConfigException If the instance does not exist
	 */
	public static RoutinesConfig getInstance(String instanceName) throws ConfigException {
		RoutinesConfig requestedInstance = instances.get(instanceName);
		if (null == requestedInstance) {
			throw new ConfigException(null, "There is no instance named '" + instanceName + "'");
		}
		
		return requestedInstance;
	}
	
	/**
	 * Retrieves the unnamed {@code RoutinesConfig} instance
	 * @return The instance
	 * @throws ConfigException If the instance does not exist
	 */
	public static RoutinesConfig getInstance() throws ConfigException {
		return getInstance(DEFAULT_CONFIG_NAME);
	}
	
	/**
	 * Destroys the unnamed {@code RoutinesConfig} instance
	 */
	public static void destroy() {
		destroy(DEFAULT_CONFIG_NAME);
	}
	
	/**
	 * Destroys a named {@code RoutinesConfig} instance
	 * @param instanceName The instance
	 */
	public static void destroy(String instanceName) {
		instances.remove(instanceName);
	}
	
	/**
	 * Read and parse the configuration file
	 * @throws ConfigException If the configuration cannot be loaded
	 */
	private void readFile() throws ConfigException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(configFilename));
			try {
				String line = reader.readLine();
				int lineCount = 1;
				
				while (null != line) {
					if (!isComment(line)) {
						List<String> fields = Arrays.asList(line.split(","));
						fields = trimList(fields);
						
						// The first field is the class name. Grab it and remove
						// it from the list, so what's left is the parameters.
						String className = fields.remove(0);
						String fullClassName = ROUTINE_CLASS_ROOT + className + "." + className + ROUTINE_CLASS_TAIL;

						if (className.equalsIgnoreCase("")) {
							throw new ConfigException(configFilename, lineCount, "Routine class name cannot be empty");
						} else {
							try {
								// Instantiate the class and call the initialise method
								// to make sure everything's OK.
								Class<?> routineClass = Class.forName(fullClassName);
								Routine routineInstance = (Routine) routineClass.newInstance();
								routineInstance.initialise(fields, ColumnConfig.getInstance());
								
								// Add the checker class to the list of all known checkers.
								// These will be instantiated in the getInstances() method.
								routineClasses.add(new CheckerInitData(routineClass, fields));
							} catch(ClassNotFoundException e) {
								throw new ConfigException(configFilename, lineCount, "Routine check class '" + fullClassName + "' does not exist");
							} catch(Exception e) {
								throw new ConfigException(configFilename, lineCount, "Error creating Routine check class", e);
							}
						}
					}
					
					line = reader.readLine();
					lineCount++;
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			throw new ConfigException(configFilename, "I/O Error while reading file", e);
		}
	}
	
	/**
	 * Returns a list containing fresh instances of all the configured routine classes
	 * @return A list containing fresh instances of all the configured routine classes
	 * @throws RoutineException If the routine cannot be initialised
	 */
	public List<Routine> getRoutines() throws RoutineException {
		List<Routine> checkers = new ArrayList<Routine>(routineClasses.size());
		
		try {
			for (CheckerInitData checkerData: routineClasses) {
				Routine checkInstance = (Routine) checkerData.checkerClass.newInstance();
				checkInstance.initialise(checkerData.params, ColumnConfig.getInstance());
				checkers.add(checkInstance);
			}
		} catch (Exception e) {
			if (e instanceof RoutineException) {
				throw (RoutineException) e;
			} else {
				throw new RoutineException("Error initialising routine instance", e);
			}
		}
		
		return checkers;
	}
	
	/**
	 * A helper class to hold details of a given routine.
	 * A new instance of each routine is created for each file,
	 * and this class contains the details required to construct it.
	 */
	private class CheckerInitData {
		
		/**
		 * The class of the routine
		 */
		private Class<?> checkerClass;
		
		/**
		 * The parameters for the routine
		 */
		private List<String> params;
		
		/**
		 * Builds an object containing all the details required to initialise
		 * a given routine.
		 * @param checkerClass The class of the routine
		 * @param params The parameters for the routine
		 */
		private CheckerInitData(Class<?> checkerClass, List<String> params) {
			this.checkerClass = checkerClass;
			this.params = params;
		}
	}

	/**
	 * Determines whether or not a line is a comment, signified by it starting with {@code #} or {@code !} or {@code //}
	 * @param line The line to be checked
	 * @return {@code true} if the line is a comment; {@code false} otherwise.
	 */
	private boolean isComment(String line) {
		String trimmedLine = line.trim();
		return trimmedLine.length() == 0 || trimmedLine.charAt(0) == '#' || trimmedLine.charAt(0) == '!' || trimmedLine.startsWith("//", 0);
	}
	
	/**
	 * Trims all items in a list of strings
	 * @param source The strings to be converted 
	 * @return The converted strings
	 */
	private List<String> trimList(List<String> source) {
		
		List<String> result = new ArrayList<String>(source.size());
		
		for (int i = 0; i < source.size(); i++) {
			result.add(source.get(i).trim());
		}
		
		return result;
	}
}

