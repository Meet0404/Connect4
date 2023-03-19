package com.company;

import java.io.*;
import java.net.*;

public class GameServer {
    private ServerSocket ss;// creates the Server socket
    private int numPlayers;//to track number of player
    private ServerSideConnection p1;
    private ServerSideConnection p2;

    //this constructor is used to initialize the socket with port number
    public GameServer(){
        numPlayers=0;
        System.out.println("-------Game Server Running-------");
        try{
            ss = new ServerSocket(4444);
        }
        catch (IOException ex){
            System.out.println("Error in game server constructor ");
        }
    }

    public void AcceptConnections(){
        try{
            System.out.println("Waiting for connection.....");
            //this is used to monitor the maximum number of players allowed
            while(numPlayers<2){
                Socket s= ss.accept();
                numPlayers++;
                System.out.println("Player #"+numPlayers+" has connected.");
                ServerSideConnection Connection= new ServerSideConnection(s,numPlayers);
                //this assigns the connection socket to correct player
                if(numPlayers==1) {
                    p1 = Connection;///sets up the connection and send connection details of player
                }
                else {
                    p2 = Connection;///sets up the connection and send connection details of player
                }
                //this initializes and starts the Connection Thread
                Thread ConnectionThread= new Thread(Connection);
                ConnectionThread.start();
            }
            System.out.println("Limit to allow maximum number of players to connect is reached. Thus, no longer accepting connections");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this method creates the runnable object for each player i.e., separating into two threads
    private class ServerSideConnection implements Runnable{
        private Socket skt;
        private DataInputStream dIn;
        private DataOutputStream dOut;
        private int pid;
        public ServerSideConnection(Socket s, int id){
            skt=s;
            pid=id;
            try {
                dIn= new DataInputStream(skt.getInputStream());//gets the input stream of the socket
                dOut= new DataOutputStream(skt.getOutputStream());//gets the output stream of the socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run() {
            try {
                //this sents the pid to the client side
                dOut.writeInt(pid);
                //System.out.println(pid);
                dOut.flush();
            }
            catch (IOException ex){
                System.out.println("error in Gameserver Run method");
            }
        }
    }
//    public static void main(String[] args) {
//        // write your code here
//        GameServer gs = new GameServer();
//        gs.AcceptConnections();
//    }
}