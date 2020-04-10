package game.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class offers services for reading game data from file,
 * generating the MapData object and manipulating file contents. 
 * 
 * @author Nikolai Kolbenev 15897074
 */
public final class DataTransfer 
{
	//======Recognised tokens======
	private static final String ITEMS_TAG = "@items";
	private static final String CONNECT_TAG = "@connect";
	private static final String DESCRIPTION_TAG = "@description";
	
	private static final String TOKEN_DELIMITER = ",";
	private static final String CURRENCY = "currency";
	private static final String PRIMARY_WEAPON = "primary weapon";
	private static final String SECONDARY_WEAPON = "secondary weapon";
	//=============================
	
	/**
	 * Attempts to read a file in the specified path and instantiate a
	 * MapData object from it.
	 * 
	 * @return The MapData object with all fields, containing game data, initialized
	 * @param filePath The path to a file with game data
	 * @throws FileNotFoundException if the specified filePath path does not exist
	 * @author Nikolai Kolbenev 15897074
	 */
	public static MapData readMapData(String filePath) throws FileNotFoundException
	{
		MapData gameData = new MapData();
		Scanner fileScan = new Scanner(new File(filePath));

		gameData.numberOfRooms = Integer.parseInt(fileScan.nextLine());
		System.out.println("Initialising game map -- number of rooms: " + gameData.numberOfRooms);
		
		gameData.roomLayout = new int[gameData.numberOfRooms][];
		gameData.roomDescriptions = new String[gameData.numberOfRooms];
		gameData.roomsWithPrimaryWeapon = new boolean[gameData.numberOfRooms];
		gameData.roomsWithSecondaryWeapon = new boolean[gameData.numberOfRooms];
		gameData.currencyInRooms = new int[gameData.numberOfRooms];

		int roomIndex = -1; //The number of the existing room from file minus one
		
		while (fileScan.hasNext())
		{
			String data = fileScan.nextLine().trim();
			if(fileScan.hasNext() == false)
			{
				break;
			}

			if (data.equalsIgnoreCase(ITEMS_TAG))
			{
				String scannedItems = fileScan.nextLine();
				gameData.roomsWithPrimaryWeapon[roomIndex] = isItemPresent(scannedItems, PRIMARY_WEAPON);
				gameData.roomsWithSecondaryWeapon[roomIndex] = isItemPresent(scannedItems, SECONDARY_WEAPON);
				gameData.currencyInRooms[roomIndex] = (isItemPresent(scannedItems, CURRENCY) == true) ? (int)(Math.random() * 300 + 150) : 0;
			}
			else if (data.equalsIgnoreCase(CONNECT_TAG))
			{
				addRoomConnections(gameData.roomLayout, roomIndex, fileScan.nextLine());
			}
			else if (data.equalsIgnoreCase(DESCRIPTION_TAG))
			{
				gameData.roomDescriptions[roomIndex] = fileScan.nextLine();
			}
			else
			{
				if (roomIndex == -1)
				{
					gameData.entranceRoomNumber = Integer.parseInt(data);
				}
				roomIndex = Integer.parseInt(data) - 1;
			}
		}
		gameData.exitRoomNumber = roomIndex + 1;
		
		fileScan.close();
		
		return gameData;
	}

	/**
	 * Add connections to the room specified by its index in the
	 * specified array of rooms. Connections are paresed from adjacentRooms
	 * string value
	 * 
	 * @param roomLayout An array of intgeres, where each integer is the room index,
	 * that will be changed to add new connections
	 * @param roomIndex The room index to add connections to
	 * @param adjacentRooms A string containing room numbers, separated by a delimiter (a comma)
	 * @author Nikolai Kolbenev 15897074
	 */
	private static void addRoomConnections(int[][] roomLayout, int roomIndex, String adjacentRooms)
	{
		String[] roomConnections = adjacentRooms.split(TOKEN_DELIMITER);
		roomLayout[roomIndex] = new int[roomConnections.length];
		for(int i = 0; i < roomConnections.length; i++)
		{
			roomLayout[roomIndex][i] = Integer.parseInt(roomConnections[i]);
		}
	}

	/**
	 * Checks whether the item is present in the collection of items
	 * 
	 * @param itemsCollection A string value containing the names of items,
	 * separated by a delimiter (a comma)
	 * @param certainItem The item to search for
	 * @return True if the item is present in the specified collection
	 * @author Nikolai Kolbenev 15897074 
	 */
	private static boolean isItemPresent(String itemsCollection, String certainItem)
	{
		boolean isItemPresent = false;
		certainItem = certainItem.toLowerCase();
		String[] items = itemsCollection.split(TOKEN_DELIMITER);

		for (int i = 0; i < items.length; i++)
		{
			if(items[i].equalsIgnoreCase(certainItem))
			{
				isItemPresent = true;
				break;
			}
		}

		return isItemPresent;
	}
}
