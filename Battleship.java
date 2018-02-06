import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;



/**
 * Creates a new Battleship application
 */
public class Battleship extends JFrame implements ActionListener {

 private JFrame playerOneWindow;
 private JFrame playerTwoWindow;
 private JPanel playerOnePanel;
 private JPanel playerTwoPanel;
 private JPanel playerOneOpponentPanel; // p1 looking at p2's limited grid
 private JPanel playerTwoOpponentPanel;
 private OceanGrid playerOneGrid;
 private OceanGrid playerTwoGrid;
 private Coord lastSelectedCoordinate;
 private LinkedList<Turn> turnList = new LinkedList<Turn>();
 private JTextArea textAreaPOne;
 private JTextArea textAreaPTwo;
 private JScrollPane scrollBarPOne;
 private JScrollPane scrollBarPTwo;
 private boolean readyToContinueSetup = false;

	/**
	 * constructs a Battleship with GUI elements
	 */
    public Battleship() //throws InterruptedException
    {
	     playerOneWindow = new JFrame();
	     playerTwoWindow = new JFrame();
	     playerOneGrid = new OceanGrid();
	     playerTwoGrid = new OceanGrid();
	     playerOnePanel = new JPanel();
	     playerTwoPanel = new JPanel();
	     playerOneOpponentPanel = new JPanel();
	     playerTwoOpponentPanel = new JPanel();
	     
	     playerOneWindow.setLayout(new BorderLayout());
	     playerTwoWindow.setLayout(new BorderLayout());
	     playerOnePanel.setLayout(new GridLayout(10,10));
	     playerTwoPanel.setLayout(new GridLayout(10,10));
	     playerOneOpponentPanel.setLayout((new GridLayout(10,10)));
	     playerTwoOpponentPanel.setLayout(new GridLayout(10,10));
	     
	     lastSelectedCoordinate = new Coord("99");
	 	 
	     final int ROWS = 100;
	     final int COLUMNS = 1;
	     final int LENGTH = 50;
	     final int WIDTH = 50;
	     textAreaPOne = new JTextArea(ROWS, COLUMNS);
	     textAreaPTwo = new JTextArea(ROWS, COLUMNS);
	     textAreaPOne.setEditable(false);
	     textAreaPTwo.setEditable(false);
	     scrollBarPOne = new JScrollPane(textAreaPOne);
	     scrollBarPTwo = new JScrollPane(textAreaPTwo);
	     
		 updateAllGrids(1);
		 
	     playerOneWindow.add(scrollBarPOne, BorderLayout.CENTER);
	     playerTwoWindow.add(scrollBarPTwo, BorderLayout.CENTER);
	     playerOneWindow.add(playerOnePanel, BorderLayout.SOUTH);
	     playerTwoWindow.add(playerTwoPanel, BorderLayout.SOUTH);
	     playerOneWindow.add(playerOneOpponentPanel, BorderLayout.NORTH);
	     playerTwoWindow.add(playerTwoOpponentPanel, BorderLayout.NORTH);
	     playerOneWindow.setSize(700, 700);
	     playerTwoWindow.setSize(700, 700);
	     playerOneWindow.setTitle("Battleship (Player 1)");
	     playerTwoWindow.setTitle(("Battleship (Player 2)"));
	     playerOneWindow.setVisible(true);
	     playerTwoWindow.setVisible(true);
	     playerOneWindow.setLocation(0, 20);
	     playerTwoWindow.setLocation(800, 20);
	     playerOneWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     playerTwoWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	     playerOneWindow.setResizable(false);
	     playerTwoWindow.setResizable(false);
	     try
	     {
	     	setupGame();
	     }
	     catch (Exception e) {System.out.println("exception caught");}
	     
	     int switcher = 1;
	     
	     
	     while (!gameOver())
	     {
            System.out.println("checking game over");
	     	updateAllGrids(switcher);
            try {turn(switcher);} catch (InterruptedException e){};
	     	if (switcher == 1) switcher = 2;
	     	else switcher = 1;
	     }
	     
	     if (playerOneGrid.shipExists())
	     	JOptionPane.showMessageDialog(playerOneWindow, "You win, Player 1!");
	     else
	     	JOptionPane.showMessageDialog(playerTwoWindow, "You win, Player 2!");
	
	    playerOneWindow.dispose();
	    playerTwoWindow.dispose();
	    System.exit(0);

     
    }
    
    /**
     * gameOver determines if the game has ended based on who still has ships remaining
     * @return a boolean of wheteher the game is over or not
     */
    public boolean gameOver()
    {
     return (!playerOneGrid.shipExists() || !playerTwoGrid.shipExists());
    }
    
    /**
     * setupGame creates the initial setup of the battleship game
     */
    public void setupGame() throws InterruptedException
    {
    	Coord old = lastSelectedCoordinate;
    	boolean done = false;
    	String[] options = {"Vertical", "Horizontal"};
    	int choice = -1;
    	Ship.ShipType[] ships = {Ship.ShipType.AIRCRAFT_CARRIER, Ship.ShipType.BATTLESHIP, Ship.ShipType.DESTROYER, Ship.ShipType.SUBMARINE, Ship.ShipType.PATROL_BOAT};
        System.out.print("1");
    	updateAllGrids(1);
        System.out.println("2");
    	
    	// Player One setup
    	for (int i = 0; i < 5; i ++)
    	{
    		done = false;
    		while (!done)
    		{
                System.out.println("f");
    			choice = JOptionPane.showOptionDialog(playerOneWindow, "Choose a valid location for your " + ships[i].toString(), "Setup", JOptionPane.PLAIN_MESSAGE, 1, null, options, options[0]);
    			while (lastSelectedCoordinate.equals(old)) {Thread.sleep(200); System.out.println("c"); }
    			if (playerOneGrid.addShip(choice, new Ship(ships[i]), lastSelectedCoordinate.getX(), lastSelectedCoordinate.getY()))
    			{
    				updateAllGrids(1);
    				done = true;
    			}
    			old = lastSelectedCoordinate;
    		}
    	}
    	
    	// Player two setup
    	updateAllGrids(2);
    	for (int i = 0; i < 5; i ++)
    	{
    		done = false;
    		while (!done)
    		{
                System.out.println("f");
    			choice = JOptionPane.showOptionDialog(playerTwoWindow, "Choose a valid location for your " + ships[i].toString(), "Setup", JOptionPane.PLAIN_MESSAGE, 1, null, options, options[0]);
    			while (lastSelectedCoordinate.equals(old)) {Thread.sleep(200); System.out.println("c"); }
    			if (playerTwoGrid.addShip(choice, new Ship(ships[i]), lastSelectedCoordinate.getX(), lastSelectedCoordinate.getY()))
    			{
    				updateAllGrids(2);
    				done = true;
    			}
    			old = lastSelectedCoordinate;
    		}
    	}
    }
    
    /**
     * turn determines whose turn it is in order to allow that player an attack
     * @param i the player whose turn it currently is 
     */
    public void turn(int i) throws InterruptedException
    {
    	OceanGrid.Location temp = null;
    	Coord attackSpot = null;
    	
    	if (i == 1)
    	{
    		updateAllGrids(1);
    		while (temp == null || temp.equals(OceanGrid.Location.ATTACKEDSHIP) || temp.equals(OceanGrid.Location.ATTACKEDWATER))
    		{
    			Coord old = lastSelectedCoordinate;
		    	JOptionPane.showMessageDialog(playerOneWindow, "Choose a valid location to attack on the top grid.");
		    	while(old.equals(lastSelectedCoordinate)){ Thread.sleep(200); System.out.print("waiting"); }
		    	attackSpot = lastSelectedCoordinate;
		    	temp = playerTwoGrid.attack(attackSpot.getX(), attackSpot.getY());
    		}

	    	updateAllGrids(1);
	    	switch (temp)
	    	{
	    		case WATER: 
	    		{
	    			turnList.add(new Turn(i, attackSpot, false));
	    			JOptionPane.showMessageDialog(playerOneWindow, "You Missed..."); 
	    			break;
	    		}
	    		case SHIP: 
	    		{
	    			turnList.add(new Turn(i, attackSpot, true));
	    			JOptionPane.showMessageDialog(playerOneWindow, "You hit a ship!");
	    			break;
	    		}
	    	}
	    	
	    	updateAllGrids(3);
	    	JOptionPane.showMessageDialog(playerOneWindow, "Pass computer to player 2.");
    	}
    	//player 2's turn
    	else
    	{
    		updateAllGrids(2);
    		while(temp == null || temp.equals(OceanGrid.Location.ATTACKEDSHIP) || temp.equals(OceanGrid.Location.ATTACKEDWATER))
    		{
    			Coord old = lastSelectedCoordinate;
		    	JOptionPane.showMessageDialog(playerTwoWindow, "Choose a valid location to attack.");
		    	while(old.equals(lastSelectedCoordinate)){ Thread.sleep(200); System.out.print("waiting"); }
		    	attackSpot = lastSelectedCoordinate;
		    	temp = playerOneGrid.attack(attackSpot.getX(), attackSpot.getY());
    		}
	    	updateAllGrids(2);
	    	switch (temp)
	    	{
	    		case WATER: 
	    		{
	    			turnList.add(new Turn(i, attackSpot, false));	    		
	    			JOptionPane.showMessageDialog(playerTwoWindow, "You Missed..."); 
	    			break;
	    		}
	    		case SHIP: 
	    		{
	    			turnList.add(new Turn(i, attackSpot, true));
	    			JOptionPane.showMessageDialog(playerTwoWindow, "You hit a ship!"); 
	    			break;
	    		}
	    	}
	    	updateAllGrids(3);
	    	JOptionPane.showMessageDialog(playerOneWindow, "Pass computer to player 1.");
    	}

    	
    }
    
    /**
     * updateAllGrids updates the grids after a turn 
     * @param i the player's grid that is being updated
     */
    public void updateAllGrids(int i)
    {
    	// Hiding and showing
    	
    	if (i == 1) // show p1 and hide p2
    	{
    		playerTwoPanel.setVisible(false);
    		playerTwoOpponentPanel.setVisible(false);
    		playerOnePanel.setVisible(true);
    		playerOneOpponentPanel.setVisible(true);	
    		scrollBarPOne.setVisible(true);
    		scrollBarPTwo.setVisible(false);
            String text = turnList.toString().substring(1,turnList.toString().length()-1);
            String newText = text.replaceAll("\n, ", "\n");
            System.out.println("TEXT: \n" + newText);
    		textAreaPOne.setText(newText); 

    	}
    	if (i == 2) // show p2 and hide p1
    	{
    		playerOnePanel.setVisible(false);
    		playerOneOpponentPanel.setVisible(false);
    		playerTwoPanel.setVisible(true);
    		playerTwoOpponentPanel.setVisible(true);
    		scrollBarPOne.setVisible(false);
    		scrollBarPTwo.setVisible(true);
    		String text = turnList.toString().substring(1,turnList.toString().length()-1);
            String newText = text.replaceAll("\n, ", "\n");
            System.out.println("TEXT: \n" + newText);
            textAreaPOne.setText(newText); 
    	}
    	if (i == 3) // hide everything
    	{
    		playerOnePanel.setVisible(false);
    		playerOneOpponentPanel.setVisible(false);
    		playerTwoPanel.setVisible(false);
    		playerTwoOpponentPanel.setVisible(false);
    		scrollBarPOne.setVisible(false);
    		scrollBarPTwo.setVisible(false);
    	}
    	
    	
    	// Update personal grids
    	
    	if (i ==1)
    	{
    		JButton[][] pOneButtonGrid = playerOneGrid.getFullButtonGrid();
	    	playerOnePanel.removeAll();
	        for (int a = 0; a < 10; a++)
		     	for (int b = 0; b <10; b++)
		     	{
		     		JButton temp = pOneButtonGrid[b][a];
		     		temp.setToolTipText("" + b + a);
		     		temp.addActionListener(this);
		     		playerOnePanel.add(temp);
		     		playerOnePanel.updateUI();
		     	}
    	}
    	
     	if (i == 2)
     	{
     		playerTwoPanel.removeAll();
	     	JButton[][] pTwoButtonGrid = playerTwoGrid.getFullButtonGrid();
	     	for (int a = 0; a < 10; a++)
		     	for (int b = 0; b <10; b++)
		     	{
		     		JButton temp = pTwoButtonGrid[b][a];
		     		temp.setToolTipText("" + b + a);
		     		temp.addActionListener(this);
		     		playerTwoPanel.add(temp);
		     		playerTwoPanel.updateUI();
		     	}
     	}
     	
	     	
	     	
	     // Update view of opponents' grids
     	 if (i==1)
     	 {
     		 JButton[][] pOneOpponentButtonGrid = playerTwoGrid.getLimitedButtonGrid();
		     playerOneOpponentPanel.removeAll();
	         for (int a = 0; a < 10; a++)
		     	for (int b = 0; b <10; b++)
		     	{
		     		JButton temp = pOneOpponentButtonGrid[b][a];
		     		temp.setToolTipText("" + b + a);
		     		temp.addActionListener(this);
		     		playerOneOpponentPanel.add(temp);
		     		playerOneOpponentPanel.updateUI();
		     	}
     	 }
	     
     	 if (i == 2)
     	 {
     		 JButton[][] pTwoOpponentButtonGrid = playerOneGrid.getLimitedButtonGrid();
	     	 playerTwoOpponentPanel.removeAll();
	     	 for (int a = 0; a < 10; a++)
		     	for (int b = 0; b <10; b++)
		     	{
		     		JButton temp = pTwoOpponentButtonGrid[b][a];
		     		temp.setToolTipText("" + b + a);
		     		temp.addActionListener(this);
		     		playerTwoOpponentPanel.add(temp);
		     		playerTwoOpponentPanel.updateUI();
		     	}
     	 }
 
	     
    }
    
    class Coord
    {
    	private int x; private int y;
    	/**
    	 * Constructs a coordinate with an x and y
    	 * @param s 
    	 */
    	public Coord(String s)
    	{
    		x = Integer.parseInt(s.substring(0,1));
    		y = Integer.parseInt(s.substring(1,2));
    	}
    	
    	/**
    	 * getX returns the x coordinate of Coord 
    	 * @return the x coordinate
    	 */
    	public int getX() { return x; }
    	
    	/**
    	 * getY returns the y coordinate of Coord
    	 * @return the y coordinate 
    	 */
    	public int getY() { return y; }
    	
    	/**
    	 * equals returns whether two Coords are equal or not
    	 * @param o the other Coord
    	 * @return whether they are equal or not
    	 */
    	public boolean equals(Object o) 
    	{	
    		Coord other = (Coord) o;
    		if (this == null && other == null) return true;
    		return (getX()==other.getX() && getY()==other.getY());
    	}
    	
    	/**
    	 * inBounds returns whether the Coord is in the OceanGrid or not
    	 * @return whether the Coord is in bounds or not
    	 */
    	public boolean inBounds()
    	{
    		return (x < 10 && y < 10);
    	}
    	
    	public String toString()
    	{
    		return "[" + x + ", " + y + "]";
    	}
    	
    
    }
    
    class Turn
    {
    	private int player;
    	private Coord attackedCoord;
    	private boolean hit;
    	
    	public Turn(int p, Coord a, boolean h)
    	{
    		player = p;
    		attackedCoord = a;
    		hit = h;
    	}
    	public String toString()
    	{
    		if (hit) return "Player " + player + " attacked " + attackedCoord + " and hit a ship.\n";
    		else return "Player " + player + " attacked " + attackedCoord + " and hit water.\n";
    	}
    }
    
    /**
     * actionPerformed reads in where the user clicked and sets it to lastSelectedCoordinate
     * @param e the action performed
     */
    public void actionPerformed(ActionEvent e)
    {
        System.out.print("new coordinate selected: ");
        readyToContinueSetup = true;
     	JButton source = (JButton) e.getSource();
     	lastSelectedCoordinate = new Coord(source.getToolTipText());
        System.out.println("" + lastSelectedCoordinate.getX() + lastSelectedCoordinate.getY());
    }

    
}