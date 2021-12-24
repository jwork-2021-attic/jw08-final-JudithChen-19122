package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import screen.WorldScreen;

public class Client_receive implements Runnable{
    private String rece = null;
    private static DataInputStream stream;
    WorldScreen worldscreen;
    
    public Client_receive(Socket socket, WorldScreen w) throws IOException{
        worldscreen=w;
        stream = new DataInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while(true) {
            try {
                //The if condition determines whether there are available bytes. If you do not judge and keep reading, a read exception will be thrown at the end. There is no at run time.
                Thread.sleep(5);
                if(stream.available()!=0) {
                    rece = stream.readUTF();
                    //System.out.println(rece);
                    worldscreen.client_receive_info(rece);
                    
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
}
