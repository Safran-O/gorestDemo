package tests;

import helpers.UserDataHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PostUser {

    private String accessToken;
    private String uniqueEmail;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://gorest.co.in/public/v1";
        accessToken = "1db9c9b6c959682be7c96f74ca532c3cb0bd331f46b86a92602f8d319481b6f5";
        uniqueEmail = UserDataHelper.generateUniqueEmail(); // `helpers` sınıfından çağır
    }

    @Test
    public void testCreateUserSuccessfully() {
        JSONObject requestData = UserDataHelper.createUserData(uniqueEmail);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestData.toJSONString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();

        System.out.println("First Creation Response: " + response.getBody().asString());

        assertThat(response.jsonPath().getString("data.name"), equalTo("test"));
        assertThat(response.jsonPath().getString("data.email"), equalTo(uniqueEmail));
        assertThat(response.jsonPath().getString("data.gender"), equalTo("male"));
        assertThat(response.jsonPath().getString("data.status"), equalTo("active"));
    }

    @Test
    public void testCreateUserWithDuplicateEmail() {
        JSONObject requestData = UserDataHelper.createUserData(uniqueEmail);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestData.toJSONString())
                .when()
                .post("/users")
                .then()
                .statusCode(422)
                .extract().response();

        String errorMessage = response.jsonPath().getString("data[0].message");
        System.out.println("Second Creation Error Message: " + errorMessage);
    }
}
