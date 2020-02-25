package frc.util;

import edu.wpi.first.wpiutil.math.Num;

import java.io.IOException;
import java.net.*;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;

public class udpServer implements Runnable {
    private DatagramSocket udpSocket;
    private int port;

    private volatile double[] data;

    private Semaphore lock;
    public udpServer(int port) throws SocketException, IOException {
        this.port = port;
        this.udpSocket = new DatagramSocket(this.port);
        this.lock = new Semaphore(1);
        data = new double[2];
    }

    public void run() {
        try {
            System.out.println("-- Running Server at " + InetAddress.getLocalHost() + "--");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String msg;

        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        while (true) {
            // blocks until a packet is received
            try {
                udpSocket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            msg = new String(packet.getData(), packet.getOffset(), packet.getLength());

            String[] data = msg.split(",");
            double distance = 0;
            double angle = 0;
            try {
                distance = Double.parseDouble(data[0]);
            }catch (NumberFormatException e) {
                distance = 0;
            }

            try {
                angle = Double.parseDouble(data[1]);
            } catch (NumberFormatException e) {
                angle = 0;
            }

            angle = Double.parseDouble(data[1]);

            try {
                this.lock.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.data[0] = distance;
            this.data[1] = angle;
            this.lock.release();

//            System.out.println("Distance: "+ distance + ", Angle" + angle);
        }
    }

    public double[] getData() throws InterruptedException {
        this.lock.acquire();
        double distance, angle;
        distance = this.data[0];
        angle = this.data[1];
        this.lock.release();
        return new double[]{distance, angle};
    }
}