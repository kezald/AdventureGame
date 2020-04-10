package game.models;

import game.AdventureGame;
import game.models.Player;
import game.logic.Printing;

/**
 * This class represents a monster in the adventure game.
 * It encompasses several types of data such as
 * currency and attributes that this monster possesses, and methods to access and modify them. 
 * This class contains a lot of members that are similar to Player class. That's why new superclasses,
 * abstract classes or interfaces could be introduced to reduce code complexity and unnecessary typing.
 * 
 * @author Nikolai Kolbenev 15897074
 */
public class Monster 
{
	public static final double MONSTER_MAX_HEALTH = 325.0;
	public static final double MONSTER_MINIMUM_HEALTH = 100.0;
	
	private int currencyInPossession;

	private double fullHealth;
	private double health;
	private double damageAverage;
	
	/**
	 * Initializes a new Monster instance with pre-defined health, damage and
	 * currency values. This constructor is used from inside of this class only. All outside methods
	 * make use of another method in this class that calls this private constructor and passes
	 * random attributes to it.
	 * 
	 * @param health The health of this instance, expresses as health points
	 * @param damageValue The average damage that this instance can do
	 * @param currency The amount of in-game currency that this monster possesses
	 * @author Nikolai Kolbenev 15897074
	 */
	private Monster(double health, double damageValue, int currency)
	{
		this.fullHealth = health;
		this.health = health;
		this.damageAverage = damageValue;
		this.currencyInPossession = currency;
	}
	
	/**
	 * With a certain chance creates a monster with randomly generated health, damage and
	 * currency values. 
	 * 
	 * @param chance A double value that corresponds to the chance in percentage of 
	 * creating a Monster object
	 * @return The newly created Monster object if the randomly generated number allows that.
	 * Otherwise, returns null
	 * @author Nikolai Kolbenev 15897074
	 */
	public static Monster generateRandomly(double chance)
	{
		Monster newMonster = null;
		
		double generation = Math.random() * 100.0;
		if (generation < chance)
		{
			double randomHealth = MONSTER_MAX_HEALTH - Math.random() * (MONSTER_MAX_HEALTH - MONSTER_MINIMUM_HEALTH);
			double randomAverageDamage = (Math.random() * 10 - 5) + Player.PLAYER_MAX_HEALTH / 10.0;
			int randomCurrency = (int)(Math.random() * 100.0);
			
			newMonster = new Monster(randomHealth, randomAverageDamage, randomCurrency);
		}
		
		return newMonster;
	}
	
	/**
	 * Performs an attack on a particular player.
	 * The amount of damage is calculated from this instance attributes.
	 * The result of this attack is printed to stdout and may cause end
	 * of game.
	 * 
	 * @param player The Player object to attack
	 * @author Nikolai Kolbenev 15897074
	 */
	public void performAttack(Player player)
	{
		if (player.getPlayerClass() == Player.CLASS_WARRIOR && player.getHasSecondaryWeapon() == true)
		{
			double chanceOfMissing = Math.random() * 100;
			if (chanceOfMissing <= 40)
			{
				System.out.println("You block the monster's attack!");
				return;
			}
		}
		
		double damageToDeal =  damageAverage + Math.random() * 20 - 10;
		
		if (damageToDeal < 0)
		{
			damageToDeal = 0;
		}
		
		double healthLost = player.setHealth(player.getHealth() - damageToDeal);
		System.out.println("The monster deals " + Printing.HEALTH_FORMAT.format(damageToDeal) + " damage points!");
		System.out.print("You lose " + Printing.PERCENT_FORMAT.format(healthLost / Player.PLAYER_MAX_HEALTH) + " Health. ");
		Printing.printPlayerHealth(player);
		
		if (player.getHealth() <= 0.0)
		{
			Printing.printSeparator();
			System.out.println("You did not survive.\nEnd of game :)");
			AdventureGame.endGame();
		}
	}
	
	/**
     * This method returns the ratio of current health to max health points
	 * and the percentage that current health value constitutes.
	 * This is similar to the Player class getHealthStatus() method,
	 * which should be avoided by declaring a shared superclass or an interface
	 * 
	 * @return A string indicating the current health of this instance in
	 * both health points and percentage
	 * @author Nikolai Kolbenev 15897074
	 */
	public String getHealthStatus()
	{
		double healthPercentage = (health / fullHealth);
		String healthStatus = Printing.HEALTH_FORMAT.format(health) + "/" + Printing.HEALTH_FORMAT.format(fullHealth) + 
		" (" + Printing.PERCENT_FORMAT.format(healthPercentage) + ")";
		return healthStatus;
	}
	
	/**
	 * @return An integer corresponding to the currency that
	 * this instance possesses
	 * @author Nikolai Kolbenev 15897074
	 */
	public int getCurrencyInPossession() {
		return currencyInPossession;
	}

	/**
	 * Sets the currency value for this instace, which will be droped
	 * when this instance is defeated by player
	 * 
	 * @param currencyInPossession An integer that specifies the amount of
	 * currency to set for this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public void setCurrencyInPossession(int currencyInPossession) {
		this.currencyInPossession = currencyInPossession;
	}

	/**
	 * @return A double corresponding to the full health of
	 * this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public double getFullHealth() {
		return fullHealth;
	}
	
	/**
	 * @return A double corresponding to the current health of
	 * this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public double getHealth() {
		return health;
	}

	/**
	 * Sets health for this instance, in health points. 
	 * This is similar to setHealth method in Player class.
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
		
		healthDifference = initialHealth - this.health;
		return healthDifference;
	}

	/**
	 * @return A double corresponding to the average damage of
	 * this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public double getDamageAverage() {
		return damageAverage;
	}

	/**
	 * 
	 * @param damageAverage The double value which is the average damage
	 * value to set for this instance
	 * @author Nikolai Kolbenev 15897074
	 */
	public void setDamageAverage(double damageAverage) {
		this.damageAverage = damageAverage;
	}
}
