/*
 * Michael Zetune, Brett Mendenhall
 * 3/31/16
 * Gallatin 2nd
 */

public class Ship {
	
	private ShipType type;
	
	/**
	 * Constructs a ship object with a ship type
	 * @param type the type of the ship
	 */
    public Ship(ShipType type) 
    {
    	this.type = type;
    }
    
    /**
     * Creates a new ship enum to distinguish ship type
     */
    public enum ShipType
    {
    	AIRCRAFT_CARRIER, BATTLESHIP, SUBMARINE, DESTROYER, PATROL_BOAT
    }
    
    /**
     * getShipType gets a ship's type
     * @return the ship's type
     */
    public ShipType getShipType()
    {
    	return type;
    }
    
    /**
     * getSize gets the ship type's size
     * @return the size of the integer size of a ship's type
     */
    public int getSize()
    {
    	switch(type)
    	{
    		case AIRCRAFT_CARRIER:
    			return 5;
    		case BATTLESHIP:
    			return 4;
    		case SUBMARINE:
    			return 3;
    		case DESTROYER:
    			return 3;
    		case PATROL_BOAT:
    			return 2;
    			
    	}
    	return -1;
    }
}