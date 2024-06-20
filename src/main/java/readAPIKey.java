import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readAPIKey {

    public static String getAPIKey(String testStnName) throws IOException {
        String metroAPI; // BufferedReader는 문자열로 읽어오기 때문에 문자열 변수 선언

        try (BufferedReader APIgetter = new BufferedReader(new FileReader("src/main/resources/API.txt"))) {
            // 파일을 읽어오기 위해서 BufferedReader 객체 생성
            metroAPI = APIgetter.readLine();
            int cnt = 0;
            while(metroAPI != null){
                boolean isValidKey = searchAPI.isValidAPI(metroAPI, testStnName);
                if(isValidKey){
                    return metroAPI;
                } else {
                    metroAPI = APIgetter.readLine();
                }
            }
            return metroAPI;
        } catch (IOException e) {
            System.err.println(e);
            return "저장된 API를 읽는 도중 오류 발생.";
        }
    }
}