import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;

public class NationalitiesFunctionTests {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String nation_id;


    @BeforeClass

    public void setup() {


        RestAssured.baseURI = "https://demo.mersys.io";
        reqSpec = given()
                .log().body()
                .contentType(ContentType.JSON);

    }

    @Test(priority = 1)
    public void loginTest() {

        HashMap<String,String> credentials = new HashMap<>();
        credentials.put("username", "Richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username",equalTo(credentials.get("username")))
                .extract().detailedCookies();

    }

    @Test(priority = 2)
    public void createNationalityTest() {

        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("name", "Postman NilNation");

        nation_id = given()// we add nation_id because we are creating a new one
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id");

    }

    @Test(priority = 3)
    public void createNationalityNegativeTest() {

        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("name", "Postman NilNation");

        given() // we didnt add the nation_id because we are not create a new one again
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/nationality")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 4)
    public void getNationalityTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nation_id)
                .then()
                .statusCode(200);
    }


    @Test(priority = 5)
    public void updateNationalityTest() {
        HashMap<String, String> updatedNat = new HashMap<>();
        updatedNat.put("name","Updated PostmanNil Test Nationality");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updatedNat)
                .when()
                .put("/school-service/api/nationality/")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updatedNat.get("name")));

    }

    @Test(priority = 6)
    public void deleteNationalityTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nation_id)
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(priority = 7)
    public void getNationalityNegativeTest() {

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .get("/school-service/api/nationality/" + nation_id)
                .then()
                .statusCode(404);
    }

    @Test(priority = 8)
    public void deleteNationalityNegativeTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/nationality/" + nation_id)
                .then()
                .log().body()
                .statusCode(404);
    }

}
