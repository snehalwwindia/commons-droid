package com.commonsdroid.utils;

import java.util.regex.Pattern;

/**
 * The Class <code>ValidationUtils.</code>.<br/>
 * provides utility methods to perform some common bitmap operations
 * @version 12.10.2013
 */
public class ValidationUtils {

	/**
	 * Email validation pattern
	 */
	private static final Pattern EMAIL_ADDRESS = Pattern
			.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
					+ "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");

					
	/**
	 * Validation for email. for Android2.2 onwards android.util.Pattern
	 * @author Snehal Penurkar
	 * @param email
	 * @return true if email is valid email
	 */
	public static boolean isEmailValid(CharSequence email) {
		return EMAIL_ADDRESS.matcher(email).matches();
	}
	
}
