import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Map<String, String> LineNameCode = new HashMap<>(); //키와 값 쌍을 저장할 맵 생성

    private static boolean isInvalidStn(String stnName){
        String[] Jinjeop = {"진접", "오남", "별내별가람"}; // 진접선의 한쪽 종점인 당고개역은 서울 지하철 4호선의 일부이므로 조회가 가능하여 제외.
        String[] Sinlim = {"관악산", "서울대벤처타운", "서원", "신림", "당곡", "보라매병원", "보라매공원", "서울지방병무청"}; // 신림선 역 중 환승역(보라매, 대방) 제외
        String[] Uijeongbu = {"탑석", "송산", "어룡", "곤제", "효자", "경기도청북부청사", "새말", "동오", "의정부중앙", "흥선", "의정부시청", "경전철의정부", "범골", "발곡"}; // 의정부경전철 역 중 환승역(회룡) 제외
        boolean isInvalid = false;

        for(String stn : Jinjeop){
            if(stnName.equals(stn)){
                isInvalid = true;
                break;
            }
        }

        for(String stn : Sinlim){
            if(stnName.equals(stn)){
                isInvalid = true;
                break;
            }
        }

        for(String stn : Uijeongbu){
            if(stnName.equals(stn)){
                isInvalid = true;
                break;
            }
        }

        return isInvalid;
    }

    public static void setStnCode(){
        for(int i = 0; i < 19; i++){
            LineNameCode.put("1호선", "1001");
            LineNameCode.put("1", "1001");
            LineNameCode.put("2호선", "1002");
            LineNameCode.put("2", "1002");
            LineNameCode.put("3호선", "1003");
            LineNameCode.put("3", "1003");
            LineNameCode.put("4호선", "1004");
            LineNameCode.put("4", "1004");
            LineNameCode.put("5호선", "1005");
            LineNameCode.put("5", "1005");
            LineNameCode.put("6호선", "1006");
            LineNameCode.put("6", "1006");
            LineNameCode.put("7호선", "1007");
            LineNameCode.put("7", "1007");
            LineNameCode.put("8호선", "1008");
            LineNameCode.put("8", "1008");
            LineNameCode.put("9호선", "1009");
            LineNameCode.put("9", "1009");
            LineNameCode.put("경의중앙선", "1063");
            LineNameCode.put("경의선", "1063");
            LineNameCode.put("중앙선", "1063");
            LineNameCode.put("경중선", "1063");
            LineNameCode.put("공항철도", "1065");
            LineNameCode.put("경춘선", "1067");
            LineNameCode.put("경춘", "1067");
            LineNameCode.put("수인분당선", "1075");
            LineNameCode.put("수인선", "1075");
            LineNameCode.put("분당선", "1075");
            LineNameCode.put("수인분당", "1075");
            LineNameCode.put("신분당선", "1077");
            LineNameCode.put("우이신설선", "1092");
            LineNameCode.put("서해선", "1093");
            LineNameCode.put("경강선", "1081");
            LineNameCode.put("GTX-A", "1032");
            LineNameCode.put("GTXA", "1032");
        }
    }

    public static void main(String[] args) throws IOException{
        setStnCode(); // 해쉬맵에 호선명:호선 코드 키 쌍을 추가.
        StnNameEdit.setStnName(); // 일반적인 역명 (예: 화랑대)과 API에서 사용하는 역명(예: 화랑대(서울여대입구)) 등을 일치시키기 위한 함수 설정
        StnNameEdit.setStnCodeName(); // 역 코드 검색 API에서 사용할 역명과 실제 역명을 일치시키는 작업에 사용할 해쉬맵 초기화
        searchStnCode.initMap(); // 역코드 탐색에 사용할 다른 해쉬맵 초기화
        // 서로 다른 공공데이터API이기 때문에 1호선 - 01호선, 5호선 - 05호선, 경의중앙선 - 경의선, 우이신설선 - 우이신설경전철과 같이 다르게 표기되는 경우가 있어 별도 해쉬맵 생성
        String stationInput;
        String lineInput ;// 호선명과 역명을 입력받을 변수 선언

        String metroAPIKey = readAPIKey.getAPIKey("강남");
        // 서울시 지하철호선별 역별 승하차 인원 정보 기준 최다 승객이 이용하여 예시로 선정.

        if (metroAPIKey == null || metroAPIKey.equals("저장된 API를 읽는 도중 오류 발생.")){
            System.out.println("공공데이터 API키를 읽는 과정에서 문제가 발생하여 프로그램을 종료합니다.");
            System.out.println(metroAPIKey);
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("\n***** 서울 지하철 실시간 도착정보 안내 프로그램 *****\n" +
                    "서울시에서 제공하는 공공데이터 API를 활용한 지하철 실시간 도착정보 안내 프로그램입니다.\n" +
                    "역명을 입력하신 후에 노선을 입력해주시면 찾으시는 역의 실시간 열차 도착 정보를 상행과 하행 각각 2개씩 출력해 드립니다.\n" +
                    "!! 주의 : 노선의 기점/종점역의 경우 해당 역에서 출발하는 열차가 출력되지 않을 수 있습니다. !!\n\n");

            System.out.print("\n***** 찾고자 하는 역명('역' 제외 이름만)을 입력해주세요. *****\n" +
                    "!!! 진접선(별내별가람 ~ 진접) / 신림선 / 의정부 경전철 구간은 지원되지 않습니다. !!!\n" +
                    "검색을 종료하시려면 " + "[ 종료 ]" +" 를 입력해주세요. >>> ");
            stationInput = scanner.nextLine();
            if(!StnNameEdit.getstnName(stationInput).equals("해당사항없음")){
                stationInput = StnNameEdit.getstnName(stationInput);
            }

            boolean isInValidStn = isInvalidStn(stationInput);
            while(isInValidStn){
                System.out.print("\n***** 찾고자 하는 역명('역' 제외 이름만)을 입력해주세요. *****\n" +
                        "!!! 진접선(별내별가람 ~ 진접) / 신림선 / 의정부 경전철 구간은 지원되지 않습니다. !!!\n" +
                        "검색을 종료하시려면 " + "[ 종료 ]" +" 를 입력해주세요. >>> ");
                stationInput = scanner.nextLine();
                isInValidStn = isInvalidStn(stationInput);
                if(!isInValidStn){
                    break;
                }
            }

            if(stationInput.equals("종료")){
                break;
            }
            System.out.print("\n***** 노선명을 입력해주세요. *****\n" +
                    "$ 예시) 1호선, 4호선, 경의중앙선, GTX-A) >>> ");
            lineInput = scanner.nextLine();

            String apiUrl = "http://swopenapi.seoul.go.kr/api/subway/" + metroAPIKey + "/json/realtimeStationArrival/0/15/" + stationInput;
            // -> 요청을 보낼 API 키 조합
            metroTimestamp stnTimestamp = new metroTimestamp(stationInput, LineNameCode.get(lineInput)); // 찾고자 하는 역의 근처에 있는 열차들의 정보를 가진 Train 클래스의 모음인 metroTimestamp 객체 생성

            try {
                getData.getMetroData(apiUrl, stnTimestamp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}