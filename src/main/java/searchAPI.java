import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class searchAPI {
    private static final OkHttpClient client = new OkHttpClient();

    private static int getLines(){ // API(줄바꿈으로 구분됨)가 저장된 개수를 확인하는 함수
        int lines = 0;

        try(BufferedReader getlines = new BufferedReader(new FileReader("src/mains/resources/lines.txt"))){
            while(getlines.readLine() != null){
                lines++;
            }
        } catch (IOException e) {
            System.err.println(e);
            return -1;
        }

        return lines;
    }
    
    public static String isValidAPI(String station){
        int API_Length = getLines();
        String[] API = new String[API_Length]; // API 키를 저장할 배열 생성
        String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + API[0] + "/json/realtimeStationArrival/0/1/" + station;

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
                return API[0];
            } else {
                return "다음키입력";
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        String validAPI = "";
        return validAPI;
    }
}
