package com.commonsdroid.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

/**
 * The Class <code>StringUtils</code>.<br/>
 * provides utility methods to perform some common string operations
 * @author siddhesh
 * @version 12.10.2013
 * 
 */
public class StringUtils {
	
	/** The Constant EMPTY. */
	public final static String EMPTY = "";
	
	/** The Constant BLANK. */
	public static final String BLANK = " ";
	
	/**
	 * Instantiates a new string utils.
	 */
	private StringUtils(){};
	
	/**
	 * <b>public static String removeBlanks(String string)<b/><br/>
	 * Removes the blanks.
	 * @author siddhesh
	 * @param string the string
	 * @return the string
	 * @version 12.10.2013
	 */
	public static String removeBlanks(String string) {
        return string.replace(BLANK, EMPTY);
    }
	
	/**
	 * <b>public static String join(Collection collection, String delimiter)<b/><br/>
	 * Joins the string objects in given collection object.
	 * @author siddhesh
	 * @param collection the Collection object
	 * @param delimiter the delimiter to be used for joining string
	 * @return the string
	 * @version 12.10.2013
	 */
	@SuppressWarnings("rawtypes")
	public static String join(Collection collection, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator i = collection.iterator();
        while (i.hasNext()) {
            buffer.append(i.next());
            if (i.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
	
	/**
	 * <b>public static String join(Object object[], String delimiter)<b/><br/>
	 * Joins the string objects in given Object array.
	 * @author siddhesh
	 * @param object the object array
	 * @param delimiter the delimiter to be used for joining string
	 * @return the string
	 * @version 12.10.2013
	 */
	public static String join(Object object[], String delimiter) {
        StringBuffer buffer = new StringBuffer();
        int lenght = object.length;
        for(int i = 0 ; i < lenght ; i++){
            buffer.append(object[i] == null ? "" : object[i].toString());
            if (i != lenght-1) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
	
	/**
	 * <b>public static int getIntValue(String val)<b/><br/>
	 * Gets the int value.
	 * @author siddhesh
	 * @param val the string from which value to be extracted
	 * @return the int value
	 * @version 12.10.2013
	 */
	public static int getIntValue(String val){
		if(null != val && !"".equals(val)){
			try{
				return Integer.parseInt(val);
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				return 0;
			}
		}
		return 0;
		
	}
	
	/**
	 * <b>public static float getFloatValue(String val)<b/><br/>
	 * Gets the float value.
	 * @author siddhesh
	 * @param val the val
	 * @return the float value
	 * @version 12.10.2013
	 */
	public static float getFloatValue(String val){
		if(null != val && !"".equals(val)){
			try{
				return Float.parseFloat(val);
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * <b>public static double getDoubleValue(String val)<b/><br/>
	 * Gets the double value.
	 * @author siddhesh
	 * @param val the val
	 * @return the double value
	 * @version 12.10.2013
	 */
	public static double getDoubleValue(String val){
		if(null != val && !"".equals(val)){
			try{
				return Double.parseDouble(val);
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * <b>public static BigDecimal getBigDecimalValue(String val)<b/><br/>
	 * Gets the big decimal value.
	 * @author siddhesh
	 * @param val the val
	 * @return the big decimal value
	 * @version 12.10.2013
	 */
	public static BigDecimal getBigDecimalValue(String val){
		BigDecimal value;
		if(null != val && !"".equals(val)){
			try{
				value = new BigDecimal(val);
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				value = new BigDecimal(0);
			}
		}else{
			value = new BigDecimal(0);
		}
		return value;
	}
	

}
