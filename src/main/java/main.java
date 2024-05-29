import com.google.gson.*;
import kotlin.time.TimeSource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class main {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, String> stationCode = new HashMap<>();
    private static JsonArray trainArray = new JsonArray();
    //private static JsonArray UPList = new JsonArray();
    //private static JsonArray DOWNList = new JsonArray();

    public static void setstnCode(){
        for(int i = 0; i < 19; i++){
            stationCode.put("1", "1001");
            stationCode.put("2", "1002");
            stationCode.put("3", "1003");
            stationCode.put("4", "1004");
            stationCode.put("5", "1005");
            stationCode.put("6", "1006");
            stationCode.put("7", "1007");
            stationCode.put("8", "1008");
            stationCode.put("9", "1009");
            stationCode.put("경의중앙선", "1063");
            stationCode.put("공항철도", "1065");
            stationCode.put("경춘선", "1067");
            stationCode.put("수인분당선", "1075");
            stationCode.put("신분당선", "1077");
            stationCode.put("우이신설선", "1092");
            stationCode.put("서해선", "1093");
            stationCode.put("경강선", "1081");
            stationCode.put("GTX-A", "1032");
        }
    }

    public static void main(String[] args) throws IOException{
        setstnCode();
        String lineInput = "", stationInput = "";
        readAPIKey getKey = new readAPIKey();
        String apiKey = getKey.getAPIKey();
        if (apiKey == null || apiKey.equals("저장된 공공데이터 API 키를 읽어오는 도중에 오류가 발생했습니다.")){
            System.out.println("공공데이터 API키를 읽는 과정에서 문제가 발생하여 프로그램을 종료합니다.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("찾고자 하는 역명('역' 제외 이름만)을 입력해주세요.\n검색을 종료하시려면 [종료]를 입력해주세요. >>> ");
            stationInput = scanner.nextLine();
            if(stationInput.equals("종료")){
                break;
            } else {
                System.out.print("노선명을 입력해주세요.\n(1~9호선은 숫자만, 나머지는 노선 숫자(이름) 입력. 예: 1, 4, 경의중앙선, GTX-A) >>> ");
                lineInput = scanner.nextLine();
            }

            String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + apiKey + "/json/realtimeStationArrival/0/25/" + stationInput;
            metroTimestamp stnTimestamp = new metroTimestamp(stationInput, stationCode.get(lineInput)); // 찾고자 하는 역의 근처에 있는 열차들의 정보를 가진 Train 클래스의 모음인 metroTimestamp 객체 생성

            try {
                getMetroData(apiUrl, stnTimestamp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getMetroData(String apiUrl, metroTimestamp Timestamps) throws IOException {

        Request request = new Request.Builder() //요청을 보낼 객체 생성
                .url(apiUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("예상하지 못한 오류 발생 :  " + response);
            }

            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("열차 도착 정보가 비어있어 정보를 제공할 수 없습니다.");
            }

            String jsonData = responseBody.string();
            JsonElement JSElement = JsonParser.parseString(jsonData);
            JsonObject metroobject = JSElement.getAsJsonObject();

            //응답 상태 반환 코드 확인
            JsonObject statusObject = metroobject.getAsJsonObject("errorMessage");
            String statusCode = statusObject.get("code").getAsString();

            JsonArray UPList = new JsonArray();
            JsonArray DOWNList = new JsonArray();


            if(statusCode.equals("INFO-000")){
                System.out.println("디버깅 >>> 요청 수신에 성공하였습니다.");
                trainArray = metroobject.getAsJsonArray("realtimeArrivalList");

                for(JsonElement tA : trainArray){
                    String Line = tA.getAsJsonObject().get("subwayId").getAsString();
                    if(Timestamps.getStnLine().equals(Line)){
                        String UPDOWN = tA.getAsJsonObject().get("updnLine").getAsString();
                        if(UPDOWN.equals("상행") || UPDOWN.equals("내선")){
                            UPList.add(tA);
                        } else if(UPDOWN.equals("하행") || UPDOWN.equals("외선")) {
                            DOWNList.add(tA);
                        }
                    } else {
                        System.out.println("디버깅 >>> 노선 정보가 일치하지 않습니다." + Line);
                    }
                }

                for(JsonElement UT : UPList){
                    System.out.println(UT);
                }

                System.out.println("+++++구분선+++++");

                for(JsonElement DT : DOWNList){
                    System.out.println(DT);
                }

                /*
                ! 남은 도착 시간을 초단위로 나타내주는 barvlDt 속성은 운영 주체가 서울교통공사인 노선에서는 매우 잘 나타나는데,
                데이터의 원천이 코레일(한국철도공사)인 경우 이 barvlDt 속성값이 0으로 나옴.
                ! 그래서 다른 속성 중 arvlMsg3 속성을 이용하여 코레일 노선은 열차가 현재 X전역에 있는지 출력할 계획
                ! 코레일 담당 노선 : 1호선: 청량리-서울역 제외 전부
                3호선 : 지축-대화
                4호선 : 남태령-오이도
                경춘선, 경의중앙선, 경강선, 수인분당선, 신분당선, 우이신설선, 공항철도, GTX-A
                 */

                String stnName = Timestamps.getStnName();

                //저장한 상행열차 정보를 출력
                System.out.println("********" + stnName + "역 상행열차 정보를 출력합니다.********");
                for(JsonElement up : UPList){
                    int trainID = up.getAsJsonObject().get("btrainNo").getAsInt(); //열차번호
                    String destination = up.getAsJsonObject().get("bstatnNm").getAsString(); //행선지
                    String nowLocation = up.getAsJsonObject().get("arvlMsg3").getAsString(); // 현재 위치
                    int arrivalTime = up.getAsJsonObject().get("barvlDt").getAsInt(); // 도착 예정 시간
                    Timestamps.addTimestamp_UP(trainID, destination, nowLocation, arrivalTime, stnName);
                    Timestamps.showUPStamp();
                }

                //저장한 하행열차 정보를 출력
                System.out.println("********" + stnName + "역 하행열차 정보를 출력합니다.********");
                for(JsonElement down : DOWNList){
                    int trainID = down.getAsJsonObject().get("btrainNo").getAsInt(); //열차번호
                    String destination = down.getAsJsonObject().get("bstatnNm").getAsString(); //행선지
                    String nowLocation = down.getAsJsonObject().get("arvlMsg3").getAsString(); // 현재 위치
                    int arrivalTime = down.getAsJsonObject().get("barvlDt").getAsInt(); // 도착 예정 시간
                    Timestamps.addTimestamp_DOWN(trainID, destination, nowLocation, arrivalTime, stnName);
                    Timestamps.showDOWNStamp();
                }
            } else {
                System.out.println("디버깅 >>> 요청 수신에 실패하였습니다.");
            }
        } catch (IOException e) {
            System.err.println("API를 요청하는 동안 오류가 발생했습니다. >>> " + e.getMessage());
            throw e;
        }
    }
}
