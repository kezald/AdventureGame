package game.models;

import java.util.Random;

import game.logic.Printing;

/**
 * This class represents a player in the adventure game.
 * It encompasses several types of data such as
 * the "in-game" classes (Programmer/Warrior), items and attributes that are specific
 * to the player, and methods to access and modify them. 
 * Many of the containing attributes are better to be taken out to a separate class
 * to avoid a large amount of checking, 
 * e.g create a separate class for each "in-game" class with its own available items, item names
 * bonuses, health and damage attributes.
 * 
 * @author Nikolai Kolbenev 15897074
 */
public class Player 
{
	//======In-game items======
	public static final String IN_GAME_CURRENCY = "silver"; //Change this to anything
	public static final String KEYBOARD = "keyboard";
	public static final String GLASSES = "glasses";
	public static final String SWORD = "sword";
	public static final String SHIELD = "shield";
	//=========================
	
	//=====In-game classes=====
	public static final String CLASS_PROGRAMMER = "Computer Programmer";
	public static final String CLASS_WARRIOR = "Warrior";
	//=========================
	
	private static final String[] avalaibleItemsForProgrammer = { KEYBOARD, GLASSES, IN_GAME_CURRENCY };
	private static final String[] avalaibleItemsForWarrior = { SWORD, SHIELD, IN_GAME_CURRENCY };
	
	private static final String[] availableInGameItems = { IN_GAME_CURRENCY, KEYBOARD, GLASSES, SWORD, SHIELD };
	
	public static final double PLAYER_MAX_HEALTH = 750.0;
	
	private String playerClass;
	private boolean hasPrimaryWeapon;
	private boolean hasSecondaryWeapon;
	private int roomNumber;
	private int currencyInPossession;
	private double health;
	private double damageWithHands;
	
	/**
	 * Creats a new instance of Player. The class and room position
	 * are specified in parameters. Other attributes are set implicitly
	 * 
	 * @param playerClass The class of a player
	 * @param roomPosition The room number where this player should appear
	 * @author Nikolai Kolbenev
	 */
	public Player(String playerClass, int roomPosition)
	{
		this.playerClass = playerClass;
		currencyInPossession = 0;
		health = PLAYER_MAX_HEALTH;
		
		if (playerClass == CLASS_PROGRAMMER)
		{
			damageWithHands = 60.0;
		}
		else
		{
			damageWithHands = 40.0;
		}
		
		hasPrimaryWeapon = false;
		hasSecondaryWeapon = false;
		roomNumber = roomPosition;
	}
	
	/**
	 * The player attacks the monster.
	 * The player's class, attributes and other parameters are considered when
	 * making attacks and calculating the amount of damage. The result of attack
	 * is displayed to stdout.
	 * 
	 * @param monster A monster object that will receive the damage from this player.
	 */
	public void performAttack(Monster monster)
	{
		if (playerClass == CLASS_PROGRAMMER && this.hasSecondaryWeapon == false)
		{
			double chanceOfMissing = Math.random() * 100;
			if (chanceOfMissing >= 50)
			{
				System.out.println("Your weak sight prevents you from hitting the monster.");
				return;
			}
		}
		
		double damageToDeal =  damageWithHands + Math.random() * 20.0 - 10.0;
		
		if (hasPrimaryWeapon)
		{
			damageToDeal *= 2.4;
		}
		
		if (damageToDeal < 0.0)
		{
			damageToDeal = 0.0;
		}
		
		double healthLost = monster.setHealth(monster.getHealth() - damageToDeal);
		
		if (this.playerClass == CLASS_PROGRAMMER && this.hasPrimaryWeapon)
		{
			generateKeyboardSymbols();
		}
		
		String damageStatus = "You struck the monster with your ";
		if (this.hasPrimaryWeapon)
		{
			damageStatus += (this.playerClass == Player.CLASS_PROGRAMMER) ? Player.KEYBOARD : Player.SWORD;
		}
		else
		{
			damageStatus += "bare hands";
		}
		damageStatus += ", dealing " + Printing.HEALTH_FORMAT.format(damageToDeal) + " damage points!\n";
		damageStatus += "The monster loses " + Printing.PERCENT_FORMAT.format(healthLost/monster.getFullHealth()) + " Health. ";
		
		System.out.print(damageStatus);
		Printing.printMonsterHealth(monster);
	}
	
	/**
	 * This method returns the ratio of current health to max health points
	 * and the percentage that current health value constitutes.
	 * 
	 * @return A string indicating the player's current health in
	 * both health points and percentage
	 * @author Nikolai Kolbenev 15897074
	 */
	public String getHealthStatus()
	{
		double healthPercentage = (health / PLAYER_MAX_HEALTH);
		String healthStatus = Printing.HEALTH_FORMAT.format(health) + "/" + Printing.HEALTH_FORMAT.format(PLAYER_MAX_HEALTH) + 
				" (" + Printing.PERCENT_FORMAT.format(healthPercentage) + ")";
		return healthStatus;
	}
	
	/**
	 * Checks if there is an item available for the player's current class
	 * 
	 * @param item The name of an item.
	 * @return true if the item is available for the player's class,
	 * Otherwise, false
	 * @author Nikolai Kolbenev 15897074
	 */
	public boolean isItemAvailable(String item)
	{
		String[] availableItems = (this.playerClass == CLASS_PROGRAMMER) ? Player.avalaibleItemsForProgrammer: Player.avalaibleItemsForWarrior;
		return itemBelongsToCollection(item, availableItems);
	}
	
	/**
	 * Checks if the item exists in game.
	 * 
	 * @param item The name of an item.
	 * @return true if the item is a valid in-game item,
	 * Otherwise, false.
	 * @author Nikolai Kolbenev 15897074
	 */
	public static boolean doesItemExist(String item)
	{
		return itemBelongsToCollection(item, availableInGameItems);
	}
	
	/**
	 * @return A string corresponding to the class of this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public String getPlayerClass() {
		return playerClass;
	}
	
	/**
	 * @return An integer corresponding to the amount of currency
	 * that this instance holds
	 * @author Nikolai Kolbenev 15897074
	 */
	public int getCurrencyInPossession() {
		return currencyInPossession;
	}

	/**
	 * sets the amount of currency that this instance holds
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	public void setCurrencyInPossession(int goldInPossession) 
	{
		this.currencyInPossession = goldInPossession;
	}

	/**
	 * @return A double value corresponding to the health of this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public double getHealth() 
	{
		return health;
	}

	/**
	 * Sets health for this instance, in health points. 
	 * The health of the current instance is adjusted so that
	 * it doesn't go below 0 and above the maximum limit, which is
	 * defined by PLAYER_MAX_HEALTH constant
	 * 
	 * @param health A new health value to set for this instance
	 * @return A double value which is the absolute difference between the health
	 * of this instance before a new health value is set and after it is set
	 * @author Nikolai Kolbenev 15897074
	 */
	public double setHealth(double health) 
	{
		double healthDifference;
		double initialHealth = this.health;
		
		this.health = health;
		if (this.health < 0.0)
		{
			this.health = 0.0;
		}
		else if (this.health > PLAYER_MAX_HEALTH)
		{
			this.health = PLAYER_MAX_HEALTH;
		}
		
		healthDifference = Math.abs(initialHealth - this.health);
		return healthDifference;
	}
	
	/**
	 * @return true if this instance has primary weapon.
	 * Otherwise, false
	 * @author Nikolai Kolbenev 15897074
	 */
	public boolean getHasPrimaryWeapon() {
		return hasPrimaryWeapon;
	}

	/**
	 * Specifies whether this instance has a primary weapon.
	 * Primary weapon increases the amount of damage that this instance deals.
	 * Different notifications are output to the console, depending on the current instance class
	 * and the presence of a primary weapon. 
	 * 
	 * @param hasPrimaryWeapon true to give this instance a primary weapon.
	 * false to deprive this instance of it.
	 * @author Nikolai Kolbenev
	 */
	public void setHasPrimaryWeapon(boolean hasPrimaryWeapon) 
	{
		if (hasPrimaryWeapon = true)
		{
			if (this.hasPrimaryWeapon == true)
			{
				System.out.println("You already have this item!");
			}
			else
			{
				if (this.playerClass == Player.CLASS_PROGRAMMER)
				{
					System.out.println("You found a " + Player.KEYBOARD + "! You can use it as a weapon, increasing your damage by 240%");
				}
				else if (this.playerClass == Player.CLASS_WARRIOR)
				{
					System.out.println("You pick up a " + Player.SWORD + ", increasing your damage by 240%");
				}
			}
		}
		
		this.hasPrimaryWeapon = hasPrimaryWeapon;
	}

	/**
	 * Specifies whether this instance has a secondary weapon.
	 * Secondary weapon gives bonuses depending on the class.
	 * Different notifications are output to the console, depending on the current instance class
	 * and the presence of a secondary weapon. 
	 * 
	 * @param hasSecondaryWeapon true to give this instance a secondary weapon.
	 * false to deprive this instance of it.
	 * @author Nikolai Kolbenev
	 */
	public void setHasSecondaryWeapon(boolean hasSecondaryWeapon) 
	{
		if (hasSecondaryWeapon = true)
		{
			if (this.hasSecondaryWeapon == true)
			{
				System.out.println("You already have this item!");
			}
			else
			{
				if (this.playerClass == Player.CLASS_PROGRAMMER)
				{
					System.out.println("You acquire " + Player.GLASSES + "! Now you can see monsters very well. Your accuracy increases to 100%");
				}
				else if (this.playerClass == Player.CLASS_WARRIOR)
				{
					System.out.println("You acquire " + Player.SHIELD + "! Now you have a 40% chance of blocking attacks!");
				}
			}
		}
		
		this.hasSecondaryWeapon = hasSecondaryWeapon;
	}

	/**
	 * @return true if this instance has secondary weapon.
	 * Otherwise, false
	 * @author Nikolai Kolbenev 15897074
	 */
	public boolean getHasSecondaryWeapon() {
		return hasSecondaryWeapon;
	}
	
	/**
	 * @return An integer indicating the room number of this instance.
	 * @author Nikolai Kolbenev 15897074
	 */
	public int getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @param roomNumber the room number to move this instance to
	 * @author Nikolai Kolbenev 15897074
	 */
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}
	
	/**
	 * Prints a number of random symbols that are accessible on keyboards.
	 * Used primarily with 'Computer Programmer' class to reflect
	 * the reality of hitting someone with a keyboard
	 * 
	 * @author Nikolai Kolbenev 15897074
	 */
	private void generateKeyboardSymbols()
	{
		Random rand = new Random();
		
		String randomSymbols = "";
		int keyboardSymbol;
		
		for(int i = 0; i < 20; i++)
		{
			keyboardSymbol = rand.nextInt(95) + 32;
			randomSymbols += (char)keyboardSymbol;
		}
		System.out.println(randomSymbols);
	}
	
	/**
	 * Browses a collection of items and searches for the specified item.
	 * Used to check the existance of an item in the item collection of the game,
	 * a player or a class.
	 * 
	 * @param item A particular item to search for
	 * @param itemCollection An array of String values, each corresponding to the name
	 * of a particular item
	 * @return True if the specified collection of items contains a string with the name
	 * of the specified item. Otherwise, false
	 * @author Nikolai Kolbenev 15897074
	 */
	private static boolean itemBelongsToCollection(String item, String[] itemCollection)
	{
		boolean belongsTo = false;
		
		for (int i = 0; i < itemCollection.length; i++)
		{
			if (itemCollection[i].equals(item))
			{
				belongsTo = true;
				break;
			}
		}
		
		return belongsTo;
	}
}
