package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import screen.WorldScreen;

public class Server_receive implements Runnable{
    private DataInputStream stream = null;
    private Socket socket = null;
    WorldScreen worldscreen;
    
    public Server_receive(Socket socket, WorldScreen w) throws IOException {
        this.socket = socket;
        worldscreen=w;
        stream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        String rece = null;
        while(true) {
            try {
                Thread.sleep(5);
                if (stream.available()!=0) {
                    try {
                        rece = stream.readUTF();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(rece);
                    worldscreen.server_receive_info(rece);
                    //System.out.println(rece+"OK");
                    //for (Socket Ssocket:Server.socketList)
                    //    new DataOutputStream(Ssocket.getOutputStream()).writeUTF(rece);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
}
