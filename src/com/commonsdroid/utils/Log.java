package com.commonsdroid.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * The Class Log.
 *
 * @author vishal vyas
 */
public class Log {

	// Configuration variables
	/** The write to sd card. */
	public static boolean writeToSdCard = false;
	
	/** The project name. */
	public static String projectName = null;
	
	/** The log file name. */
	private static String logFileName = null;

	static {
		if (projectName == null) {
			throw new IllegalArgumentException(
					"Variable \"projectName\" not set! Please set it to a valid project name :)");
		}
		
		if (logFileName == null) {
			throw new IllegalArgumentException(
					"Variable \"logFileName\" not set! Please set it to a valid log file name :)");
		}
	}

	/** The Constant TAG_DEBUG. */
	public static final String TAG_DEBUG = "d" + projectName;
	
	/** The Constant TAG_ERROR. */
	public static final String TAG_ERROR = "e" + projectName;
	
	/** The Constant TAG_INFO. */
	public static final String TAG_INFO = "i" + projectName;
	
	/** The Constant format. */
	private static final String format = "yyyy-MM-dd hh:mm:ss";

	/** The date. */
	private static Date date;
	
	/** The sdf. */
	private static SimpleDateFormat sdf;

	/** The sd card. */
	private static File sdCard;
	
	/** The log file. */
	private static File logFile;

	/** The string writer. */
	private static StringWriter stringWriter;
	
	/** The print writer. */
	private static PrintWriter printWriter;
	
	/** The str exception. */
	private static String strException = null;

	
	/**
	 * D.
	 *
	 * @param e the e
	 */
	public static void d(Exception e) {
		strException = execptionToString(e);
		writeLog(TAG_DEBUG + " " + strException);
		android.util.Log.d(TAG_DEBUG, strException);
	}

	/**
	 * D.
	 *
	 * @param strValue the str value
	 */
	public static void d(String strValue) {
		writeLog(TAG_DEBUG + " " + strValue);
		android.util.Log.d(TAG_DEBUG, strValue);
	}

	/**
	 * D.
	 *
	 * @param tag the tag
	 * @param strValue the str value
	 */
	public static void d(String tag, String strValue) {
		writeLog(tag + " " + strValue);
		android.util.Log.d(tag, strValue);
	}

	/**
	 * I.
	 *
	 * @param e the e
	 */
	public static void i(Exception e) {
		strException = execptionToString(e);
		writeLog(TAG_INFO + " " + strException);
		android.util.Log.i(TAG_INFO, strException);
	}

	/**
	 * I.
	 *
	 * @param strValue the str value
	 */
	public static void i(String strValue) {
		writeLog(TAG_INFO + " " + strValue);
		android.util.Log.i(TAG_INFO, strValue);
	}

	/**
	 * E.
	 *
	 * @param e the e
	 */
	public static void e(Exception e) {
		strException = execptionToString(e);
		writeLog(TAG_ERROR + " " + strException);
		android.util.Log.e(TAG_ERROR, strException);
	}

	/**
	 * E.
	 *
	 * @param strValue the str value
	 */
	public static void e(String strValue) {
		writeLog(TAG_ERROR + " " + strValue);
		android.util.Log.e(TAG_ERROR, strValue);
	}

	/**
	 * Writes logs and exceptions in SDCard.
	 * 
	 * What if external storage is not available?
	 *
	 * @param strValue the string value
	 */
	private static void writeLog(String strValue) {
		if (writeToSdCard) {
			FileWriter fileWriter = null;
			BufferedWriter out = null;
			try {
				date = new Date();
				sdf = new SimpleDateFormat(format);
				sdCard = Environment.getExternalStorageDirectory();
				logFile = new File(sdCard, logFileName);

				if (!logFile.exists()) {
					logFile.createNewFile();
				}

				fileWriter = new FileWriter(logFile, true);
				out = new BufferedWriter(fileWriter);

				out.write(System.getProperty("line.separator"));
				out.write(sdf.format(date) + " => " + strValue);
				out.close();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (null != fileWriter) {
					try {
						fileWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					fileWriter = null;
				}

				if (null != out) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					out = null;
				}
			}
		}
	}

	/*
	 * Convert exception to string.
	 */
	/**
	 * Convert's Exception to string
	 *
	 * @param e the exception
	 * @return the string representation of Exception
	 */
	public static String execptionToString(Exception e) {
		if (null == stringWriter) {
			stringWriter = new StringWriter();
		}
		if (null == printWriter) {
			printWriter = new PrintWriter(stringWriter, true);
		}
		e.printStackTrace(printWriter);
		printWriter.flush();
		stringWriter.flush();
		return stringWriter.toString();
	}
}
