package Server;

import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class ChatServ extends Thread {
    private static final Integer SERVER_PORT = 4044;
    public static DatagramSocket serverSocket;
    private final ArrayList<InetAddress> addresses;
    private final ArrayList<Integer> ports;
    private final HashSet<String> clients;

    public static void main(String[] args) throws SocketException {
        ChatServ chatServ = new ChatServ();
        chatServ.start();

    }
    public ChatServ() throws SocketException {
        serverSocket = new DatagramSocket(SERVER_PORT);
        addresses = new ArrayList<>();
        ports = new ArrayList<>();
        clients = new HashSet<>();
    }

    @Override

    public void run() {
        byte[] buf = new byte[256];
        do {
            Arrays.fill(buf, (byte) 0);
            DatagramPacket message = new DatagramPacket(buf, buf.length);
            try {
                serverSocket.receive(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String content = new String(buf, buf.length);

            InetAddress address = message.getAddress();
            int port = message.getPort();

            String id = address.toString() + "," + port;
            if (!clients.contains(id)) {
                clients.add(id);
                ports.add(port);
                addresses.add(address);
            }

            System.out.println(id + " : " + content);
            byte[] data = (id + " : " + content).getBytes();
            for (int i = 0; i < addresses.size(); i++) {
                InetAddress cl = addresses.get(i);
                int cp = ports.get(i);
                message = new DatagramPacket(data, data.length, cl, cp);
                try {
                    serverSocket.send(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } while (true);
    }
}