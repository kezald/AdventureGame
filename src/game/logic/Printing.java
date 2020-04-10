package game.logic;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import game.AdventureGame;
import game.data.MapData;
import game.models.Player;
import game.models.Monster;

/**
 * This class offers a bunch of methods that print
 * data to stdout in a variety of formats. The formats are accessible from outside
 * of this class. All methods tend to be called mostly from the AventureGame class
 * 
 * @author Nikolai Kolbenev 15897074
 */

public class Printing 
{
	public static final NumberFormat PERCENT_FORMAT = NumberFormat.getPercentInstance();
	public static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("$0");
	public static final DecimalFormat HEALTH_FORMAT = new DecimalFormat("0");
	
	/**
	 * 
	 * This method prints standard information about the room, player status
	 * and the presence of a monster. This method is called each time
	 * the player enters a new room.
	 * 
	 * @param roomIndex The room to display information for
	 * @param mapData The MapData object that contains
	 * necessary information about rooms, connections and items
	 * @param playerData The Player object that will
	 * take part in displaying some of the attributes
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printRoomInformation(int roomIndex, MapData mapData, Player playerData)
	{
		printRoomDescription(roomIndex, mapData);
		printConnectedDoors(roomIndex, mapData);
		printRoomItems(roomIndex, mapData, playerData.getPlayerClass());
		printPlayerHealth(playerData);
		
		if (AdventureGame.getGameMode() == AdventureGame.BATTLE_MODE)
		{
			printMonsterAction();
		}
	}
	
	/**
	 * Prints description of the specified room
	 * 
	 * @param roomIndex The room to display information for
	 * @param mapData The MapData object containing
	 * room descriptions for printing
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printRoomDescription(int roomIndex, MapData mapData)
	{
		System.out.println(mapData.roomDescriptions[roomIndex]);
	}
	
	/**
	 * Prints the number of each room which is adjacent to
	 * the room with the specified number.
	 * 
	 * @param roomIndex The room to display information for
	 * @param mapData The MapData object containing
	 * room layout for printing
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printConnectedDoors(int roomIndex, MapData mapData)
	{
		int[][] roomLayout = mapData.roomLayout;
		String info = (roomLayout[roomIndex].length == 1) ? "There is a door labeled with the number " : "There are doors labeled with numbers ";
		for(int i = 0; i < roomLayout[roomIndex].length; i++)
		{
			info += " "+roomLayout[roomIndex][i];
		}

		System.out.println(info);
	}
	
	/**
	 * Prints each item that is in the room with specified number.
	 *  Item names may appear differently in game, depending on the class
	 *  of player
	 * 
	 * @param roomIndex The room to display information for
	 * @param mapData The MapData object containing
	 * room details for printing
	 * @param playerClass A string value that corresponds to
	 * the player's class
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printRoomItems(int roomIndex, MapData mapData, String playerClass)
	{
		if (mapData.currencyInRooms[roomIndex] > 0)
		{
			System.out.println("There is " + CURRENCY_FORMAT.format(mapData.currencyInRooms[roomIndex]) + " of " + Player.IN_GAME_CURRENCY + " on the floor in front of you.");
		}
		
		if (mapData.roomsWithPrimaryWeapon[roomIndex] == true)
		{
			if (playerClass == Player.CLASS_PROGRAMMER)
			{
				System.out.println("There is a " + Player.KEYBOARD + " on the floor in front of you.");
			}
			else if (playerClass == Player.CLASS_WARRIOR)
			{
				System.out.println("There is a " + Player.SWORD + " on the floor in front of you.");
			}
		}
		
		if (mapData.roomsWithSecondaryWeapon[roomIndex] == true)
		{
			if (playerClass == Player.CLASS_PROGRAMMER)
			{
				System.out.println("There are " + Player.GLASSES + " on the floor in front of you.");
			}
			else if (playerClass == Player.CLASS_WARRIOR)
			{
				System.out.println("There is a " + Player.SHIELD + " on the floor in front of you.");
			}
		}
	}
	
	/**
	 * Prints what monster does when it attacks
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printMonsterAction()
	{
		System.out.println("\nA monster rushes towards you!");
		System.out.println("Prepare for battle!");
	}
	
	/**
	 * Prints the health status of the player
	 * 
	 * @param playerData The Player object containing its health status
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printPlayerHealth(Player playerData)
	{
		System.out.println("Your health status: " + playerData.getHealthStatus());
	}
	
	/**
	 * Prints the health status of the monster
	 * 
	 * @param monsterData The Monster object with its health status
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printMonsterHealth(Monster monsterData)
	{
		System.out.println("Monster's health status: " + monsterData.getHealthStatus());
	}
	
	/**
	 * Prints a separator to separate distinct events or
	 * outputs in the console
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printSeparator()
	{
		System.out.println("-------------------------------------------------------------");
	}
	
	/**
	 * Prints information about the player, in particular
	 * the health status, location and possessions
	 * 
	 * @param playerData The Player object containing
	 * attributes for printing
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void printPlayerInfo(Player playerData)
	{
		printSeparator();
		printPlayerHealth(playerData);
		
		String possessions = "You have:";
		
		possessions += " ";
		possessions += CURRENCY_FORMAT.format(playerData.getCurrencyInPossession()) + " " + Player.IN_GAME_CURRENCY;
		
		if (playerData.getHasPrimaryWeapon())
		{
			possessions += ", ";
			possessions += (playerData.getPlayerClass() == Player.CLASS_PROGRAMMER) ? Player.KEYBOARD : Player.SWORD;

		}
		if (playerData.getHasSecondaryWeapon())
		{
			possessions += ", ";
			possessions += (playerData.getPlayerClass() == Player.CLASS_PROGRAMMER) ? Player.GLASSES : Player.SHIELD;
		}

		possessions += "\nYou are in room " + playerData.getRoomNumber();
		
		System.out.println(possessions + "\n");
	}
}
