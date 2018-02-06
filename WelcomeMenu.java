/*
 * Michael Zetune, Brett Mendenhall
 * 3/31/16
 * Gallatin 2nd
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;

/**
 * Creates a WelcomeMenu for the battleship application
 */
public class WelcomeMenu implements ActionListener {

 private JFrame frame;
 private JPanel panel;
 private JLabel title;
 private JButton playGameButton;
 private JButton helpButton;


	/**
	 * Constructs a WelcomeMenu with GUI elements
	 */
    public WelcomeMenu() {

	 frame = new JFrame();
     panel = new JPanel();
     title = new JLabel(new ImageIcon(((new ImageIcon("battleship-wallpaper.jpg")).getImage()).getScaledInstance(600, 250, java.awt.Image.SCALE_SMOOTH)));
     playGameButton = new JButton("Play Game");
     helpButton = new JButton("Help");
     
     playGameButton.addActionListener(this);
     helpButton.addActionListener(this);
     
     //JLabel wallpaper = new JLabel(new ImageIcon(getClass().getResource("battleship-wallpaper.jpg")));
     //title.add(wallpaper);
	
     
     panel.setLayout(new GridLayout(3,1));
     panel.add(title);
     panel.add(playGameButton);
     panel.add(helpButton);
     

     
     frame.setLayout(new BorderLayout());
     frame.setSize(500, 500);
     frame.setTitle("Battleship");
     frame.add(panel);
     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     frame.setVisible(true);


     
    }
    
    /**
     * actionPerformed reads in an actionEvent by the user to determine what happens next
     * @param e the action performed
     */
    public void actionPerformed(ActionEvent e)
    {
		JButton source = (JButton) e.getSource();
		switch (source.getText())
		{
			case "Play Game":
			{
				frame.dispose();

				//Battleship b = new Battleship();
				(new Thread() {
				public void run() {
				Battleship b = new Battleship();
				}
				}).start();
		
			} break;
			case "Help": 
				JOptionPane.showMessageDialog(frame, "To begin the game, Each player will drag their ships to the grid in locations of their choosing. \nAfter each of the ships have been placed, Player One will start off attacking by clicking on a square in the grid located above the grid containing his ships. \nA message will appear stating whether the attack was a hit or a miss. \nThe upper grid will indicate this by being filled red (when a ship is hit), or white (when the attack is missed). \nThe remaining tiles are filled blue to indicate they have not been targeted. \nOnce a ship has sunk, another message will appear stating which type of ship was sunk.  \nOnce all of the ships from one player have sunk, the game is over and the opponent wins." +
				"\n\n Key for colors used: \n blue - empty water spaces \n gray - ships \n red - attacked ships \n white - attacked water");
						 
			break;
		}
    }
}