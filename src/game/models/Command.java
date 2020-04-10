package game.models;

/**
 * This class is for convenience of adding and maintaing new commands in the game
 * It contains attributes and methods that are common to all commands
 * 
 * @author Nikolai Kolbenev 15897074
 */
public class Command 
{
	private final int numberOfArgs;
	private final String commandIdentifier;
	private final String commandDescription;
	
	/**
	 * Instanciates a new Command object. Each command must have an
	 * identifier, description and the number of arguments it requires
	 * 
	 * @param numberOfArgs The number of arguments that this command requires
	 * @param commandIdentifier The name of a command
	 * @param commandDescription The description that will appear in the list of 
	 * available commands
	 * @author Nikolai Kolbenev 15897074
	 */
	public Command(int numberOfArgs, String commandIdentifier, String commandDescription)
	{
		this.numberOfArgs = numberOfArgs;
		this.commandIdentifier = commandIdentifier;
		this.commandDescription = commandDescription;
	}
	
	/**
	 * 
	 * @return The name of this command
	 * @author Nikolai Kolbenev 15897074
	 */
	public final String getCommandIdentifier()
	{
		return commandIdentifier;
	}
	
	/**
	 * @return The description of this command
	 * @author Nikolai Kolbenev 15897074
	 */
	public final String getCommandDescription()
	{
		return commandDescription;
	}
	
	
	/**
	 * This method is used to quickly identify a possibly valid command that
	 * the user types in. It is also used to reject commands that do not exist in the game or 
	 * are not available in the current context
	 * 
	 * @param commandIdentifier The name of a command that this instance will be
	 * checked against
	 * @return True if the name of this command matches with the name of the specified command
	 * @author Nikolai Kolbenev 15897074
	 */
	public final boolean matches(String commandIdentifier)
	{
		boolean matches = false;
		
		if (commandIdentifier.equals(this.commandIdentifier))
		{
			matches = true;
		}
		
		return matches;
	}
	
	/**
	 * This method compares the number of arguments required for this instance with the number of
	 * 		arguments in the specified command and optionally provides feedback about the mismatch.
	 * 
	 * @param commandTokens An array of string values, or command tokens, such as the command name and arguments.
	 * @param giveFeedback True to print feedback to stdout. False to remain silent
	 * @return true if commandTokens parameter has the length that is one less than the number
	 * of arguments required for this instance; otherwise, false.
	 * @author nkolbenev 15897074 
	 */
	public final boolean numberOfArgsEquals(String[] commandTokens, boolean giveFeedback)
	{
		boolean sameNumOfArgs = true;

		if (commandTokens.length - 1 != this.numberOfArgs)
		{
			sameNumOfArgs = false;

			if (giveFeedback)
			{
				if (this.numberOfArgs == 0)
				{
					System.out.println("No arguments for command \'" + commandIdentifier + "\' are required.");
				}
				else if (this.numberOfArgs == 1)
				{
					System.out.println("Invalid number of arguments. Please enter command \'" + commandIdentifier + "\' followed by a space and one argument.");
				}
				else
				{
					System.out.println("Invalid number of arguments for command \'" + commandIdentifier + "\'");
				}
			}
		}

		return sameNumOfArgs;
	}
}
