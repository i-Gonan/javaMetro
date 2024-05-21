import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import java.io.IOException;
import java.util.Scanner;

class Main {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("검색할 역명을 입력하세요: ");
        String userInput = scanner.nextLine(); // 사용자 입력 받기

        // 사용자 입력을 URL에 추가"
        String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/71546e56626d64633131304745437562/json/realtimeStationArrival/0/15/" + userInput; // 실제 API URL로 변경, 'query'는 예시 파라미터

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

            System.out.println("응답: " + jsonData); // 응답 출력
        } catch (IOException e) {
            System.err.println("API 요청 중 오류 발생: " + e.getMessage());
            throw e;
        }
    }
}