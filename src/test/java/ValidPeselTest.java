import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.get;

public class ValidPeselTest {
    @Test
    public void testGetRequest_StatusCodeis200() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97041753796");

        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test
    public void testGetRequest_PeselisValid() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=57041719268");

        Assert.assertTrue(response.path("isValid"));
    }

    @Test
    public void testGetRequest_BirthDateisValid() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90030359528");

        String birthdate = response.path("dateOfBirth");

        Assert.assertEquals(birthdate, "1990-03-03T00:00:00");
    }

    @Test
    public void testGetResponse_FemaleGenderisValid() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90030359528");

        String actualResponse = response.path("gender");

        Assert.assertEquals(actualResponse, "Female");
    }

    @Test
    public void testGetResponse_MaleGenderisValid() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90030344414");

        String actualResponse = response.path("gender");

        Assert.assertEquals(actualResponse, "Male");
    }

    @Test
    public void testGetResponse_BodyisValid() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90111199540");

        String actualBody = response.getBody().asString();
        String expectedBody = "{\"pesel\":\"90111199540\",\"isValid\":true,\"dateOfBirth\":\"1990-11-11T00:00:00\",\"gender\":\"Female\",\"errors\":[]}";

        Assert.assertEquals(actualBody, expectedBody);
    }
}