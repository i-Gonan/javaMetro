import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class readAPIKey {
    private String metroAPI;

    public readAPIKey(){
        this.metroAPI = "";
    }

    public String getAPIKey() throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/API.txt"))){
            this.metroAPI = bufferedReader.readLine();
            bufferedReader.close();
            if(this.metroAPI == null) {
                return null;
            } else {
                return this.metroAPI;
            }
        } catch(IOException e){
            return "저장된 공공데이터 API 키를 읽어오는 도중에 오류가 발생했습니다.";
        }

    }
}
