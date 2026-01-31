package main;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {
		
		JFrame windows = new JFrame ();
		windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windows.setResizable(false);
		windows.setTitle("Campus Crisis");
		
		GamePanel gamepanel = new GamePanel ();
		windows.add(gamepanel);
		
		windows.pack();
		
		windows.setLocationRelativeTo(null);
		windows.setVisible(true);
		
		gamepanel.startGameThread();
		
	}	

}
