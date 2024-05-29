import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readAPIKey {
    private String metroAPI;

    public readAPIKey(){
        this.metroAPI = "";
    }

    public String getAPIKey() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("src/Main/resources/API.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (isAPIKeyValid(line)) {
                    this.metroAPI = line;
                    return this.metroAPI;
                }
            }
            return null; // 유효한 API 키를 찾지 못한 경우
        } catch (IOException e) {
            return "저장된 공공데이터 API 키를 읽어오는 도중에 오류가 발생했습니다.";
        }
    }

    private boolean isAPIKeyValid(String apiKey) {
        // 여기에 API 키의 유효성을 검사하는 로직을 추가합니다.
        // 예를 들어, API 키를 사용해 실제 API 호출을 시도하고 응답을 확인하는 방법이 있을 수 있습니다.
        // 여기서는 단순히 키가 비어있지 않은지 확인하는 예시를 보여줍니다.
        return apiKey != null && !apiKey.trim().isEmpty();
    }

    public static void main(String[] args) {
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
    }
}
