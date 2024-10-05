package helpers;

import org.json.simple.JSONObject;
import java.time.LocalDate;
import java.util.Random;

public class UserDataHelper {
    public static String generateUniqueEmail() {
        LocalDate today = LocalDate.now();
        String formattedDate = today.toString().replace("-", "");

        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);

        return "lely" + formattedDate + randomNumber + "@lelydemotest210243.com";
    }

    public static JSONObject createUserData(String email) {
        JSONObject request = new JSONObject();
        request.put("email", email);
        request.put("name", "test");
        request.put("gender", "male");
        request.put("status", "active");
        return request;
    }
}