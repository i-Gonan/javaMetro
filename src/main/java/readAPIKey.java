import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readAPIKey {
    private String[] APIList = new String[getLines()]; // API 키를 저장할 배열 생성

    private static int getLines(){ // API(줄바꿈으로 구분됨)가 저장된 개수를 확인하는 함수
        int lines = 0;

        try(BufferedReader getlines = new BufferedReader(new FileReader("src/mains/resources/lines.txt"))){
            while(getlines.readLine() != null){
                lines++;
            }
        } catch (IOException e) {
            System.err.println(e);
            return 0;
        }

        return lines;
    }

    public static String getAPIKey(String testStnName) throws IOException {
        String metroAPI; // BufferedReader는 문자열로 읽어오기 때문에 문자열 변수 선언
        
        try (BufferedReader APIgetter = new BufferedReader(new FileReader("src/main/resources/API.txt"))) {
            // 파일을 읽어오기 위해서 BufferedReader 객체 생성
            //String API;
            metroAPI = APIgetter.readLine();
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
