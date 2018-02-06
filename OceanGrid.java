import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OceanGrid
{
	private Location[][] ocean;
	
	/**
	 * Creates a new OceanGrid enum to distinguish a space on the grid
	 */
	public enum Location
 	{
  		WATER, SHIP, ATTACKEDWATER, ATTACKEDSHIP
 	}
 

 	/**
 	 * Constructs an OceanGrid object that is a 10 x 10 2D Array
 	 */
 	public OceanGrid()
 	{
  		ocean = new Location[10][10];
  		for (int x = 0; x < ocean.length; x++)
   		for (int y = 0; y < ocean[0].length; y++)
    	ocean[x][y] = Location.WATER;
 	}
 
 	/**
 	 * addShip adds a ship to the OceanGrid  
 	 * @param choice determines whether the user will want the ship placed vertically or horizontally
 	 * @param ship the shipType of the ship being added 
 	 * @param x the number of the column where the ship will be added
 	 * @param y the number of the row where the ship will be added
 	 */
 	public boolean addShip(int choice, Ship ship, int x, int y)
 	{
 		
 		if (choice == 0)
 		{
 			if (y > 10-ship.getSize()) return false;
 			
 			for (int i = 0; i<ship.getSize(); i++ )
	 		{
	 			if (ocean[x][y+i] == Location.SHIP)
	 				return false;
	 		}
 		
	 		for (int i = 0; i<ship.getSize(); i++ )
	 		{
	 			ocean[x][y+i] = Location.SHIP;
	 		}
	 		return true;
 		}
 		else if (choice == 1)
 		{
 			if ( x > 10 - ship.getSize()) return false;
 			
 			for (int i = 0; i < ship.getSize(); i++)
 			{
 				if (ocean[x+i][y] == Location.SHIP)
 					return false;
 			}
 			
 			for (int i = 0; i < ship.getSize(); i++)
 			{
 					ocean[x+i][y] = Location.SHIP;
 			}
 			return true;
 		}
 		
 		return false;
 	}
 	/**
 	 * attack determines what Location enum was selected by the user
 	 * @param x the x-coordinate of the selected location
 	 * @param y the y-coordinate of the selected location
 	 */
	 public Location attack(int x, int y)
	 {
	  Location temp = ocean[x][y];
	  
	  switch (temp)
	  {
	  	case ATTACKEDSHIP:
	   		return Location.ATTACKEDSHIP;
	   	case ATTACKEDWATER:
	   		return Location.ATTACKEDWATER;
	   	
	   case WATER:
	    	ocean[x][y] = Location.ATTACKEDWATER; break;
	   case SHIP:
	    	ocean[x][y] = Location.ATTACKEDSHIP; break;
	   
	  }
	  
	  return temp;
	 }
	 
	 /**
	  * shipExists determines if a ship still remains on an OceanGrid
	  * @return whether or not a ship exists
	  */
	 public boolean shipExists()
	 {
	  for (int x = 0; x < ocean.length; x++)
	  {
	   for (int y = 0; y < ocean[0].length; y++)
	   {
	    if(ocean[x][y] == Location.SHIP)
	    {
	    	System.out.println ("1");
	    	return true;
	    }
	     
	   }   
	  }
	  
	  return false;
	 }
	 
	 /**
	  * getGrid returns the OceanGrid
	  * @return the current OceanGrid
	  */
	 public Location[][] getGrid()
	 {
	  return ocean;
	 }
	 
	 /**
	  * getFullButtonGrid gets the bottom OceanGrid set up
	  * @return a 2D Array of JButtons
	  */
	 public JButton[][] getFullButtonGrid()
	 {
	 	JButton[][] ret = new JButton[10][10];
	 	for (int a = 0; a < 10; a ++)
	 		for (int b = 0; b < 10; b++)
	 		{
	 			JButton temp = new JButton();
	 			temp.setPreferredSize(new Dimension(25, 25));
	 			switch (ocean[a][b])
	 			{
	 				case SHIP:
	 					temp.setBackground(Color.black);
	 					temp.setOpaque(true);
	 					break;
	 				case WATER:
	 					temp.setBackground(Color.blue);
	 					temp.setOpaque(true);
	 					break;
	 				case ATTACKEDSHIP:
	 					temp.setBackground(Color.red);
	 					temp.setOpaque(true);
	 					break;
	 				case ATTACKEDWATER:
	 					temp.setBackground(Color.gray);
	 					temp.setOpaque(true);
	 					break;
	 			}
	 			ret[a][b] = temp;
	 		}
	 		return ret;
	 }
	 
	 /**
	  * getLimitedButtonGrid gets the upper OceanGrid set up
	  * @return a 2D Array of JButtons
	  */
	 public JButton[][] getLimitedButtonGrid()
	 {
	 	JButton[][] ret = new JButton[10][10];
	 	for (int a = 0; a < 10; a ++)
	 		for (int b = 0; b < 10; b++)
	 		{
	 			JButton temp = new JButton();
	 			temp.setPreferredSize(new Dimension(25, 25));
	 			switch (ocean[a][b])
	 			{
	 				case SHIP:
	 					temp.setBackground(Color.blue);
	 					temp.setOpaque(true);
	 					break;
	 				case WATER:
	 					temp.setBackground(Color.blue);
	 					temp.setOpaque(true);
	 					break;
	 				case ATTACKEDSHIP:
	 					temp.setBackground(Color.red);
	 					temp.setOpaque(true);
	 					break;
	 				case ATTACKEDWATER:
	 					temp.setBackground(Color.lightGray);
	 					temp.setOpaque(true);
	 					break;
	 			}
	 			ret[a][b] = temp;
	 		}
	 		
	 		return ret;
	 }
 
}