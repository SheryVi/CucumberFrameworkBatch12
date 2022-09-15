package API;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HardCodedExamples {

    String baseURI = RestAssured.baseURI = "http://hrm.syntaxtechs.net/syntaxapi/api";
    String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NTU0OTI1NTAsImlzcyI6ImxvY2FsaG9zdCIsImV4cCI6MTY1NTUzNTc1MCwidXNlcklkIjoiMzk2MCJ9.Rm2aLh5Mcrb6dpU2U-Tb7EgprYBv1tGpHm5aLSj0cQU";
    static String employee_id;


    @Test
    public void acreateEmployee(){
    RequestSpecification request = given().header("Content-Type", "application/json").
                header("Authorization", token).body("{\n" +
                        "  \"emp_firstname\": \"Vi\",\n" +
                        "  \"emp_lastname\": \"Sh\",\n" +
                        "  \"emp_middle_name\": \"MS\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"1999-06-15\",\n" +
                        "  \"emp_status\": \"Probation\",\n" +
                        "  \"emp_job_title\": \"QA\"\n" +
                        "}");

    Response response = request.when().post("/createEmployee.php");
    response.prettyPrint();
    response.then().assertThat().statusCode(201);
    //Hamcrest matchers
    response.then().assertThat().body("Message", equalTo("Employee Created"));
    response.then().assertThat().body("Employee.emp_firstname", equalTo("Vi"));


    //using jsonPath(), to specify the key in the body so that it returns the value against it
    employee_id = response.jsonPath().getString("Employee.employee_id");
    System.out.println(employee_id);

    }

    @Test

    public void bgetCreatedEmployee (){
        RequestSpecification preparedRequest = given().header("Content-Type", "application/json").
                header("Authorization", token).queryParam("employee_id", employee_id);

        Response response = preparedRequest.when().get("/getOneEmployee.php");
        response.prettyPrint();

        response.then().assertThat().statusCode(200);
        String tempId = response.jsonPath().getString("employee.employee_id");
        System.out.println(tempId);
        Assert.assertEquals(tempId, employee_id);

    }

    @Test
    public void cupdateEmployee(){
        RequestSpecification preparedRequest = given().header("Content-Type", "application/json").
                header("Authorization", token).
                body("{\n" +
                        "  \"employee_id\": \"" + employee_id + "\",\n" +
                        "  \"emp_firstname\": \"Angelina\",\n" +
                        "  \"emp_lastname\": \"Joly\",\n" +
                        "  \"emp_middle_name\": \"MS1\",\n" +
                        "  \"emp_gender\": \"F\",\n" +
                        "  \"emp_birthday\": \"1970-06-15\",\n" +
                        "  \"emp_status\": \"confirmed\",\n" +
                        "  \"emp_job_title\": \"Actress\"\n" +
                        "}");

        Response response = preparedRequest.when().put("/updateEmployee.php");

        response.prettyPrint();
        response.then().assertThat().statusCode(200);

    }
    @Test
    public  void  dGetUpdateEmployee(){
        RequestSpecification request = given().header("Content-Type", "application/json")
                .header("Authorization", token).queryParam("employee_id", employee_id);

        Response response = request.when().get("/getOneEmployee.php");
        response.then().assertThat().statusCode(200);
        response.prettyPrint();

    }
    @Test
    public void eGetAllEmployees(){

        RequestSpecification request = given().header("Authorization", token ).
                header("Content-Type", "application/json");

        Response response = request.when().get("/getAllEmployees.php");

        //it returns string of response
        String allEmployees = response.prettyPrint();

        //jsonPath() vs jsonPath
        //jsonPath is a class that contains method for converting the values into json object
        //jsonPath() is a method belongs to jsonPath class

        //creating object of jsonPath class
        JsonPath js = new JsonPath(allEmployees);

        //retrieving the total number of employees
        int count = js.getInt("Employees.size()");
        System.out.println(count);

        //to print only employee id of all the employees
        for (int i=0; i<count; i++){
            String empID = js.getString("Employees[ "+ i + " ].employee_id");
            System.out.println(empID);
        }


    }

}
