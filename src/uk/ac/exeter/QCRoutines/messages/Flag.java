package uk.ac.exeter.QCRoutines.messages;

/**
 * Represents a WOCE-type flag, with Good, Questionable or Bad values.
 * There is also a special case for an Unset flag.
 * @author Steve Jones
 *
 */
public class Flag implements Comparable<Flag> {

	/**
	 * The WOCE value for a good flag
	 */
	public static final int VALUE_GOOD = 2;
	
	/**
	 * The text value for a good flag
	 */
	protected static final String TEXT_GOOD = "Good";
	
	/**
	 * The WOCE value for a good flag
	 */
	public static final int VALUE_ASSUMED_GOOD = -2;
	
	/**
	 * The text value for a good flag
	 */
	protected static final String TEXT_ASSUMED_GOOD = "Assumed Good";
	
	/**
	 * The WOCE value for a questionable flag
	 */
	public static final int VALUE_QUESTIONABLE = 3;
	
	/**
	 * The text value for a questionable flag
	 */
	protected static final String TEXT_QUESTIONABLE = "Questionable";

	/**
	 * The WOCE value for a bad flag
	 */
	public static final int VALUE_BAD = 4;
	
	/**
	 * The text value for a bad flag
	 */
	protected static final String TEXT_BAD = "Bad";

	/**
	 * The special value for a fatal flag
	 */
	public static final int VALUE_FATAL = 44;
	
	/**
	 * The text value for a fatal flag
	 */
	protected static final String TEXT_FATAL = "Fatal";

	/**
	 * The special value for an unset flag
	 */
	public static final int VALUE_NOT_SET = -1000;

	/**
	 * The text value for an unset flag
	 */
	protected static final String TEXT_NOT_SET = "Not Set";

	/**
	 * The special value for a needed flag
	 */
	public static final int VALUE_NEEDED = -10;

	/**
	 * The text value for a needed flag
	 */
	protected static final String TEXT_NEEDED = "Needed";
	
	/**
	 * The special value for an Ignored flag
	 */
	public static final int VALUE_IGNORED = -1002;
	
	/**
	 * The text value for an Ignored flag
	 */
	protected static final String TEXT_IGNORED = "Ignored";

	/**
	 *  An instance of a Good flag
	 */
	public static final Flag GOOD = makeGoodFlag();
	
	/**
	 *  An instance of an Assumed Good flag
	 */
	public static final Flag ASSUMED_GOOD = makeAssumedGoodFlag();
	
	/**
	 *  An instance of a Questionable flag
	 */
	public static final Flag QUESTIONABLE = makeQuestionableFlag();
	
	/**
	 *  An instance of a Bad flag
	 */
	public static final Flag BAD = makeBadFlag();
	
	/**
	 * An instance of a Fatal flag
	 */
	public static final Flag FATAL = makeFatalFlag();
	
	/**
	 *  An instance of a Not Set flag
	 */
	public static final Flag NOT_SET = makeNotSetFlag();
	
	/**
	 *  An instance of a Not Set flag
	 */
	public static final Flag NEEDED = makeNeededFlag();
	
	/**
	 * An instance of an Ignored flag
	 */
	public static final Flag IGNORED = makeIgnoredFlag();
	
	/**
	 * The WOCE value for this flag
	 */
	protected int flagValue;
	
	/**
	 * Creates a Flag instance with the specified value
	 * @param flagValue The flag's WOCE value
	 * @throws InvalidFlagException If the flag value is invalid
	 */
	public Flag(int flagValue) throws InvalidFlagException {
		if (!isValidFlagValue(flagValue)) {
			throw new InvalidFlagException(flagValue);
		}
		
		this.flagValue = flagValue;
	}
	
	/**
	 * Returns the flag's numeric value
	 * @return The flag's numeric value
	 */
	public int getFlagValue() {
		return flagValue;
	}
	
	/**
	 * Converts the flag's numeric value into a String value
	 */
	@Override
	public String toString() {
		String result;
		
		switch (flagValue) {
		case VALUE_GOOD: {
			result = TEXT_GOOD;
			break;
		}
		case VALUE_ASSUMED_GOOD: {
			result = TEXT_ASSUMED_GOOD;
			break;
		}
		case VALUE_QUESTIONABLE: {
			result = TEXT_QUESTIONABLE;
			break;
		}
		case VALUE_BAD: {
			result = TEXT_BAD;
			break;
		}
		case VALUE_FATAL: {
			result = TEXT_FATAL;
			break;
		}
		case VALUE_NOT_SET: {
			result = TEXT_NOT_SET;
			break;
		}
		case VALUE_NEEDED: {
			result = TEXT_NEEDED;
			break;
		}
		case VALUE_IGNORED: {
			result = TEXT_IGNORED;
			break;
		}
		default: {
			// This should never happen!
			result = "***INVALID FLAG VALUE***";
		}
		}
		
		return result;
	}
	
	/**
	 * Checks to ensure that a flag value is valid. If the value is valid, the method does nothing.
	 * If it is not valid, an exception is thrown.
	 * @param value The flag value
	 * @return {@code true} if the flag value is valid; {@code false} if it is not
	 */
	public static boolean isValidFlagValue(int value) {
		return (value == VALUE_GOOD || value == VALUE_ASSUMED_GOOD || value == VALUE_QUESTIONABLE || 
				value == VALUE_BAD || value == VALUE_FATAL || value == VALUE_NOT_SET || value == VALUE_NEEDED ||
				value == VALUE_IGNORED);
	}
	
	/**
	 * Create an instance of a Good flag
	 * @return A Good flag
	 */
	private static Flag makeGoodFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_GOOD);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Good flag
	 * @return A Good flag
	 */
	private static Flag makeAssumedGoodFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_ASSUMED_GOOD);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Questionable flag
	 * @return A Questionable flag
	 */
	private static Flag makeQuestionableFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_QUESTIONABLE);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Bad flag
	 * @return A Bad flag
	 */
	private static Flag makeBadFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_BAD);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Fatal flag
	 * @return A Fatal flag
	 */
	private static Flag makeFatalFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_FATAL);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Not Set flag
	 * @return A Not Set flag
	 */
	private static Flag makeNotSetFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_NOT_SET);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of a Not Set flag
	 * @return A Not Set flag
	 */
	private static Flag makeNeededFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_NEEDED);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	/**
	 * Create an instance of an Ignored flag
	 * @return An Ignored flag
	 */
	private static Flag makeIgnoredFlag() {
		Flag flag = null;
		try {
			flag = new Flag(VALUE_IGNORED);
		} catch (InvalidFlagException e) {
			// This won't be thrown; do nothing
		}
		
		return flag;
	}
	
	@Override
	public boolean equals(Object compare) {
		boolean result = false;
		if (compare instanceof Flag) {
			result = ((Flag) compare).flagValue == flagValue;
		}
		return result;
	}

	@Override
	public int compareTo(Flag flag) {
		return this.flagValue - flag.flagValue;
	}
	
	/**
	 * Determines whether or not this flag is more
	 * significant than the specified flag.
	 * 
	 * The order of significance for flags is (lowest significance first):
	 * Not Set, Good, Questionable, Bad
	 * 
	 * @param flag The flag to be compared
	 * @return {@code true} if this flag is more significant than the supplied flag; {@code false} if it is not.
	 */
	public boolean moreSignificantThan(Flag flag) {
		return (compareTo(flag) > 0);
	}
	
	/**
	 * Determines whether or not this flag represents a Good value.
	 * Both Good and Assumed Good flags pass the check.
	 * @return {@code true} if this flag is Good; {@code false} if it is not.
	 */
	public boolean isGood() {
		return Math.abs(flagValue) == VALUE_GOOD;
	}
	
	/**
	 * Return the WOCE value for a flag.
	 * @return The WOCE value for the flag
	 * @see #getWoceValue(int)
	 */
	public int getWoceValue() {
		return getWoceValue(flagValue);
	}
	
	/**
	 * Return the WOCE value for a given flag value
	 * 
	 * <ul>
	 *   <li>Good and Assumed Good will return 2</li>
	 *   <li>Questionable will return 3</li>
	 *   <li>Bad and Fatal will return 4</li>
	 *   <li>All other flag types will return -1, because there is no corresponding WOCE value.</li>
	 * </ul>
	 * 
	 * @return The WOCE value for the flag
	 */
	public static int getWoceValue(int flagValue) {
		int result;
		
		switch(flagValue) {
		case VALUE_GOOD:
		case VALUE_ASSUMED_GOOD: {
			result = 2;
			break;
		}
		case VALUE_QUESTIONABLE: {
			result = 3;
			break;
		}
		case VALUE_BAD:
		case VALUE_FATAL: {
			result = 4;
			break;
		}
		default: {
			result = -1;
		}
		}
		
		return result;
	}
}
