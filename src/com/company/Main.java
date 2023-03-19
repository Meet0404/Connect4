package com.company;

public class Main {
    public static void main(String[] args) {
	// write your code here
        GameServer gs = new GameServer();
        gs.AcceptConnections();
        GuiConnect4 c = new GuiConnect4();
    }
}
