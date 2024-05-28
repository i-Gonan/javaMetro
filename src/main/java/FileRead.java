import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileRead {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/API.txt"));
        while (true) {
            String API = bufferedReader.readLine();
            if (API == null)
                break;
            Main.storeLine(API); // Call static method from Main class
        }
        bufferedReader.close();
    }
}
