package frc.util;

import java.net.*;
import java.io.*;

public class TcpServer {

    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream dataInputStream = null;


    public TcpServer() {
        try {
            serverSocket = new ServerSocket();
        }
    }

}
