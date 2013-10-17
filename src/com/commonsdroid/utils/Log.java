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

/**
 * @author vishal vyas
 *
 */
public class Log {

	// Configuration variables
	public static boolean writeToSdCard = false;
	public static String projectName = null;
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

	// Constants
	public static final String TAG_DEBUG = "d" + projectName;
	public static final String TAG_ERROR = "e" + projectName;
	public static final String TAG_INFO = "i" + projectName;
	
	private static final String format = "yyyy-MM-dd hh:mm:ss";

	private static Date date;
	private static SimpleDateFormat sdf;

	private static File sdCard;
	private static File logFile;

	private static StringWriter stringWriter;
	private static PrintWriter printWriter;
	private static String strException = null;

	
	/**
	 * @param e
	 */
	public static void d(Exception e) {
		strException = execToString(e);
		writeLog(TAG_DEBUG + " " + strException);
		android.util.Log.d(TAG_DEBUG, strException);
	}

	public static void d(String strValue) {
		writeLog(TAG_DEBUG + " " + strValue);
		android.util.Log.d(TAG_DEBUG, strValue);
	}

	public static void d(String tag, String strValue) {
		writeLog(tag + " " + strValue);
		android.util.Log.d(tag, strValue);
	}

	public static void i(Exception e) {
		strException = execToString(e);
		writeLog(TAG_INFO + " " + strException);
		android.util.Log.i(TAG_INFO, strException);
	}

	public static void i(String strValue) {
		writeLog(TAG_INFO + " " + strValue);
		android.util.Log.i(TAG_INFO, strValue);
	}

	public static void e(Exception e) {
		strException = execToString(e);
		writeLog(TAG_ERROR + " " + strException);
		android.util.Log.e(TAG_ERROR, strException);
	}

	public static void e(String strValue) {
		writeLog(TAG_ERROR + " " + strValue);
		android.util.Log.e(TAG_ERROR, strValue);
	}

	/*
	 * Maintain exceptions in SDCard.
	 * 
	 * What if external storage is not available?
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
	private static String execToString(Exception e) {
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
