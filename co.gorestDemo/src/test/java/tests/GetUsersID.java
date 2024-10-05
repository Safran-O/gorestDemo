package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GetUsersID {

    @Test
    public void getUsersID() {

        Response response = RestAssured.get("https://gorest.co.in/public/v1/users");

        System.out.println(response.getTime());
        System.out.println(response.getBody().asString());
        System.out.println(response.getStatusLine());

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode,200);

        List<Integer> ids = response.jsonPath().getList("data.id");
        assertThat(ids, everyItem(notNullValue()));
        assertThat(ids, everyItem(greaterThanOrEqualTo(1000000)));
        assertThat(ids, everyItem(lessThanOrEqualTo(9999999)));
    }
}
