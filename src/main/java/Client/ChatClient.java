package Client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class ChatClient {
    private static final Integer SERVER_PORT = 4044;
    private static byte[] buf;
    private static InetAddress address;
    public static DatagramSocket clientSocket;

    {
        try {
            clientSocket = new DatagramSocket(SERVER_PORT);
            address = InetAddress.getByName("127.0.0.1");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
        clientSocket.send(packet);
        packet = new DatagramPacket(buf, buf.length);
        clientSocket.receive(packet);
    }


}
