package Client2.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class ChatClient {
    private static final Integer SERVER_PORT = 4044;
    private static byte[] buf;
    private static InetAddress address;
    public static DatagramSocket clientSocket;

    public static void main(String[] args) throws SocketException {
        String host = null;
        if (args.length < 1) {
            System.out.println("ChatClient");
            System.exit(0);
        } else {
            host = args[0];
        }
        DatagramSocket datagramSocket = new DatagramSocket();
        ReceiveMSG receiveMSG = new ReceiveMSG(datagramSocket);
        SendMSG sendMSG = new SendMSG(datagramSocket, host);
        Thread threadReceive = new Thread(receiveMSG);
        Thread threadSend = new Thread(sendMSG);
        threadReceive.start();
        threadSend.start();

    }

}


class SendMSG implements Runnable {
    private static final Integer SERVER_PORT = 4044;
    private DatagramSocket clientSocket;
    private static byte[] buf;
    private static InetAddress address;
    private String hostname;

    SendMSG(DatagramSocket c, String h) {
        clientSocket = c;
        hostname = h;
    }

    private void sendMess(String c) throws IOException {
        buf = c.getBytes();
        address = InetAddress.getByName(hostname);
        DatagramPacket message = new DatagramPacket(buf, buf.length, address, SERVER_PORT);
        clientSocket.send(message);
    }

    @Override
    public void run() {
        boolean starting = false;
        do {
            try {
                sendMess("Welcome");
                starting = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!starting);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        do {
            while (true) {
                try {
                    if (!input.ready()) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                sendMess(input.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (true);

    }

}

class ReceiveMSG implements Runnable {
    public DatagramSocket clientSocket;
    private static byte[] buf;

    ReceiveMSG(DatagramSocket c) {
        clientSocket = c;
        buf = new byte[256];
    }

    @Override
    public void run() {
        while (true) {
            DatagramPacket message = new DatagramPacket(buf, buf.length);
            try {
                clientSocket.receive(message);
                String recieved = new String(message.getData(), 0, message.getLength());
                System.out.println(recieved);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}

