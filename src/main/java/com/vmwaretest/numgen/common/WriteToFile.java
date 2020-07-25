package com.vmwaretest.numgen.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmwaretest.numgen.model.LoggingModel;
import com.vmwaretest.numgen.model.TaskDetail;

/**
 * @author arjun9218
 *
 */
public class WriteToFile implements Runnable {
	
	private int goal;
	private int step;
	private TaskDetail taskDetail;
	private String status = new String();
	
	Logger logger = LoggerFactory.getLogger(WriteToFile.class);
	
	/**
	 * Constructor
	 * @param goal
	 * @param step
	 * @param taskDetail
	 */
	public WriteToFile(int goal, int step, TaskDetail taskDetail) {
		this.goal = goal;
		this.step = step;
		this.taskDetail = taskDetail;
	}

	@Override
	public void run() {
		writeToFile();
	}
	
	/**
	 * This method writes the output to the uuId_output.txt file.
	 * The file will be stored in the tmp folder of the project's root directory
	 */
	public void writeToFile() {
		LoggingModel log = new LoggingModel("Thread " + Thread.currentThread().getName() + " start", "WriteToFile::writeToFile()", System.currentTimeMillis());
		logger.debug(log.toString());
		BufferedWriter writer = null;
		try {
			setStatus(Constants.STATUS_IN_PROGRESS);
			writer = new BufferedWriter(new FileWriter(new File("tmp/" + taskDetail.getTask() + "_output.txt")));
			while(goal >= 0) {
				if (goal - step < 0) {
					writer.append(goal + "");
				} else {
					writer.append(goal + ",");
				}
				goal -= step;
			}
			setStatus(Constants.STATUS_COMPLETED);
		} catch (IOException ioe) {
			setStatus(Constants.STATUS_ERROR);
		} finally {
	    	if (writer != null) {
	    		try {
					writer.close();
					log = new LoggingModel("Thread " + Thread.currentThread().getName() + " stop", "WriteToFile::writeToFile()", System.currentTimeMillis());
					logger.debug(log.toString());
				} catch (IOException e) {
					setStatus(Constants.STATUS_ERROR);
				}
	    	}
	    }
	}

	/**
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public TaskDetail getTaskDetail() {
		return taskDetail;
	}

	/**
	 * @param taskDetail
	 */
	public void setTaskDetail(TaskDetail taskDetail) {
		this.taskDetail = taskDetail;
	}
}
