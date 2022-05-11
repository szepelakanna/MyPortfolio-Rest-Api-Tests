import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.get;

public class InvalidPeselTest {

    @Test
    public void testGetRequest_ResponseStatusCodeBadRequest() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel");

        Assert.assertEquals(response.statusCode(),400);
    }

    @Test
    public void testGetRequest_InvalidPeselLenght() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=690306165447");

        String actualResponse = response.path("errors[0].errorCode");
        String errorMsg = response.path("errors[0].errorMessage");

        Assert.assertEquals(actualResponse, "INVL");

        Assert.assertEquals(errorMsg, "Invalid length. Pesel should have exactly 11 digits.");
    }

    @Test
    public void testGetRequest_InvalidPeselCharacters() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=9715179711a");

        String errorMsg = response.path("errors[0].errorMessage");
        String actualResponse = response.path("errors[0].errorCode");

        Assert.assertEquals(actualResponse, "NBRQ");

        Assert.assertEquals(errorMsg, "Invalid characters. Pesel should be a number.");
    }
    @Test
    public void testGetRequest_InvalidPeselSum() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=97041751496");

        String errorMsg = response.path("errors[0].errorMessage");
        String actualResponse = response.path("errors[0].errorCode");

        Assert.assertEquals(actualResponse, "INVC");

        Assert.assertEquals(errorMsg, "Check sum is invalid. Check last digit.");
    }

    @Test
    public void testGetRequest_InvalidPeselDay() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90063102119");

        String errorMsg = response.path("errors[0].errorMessage");
        String actualResponse = response.path("errors[0].errorCode");

        Assert.assertEquals(actualResponse, "INVD");

        Assert.assertEquals(errorMsg, "Invalid day.");
    }

    @Test
    public void testGetRequest_InvalidPeselMonth() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=90001512147");

        String errorMsgYear = response.path("errors[1].errorMessage");
        String actualYear = response.path("errors[1].errorCode");

        Assert.assertEquals(actualYear, "INVM");

        Assert.assertEquals(errorMsgYear, "Invalid month.");
    }

    @Test
    public void testGetRequest_InvalidPeselYear() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=45936781269");

        String errorMsg = response.path("errors[0].errorMessage");
        String actualResponse = response.path("errors[0].errorCode");

        Assert.assertEquals(actualResponse, "INVY");

        Assert.assertEquals(errorMsg, "Invalid year.");
    }

    @Test
    public void testGetRequest_PeselGenderisnull() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=690306165447");

        String actualResponse = response.path("gender");

        Assert.assertNull(actualResponse);
    }

    @Test
    public void testGetRequest_PeselDateOfBirthisnull() {
        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=45936781269");

        String birthdate = response.path("dateOfBirth");

        Assert.assertNull(birthdate);
    }

    @Test
    public void testGetRequest_VerifyThreeErrorCodes(){
        List<String> expectedErrorCodesList = new ArrayList<String>();
        expectedErrorCodesList.add("INVC");
        expectedErrorCodesList.add("INVY");
        expectedErrorCodesList.add("INVM");

        Response response = get("https://peselvalidatorapitest.azurewebsites.net/api/Pesel?pesel=99130181966");
        List<String> errorCodesList = response.path("errors.errorCode");

        Assert.assertEquals(errorCodesList,expectedErrorCodesList);
    }
}