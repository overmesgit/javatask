package secondtask;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by overmes on 17.11.14.
 */
public class Server {
    ConcurrentHashMap<Integer, Socket> socketMap = new ConcurrentHashMap<>();
    public static boolean isWork = true;

    public static void main(String[] args){
        Server server = new Server();
        server.run(9999);
    }

    public void run(int port){

        ServerSocket ss;

        try {
            ss = new ServerSocket(port);

            int counter = 0;
            while (true) {
                System.out.println("Waiting for a client");
                Socket socket = ss.accept();
                System.out.println("Got a client");
                socketMap.put(counter, socket);
                Thread thread = new Thread(new Listener(counter, socket, socketMap));
                thread.start();

                counter++;
            }
        } catch (IOException e) {
            isWork = false;
            e.printStackTrace();
        }
    }

    class Listener implements Runnable {
        Socket socket;
        Integer id;
        Map<Integer, Socket> socketMap;

        public Listener(int id, Socket currentSocket, Map<Integer, Socket> socketMap) {
            this.id = id;
            this.socket = currentSocket;
            this.socketMap = socketMap;
        }

        public void run() {
            try {
                while (!socket.isClosed() && isWork) {
                    String currentMessage = convertStreamToString(socket.getInputStream());
                    if (currentMessage.length() > 0) {
                        System.out.println(String.format("Received message %s", currentMessage));
                        Messenger.sentMessageToAll(socketMap, currentMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String convertStreamToString(InputStream in){
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            try {
                line = reader.readLine();
                while (line != null && line.length() != 0) {
                    out.append(line);
                    line = reader.readLine();
                }
                return out.toString();
            }
            catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        }

    }
}


class Messenger {
    public static synchronized void sentMessageToAll(Map<Integer, Socket> socketMap, String message){
        ArrayList<Integer> toDelete = new ArrayList<>();
        for (Map.Entry<Integer, Socket> current : socketMap.entrySet()) {
            Socket currentSocket = current.getValue();
            Integer currentId = current.getKey();
            try {
                OutputStream outputStream = currentSocket.getOutputStream();
                outputStream.write((message + "\n\n").getBytes());
            } catch (IOException e) {
                toDelete.add(currentId);
            }
        }

        for (Integer id: toDelete) {
            Socket currentSocket = socketMap.get(id);
            if (!currentSocket.isClosed()) {
                try {
                    currentSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socketMap.remove(id);
        }
    }
}
