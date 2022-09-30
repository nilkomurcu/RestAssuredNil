
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUserFunction {

    private RequestSpecification reqSpec; // we declare this outside of the method
    // so all the methods can use it !!!
    // we make this private because we need protection
    private HashMap<String, String> requestBody;
    private Object user_id;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in/";

        reqSpec =  given()
                .log().body() // for  request body
                .header("Authorization", "Bearer 717703983e684d46682139702938c0b2a0ec8c69db9fd3ecdfafdc8aeb2c3033")
                .contentType(ContentType.JSON);

        requestBody = new HashMap<>();
        requestBody.put("name", "Marlon Brando");
        requestBody.put("email", "marlonnbrando1924@gorest.com");
        requestBody.put("gender", "male");
        requestBody.put("status", "active");
    }


    @Test
    public void createUserTest() {
        user_id = given()// Can i use this user id in another test? -no (it is local we make it private)
               // .log().body()
               // .header("Authorization", "717703983e684d46682139702938c0b2a0ec8c69db9fd3ecdfafdc8aeb2c3033")
               // .contentType(ContentType.JSON)
              // we are not gonna write it because it has already written at the top on reqspec
                .spec(reqSpec) // this is included by 3 line
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(requestBody.get("name")))  //representing response body
                .extract().path("id"); // it is about creating new id
    }

    @Test(dependsOnMethods = "createUserTest")
    public void createUserNegativeTest() {
        given()
                .spec(reqSpec)
                .body(requestBody)
                .when()
                .post("/public/v2/users")
                .then()
                .log().body()
                .statusCode(422)
                .body("message[0]", equalTo("has already been taken"));
    }


    @Test(dependsOnMethods = "createUserNegativeTest")
    public void getUserAndValidate() {
        given()
                .spec(reqSpec)
                .when()
                .get("/public/v2/users/" + user_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(requestBody.get("name")))
                .body("email", equalTo(requestBody.get("email")))
                .body("gender", equalTo(requestBody.get("gender")))
                .body("status", equalTo(requestBody.get("status")));
    }


    @Test(dependsOnMethods = "getUserAndValidate")
    public void editUserTest() {

        HashMap<String, String> reqBodyForUpdate = new HashMap<>();
        reqBodyForUpdate.put("name", "newMarlon Brando Test");

        given()
                .spec(reqSpec)
                .body(reqBodyForUpdate) // we put the new hashmap it is including new data which we wanna edit
                .when()
                .put("/public/v2/users/" + user_id)
                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(reqBodyForUpdate.get("name")));
    }

    @Test(dependsOnMethods = "editUserTest")
    public void deleteUserTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + user_id)
                .then()
                .log().body()
                .statusCode(204);
    }


    @Test(dependsOnMethods = "deleteUserTest")
    public void deleteUserNegativeTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + user_id)
                .then()
                .log().body()
                .statusCode(404);
    }


}
