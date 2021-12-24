package network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import screen.WorldScreen;

public class Server implements Runnable{

    public static ArrayList<Socket> socketList = new ArrayList<>();//store sockets
    private static int num = 0; //connected num
    WorldScreen worldscreen;

    public Server(WorldScreen w){
        worldscreen = w;
    }

    public void send_info(String s){
        //System.out.println("come to send info: " + s);
        for (Socket Ssocket:Server.socketList){
            try {
                new DataOutputStream(Ssocket.getOutputStream()).writeUTF(s);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        //System.out.println("come to send info: end" + s);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        ServerSocket socket=null;
        Socket socket2 = null;
        try {
            socket = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(num < 2) {
            //Unlimited listening for client connections
            try {
                    Thread.sleep(1);
                    socket2=socket.accept();
                    if(socket2!=null){
                        socketList.add(socket2);
                        ++num;
                       // System.out.println(num);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            try {
                    //Assign ID number
                    new DataOutputStream(socket2.getOutputStream()).writeUTF("id " + num);
                    //Separate the message read thread.
                    Server_receive receive = new Server_receive(socket2,worldscreen);
                    Thread thread  = new Thread(receive);
                    thread.start();
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }
        try {
            worldscreen.build_online_game();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
