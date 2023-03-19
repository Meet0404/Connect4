package com.company;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;

public class GuiConnect4 {
    //creates the frame for the game
    JFrame frame= new JFrame();
    //this is used to make the title of the game
    JPanel Title_Panel= new JPanel();
    //this is used to make the pannel for the buttons
    JPanel button_Panel= new JPanel();
    //used to create labels
    JLabel Textfield= new JLabel();
    //NO. OF BUTTONS
    JButton[][] buttons=new JButton[6][7];
    //checker for the turns
    Boolean P1;
    int column,firstturn=0;
    //t acts as an counter to keep track of total number of moves
    int t=1;

    private ClientSideConnection CSC;
    private int pid;


    //this makes the GUI
    public GuiConnect4(){
        ConnectServer();
        SetupGame();
        Turn();
    }

    //this method helps to establish the connection with server
    public void ConnectServer(){

        CSC= new ClientSideConnection();

    }

    //client connection inner class
    private static class ClientSideConnection{
        private Socket socket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;
        public ClientSideConnection(){
            System.out.println("----Client Connected----");
            try{
                socket=new Socket("localhost",4444);//initiates connection request to server
                dataIn= new DataInputStream(socket.getInputStream());//gets the input stream of  the socket
                dataOut= new DataOutputStream(socket.getOutputStream());//gets the output stream of the socket
            } catch (IOException e) {
                System.out.println("Buddy Error in ClientConnection constructor  in GuiConnect4");
            }
        }
    }



    public void SetupGame(){
        //setting the area if the frame
        frame.setSize(1000,1000);
        //exits the program when you close the popup window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(50,50,50));
        frame.setLayout(new BorderLayout());
        //makes the frame visible i.e, it pops up when you run the gui method
        frame.setVisible(true);

        //sets the background of the text field to black
        Textfield.setBackground(new Color(25,25,25));
        //sets the font color to cyan
        Textfield.setForeground(Color.cyan);
        Textfield.setFont(new Font("Ink Free",Font.BOLD,50));
        Textfield.setHorizontalAlignment(JLabel.CENTER);
        Textfield.setText("CONNECT FOUR");
        Textfield.setOpaque(true);
        //frame.add(panel);//adds panel on the frame

        //sets the panel for the button
        Title_Panel.setLayout(new BorderLayout());
        Title_Panel.setBounds(0,0,1000,100);

        //creates the background for the buttons
        button_Panel.setLayout(new GridLayout(6,7));
        button_Panel.setBackground(new Color(150,150,150));

        //setting up buttons
        for(int i=0;i<6;i++){
            for(int j=0;j<7;j++) {
                buttons[i][j]= new JButton();
                button_Panel.add(buttons[i][j]);
                buttons[i][j].setFont(new Font("MV BOLI", Font.BOLD, 25));
                buttons[i][j].setFocusable(false);
            }
        }

        Title_Panel.add(Textfield);
        frame.add(Title_Panel,BorderLayout.NORTH);
        frame.add(button_Panel);

    }
    public void Turn(){
        while(t<43) {
            //if the random number is 0 then player 1's turn else player 2's turn
            if (firstturn == 0) {
                P1 = true;
                Textfield.setText("Player 1's turn aka place 'R' ");
                //asks the user in which column they want to drop their coin
                column = Integer.parseInt(JOptionPane.showInputDialog(null, "Player 1's turn \nEnter the column in which you want to add R = "));
                for (int i=5; i>=0; i--) {
                    for (int j=0; j<7; j++) {
                        if (j == (column-1) && P1==true) {
                            if (buttons[i][j].getText() == "") {
                                buttons[i][j].setForeground(new Color(255, 0, 0));
                                buttons[i][j].setText("R");
                                P1 = false;
                                firstturn=1;
                                if(CheckWin()){
                                    Textfield.setText("PLAYER 1 WINS ");
                                    return;
                                }
                                else {
                                    Textfield.setText("Turn for Player 2");
                                }
                            }
                        }
                    }
                }
            } else {
                P1 = false;
                firstturn=0;
                Textfield.setText("Player 2's turn aka place 'B' ");
                //asks the user in which column they want to drop their coin
                column = Integer.parseInt(JOptionPane.showInputDialog(null, "Player 2's turn \nEnter the column in which you want to add B = "));
                for (int i = 5; i>=0 ; i--) {
                    for (int j = 0; j < 7; j++) {
                        if (j == (column-1) && P1==false) {
                            if (buttons[i][j].getText() == "") {
                                buttons[i][j].setForeground(new Color(49, 139, 192));
                                buttons[i][j].setText("B");
                                P1 = true;
                                if(CheckWin()){
                                    Textfield.setText("PLAYER 2 WINS ");
                                    return;
                                }
                                else{
                                    Textfield.setText("Turn for Player 1");
                                }
                            }
                        }
                    }
                }
            }
        }
        if(t==43){
            Textfield.setText("Its A Tie");
        }
        t++;
    }



    //checking if any player wins
    public boolean CheckWin(){

        for(int r=0;r<6;r++) {
            for(int c=0;c<7;c++) {
                //checks if all the four elements horizontally are same or not
                if(c<=3 && buttons[r][c].getText().equals("R") && buttons[r][c+1].getText().equals("R") && buttons[r][c+2].getText().equals("R") && buttons[r][c+3].getText().equals("R"))
                    return true;

                //checks if all the four elements vertically are same or not
                if(r<=2 && buttons[r][c].getText().equals("R") && buttons[r+1][c].getText().equals("R") && buttons[r+2][c].getText().equals("R") && buttons[r+3][c].getText().equals("R")) {
                    return true;
                }

                //this method checks if the same element is present on the right diagonal
                if(r<=2 && c<=3){
                    if(buttons[r][c].getText().equals("R") && buttons[r+1][c+1].getText().equals("R") && buttons[r+2][c+2].getText().equals("R") && buttons[r+3][c+3].getText().equals("R"))
                        return true;
                }

                //check if all the elements present on the left diagonal are same
                if(r<=2 && c>=3) {
                    if(buttons[r][c].getText().equals("R") && buttons[r+1][c-1].getText().equals("R") && buttons[r+2][c-2].getText().equals("R") && buttons[r+3][c-3].getText().equals("R"))
                        return true;
                }

                //checks if all the four elements horizontally are same or not
                if(c<=3 && buttons[r][c].getText().equals("B") && buttons[r][c+1].getText().equals("B") && buttons[r][c+2].getText().equals("B") && buttons[r][c+3].getText().equals("B"))
                    return true;

                //checks if all the four elements vertically are same or not
                if(r<=2 && buttons[r][c].getText().equals("B") && buttons[r+1][c].getText().equals("B") && buttons[r+2][c].getText().equals("B") && buttons[r+3][c].getText().equals("B")) {
                    return true;
                }

                //this method checks if the same element is present on the right diagonal
                if(r<=2 && c<=3){
                    if(buttons[r][c].getText().equals("B") && buttons[r+1][c+1].getText().equals("B") && buttons[r+2][c+2].getText().equals("B") && buttons[r+3][c+3].getText().equals("B"))
                        return true;
                }

                //check if all the elements present on the left diagonal are same
                if(r<=2 && c>=3) {
                    if(buttons[r][c].getText().equals("B") && buttons[r+1][c-1].getText().equals("B") && buttons[r+2][c-2].getText().equals("B") && buttons[r+3][c-3].getText().equals("B"))
                        return true;
                }
            }
        }
        // if neither of the horizontal, vertical, left diagonal or right diagonal match are found then the false is returned
        return false;
    }


}

