import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountriesFunctionTest {

    private RequestSpecification reqSpec;
    private Cookies cookies;
    private String country_id;

    @BeforeClass
    public void setup() {

        RestAssured.baseURI = "https://demo.mersys.io/";

        reqSpec = given()
                .log().body() // represents print the window, it is good to have
                .contentType(ContentType.JSON);
    }


    @Test
    public void loginTest() {

        HashMap<String,String> credentials = new HashMap<>();
        credentials.put("username", "richfield.edu");
        credentials.put("password", "Richfield2020!");
        credentials.put("rememberMe", "true");

        cookies = given()  // we create a cookie varaible and make it private // same with rest assured // we store it inside a variable
                .spec(reqSpec)
                .body(credentials)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().detailedCookies(); //storing the data

    }

    @Test(dependsOnMethods = "loginTest")
    public void createCountryTest() {

        HashMap<String,String> reqBody = new HashMap<>();
        reqBody.put("name","Halıl Inalckk35");
        reqBody.put("code","IHCC");
        reqBody.put("hasState", "false");

        country_id = given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(reqBody)
                .when()
                .post("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(201)
                .body("name", equalTo(reqBody.get("name")))
                .body("code",equalTo(reqBody.get("code")))
                .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "createCountryTest")
    public void updateCountryTest() {
        HashMap<String,String> updatedReqBody = new HashMap<>();
        updatedReqBody.put("id", country_id);
        updatedReqBody.put("name", "Updated Sınan Meydan");
        updatedReqBody.put("code", "SNH");
        updatedReqBody.put("hasState", "false");

        given()
                .spec(reqSpec)
                .cookies(cookies)
                .body(updatedReqBody)
                .when()
                .put("/school-service/api/countries")
                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updatedReqBody.get("name"))) // Check!
                .body("id",equalTo(updatedReqBody.get(country_id))); // Check it is true or not

    }

    @Test(dependsOnMethods = "updateCountryTest")
    public void deleteCountryTest() {
        given()
                .spec(reqSpec)
                .cookies(cookies)
                .when()
                .delete("/school-service/api/countries/" + country_id)
                .then()
                .log().body()
                .statusCode(200);

    }

}










