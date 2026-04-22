package main;

import javax.swing.*;

//THIS IS THE MAIN CLASS
public class Main {

	public static void main(String[] args) {
		
		
		JFrame windows = new JFrame ();
		//Handles leaving the program
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Handle window size
		windows.setResizable(false);
		//Handles Title
		windows.setTitle("Campus Crisis");
		
		//Handle Gamepanel
		GamePanel gamepanel = new GamePanel ();
		windows.add(gamepanel);
		
		windows.pack();
		
		windows.setLocationRelativeTo(null);
		windows.setVisible(true);
		
		gamepanel.startGameThread();
		
	}	

}
