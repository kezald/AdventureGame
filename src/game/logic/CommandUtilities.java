package game.logic;

import game.AdventureGame;
import game.models.Command;
import game.logic.Printing;

import java.util.Scanner;

/**
 * This class provides the basic infrastructre for services related to command processing.
 * Support is added to request, validate and maintain all in-game commands.
 * 
 * @author Nikolai Kolbenev 15897074
 */
public class CommandUtilities 
{
	//=====In-game commands=====
	private final static Command commandHelp = new Command(0, "help",         "HELP            displays available commands in the current context");
	private final static Command commandRoomInfo = new Command(0, "roominfo", "ROOMINFO        displays information about the current room");
	private final static Command commandSelfInfo = new Command(0, "selfinfo", "SELFINFO        displays your current health, possessions and room number");
	private final static Command commandOpen = new Command(1, "open",         "OPEN n          open the door labeled n and enter the room");
	private final static Command commandPickup = new Command(1, "pickup",     "PICKUP item     pick up an item in the room");
	private final static Command commandSearch = new Command(0, "search",     "SEARCH          search the room to find the exit");
	private final static Command commandRest = new Command(0, "rest",         "REST            have a rest and restore your health");
	private final static Command commandAttack = new Command(0, "attack",     "ATTACK          hit the monster in the room");
	private final static Command commandRun = new Command(0, "run",           "RUN             run away from the monster going through a randomly selected door");
	private final static Command commandIgnore = new Command(0, "ignore",     "IGNORE          stand still and wait to see if the monster leaves you in peace");
	//=========================
	
	private final static Command[] availableExploreCommands = new Command[]{commandOpen, commandPickup, commandSearch, commandRoomInfo, commandSelfInfo, commandRest, commandHelp};
	private final static Command[] availableBattleCommands = new Command[]{commandAttack, commandRun, commandIgnore, commandHelp};
	
	private final static Command[] availableInGameCommands = new Command[]{commandOpen, commandPickup, commandSearch, commandAttack, commandRun, commandIgnore, commandRoomInfo, commandSelfInfo, commandRest, commandHelp};
	
	/**
	 * This method asks the user to type in a command and validates and processes the input.
	 * Because the game cannot progress any further if the command is invalid, the user is queried until
	 * a valid command is provided. The game mode parameter ensures that 
	 * the player cannot use commands that are unavailable in the current context.
	 * 
	 * @param gameMode The mode of game that the player is currently in.
	 * @author Nikolai Kolbenev 15897074
	 */
	public static void getCommandFromUser(String gameMode)
	{
		Scanner commandScanner = new Scanner(System.in);
		
		System.out.print("Command? ");
		String[] userCommandTokens = commandScanner.nextLine().trim().toLowerCase().split(" ");
		
		Command matchingCommand = null;
		if (browseCommandCollection(userCommandTokens, availableInGameCommands) == null)
		{
			System.out.println("\'" + userCommandTokens[0] + "\' is not a valid in-game command!");
			return;
		}
		else if (gameMode.equals(AdventureGame.EXPLORE_MODE))
		{
			matchingCommand = browseCommandCollection(userCommandTokens, availableExploreCommands);
		}
		else if (gameMode.equals(AdventureGame.BATTLE_MODE))
		{
			matchingCommand = browseCommandCollection(userCommandTokens, availableBattleCommands);
		}
		
		if (matchingCommand == null)
		{
			System.out.println("Command \'" + userCommandTokens[0] + "\' is not available at the moment." );
		}
		else if (matchingCommand.numberOfArgsEquals(userCommandTokens, true) == true)
		{
			processCommand(userCommandTokens, gameMode);
		}
	}
	
	/**
	 * There are cases in the game where the user must choose an item,
	 * action or an option from the selection menu. The number of options
	 * may be different and this method allows to get a valid user choice,
	 * as an integer, from any number of options available.
	 * 
	 * @param numberOfOptions The number of options available for the
	 * user to choose from
	 * @param queryPhrase A string value that will be used to 
	 * query the user until a valid option is chosen
	 * @return An integer corresponding to a valid option, chosen
	 * by the user
	 * @author Nikolai Kolbenev 15897074
	 */
	public static int getSelectionFromUser(int numberOfOptions, String queryPhrase)
	{
		if (numberOfOptions < 1)
		{
			return 0;
		}
		
		int selectedOption = 0;
		
		Scanner scan = new Scanner(System.in);
		boolean optionSelected = false;
		while (optionSelected == false)
		{
			System.out.print(queryPhrase);
			String userChoice = scan.nextLine().trim();
			
			try
			{
				selectedOption = Integer.parseInt(userChoice);
				if (selectedOption > 0 && selectedOption <= numberOfOptions)
				{
					optionSelected = true;
				}
			}
			catch (Exception ex)
			{
				//Do nothing...
			}
			
			if (optionSelected == false)
			{
				System.out.println("Invalid input. Please enter a number between 1 and " + numberOfOptions + " inclusive.");
			}
		}
		
		return selectedOption;
	}
	
	/**
	 * Checks whether the name of a command (the first element of commandTokens array) 
	 * matches the name of any command in the specified command collection and returns that
	 * particular Command object with the same name. A command collection is usually a collection
	 * of all in-game commands, all battle-mode commands or all explore-mode commands.
	 * The method checks if command is available in the current context
	 * 
	 * @param commandTokens An array of string values, or command tokens, such as the command name and arguments.
	 * @param commandCollection An array of Command objects that will be searched for 
	 * a particular command name
	 * @return The Command object with the same name as the 
	 * first element of commandTokens parameter
	 * @author Nikolai Kolbenev 15897074
	 */
	private static Command browseCommandCollection(String[] commandTokens, Command[] commandCollection)
	{
		Command matchingCommand = null;
		
		for (int i = 0; i < commandCollection.length; i++)
		{
			if (commandCollection[i].matches(commandTokens[0]))
			{
				matchingCommand = commandCollection[i];
				break;
			}
		}
		
		return matchingCommand;
	}
	
	/**
	 * Allows to call AdventureGame class methods that correspond
	 * to a particular command. The command is identified and a matching 
	 * method is called from AdventureGame class. 
	 * Not all commands require a method from the AdventureGame class!
	 * 
	 * @param commandTokens An array of string values, or command tokens, such as the command name and arguments.
	 * @param gameMode The mode of game that the player is currently in.
	 * @author Nikolai Kolbenev 15897074
	 */
	private static void processCommand(String[] commandTokens, String gameMode)
	{
		if (commandTokens[0].equals(commandOpen.getCommandIdentifier()))
		{
			try
			{
				int roomToOpen = Integer.parseInt(commandTokens[1]);
				AdventureGame.setPlayerPosition(roomToOpen);
			}
			catch (Exception ex)
			{
				System.out.println("Invalid argument \'" + commandTokens[1] + "\' for command \'" + commandTokens[0] + "\'.");
			}
		}
		else if (commandTokens[0].equals(commandPickup.getCommandIdentifier()))
		{
			AdventureGame.pickupItem(commandTokens[1]);
		}
		else if (commandTokens[0].equals(commandSearch.getCommandIdentifier()))
		{
			AdventureGame.searchForExit();
		}
		else if (commandTokens[0].equals(commandHelp.getCommandIdentifier()))
		{
			if (gameMode.equals(AdventureGame.EXPLORE_MODE))
			{
				displayAvailableCommands(availableExploreCommands);
			}
			else if (gameMode.equals(AdventureGame.BATTLE_MODE))
			{
				displayAvailableCommands(availableBattleCommands);
			}
		}
		else if (commandTokens[0].equals(commandRoomInfo.getCommandIdentifier()))
		{
			AdventureGame.displayRoomInfo();
		}
		else if (commandTokens[0].equals(commandSelfInfo.getCommandIdentifier()))
		{
			AdventureGame.displaySelfInfo();
		}
		else if (commandTokens[0].equals(commandRest.getCommandIdentifier()))
		{
			AdventureGame.haveRest();
		}
		else if (commandTokens[0].equals(commandAttack.getCommandIdentifier()))
		{
			AdventureGame.attackMonster();
		}
		else if (commandTokens[0].equals(commandRun.getCommandIdentifier()))
		{
			AdventureGame.runAway();
		}
		else if (commandTokens[0].equals(commandIgnore.getCommandIdentifier()))
		{
			AdventureGame.ignoreMonster();
		}
	}

	/**
	 * Displayes all commands in the specified command collection.
	 * A command collection is usually a collection
	 * of all in-game commands, all battle-mode commands or all explore-mode commands.
	 * 
	 * @param commandCollection A collection of Command objects
	 * @author Nikolai Kolbenev 15897074
	 */
	private static void displayAvailableCommands(Command[] commandCollection)
	{
		Printing.printSeparator();
		System.out.println("At the moment, the following commands are available:");
		for (int i = 0; i < commandCollection.length; i++)
		{
			System.out.println(commandCollection[i].getCommandDescription());
		}
		System.out.println();
	}
}
