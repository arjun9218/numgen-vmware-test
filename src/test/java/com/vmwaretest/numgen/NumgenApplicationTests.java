package com.vmwaretest.numgen;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONObject;

import com.vmwaretest.numgen.common.Constants;
import com.vmwaretest.numgen.common.Utils;
import com.vmwaretest.numgen.controller.NumGenController;
import com.vmwaretest.numgen.model.ErrorMessage;
import com.vmwaretest.numgen.model.NumGenAttributes;
import com.vmwaretest.numgen.model.ResultObject;
import com.vmwaretest.numgen.model.TaskDetail;

/**
 * @author arjun9218
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class NumgenApplicationTests {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private NumGenController controller;
	
	@Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateShouldReturnAcceptedAndTaskId() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		ResponseEntity<TaskDetail> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, TaskDetail.class);
		boolean isValid = false;
		String pattern = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
		isValid = Utils.patternMatch(actaulResponse.getBody().getTask(), pattern);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
		assertThat(isValid).isEqualTo(true);
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void getTaskStatusForCompletedTaskShouldReturnCompleteStatus() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		ResponseEntity<TaskDetail> actaulResponse1 = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, TaskDetail.class);
		Thread.sleep(500);
		String taskId = actaulResponse1.getBody().getTask();
		ResultObject expectedResult = new ResultObject(Constants.STATUS_COMPLETED);
		ResponseEntity<ResultObject> expectedResponse = new ResponseEntity<ResultObject>(expectedResult, HttpStatus.OK);
		ResponseEntity<ResultObject> actualResponse2 = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/" + taskId + "/status",
				ResultObject.class);
		assertThat(actualResponse2.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
		assertThat(actualResponse2.getBody()).isEqualTo(expectedResponse.getBody());
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void getTaskStatusForRunningWriteOperationShouldShowInProgress() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("100000");
		numGenAttributes.setStep("1");
		ResponseEntity<TaskDetail> actaulResponse1 = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, TaskDetail.class);
		String taskId = actaulResponse1.getBody().getTask();
		ResultObject expectedResult = new ResultObject(Constants.STATUS_IN_PROGRESS);
		ResponseEntity<ResultObject> expectedResponse = new ResponseEntity<ResultObject>(expectedResult, HttpStatus.OK);
		ResponseEntity<ResultObject> actualResponse2 = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/" + taskId + "/status",
				ResultObject.class);
		assertThat(actualResponse2.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
		assertThat(actualResponse2.getBody()).isEqualTo(expectedResponse.getBody());
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void getNumListForCompletedTaskShouldReturnNumListResult() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		ResponseEntity<TaskDetail> actaulResponse1 = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, TaskDetail.class);
		Thread.sleep(500);
		String taskId = actaulResponse1.getBody().getTask();
		JSONObject expectedJson = Utils.getJsonObject(getClass().getResource("/expected_data/expected_numlist_response.json").getFile());
		ResponseEntity<String> actualResponse2 = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/" + taskId,
				String.class);
		JSONAssert.assertEquals(expectedJson.toString(), actualResponse2.getBody(), true);
	}
	
	/**
	 * Error scenario
	 * @throws Exception
	 */
	@Test
	public void getTaskStatusForNonExistingUUIDShouldReturnErrorResult() throws Exception {
		ResultObject expectedResult = new ResultObject(Constants.STATUS_ERROR);
		ResponseEntity<ResultObject> expectedResponse = new ResponseEntity<ResultObject>(expectedResult, HttpStatus.OK);
		ResponseEntity<ResultObject> actualResponse = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/random-Str-UUID/status",
				ResultObject.class);
		assertThat(actualResponse.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse.getBody());
	}
	
	/**
	 * Error scenario
	 * @throws Exception
	 */
	@Test
	public void getNumListForNonExistingUUIDShouldReturnErrorResult() throws Exception {
		ResultObject expectedResult = new ResultObject(Constants.STATUS_ERROR);
		ResponseEntity<ResultObject> expectedResponse = new ResponseEntity<ResultObject>(expectedResult, HttpStatus.OK);
		ResponseEntity<ResultObject> actualResponse = this.restTemplate.getForEntity("http://localhost:" + port + "/api/tasks/random-Str-UUID?action=api_numlist",
				ResultObject.class);
		assertThat(actualResponse.getStatusCode()).isEqualTo(expectedResponse.getStatusCode());
		assertThat(actualResponse.getBody()).isEqualTo(expectedResponse.getBody());
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateForInvalidInputShouldReturnProperErrorResponse() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("abc");
		numGenAttributes.setStep("2");
		ErrorMessage expectedError = new ErrorMessage(400, "BadRequest", Constants.INVALID_IP_INTEGER);
		ResponseEntity<ErrorMessage> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, ErrorMessage.class);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actaulResponse.getBody()).isEqualTo(expectedError);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateForBigNumberShouldReturnProperErrorResponse() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("100000000000000000000000");
		numGenAttributes.setStep("2");
		ErrorMessage expectedError = new ErrorMessage(400, "BadRequest", Constants.INVALID_IP_NUM_TOO_BIG);
		ResponseEntity<ErrorMessage> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, ErrorMessage.class);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actaulResponse.getBody()).isEqualTo(expectedError);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateForNegativeGoalShouldReturnProperErrorResponse() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("-20");
		numGenAttributes.setStep("3");
		ErrorMessage expectedError = new ErrorMessage(400, "BadRequest", Constants.INVALID_IP_GOAL_LT_ZERO);
		ResponseEntity<ErrorMessage> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, ErrorMessage.class);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actaulResponse.getBody()).isEqualTo(expectedError);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateStepLessThanOneShouldReturnProperErrorResponse() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("-3");
		ErrorMessage expectedError = new ErrorMessage(400, "BadRequest", Constants.INVALID_IP_STEP_LT_ONE);
		ResponseEntity<ErrorMessage> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, ErrorMessage.class);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actaulResponse.getBody()).isEqualTo(expectedError);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void postGenerateStepGreaterThanGoalShouldReturnProperErrorResponse() throws Exception {
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("30");
		ErrorMessage expectedError = new ErrorMessage(400, "BadRequest", Constants.INVALID_IP_GOAL_GT_STEP);
		ResponseEntity<ErrorMessage> actaulResponse = this.restTemplate.postForEntity("http://localhost:" + port + "/api/generate", numGenAttributes, ErrorMessage.class);
		assertThat(actaulResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(actaulResponse.getBody()).isEqualTo(expectedError);
	}
	
}
