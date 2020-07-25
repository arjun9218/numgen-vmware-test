package com.vmwaretest.numgen.customexceptions;

/**
 * @author arjun9218
 *
 */
@SuppressWarnings("serial")
public class InvalidInputException extends Throwable {
	public InvalidInputException(String message) {
		super(message);
	}
}
