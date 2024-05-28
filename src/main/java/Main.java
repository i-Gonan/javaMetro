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
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException{
        readAPIKey getKey = new readAPIKey();
        String apiKey = getKey.getAPIKey();
        if (apiKey == null || apiKey.equals("저장된 공공데이터 API 키를 읽어오는 도중에 오류가 발생했습니다.")){
            System.out.println("공공데이터 API키를 읽는 과정에서 문제가 발생하여 프로그램을 종료합니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("찾고자 하는 역명을 입력해주세요.\n검색을 종료하시려면 [종료]를 입력해주세요. >>> ");
            String stationInput = scanner.nextLine();
            if(stationInput.equals("종료")){
                break;
            } else {
                System.out.print("노선명을 입력해주세요.\n(1~9호선은 숫자만, 나머지는 노선 전체 이름 입력. 예: 1 / 4 / 경의중앙선) >>> ");
                String lineInput = scanner.nextLine();
            }

            String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/json/realtimeStationArrival/0/25/" + stationInput;

            try {
                getMetroData(apiUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getMetroData(String apiUrl) throws IOException {
        Request request = new Request.Builder()
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("예상하지 못한 오류 발생 :  " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("열차 도착 정보가 비어있습니다.");
            }

            String jsonData = responseBody.string();
            Object jsonObject = gson.fromJson(jsonData, Object.class);
            String prettyJson = gson.toJson(jsonObject);
            System.out.println("전체 요청 결과 : " + prettyJson);
        } catch (IOException e) {
            System.err.println("Error occurred during API request: " + e.getMessage());
            throw e;
        }
    }
}
