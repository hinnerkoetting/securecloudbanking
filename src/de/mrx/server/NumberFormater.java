package de.mrx.server;

/**
 * 
 * @author Hinni
 * converts numbers input by users to be stored internally
 */
public class NumberFormater {
	
	/**
	 * converts an integer encoded as string to internal storage format (remove spaces)
	 * @param number
	 * @return converted string
	 * @throws NumberFormatException
	 */
	public static String convertToStorageFormat(String number) {
		String result = number.replaceAll("[\\s-]", "");
		new Integer(result); //check if is an integer, will throw exception if not
		return result;
	}
}
