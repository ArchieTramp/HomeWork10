package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ChatServ extends Thread {
    private static final Integer SERVER_PORT = 4044;
    public static DatagramSocket serverSocket;
    private static boolean starting;
    private static byte[] buf = new byte[256];

     public ChatServ() throws SocketException {
        serverSocket = new DatagramSocket(SERVER_PORT);
    }

    public void start(){
        starting = true;
    }
    public static void main(String[] args) {

        while (starting) {
            DatagramPacket message = new DatagramPacket(buf, buf.length);
            try {
                serverSocket.receive(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetAddress address = message.getAddress();
            int port = message.getPort();
            message = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(message.getData(), 0, message.getLength());

            if (received.equals("end")) {
                starting = false;
                continue;
            }
            try {
                serverSocket.send(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        serverSocket.close();
    }

}
