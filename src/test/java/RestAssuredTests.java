import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.RequestBody;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.lessThan;


public class RestAssuredTests {

    Response response;

    public void RestAssuredTests() {
        baseURI = "manav";
    }

    public RequestBody.request createRequest() {
        int id = 3;
        String name = "banana";
        Double price = 3.5;
        int stock = 250;
        return new RequestBody.request(id, name, price, stock);
    }


    @Test(priority = 1)
    public void validGetRequest() {

        response =
                given()
                        .accept(ContentType.JSON)
                        .header("Contect-Type", "application/json")
                        .when()
                        .get("/allGrocery")
                        .then()
                        .statusCode(200)
                        .time(lessThan(500L))
                        .extract().response();

        Assert.assertNotNull(response.getBody());

        JsonPath jsonPathEvaluator = response.jsonPath();
        List<RequestBody.request> allResponds = jsonPathEvaluator.getList("data", RequestBody.request.class);

        // Asserting for the first response
        Assert.assertEquals(1, allResponds.get(0).getId());
        Assert.assertEquals("apple", allResponds.get(0).getName());
        Assert.assertEquals(3, allResponds.get(0).getPrice());
        Assert.assertEquals(100, allResponds.get(0).getStock());

        // Asserting for the second response
        Assert.assertEquals(2, allResponds.get(1).getId());
        Assert.assertEquals("grapes", allResponds.get(1).getName());
        Assert.assertEquals(5, allResponds.get(1).getPrice());
        Assert.assertEquals(50, allResponds.get(1).getStock());
    }
    @Test(priority = 2)
    public void validPostRequest() {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", createRequest().getId());
        requestData.put("name", createRequest().getName());
        requestData.put("price", createRequest().getPrice());
        requestData.put("stock", createRequest().getStock());

        response = given()
                .header("Content-Type", "application/json")
                .body(requestData)
                .when()
                .post("/add")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath bodyJson = response.jsonPath();
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(3, (Integer) bodyJson.get("id"));
        Assert.assertEquals("banana", bodyJson.get("name"));
        Assert.assertEquals(3.5, (Double) bodyJson.get("price"));
        Assert.assertEquals(250, (Integer) bodyJson.get("stock"));

    }
    @Test(priority = 3)
    public void validGetNameRequest() {

        this.response =
                given()
                        .accept(ContentType.JSON)
                        .header("Contect-Type", "application/json")
                        .when()
                        .get(createRequest().getName())
                        .then()
                        .statusCode(200)
                        .time(lessThan(500L))
                        .extract().response();

        JsonPath bodyJson = response.jsonPath();
        // Asserting for the first response
        Assert.assertNotNull(response.getBody());
        Assert.assertEquals(3, (Integer) bodyJson.get("id"));
        Assert.assertEquals("banana", bodyJson.get("name"));
        Assert.assertEquals(3.5, (double) bodyJson.get("price"));
        Assert.assertEquals(250, (Integer) bodyJson.get("stock"));
    }

    @Test(priority = 4)
    public void invalidPostRequestFourHundred() {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("name", createRequest().getName()); // invalid section for 400
        requestData.put("name", createRequest().getName());
        requestData.put("price", createRequest().getPrice());
        requestData.put("stock", createRequest().getStock());

        this.response =
                given()
                        .accept(ContentType.JSON)
                        .header("Contect-Type", "application/json")
                        .body(requestData)
                        .when()
                        .get(createRequest().getName())
                        .then()
                        .statusCode(400)
                        .time(lessThan(500L))
                        .extract().response();

        JsonPath bodyJson = response.jsonPath();
        // Asserting for the first response
        Assert.assertNull(bodyJson);
    }
    @Test(priority = 5)
    public void invalidPostRequestFourHundredFour() {

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("id", 123123123); // invalid section for 404
        requestData.put("name", createRequest().getName());
        requestData.put("price", createRequest().getPrice());
        requestData.put("stock", createRequest().getStock());

        this.response =
                given()
                        .accept(ContentType.JSON)
                        .header("Contect-Type", "application/json")
                        .when()
                        .body(requestData)
                        .get(createRequest().getName())
                        .then()
                        .statusCode(404)
                        .time(lessThan(500L))
                        .extract().response();

        JsonPath bodyJson = response.jsonPath();
        // Asserting for the first response
        Assert.assertNull(bodyJson);
    }

}
