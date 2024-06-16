import java.util.HashMap;
import java.util.Map;

public class StnNameEdit {
    private static HashMap<String, String> stnNameMap = new HashMap<>();
    private static HashMap<String, String> stnNameMap_forCode = new HashMap<>();

    public static void setStnName(){
        stnNameMap.put("응암", "응암순환(상선)");
        stnNameMap.put("공릉", "공릉(서울과학기술대)");
        stnNameMap.put("이수", "총신대입구(이수)");
        stnNameMap.put("총신대입구", "총신대입구(이수)");
        stnNameMap.put("천호", "천호(풍납토성)");
        stnNameMap.put("남한산성입구", "남한산성입구(성남법원,검찰청)");
        stnNameMap.put("대모산입구", "대모산");
        stnNameMap.put("몽촌토성", "몽촌토성(평화의문)");
        stnNameMap.put("화랑대", "화랑대(서울여대입구)");
    }

    public static String getstnName(String stnName){
        for(Map.Entry<String, String> stnMap : stnNameMap.entrySet()){
            if(stnMap.getKey().equals(stnName)){
                return stnMap.getValue();
            }
        }
        return "해당사항없음";
    }

    public static void setStnCodeName(){ //역 정보를 조회할 때 사용할 해쉬맵
        stnNameMap_forCode.put("응암순환(상선)", "응암");
    }

    public static String getstnCodeName(String stnName){
        for(Map.Entry<String, String> stnMap : stnNameMap_forCode.entrySet()){
            if(stnMap.getKey().equals(stnName)){
                return stnMap.getValue();
            }
        }
        return "해당사항없음";
    }
}//입력 내용을 API에 맞게 정정하는 클래스 및 메서드 작성 바랍니다.
