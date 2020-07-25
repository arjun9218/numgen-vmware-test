package com.vmwaretest.numgen.services;

import java.io.IOException;

import com.vmwaretest.numgen.customexceptions.InvalidInputException;
import com.vmwaretest.numgen.model.NumGenAttributes;
import com.vmwaretest.numgen.model.ResultObject;
import com.vmwaretest.numgen.model.TaskDetail;

/**
 * @author arjun9218
 *
 * This is an interface NumGenService which has 3 methods.
 */
public interface NumGenService {
	
	/**
	 * This is an abstract method, this method creates a new thread and generates a file from that thread.
	 * @param numGenAttribute
	 * @return UUID of the task
	 * @throws IOException
	 * @throws InvalidInputException
	 */
	public abstract TaskDetail generateNumFile(NumGenAttributes numGenAttribute) throws IOException, InvalidInputException;
	
	/**
	 * This method is for getting the task status by passing the UUID
	 * @param uuId
	 * @return status of the task
	 */
	public abstract ResultObject getTaskStatus(String uuId);
	
	/**
	 * This method fetches the output for the UUID
	 * @param uuId
	 * @return List of output numbers.
	 * @throws IOException
	 */
	public abstract ResultObject getNumList(String uuId) throws IOException;
}
