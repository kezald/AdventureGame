package game;

import java.io.FileNotFoundException;

import game.data.*;
import game.logic.*;
import game.models.Player;
import game.models.Monster;

/**
 * This class contains basic features of the adventure game. 
 * It keeps record of crucial game data, provides methods to respond to player actions
 * and enables the user to enter commands up until the game ends
 * 
 * @author Nikolai Kolbenev 15897074
 */
public class AdventureGame 
{
	private final static String FILE_WITH_MAP = "input/custommap.txt";
	public final static String BATTLE_MODE = "battleMode";
	public final static String EXPLORE_MODE = "exploreMode";
	
	private static final int MINIMUM_CURRENCY_TO_PASS = 1000;
	
	private static String gameMode;
	private static MapData mapData;
	private static Player player;
	private static Monster monster;
	private static boolean endOfGame;

	/**
	 * This method is the entry point of the program.
	 * It initializes, plays and closes the game.
	 * @param args
	 * @throws FileNotFoundException
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		try
		{
			AdventureGame.initializeGame(FILE_WITH_MAP);
		}
		catch (Exception ex)
		{
			return;
		}
		
		Printing.printSeparator();
		Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
		while (endOfGame == false)
		{
			CommandUtilities.getCommandFromUser(gameMode);
		}
	}
	
	/**
	 * Initializes the game. Game data is read from the specified file
	 * and then the player chooses a class.
	 * 
	 * @param mapFilePath The path to a file that contains map and game data
	 * @throws FileNotFoundException if the specified mapFilePath path does not exist
	 * @author Nikolai Kolbenev 15897074
	 */
	private static void initializeGame(String mapFilePath) throws FileNotFoundException
	{
		endOfGame = false;
		
		try
		{
			mapData = DataTransfer.readMapData(mapFilePath);
			gameMode = EXPLORE_MODE;
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("Unable to load file \'input/gamemap.txt\'");
			throw ex;
		}
		
		Printing.printSeparator();
		System.out.println("Select a class:\n1. "  + Player.CLASS_PROGRAMMER + "\n2. " + Player.CLASS_WARRIOR);
		int userChoice = CommandUtilities.getSelectionFromUser(2, "Your choice? ");
		switch (userChoice)
		{
			case 1:
				player = new Player(Player.CLASS_PROGRAMMER, mapData.entranceRoomNumber);
				mapData.roomsWithPrimaryWeapon[mapData.entranceRoomNumber - 1] = true;
				System.out.println("Now you are a " + Player.CLASS_PROGRAMMER + "!");
				break;
			case 2:
				player = new Player(Player.CLASS_WARRIOR, mapData.entranceRoomNumber);
				System.out.println("Now you are a " + Player.CLASS_WARRIOR + "!");
				break;
			default:
				player = new Player(Player.CLASS_PROGRAMMER, mapData.entranceRoomNumber);
				break;
		}
	}
	
	/**
	 * Sets or rejects a new room position for the player, giving appropriate notifications
	 * about entering a new room or staying in the current location. 
	 * A monster with random attributes may or may not be generated when the player enters a new room.
	 * 
	 * @param newRoomNumber The room number to move the player to
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void setPlayerPosition(int newRoomNumber)
	{
		if (player.getRoomNumber() == newRoomNumber)
		{
			System.out.println("No need. You are already in room " + newRoomNumber + "!");
		}
		else
		{
			boolean roomFound = false;
			for (int i = 0; i < mapData.roomLayout[player.getRoomNumber() - 1].length; i++)
			{
				if (mapData.roomLayout[player.getRoomNumber() - 1][i] == newRoomNumber)
				{
					roomFound = true;

					Printing.printSeparator();
					System.out.println("Opening door " + newRoomNumber);
					player.setRoomNumber(newRoomNumber);
					
					monster = Monster.generateRandomly(50);
					if (monster != null)
					{
						AdventureGame.gameMode = BATTLE_MODE;
					}
					
					Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
					break;
				}
			}
			
			if (roomFound == false)
			{
				System.out.println("You can't find the door with number " + newRoomNumber + ".");
			}
		}
	}
	
	/**
	 * Try to pick up the specified item. Since item is passed as a string,
	 * any noncence may appear as an argument. The method handles any type of input
	 * and accepts only valid items. It gives appropriate notifications
	 * and adds the valid item to player's possessions
	 * 
	 * @param item The name for a particular item
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void pickupItem(String item)
	{
		if (Player.doesItemExist(item) == false)
		{
			System.out.println("\'" + item + "\' is not a valid in-game item!");
		}
		else if (player.isItemAvailable(item) == false)
		{
			System.out.println("This item is not available for your class.");
		}
		else if (item.equalsIgnoreCase(Player.IN_GAME_CURRENCY) && mapData.currencyInRooms[player.getRoomNumber() - 1] > 0)
		{
			player.setCurrencyInPossession(player.getCurrencyInPossession() + mapData.currencyInRooms[player.getRoomNumber() - 1]);
			mapData.currencyInRooms[player.getRoomNumber() - 1] = 0;
			Printing.printSeparator();
			System.out.println("You pick up the " + Player.IN_GAME_CURRENCY + ", increasing your " + Player.IN_GAME_CURRENCY + " to " + Printing.CURRENCY_FORMAT.format(player.getCurrencyInPossession()));
			Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
		}
		else if ((item.equalsIgnoreCase(Player.GLASSES) || item.equalsIgnoreCase(Player.SHIELD)) && mapData.roomsWithSecondaryWeapon[player.getRoomNumber() - 1] == true)
		{
			mapData.roomsWithSecondaryWeapon[player.getRoomNumber() - 1] = false;
			Printing.printSeparator();
			player.setHasSecondaryWeapon(true);
			Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
		}
		else if ((item.equalsIgnoreCase(Player.KEYBOARD) || item.equalsIgnoreCase(Player.SWORD)) && mapData.roomsWithPrimaryWeapon[player.getRoomNumber() - 1] == true)
		{
			mapData.roomsWithPrimaryWeapon[player.getRoomNumber() - 1] = false;
			Printing.printSeparator();
			player.setHasPrimaryWeapon(true);
			Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
		}
		else
		{
			System.out.println("There is no " + item + " in the room.");
		}
	}

	/**
	 * Search for exit in the current room where player is located.
	 * Only one room has exit. If exit is found, a particular sequence
	 * of events happens, which may or may not result in ending of game.
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void searchForExit()
	{
		int playerCurrencyInPossession = player.getCurrencyInPossession();
		int playerRoomNumber = player.getRoomNumber();
		int exitRoomNumber = mapData.exitRoomNumber;

		if (playerRoomNumber != exitRoomNumber)
		{
			System.out.println("You found nothing.");
		}
		else
		{
			Printing.printSeparator();
			String exitEvents = "You see the exit and try to approach it. " +
			"You notice two guardians standing side by side and blocking the passage. " +
			"The guardians demand " + Printing.CURRENCY_FORMAT.format(MINIMUM_CURRENCY_TO_PASS) + " " + Player.IN_GAME_CURRENCY + " from you. "+
			"If you pay, they will let you out.";
			String exitOptions = "1. Pay\n2. Fight\n3. Return back";

			System.out.println(exitEvents);
			System.out.println(exitOptions);

			boolean moveOn = false;
			while (moveOn == false)
			{
				int actionChosen = CommandUtilities.getSelectionFromUser(3, "Your action? ");

				if (actionChosen == 1)
				{
					if (playerCurrencyInPossession >= MINIMUM_CURRENCY_TO_PASS)
					{
						Printing.printSeparator();
						System.out.println("The guardians are impressed with your generosity and refuse to take " + 
								Player.IN_GAME_CURRENCY +".\nYou are accompanied to the exit. Your quest has ended!");
						System.out.println("You have " + Printing.CURRENCY_FORMAT.format(playerCurrencyInPossession) + " " + Player.IN_GAME_CURRENCY + " in total.");
						moveOn = true;
						endOfGame = true;
					}
					else
					{
						System.out.println("You don't have that much " + Player.IN_GAME_CURRENCY + ".");
					}
				}
				else if (actionChosen == 2)
				{
					Printing.printSeparator();
					String fightEvents = "You make your first hit. " +
					"Since the guardians are twice as big as you and are made from stone, your action has no effect. " +
					"You are thrown back to room " + playerRoomNumber + ".";
					System.out.println(fightEvents);
					moveOn = true;
				}
				else if (actionChosen == 3)
				{
					Printing.printSeparator();
					System.out.println("You are back to room " + playerRoomNumber + ".");
					moveOn = true;
				}
			}
		}
	}
	
	/**
	 * Displays information about the room where player is positioned.
	 * The main usage is access from outside of the class
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void displayRoomInfo()
	{
		Printing.printSeparator();
		Printing.printRoomInformation(player.getRoomNumber() - 1, mapData, player);
	}
	
	/**
	 * Displays information about the player.
	 * The main usage is access from outside of the class
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void displaySelfInfo()
	{
		Printing.printPlayerInfo(player);
	}

	/**
	 * Attack the monster in the room.
	 * If the monster stays alive, it responds with its own attack. 
	 * Otherwise, it drops a random amount of in-game currency.
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void attackMonster()
	{
		if (AdventureGame.monster == null)
		{
			return;
		}
		
		Printing.printSeparator();
		player.performAttack(monster);
		if (monster.getHealth() <= 0)
		{
			Printing.printSeparator();
			System.out.println("The monster has been defeated!");
			System.out.println("The monster drops " + Printing.CURRENCY_FORMAT.format(monster.getCurrencyInPossession()) + " in " + Player.IN_GAME_CURRENCY);
			mapData.currencyInRooms[player.getRoomNumber() - 1] += monster.getCurrencyInPossession();
			monster = null;
			gameMode = EXPLORE_MODE;
		}
		else
		{
			monster.performAttack(player);
		}
	}
	
	/**
	 * Run away to a randomly selected door in the room.
	 * A new monster may be encountered then.
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void runAway()
	{
		gameMode = EXPLORE_MODE;
		monster = null;
		
		int numOfRooms = mapData.roomLayout[player.getRoomNumber() - 1].length;
		int randomRoomIndex = (int)(Math.random() * numOfRooms);
		player.setRoomNumber(mapData.roomLayout[player.getRoomNumber() - 1][randomRoomIndex]);
		
		Printing.printSeparator();
		System.out.println("You run away through a randomly selected door!");
		
		monster = Monster.generateRandomly(30);
		if (monster != null)
		{
			AdventureGame.gameMode = BATTLE_MODE;
		}
		
		Printing.printRoomInformation(player.getRoomNumber() -1 , mapData, player);
	}
	
	/**
	 * Lets the player have a rest and restore some health.
	 * There is a certain chance that a monster appears during rest.
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void haveRest()
	{
		Printing.printSeparator();
		System.out.println("You decide to stop and have a rest...");
		double healthChange = player.setHealth(player.getHealth() + Player.PLAYER_MAX_HEALTH * 0.1);
		System.out.print("Your health increases by " + Printing.PERCENT_FORMAT.format(healthChange / Player.PLAYER_MAX_HEALTH) + ". ");
		Printing.printPlayerHealth(player);
		
		monster = Monster.generateRandomly(30);
		if (monster != null)
		{
			AdventureGame.gameMode = BATTLE_MODE;
			Printing.printMonsterAction();
		}
	}
	
	/**
	 * Do nothing and wait. There is a certain chance
	 * that the monster will leave the player in peace.
	 * Otherwise, it will perform the next attack on the player.
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void ignoreMonster()
	{
		if (AdventureGame.monster == null)
		{
			return;
		}
		
		Printing.printSeparator();
		System.out.println("You stand still and do nothing...");
		
		double avoidanceChance = Math.random() * 100;
		if (avoidanceChance <= 30)
		{
			System.out.println("The monster runs away! You are lucky!\n");
			gameMode = EXPLORE_MODE;
			monster = null;
		}
		else
		{
			monster.performAttack(player);
		}
	}
	
	/**
	 * Ends the game
	 * The main usage is from outside of the class
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void endGame()
	{
		endOfGame = true;
	}
	
	/**
	 * Returns the explore mode or the battle mode corresponding string
	 * 
	 * @return The string value, specifying the mode of game
	 * @author Nikolai Kolbenev
	 */
	public static String getGameMode() {
		return gameMode;
	}
}
