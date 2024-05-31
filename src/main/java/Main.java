import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    //private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Map<String, String> stationCode = new HashMap<>();

    public static String getStnCode(String Type, String Code){
        // 키에 해당하는 값을 받아올건지, 값에 해당하는 키를 받아올건지 Type으로 입력받고,
        // 타입에서 코드에 해당하는 값/키를 반환.
        if(Type.equals("노선")){
            for(Map.Entry<String, String> entry : stationCode.entrySet()){ // 호선명 : 호선 코드를 입력해놓은 역 코드 맵을 탐색
                if(entry.getKey().equals(Code)){ // 찾고 있는 호선 이름과 일치하다면
                    return entry.getKey(); // 해당 키 반환
                }
            }
        } else if(Type.equals("코드")){
            for(Map.Entry<String, String> entry : stationCode.entrySet()){ 
                if(entry.getValue().equals(Code)){ // 찾고 있는 호선 코드와 일치하다면
                    return entry.getValue(); // 해당 키 반환
                }
            }
        }
        return null;
    }

    public static void setStnCode(){
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
        
        setStnCode(); // 해쉬맵에 호선명:호선 코드 키 쌍을 추가.
        
        String lineInput, stationInput; // 호선명 / 역명을 입력받을 변수 선언
        
        String metroAPIKey = readAPIKey.getAPIKey();

        if (metroAPIKey == null || metroAPIKey.equals("저장된 API를 읽는 도중 오류 발생.")){
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
                System.out.print("노선명을 입력해주세요.\n(1~9호선은 숫자만, 나머지는 노선 이름 입력. 예: 1, 4, 경의중앙선, GTX-A) >>> ");
                lineInput = scanner.nextLine();
            }

            String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + metroAPIKey + "/json/realtimeStationArrival/0/8/" + stationInput;
            // ㄴ-> 요청을 보낼 API 키 조합
            metroTimestamp stnTimestamp = new metroTimestamp(stationInput, stationCode.get(lineInput)); // 찾고자 하는 역의 근처에 있는 열차들의 정보를 가진 Train 클래스의 모음인 metroTimestamp 객체 생성

            try {
                getData.getMetroData(apiUrl, stnTimestamp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
