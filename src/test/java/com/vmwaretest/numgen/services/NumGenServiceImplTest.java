package com.vmwaretest.numgen.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vmwaretest.numgen.common.Constants;
import com.vmwaretest.numgen.common.Utils;
import com.vmwaretest.numgen.customexceptions.InvalidInputException;
import com.vmwaretest.numgen.model.NumGenAttributes;
import com.vmwaretest.numgen.model.ResultObject;
import com.vmwaretest.numgen.model.TaskDetail;
import com.vmwaretest.numgen.services.impl.NumGenServiceImpl;

/**
 * @author arjun9218
 *
 */
@ExtendWith(MockitoExtension.class)
public class NumGenServiceImplTest {
	
	@InjectMocks
    private NumGenServiceImpl numGenService;
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void shouldGenerateNumFileSuccessfully() throws IOException, InvalidInputException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		TaskDetail taskDetail = numGenService.generateNumFile(numGenAttributes);
		boolean isValid = false;
		String pattern = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
		isValid = Utils.patternMatch(taskDetail.getTask(), pattern);
		assertThat(isValid).isEqualTo(true);
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void shouldGetTheTaskStatusForExistingUUID() throws IOException, InvalidInputException, InterruptedException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		
		TaskDetail taskDetail = numGenService.generateNumFile(numGenAttributes);
		Thread.sleep(500);
		ResultObject result = numGenService.getTaskStatus(taskDetail.getTask());	

		assertThat(result.getResult()).isEqualTo(Constants.STATUS_COMPLETED);
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void shouldGetTheInProgressTaskStatusForExistingUUID() throws IOException, InvalidInputException, InterruptedException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("100000");
		numGenAttributes.setStep("1");
		
		TaskDetail taskDetail = numGenService.generateNumFile(numGenAttributes);
		Thread.sleep(1);
		ResultObject result = numGenService.getTaskStatus(taskDetail.getTask());	

		assertThat(result.getResult()).isEqualTo(Constants.STATUS_IN_PROGRESS);
	}
	
	/**
	 * Normal scenario
	 * @throws Exception
	 */
	@Test
	public void shouldGetTheNumListForTheExistingUUID() throws IOException, InvalidInputException, InterruptedException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("2");
		
		TaskDetail taskDetail = numGenService.generateNumFile(numGenAttributes);
		Thread.sleep(500);
		ResultObject result = numGenService.getNumList(taskDetail.getTask());	

		assertThat(result.getResult()).isEqualTo("20,18,16,14,12,10,8,6,4,2,0");
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void shouldThrowInvalidInputExceptionOnInvalidInputs1() throws IOException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("abc");
		numGenAttributes.setStep("2");
		InvalidInputException exception = null;
		try {
			numGenService.generateNumFile(numGenAttributes);
		} catch (InvalidInputException iie) {
			exception = iie;
		}
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(Constants.INVALID_IP_INTEGER);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void shouldThrowInvalidInputExceptionOnInvalidInputs2() throws IOException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("100000000000000000000000");
		numGenAttributes.setStep("2");
		InvalidInputException exception = null;
		try {
			numGenService.generateNumFile(numGenAttributes);
		} catch (InvalidInputException iie) {
			exception = iie;
		}
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(Constants.INVALID_IP_NUM_TOO_BIG);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void shouldThrowInvalidInputExceptionOnInvalidInputs3() throws IOException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("-20");
		numGenAttributes.setStep("2");
		InvalidInputException exception = null;
		try {
			numGenService.generateNumFile(numGenAttributes);
		} catch (InvalidInputException iie) {
			exception = iie;
		}
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(Constants.INVALID_IP_GOAL_LT_ZERO);
	}

	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void shouldThrowInvalidInputExceptionOnInvalidInputs4() throws IOException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("-2");
		InvalidInputException exception = null;
		try {
			numGenService.generateNumFile(numGenAttributes);
		} catch (InvalidInputException iie) {
			exception = iie;
		}
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(Constants.INVALID_IP_STEP_LT_ONE);
	}
	
	/**
	 * Invalid input scenario
	 * @throws Exception
	 */
	@Test
	public void shouldThrowInvalidInputExceptionOnInvalidInputs5() throws IOException{
		NumGenAttributes numGenAttributes = new NumGenAttributes();
		numGenAttributes.setGoal("20");
		numGenAttributes.setStep("30");
		InvalidInputException exception = null;
		try {
			numGenService.generateNumFile(numGenAttributes);
		} catch (InvalidInputException iie) {
			exception = iie;
		}
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).isEqualTo(Constants.INVALID_IP_GOAL_GT_STEP);
	}
	
	/**
	 * Error scenario
	 * @throws Exception
	 */
	@Test
	public void shouldGetTheErrorTaskStatusFornonExistingUUID() throws IOException, InvalidInputException, InterruptedException{
		ResultObject result = numGenService.getTaskStatus("randomStr");	
		assertThat(result.getResult()).isEqualTo(Constants.STATUS_ERROR);
	}

}
