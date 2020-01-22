package frc.util;

import java.io.*;
import java.net.*;
import java.rmi.server.RMIClientSocketFactory;

public class TcpServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    public int port;

    public TcpServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        output = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    public String getData() throws IOException {
        return input.readLine();
    }

    public void stop() throws IOException {
        input.close();
        output.close();
        clientSocket.close();
        serverSocket.close();
    }

}
