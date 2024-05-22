import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader;
        bufferedReader = new BufferedReader(new FileReader("src\\main\\resources\\API.txt"));
        main main = new main(); //main 인스턴스 생성
        while(true) {
            String line = bufferedReader.readLine();
            if(line == null)
                break;
            main.storeLine(line); // 여기서 `line` 변수를 DataStorage 클래스의 메소드에 전달
        }
        bufferedReader.close();
    }
}
