import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Pretty print 설정 추가

    public static void storeLine(String API) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("찾고자하는 역명은?: ");
        String userInput = scanner.nextLine();

        String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + API + "/json/realtimeStationArrival/0/15/" + userInput;

        try {
            fetchData(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fetchData(String apiUrl) throws IOException {
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Response body is null");
            }

            String jsonData = responseBody.string();
            Object jsonObject = gson.fromJson(jsonData, Object.class);
            String prettyJson = gson.toJson(jsonObject);
            System.out.println("Response: " + prettyJson);
        } catch (IOException e) {
            System.err.println("Error occurred during API request: " + e.getMessage());
            throw e;
        }
    }
}
