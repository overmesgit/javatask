import java.io.*;
import java.net.Socket;

/**
 * Created by overmes on 17.11.14.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }

    public void run() throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        InputStream sin = socket.getInputStream();
        OutputStream sout = socket.getOutputStream();
        while (true) {
            String answer = convertStreamToString(sin);
            if (answer.length() > 0) {
                System.out.println(answer);
            }
        }
    }

    public String convertStreamToString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line = reader.readLine();
        while (line != null && line.length() != 0) {
            out.append(line);
            line = reader.readLine();

        }
        return out.toString();
    }
}
