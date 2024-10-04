package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.baseURI;
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
        uniqueEmail = generateUniqueEmail(); // Generate unique email once for both tests
    }

    private String generateUniqueEmail() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.toString().replace("-", "");

        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);

        return "lely" + formattedDate + randomNumber + "@lelydemotest210243.com";
    }

    private JSONObject createUserData(String email) {
        JSONObject request = new JSONObject();
        request.put("email", email);
        request.put("name", "test");
        request.put("gender", "male");
        request.put("status", "active");
        return request;
    }

    @Test
    public void testCreateUserSuccessfully() {
        // Create the user
        JSONObject requestData = createUserData(uniqueEmail);

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestData.toJSONString())
                .when()
                .post("/users")
                .then()
                .statusCode(201)  // Expecting 201 Created
                .extract().response();

        // Log
        System.out.println("First Creation Response: " + response.getBody().asString());

        assertThat(response.jsonPath().getString("data.name"), equalTo("test"));
        assertThat(response.jsonPath().getString("data.email"), equalTo(uniqueEmail));
        assertThat(response.jsonPath().getString("data.gender"), equalTo("male"));
        assertThat(response.jsonPath().getString("data.status"), equalTo("active"));

    }

    @Test
    public void testCreateUserWithDuplicateEmail() {
        JSONObject requestData = createUserData(uniqueEmail);

        // Step 2
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestData.toJSONString())
                .when()
                .post("/users")
                .then()
                .statusCode(422)
                .extract().response();

        // Log
        String errorMessage = response.jsonPath().getString("data[0].message");
        System.out.println("Second Creation Error Message: " + errorMessage);
    }

}
