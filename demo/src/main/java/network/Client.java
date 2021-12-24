package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import screen.WorldScreen;

public class Client {
    private Socket socket = null;
    private WorldScreen worldscreen;

    public Client(WorldScreen w){
        worldscreen=w;
    }

    //Connect to server
    public void connect(int port) throws UnknownHostException, IOException {
        try {
            socket = new Socket("127.0.0.1", port);
    //            System.out.println("Client-----------------------");
    //            System.out.println("Address to connect to"+socket.getInetAddress()+":"+socket.getLocalPort());  
    //            jframe.TextA("Connected");
            } catch (IOException e1) {
                e1.printStackTrace();
        }

        Runnable runnable = new Client_receive(socket,worldscreen);  //Start message read / write thread
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //Start the send () method of the message reading and writing thread and send a message to the server
    public void send(String send) throws IOException {
        DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
        stream.writeUTF(send);
    }   

}
