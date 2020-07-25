package com.vmwaretest.numgen.services.impl;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.vmwaretest.numgen.common.Constants;
import com.vmwaretest.numgen.common.Utils;
import com.vmwaretest.numgen.common.WriteToFile;
import com.vmwaretest.numgen.customexceptions.InvalidInputException;
import com.vmwaretest.numgen.model.NumGenAttributes;
import com.vmwaretest.numgen.model.ResultObject;
import com.vmwaretest.numgen.model.TaskDetail;
import com.vmwaretest.numgen.services.NumGenService;

/**
 * @author arjun9218
 *
 */
@Service
public class NumGenServiceImpl implements NumGenService {
	
	Map<String, WriteToFile> writeToFile;

	@Override
	public TaskDetail generateNumFile(NumGenAttributes numGenAttribute) throws InvalidInputException {
		TaskDetail taskDetail = null;
	    UUID uuId = UUID.randomUUID();
	    	
	    if (Utils.isValid(numGenAttribute)) {
	    	int goal = Integer.parseInt(numGenAttribute.getGoal());
		   	int step = Integer.parseInt(numGenAttribute.getStep());
		    	
		   	taskDetail = new TaskDetail(uuId);
		   	
		   	if (writeToFile == null) {
		   		writeToFile = new HashMap<String, WriteToFile>();
		   	}
		   	
		   	WriteToFile wrtf = new WriteToFile(goal, step, taskDetail);
		   	writeToFile.put(uuId.toString(), wrtf);
		   	
		   	Thread fileThread = new Thread(wrtf);
		   	fileThread.setPriority(Thread.MAX_PRIORITY);
		   	fileThread.start();
	    }
		return taskDetail;
	}

	@Override
	public ResultObject getTaskStatus(String uuId) {
		ResultObject result = null;
		if (writeToFile != null) {
			WriteToFile wrtf = writeToFile.get(uuId);
			if (wrtf != null && wrtf.getTaskDetail() != null) {
				result = new ResultObject(wrtf.getStatus());
			} else {
				result = new ResultObject(Constants.STATUS_ERROR);
			}
		} else {
			result = new ResultObject(Constants.STATUS_ERROR);
		}
		return result;
	}

	@Override
	public ResultObject getNumList(String uuId) throws IOException {
		StringBuffer content = new StringBuffer();
		ResultObject obj = getTaskStatus(uuId);		
		ResultObject result = null;
		if (obj.result.equals(Constants.STATUS_COMPLETED)) {
			File file = new File("tmp/" + uuId + "_output.txt");
			String text;
            @SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
            while((text = reader.readLine())!=null){
                content.append(text);
            }
            result = new ResultObject(content.toString());
		} else {
			result = new ResultObject(Constants.STATUS_ERROR);
		}
		return result;
	}

}
