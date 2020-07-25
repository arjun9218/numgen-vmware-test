package com.vmwaretest.numgen.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.vmwaretest.numgen.customexceptions.InvalidInputException;
import com.vmwaretest.numgen.model.NumGenAttributes;

public class Utils {
	
	private static Pattern pattern = Pattern.compile("-?\\d+");
	
	/**
	 * This method is for validating the input parameters
	 * <p>
	 * The validation is as follows:
	 * <ul>
	 * <li>If the number is too big then it is not valid 
	 * as the text file will occupy too much space and it would take too much time.
	 * <li>If goal is less than zero it is not valid
	 * <li>If step is less than one it is not valid
	 * <li>If goal is less than step then it is not valid
	 * </ul>
	 * @param numGenAttribute
	 * @return
	 * @throws InvalidInputException
	 */
	public static boolean isValid(NumGenAttributes numGenAttribute) throws InvalidInputException {
		boolean valid = true;
		if (pattern.matcher(numGenAttribute.getGoal()).matches() && pattern.matcher(numGenAttribute.getStep()).matches()) {
			int goal = 0;
			int step = 0;
			try {
				goal = Integer.parseInt(numGenAttribute.getGoal());
				step = Integer.parseInt(numGenAttribute.getStep());
			} catch (NumberFormatException nfe) {
				throw new InvalidInputException(Constants.INVALID_IP_NUM_TOO_BIG);
			}
			if (goal < 0) {
				throw new InvalidInputException(Constants.INVALID_IP_GOAL_LT_ZERO);
			} else if (step < 1) {
				throw new InvalidInputException(Constants.INVALID_IP_STEP_LT_ONE);
			} else if (goal < step) {
	    		throw new InvalidInputException(Constants.INVALID_IP_GOAL_GT_STEP);
	    	}
		} else {
    		throw new InvalidInputException(Constants.INVALID_IP_INTEGER);
		}
		return valid;
	}
	
	/**
	 * This method is for matching the pattern of uuId.
	 * Used in JUnit tests.
	 * @param str
	 * @param ptrn
	 * @return
	 */
	public static boolean patternMatch(String str, String ptrn) {
		Pattern ptrnMatcher = Pattern.compile(ptrn);
		return ptrnMatcher.matcher(str).matches();
	}
	
	/**
	 * This method is used for fetching the data from the expected JSON file.
	 * Used in JUnit tests.
	 * @param file
	 * @return
	 */
	public static JSONObject getJsonObject(String file) {
		 String json = null;
		 JSONObject obj = null;
		    try {
		        InputStream is = new FileInputStream(new File(file));
		        int size = is.available();
		        byte[] buffer = new byte[size];
		        is.read(buffer);
		        is.close();
		        json = new String(buffer, "UTF-8");
		        obj = new JSONObject(json);
		    } catch (IOException ex) {
		        ex.printStackTrace();
		        return null;
		    } catch (JSONException e) {
				e.printStackTrace();
			}
		return obj;
	}
}
