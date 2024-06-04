import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONObject;


public class searchStnCode {

    private static Map<String, String> LineCode = new HashMap<>();

    private static final OkHttpClient client = new OkHttpClient();

    private boolean isBeforeStn(String trainStn, String myStn, String Line) throws IOException{
        // 열차의 현재 역, 내가 찾는 역, 호선을 입력값으로 가짐
        String metroAPI = readAPIKey.getAPIKey("강남");
        String apiURL = "http://openAPI.seoul.go.kr:8088/" + metroAPI + "/xml/SearchInfoBySubwayNameService/1/5/";
        Request request = new Request.Builder() //요청을 보낼 객체 생성
                .url(apiURL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("예상하지 못한 오류 발생 :  " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("열차 도착 정보가 비어있어 정보를 제공할 수 없습니다.");
            }

            String LineName = Main.getStnCode("노선", Line);

            String fullData = responseBody.string();
            if(fullData.contains("INFO-000")){
                JsonElement JSElement = JsonParser.parseString(fullData);
                JsonObject metroobject = JSElement.getAsJsonObject();
            }

        }
        catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
