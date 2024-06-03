import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class searchAPI {
    private static final OkHttpClient client = new OkHttpClient();


    
    public static boolean isValidAPI(String apiKey, String station){
        String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/json/realtimeStationArrival/0/1/" + station;

        Request rq = new Request.Builder()
                .url(apiUrl)
                .build();

        try(Response rs = client.newCall(rq).execute()){
            if(!rs.isSuccessful()){
                throw new IOException("예상하지 못한 오류 발생 : " + rs);
            }

            ResponseBody rsBody = rs.body();
            if(rsBody == null){
                throw new IOException("유효한 API 키가 아닌 것 같습니다.");
            }

            String jsonData = rsBody.string();
            if(jsonData.contains("INFO-000")){
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return false;
    }
}
