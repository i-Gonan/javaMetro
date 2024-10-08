
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class searchStnCode {

    private static Map<String, String> codeToLine = new HashMap<>();
    private static final OkHttpClient client = new OkHttpClient();
    private static JsonArray stationArray = new JsonArray();

    public static void initMap(){
        codeToLine.put("1001", "01호선");
        codeToLine.put("1002", "02호선");
        codeToLine.put("1003", "03호선");
        codeToLine.put("1004", "04호선");
        codeToLine.put("1005", "05호선");
        codeToLine.put("1006", "06호선");
        codeToLine.put("1007", "07호선");
        codeToLine.put("1008", "08호선");
        codeToLine.put("1009", "09호선");
        codeToLine.put("1063", "경의선");
        codeToLine.put("1065", "공항철도");
        codeToLine.put("1067", "경춘선");
        codeToLine.put("1075", "수인분당선");
        codeToLine.put("1077", "신분당선");
        codeToLine.put("1092", "우이신설경전철");
        codeToLine.put("1093", "서해선");
        codeToLine.put("1081", "경강선");
        codeToLine.put("1032", "GTX-A");
    }

    private static String getLine(String lineCode){
        for(Map.Entry<String, String> entry : codeToLine.entrySet()){
            if(entry.getKey().equals(lineCode)){
                return entry.getValue();
            }
        }
        return null;
    }



    private static String getStnCode(String stn, String Line) throws IOException{
        if(stn.equals("총신대입구(이수)")){
            if(Line.equals("1004")){
                stn = StnNameEdit.getstnCodeName("총신대입구(이수)(4)");
            }
            if(Line.equals("1007")){
                stn = StnNameEdit.getstnCodeName("총신대입구(이수)(7)");
            }
        } else if(!StnNameEdit.getstnCodeName(stn).equals("해당사항없음")){
            stn = StnNameEdit.getstnCodeName(stn);
        }
        String metroAPI = readAPIKey.getAPIKey("강남");
        String apiURL = "http://openAPI.seoul.go.kr:8088/" + metroAPI + "/json/SearchInfoBySubwayNameService/1/5/" + stn;
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

            String LineName = getLine(Line);

            String fullData = responseBody.string();
            if(fullData.contains("INFO-000")){
                JsonElement JSElement = JsonParser.parseString(fullData);
                JsonObject metroobject = JSElement.getAsJsonObject().getAsJsonObject("SearchInfoBySubwayNameService");
                stationArray = metroobject.getAsJsonArray("row");

                String stnCD = "";

                for(JsonElement sA : stationArray){
                    String stnName = sA.getAsJsonObject().get("STATION_NM").getAsString(); // 역명을 문자열로 저장
                    String lnName = sA.getAsJsonObject().get("LINE_NUM").getAsString(); // 노선명을 문자열로 저장
                    if(lnName.equals(getLine(Line))){
                        stnCD = sA.getAsJsonObject().get("FR_CODE").getAsString();
                        return stnCD;
                    }
                }



            } else {
                System.out.println(stn + "이 잘못되었습니다. " + Line);
            }
            return null;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isBeforeStn(String trainDestStn, String myStn, String Line, String type) throws IOException{
        // 열차의 현재 역, 내가 찾는 역, 호선을 입력값으로 가짐
        trainDestStn = trainDestStn.split(" ")[0];
        String trainDestStnCode = getStnCode(trainDestStn, Line);
        String myStnCode = getStnCode(myStn, Line);
        if((type.equals("상행") || type.equals("내선")) && myStnCode.compareTo(trainDestStnCode) <= 0){
            return true;
        } else if((type.equals("하행") || type.equals("외선")) && myStnCode.compareTo(trainDestStnCode) >= 0){
            return true;
        }else{
            return false;
        }
    }
}
