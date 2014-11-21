import java.io.*;
import java.net.Socket;

/**
 * Created by overmes on 17.11.14.
 */
public class SendMessage {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 9999);
        InputStream sin = socket.getInputStream();
        OutputStream sout = socket.getOutputStream();
        sout.write((args[0]+ "\n\n").getBytes());
        socket.close();
    }
}
