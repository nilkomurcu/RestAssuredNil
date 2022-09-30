import static io.restassured.RestAssured.*;
// This import is covering everything about the RestAssured
import static org.hamcrest.Matchers.*;
//Covering everything about the hamcrest

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ZippoTest {

    @BeforeClass // once is gonna be enough
    public void setup(){
        RestAssured.baseURI = "https://api.zippopotam.us";


    }


    @Test
    public void test() {
// same idea with cucumber
//we are gonna do exact same thing the postman

        given()
                .when()
                .then();

     }
     @Test
    public void checkingStatusCode() {

        given()
                .when()
                .get("/tr/35550") // baseURL is working
                .then()
                .statusCode(200); // Test code

     }

     @Test
    public  void loggingReguestDetails() {
        given()
                .log().all()
                .when()
                .get("/tr/35550")
                .then()
                .statusCode(200);
     }

     @Test
    public void loggingResponseDetails(){
        given()
                .when()
                .get("/tr/35550")
                .then()
                .log().all()
                .statusCode(200);
     }

     @Test
    public void checkContentType() {

        given()
                .when()
                .get("/tr/35550")
                .then()
                .contentType(ContentType.JSON) //Postman'daki Body->raw->JSON formatı temsil eder
                .statusCode(200);

     }

     @Test
    public void validateCountryTest(){ // country Türkiye ye eşit mi değil mi?
        given()
                .when()
                .get("/tr/35550")
                .then()
                .body("country",equalTo("Turkey"))
                .statusCode(200);
     }

     @Test
    public void validateCountryAbv() {
        given()
                .when()
                .get("/tr/35550")
                .then()
                .body("'country abbreviation'", equalTo("TR"))
                .statusCode(200);
     }

     @Test
    public void validateStateTest(){
        given()
                .when()
                .get("/tr/35550")
                .then()
                .body("places[0].state", equalTo("İzmir"))
                .statusCode(200);
     }

     //places[0].country
    //places array  --> [{country1,state1, zipcode1} , {country2, state2, zipcode2} ]

    @Test
    public void pathParametersTest(){

        String country = "tr";
        String zipcode = "35550";
        given()
                .pathParam("country", country) // tıpkı {{baseURL}} gibi
                .pathParam("zipcode", zipcode) // kısayol sağlar;  35550 --> {{zipcode}}
                .when()
                .get("/{country}/{zipcode}")
                .then()
                .statusCode(200);
    }

    @Test
    public void extractValueTest() {

        Object countryInfo = given()
                .when()
                .get("/tr/35550")
                .then()
                .extract().path("country");
        System.out.println(countryInfo);

    }



}

