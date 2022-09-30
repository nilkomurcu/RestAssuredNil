//16 Eylül 2022
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

public class GoRestUserTests {
    private RequestSpecification reqSpec;
    private HashMap<String, String> requestBody;
    private Object userId;
    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://gorest.co.in/";

        // Creating Request Specification !
        reqSpec = given()
                .log().uri()
                .header("Authorization", "Bearer 717703983e684d46682139702938c0b2a0ec8c69db9fd3ecdfafdc8aeb2c3033")
                .contentType(ContentType.JSON);

        requestBody = new HashMap<>();
        requestBody.put("name", "Techno NilKmcBlahUser");
        requestBody.put("email", "testnil9976@test.com");
        requestBody.put("gender", "female");
        requestBody.put("status", "active");

    }

    @Test
    public void createUserTest() {

        Object userID = given()
                        .spec(reqSpec)
                        .body(requestBody)
                        .when()
                        .post("/public/v2/users")
                //.header("Authorization", "Bearer 61528a09ac9175e86c1042fb33650de0a153cf8637d6388f7d3382081b1cf15b")
                // Header is about authorization. This is how we keep api
                        .then()
                        .log().body()
                        .body("name", equalTo(requestBody.get("name")))
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createUserTest")
    public void editUserTest() {
        HashMap<String,String> updateRequestBody = new HashMap<>();
        updateRequestBody.put("name", "UpdatedNil NilKKmrc");

        given()
                .spec(reqSpec)
                .body(updateRequestBody)
                .when()
                .put("/public/v2/users" + userId)
                .then()
                .statusCode(200);
    }


    @Test(dependsOnMethods = "editUserTest")
    public void  deleteUserTest() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/public/v2/users/" + userId)
                .then()
                .log().body()
                .statusCode(204);
    }
}