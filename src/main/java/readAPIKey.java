import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readAPIKey {

    public static String getAPIKey() throws IOException {
        String metroAPI;
        
        try (BufferedReader APIgetter = new BufferedReader(new FileReader("src/Main/resources/API.txt"))) {
            // 파일을 읽어오기 위해서 BufferedReader 객체 생성
            //String API; // BufferedReader는 문자열로 읽어오기 때문에 문자열 변수 선언
            metroAPI = APIgetter.readLine();
            if(metroAPI == null){ //API 키가 유효하지 않은 경우
                return null;
            } else {
                return metroAPI;
            }
            
        } catch (IOException e) {
            return "저장된 API를 읽는 도중 오류 발생.";
        }
    }

    /*private static boolean isAPIKeyValid(String apiKey) {
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    /* public static void main(String[] args) {
        readAPIKey readAPIKey = new readAPIKey();
        try {
            String apiKey = readAPIKey.getAPIKey();
            if (apiKey != null) {
                System.out.println("사용 가능한 API 키: " + apiKey);
            } else {
                System.out.println("유효한 API 키를 찾지 못했습니다.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
