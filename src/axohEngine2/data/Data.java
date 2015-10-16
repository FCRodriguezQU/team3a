/**********************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 15, 2015
 * Test
 * Title: Data 
 * Description: Hold data from a game in variables for serializing later
 * 
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 *********************************************************************/
//Package
package axohEngine2.data;

//Imports
import java.io.Serializable;

public class Data implements Serializable {	
	//Anything being serialized needs an ID to signify its difference from something else
	private static final long serialVersionUID = -4668422157233446222L;
	
	//Variables to be updated and saved or accessed at some point
	private String _currentMapName;
	private String _currentOverlayName;
	private int _playerX;
	private int _playerY;
	private String name;
	private int potions = 0;
	private int mpotions = 0;
	//Method used to update the private variables
	public void update(String currentMapName, String currentOverlayName, int playerX, int playerY, int potion, int mpotion){
		_currentMapName = currentMapName;
		_currentOverlayName = currentOverlayName;
		_playerX = playerX;
		_playerY = playerY;
		potions = potion;
		mpotions = mpotion;
	}
	
	/***************************************************
	 * @return String - Get _currentMapName variable
	 ***************************************************/
	public String getMapName() {
		return _currentMapName;
	}
	
	/***************************************************
	 * @return String - Get _currentOverlayName variable
	 ***************************************************/
	public String getOverlayName() {
		return _currentOverlayName;
	}
	
	/***************************************************
	 * @return Int - Get _playerX variable
	 ***************************************************/
	public int getPlayerX() {
		return _playerX;
	}
	
	/***************************************************
	 * @return Int - Get _playerY variable
	 ***************************************************/
	public int getPlayerY() {
		return _playerY;
	}
	
	public void setName(String n){
		name = n.trim();
	}
	public String getName(){
		return name;
	}
	public void setLocation(int x, int y){
		_playerX = x;
		_playerY = y;
	}
	public int[] getPotions(){
		int [] temp = new int[2];
		temp[0] = potions;
		temp[1] = mpotions;
		return temp;
	}
}
