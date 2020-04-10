package game.data;

/**
 * 
 * The MapData class that is extensively used
 * throughout the game. It has vital information
 * about each room in the game.
 * 
 * @author Nikolai Kolbenev 15897074
 *
 */
public class MapData 
{
	public int numberOfRooms;
	public int[][] roomLayout;
	public String[] roomDescriptions;
	public boolean[] roomsWithPrimaryWeapon;
	public boolean[] roomsWithSecondaryWeapon;
	public int[] currencyInRooms;
	
	public int entranceRoomNumber;
	public int exitRoomNumber;
}
