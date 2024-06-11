import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class getData {
    private static final OkHttpClient client = new OkHttpClient();
    private static JsonArray trainArray = new JsonArray();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

            JsonArray UPList = new JsonArray(); // 상행열차 정보를 저장할 JSON배열
            JsonArray DOWNList = new JsonArray(); // 하행열차 정보를 저장할 JSON배열


            if(statusCode.equals("INFO-000")){
                //System.out.println("디버깅 >>> 요청 수신에 성공하였습니다.");
                trainArray = metroobject.getAsJsonArray("realtimeArrivalList");

                for(JsonElement tA : trainArray){
                    String Line = tA.getAsJsonObject().get("subwayId").getAsString();
                    if(Timestamps.getStnLine().equals(Line)){
                        String UPDOWN = tA.getAsJsonObject().get("updnLine").getAsString();
                        if(UPDOWN.equals("상행") || UPDOWN.equals("내선")){
                            if(UPDOWN.equals("상행") &&
                                    (searchStnCode.isBeforeStn(tA.getAsJsonObject().get("bstatnNm").getAsString(), Timestamps.getStnName(), Timestamps.getStnLine(), UPDOWN))){
                                //당역종착 & 이 역에 오기전에 종착하는 열차는 포함하지 않음
                            } else {
                                if(UPList.size() >= 3){
                                    //각 방향별로 최대 3개의 열차를 보여줄 것이기 때문에 3개를 넘으면 작업 종료
                                    break;
                                }
                                else {
                                    UPList.add(tA);
                                }
                            }
                        } else if(UPDOWN.equals("하행") || UPDOWN.equals("외선")) {
                            if(UPDOWN.equals("하행") &&
                                    (searchStnCode.isBeforeStn(tA.getAsJsonObject().get("bstatnNm").getAsString(), Timestamps.getStnName(), Timestamps.getStnLine(), UPDOWN))){
                                //당역종착 & 이 역에 오기전에 종착하는 열차는 포함하지 않음
                            } else {
                                if(DOWNList.size() >= 3){
                                    // 역시 똑같이 아무것도 안하고 작업 종료
                                    break;
                                } else {
                                    DOWNList.add(tA);
                                }
                            }
                        }
                    }
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

                //상행열차 정보 저장
                for(JsonElement up : UPList){
                    String trainID = up.getAsJsonObject().get("btrainNo").getAsString(); //열차번호
                    String destination = up.getAsJsonObject().get("bstatnNm").getAsString(); //행선지
                    String nowLocation = up.getAsJsonObject().get("arvlMsg3").getAsString(); // 현재 위치
                    String arrivalTime = up.getAsJsonObject().get("arvlMsg2").getAsString(); // 도착 예정 시간
                    String Direction = up.getAsJsonObject().get("updnLine").getAsString(); // 상행값 가져오기
                    Timestamps.addTimestamp_UP(trainID, destination, nowLocation, arrivalTime, stnName, Direction);
                }

                //하행열차 정보 저장
                for(JsonElement down : DOWNList){
                    String trainID = down.getAsJsonObject().get("btrainNo").getAsString(); //열차번호
                    String destination = down.getAsJsonObject().get("bstatnNm").getAsString(); //행선지
                    String nowLocation = down.getAsJsonObject().get("arvlMsg3").getAsString(); // 현재 위치
                    String arrivalTime = down.getAsJsonObject().get("arvlMsg2").getAsString(); // 도착 예정 시간
                    String Direction = down.getAsJsonObject().get("updnLine").getAsString();
                    Timestamps.addTimestamp_DOWN(trainID, destination, nowLocation, arrivalTime, stnName, Direction);
                }

                //저장한 상행열차 정보를 출력
                System.out.println("\n********" + stnName + "역 상행열차 정보를 출력합니다.********");
                Timestamps.showUPStamp();


                //저장한 하행열차 정보를 출력
                System.out.println("\n********" + stnName + "역 하행열차 정보를 출력합니다.********");
                Timestamps.showDOWNStamp();

            } else {
                System.out.println("요청 수신에 실패하였습니다.");
            }
        } catch (IOException e) {
            System.err.println("API를 요청하는 동안 오류가 발생했습니다. >>> " + e.getMessage());
            throw e;
        }
    }
}
