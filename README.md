# numgen-vmware-test
This is an answer to the first question received via email.
This project is created using [Spring Initializr](https://start.spring.io/).

## Question
Write a spring boot application that generates number:

 * The below API should write into a file /tmp/{TASK_ID}_output.txt in the descending order, a sequence of numbers in the decreasing order till 0, with step and start number given  as input.
 * The First API returns a task.
 * When the status of the task is called, the second API, it has to return appropriate status SUCCESS if done or IN_PROGRESS if task is still running.
 * The third API when called with a completed task should return the list of numbers reading from the file.

### Expectation:

### - Best design practices with modularity and class and type definitions , OO principles to be should be followed;
### - Documentation to run the code
### - Relevant test cases;

| API | METHOD | request | response | 
| ------ | ------ | ------ | ------ |
| /api/generate | POST | ```{ "Goal":"10", "Step":"2" }``` | `202 Accepted` ```{ "task":"UUID of the task" }``` |
| /api/tasks/{UUID of the task}/status | GET | N/A | `200 OK` ```{ "result":"SUCCESS/IN_PROGRESS/ERROR" }``` |
| /api/tasks/{UUID of the task}?action=get_numlist | GET | N/A | `200 OK` ```{ "result":"10,8,6,4,2,0" }``` |

## Answer
This is a spring boot application which exposes the three API as per the question.

### How to run the code?

* Make sure you have Java 8 and maven installed and configured in your system.

```sh
$ git clone https://github.com/arjun9218/numgen-vmware-test.git
$ cd numgen-vmware-test
$ mvn clean install
$ cd target
$ java -jar numgen-0.0.1-SNAPSHOT.jar
```

* Once the application is started you can start hitting the APIs from postman or browser.
* The server port is 2020.
* So the APIs start with `http://localhost:2020/api`
