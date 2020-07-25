package com.vmwaretest.numgen.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vmwaretest.numgen.common.Constants;
import com.vmwaretest.numgen.customexceptions.InvalidInputException;
import com.vmwaretest.numgen.model.ErrorMessage;
import com.vmwaretest.numgen.model.LoggingModel;
import com.vmwaretest.numgen.model.NumGenAttributes;
import com.vmwaretest.numgen.model.ResultObject;
import com.vmwaretest.numgen.model.TaskDetail;
import com.vmwaretest.numgen.services.NumGenService;

/**
 * @author arjun9218
 *
 */
@RestController
@RequestMapping("/api")
public class NumGenController {
	
	@Autowired
	NumGenService numGenService;
	
	Logger logger = LoggerFactory.getLogger(NumGenController.class);
	
	/**
	 * This method is the controller method for getting the task status based on UUID path variable
	 * @param request
	 * @param uuId
	 * @return response JSON
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/tasks/{uuId}/status")
	public ResponseEntity<ResultObject> getTaskStatus(HttpServletRequest request, @PathVariable final String uuId) {
		LoggingModel log = new LoggingModel(Constants.REQUEST_START, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		ResponseEntity<ResultObject> response = null;
		ResultObject status = numGenService.getTaskStatus(uuId);
		response = new ResponseEntity<ResultObject>(status, HttpStatus.OK);
		log = new LoggingModel(Constants.REQUEST_END, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		return response;
	}
	
	/**
	 * This method is the controller method for getting the Number list generated by the generate API
	 * @param request
	 * @param uuId
	 * @param action
	 * @return response JSON
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/tasks/{uuId}")
	public ResponseEntity<?> getNumList(HttpServletRequest request, @PathVariable final String uuId, @RequestParam(value = "action", defaultValue="get_numlist") String action) {
		LoggingModel log = new LoggingModel(Constants.REQUEST_START, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		ResponseEntity<?> response = null;
		try {
			ResultObject result = numGenService.getNumList(uuId);
			response = new ResponseEntity<ResultObject>(result, HttpStatus.OK);
		} catch (IOException ioe) {
			ErrorMessage errorMsg = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError", ioe.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
			log = new LoggingModel(Constants.STATUS_ERROR, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis(), errorMsg.toString());
			logger.error(log.toString());
		}
		log = new LoggingModel(Constants.REQUEST_END, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		return response;
	}
	
	/**
	 * This method is the controller method for generating the number file
	 * @param request
	 * @param numGenAttributes
	 * @return response JSON
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/generate")
	public ResponseEntity<?> generateNumFile(HttpServletRequest request, @RequestBody NumGenAttributes numGenAttributes) {
		LoggingModel log = new LoggingModel(Constants.REQUEST_START, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		ResponseEntity<?> response = null;
		try {
			TaskDetail taskDetail = numGenService.generateNumFile(numGenAttributes);
			response = new ResponseEntity<TaskDetail>(taskDetail, HttpStatus.ACCEPTED);
		} catch (IOException ioe) {
			ErrorMessage errorMsg = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "InternalServerError", ioe.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMsg, HttpStatus.INTERNAL_SERVER_ERROR);
			log = new LoggingModel(Constants.STATUS_ERROR, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis(), errorMsg.toString());
			logger.error(log.toString());
		} catch (InvalidInputException iie) {
			ErrorMessage errorMsg = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "BadRequest", iie.getMessage());
			response = new ResponseEntity<ErrorMessage>(errorMsg, HttpStatus.BAD_REQUEST);
			log = new LoggingModel(Constants.STATUS_ERROR, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis(), errorMsg.toString());
			logger.error(log.toString());
		}
		log = new LoggingModel(Constants.REQUEST_END, request.getRequestURL().toString(), request.getMethod(), System.currentTimeMillis());
		logger.debug(log.toString());
		return response;
	}

}
