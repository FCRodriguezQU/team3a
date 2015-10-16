/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 0.53
 * Date: June 14, 2015
 * 
 * 
 * Title: Judgement(The Game)
 * Description: This class extends 'Game.java' in order to run a 2D game with specificly defined
 *  sprites, animatons, and actions.
 *  
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package name
package axohEngine2;

//Imports
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import axohEngine2.entities.AnimatedSprite;
import axohEngine2.entities.ImageEntity;
import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;
import axohEngine2.project.InGameMenu;
import axohEngine2.project.MapDatabase;
import axohEngine2.project.OPTION;
import axohEngine2.project.STATE;
import axohEngine2.project.TYPE;
import axohEngine2.project.TitleMenu;

//Start class by also extending the 'Game.java' engine interface
public class Judgement extends Game implements ActionListener
{
	//For serializing (The saving system)
	private static final long serialVersionUID = 1L;
	
	/****************** Variables **********************/
	/*--------- Screen ---------
	 * SCREENWIDTH - Game window width
	 * SCREENHEIGHT - Game window height
	 * CENTERX/CENTERY - Center of the game window's x/y
	 */
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static int width = (int)screenSize.getWidth();
	static int height = (int)screenSize.getHeight();
	static int SCREENWIDTH = width;
	static int SCREENHEIGHT = height;
	static int CENTERX = SCREENWIDTH / 2;
	static int CENTERY = SCREENHEIGHT / 2;
	String playerName = null;
	
	/*--------- Miscellaneous ---------
	 *booleans - A way of detecting a pushed key in game
	 *random - Use this to generate a random number
	 *state - Game states used to show specific info ie. pause/running
	 *option - In game common choices at given times
	 *Fonts - Various font sizes in the Arial style for different in game text
	 *
	 */
	boolean keyLeft, keyRight, keyUp, keyDown, keyInventory, keyAction, keyBack, keyEnter, keySpace, keyEscape,keyAttack;
	Random random = new Random();
	STATE state; 
	OPTION option;
	private Font simple = new Font("Arial", Font.PLAIN, 72);
	private Font bold = new Font("Arial", Font.BOLD, 72);
	private Font bigBold = new Font("Arial", Font.BOLD, 96);
	
	/*--------- Player and scale ---------
	 * scale - All in game art is 16 x 16 pixels, the scale is the multiplier to enlarge the art and give it the pixelated look
	 * mapX/mapY - Location of the camera on the map
	 * playerX/playerY - Location of the player on the map
	 * startPosX/startPosY - Starting position of the player in the map
	 * playerSpeed - How many pixels the player moves in a direction each update when told to
	 * */
	private int scale;
	private int mapX;
	private int mapY;
	private int playerX;
	private int playerY;
	private int startPosX;
	private int startPosY;
	private int playerSpeed;
	
	/*----------- Map and input --------
	* currentMap - The currently displayed map the player can explore
	* currentOverlay - The current overlay which usually contains houses, trees, pots, etc.
	* mapBase - The database which contains all variables which pertain to specific maps(NPCs, monsters, chests...)
	* inputWait - How long the system waits for after an action is done on the keyboard
	* confirmUse - After some decisions are made, a second question pops up, true equals continue action from before.
	*/
	private Map currentMap;
	public Map currentOverlay;
	private MapDatabase mapBase;
	private int inputWait = 5;
	private boolean confirmUse = false;
	
	/*----------- Menus ----------------
	 * inX/inY - In Game Menu starting location for default choice highlight
	 * inLocation - Current choice in the in game menu represented by a number, 0 is the top
	 * sectionLoc - Current position the player could choose after the first choice has been made in the in game menu(Items -> potion), 0 is the top.
	 * titleX, titleY, titleX2, titleY2 - Positions for specific moveable sprites at the title screen (arrow/highlight).
	 * titleLocation - Current position the player is choosing in the title screen(File 1, 2, 3) 0 is top
	 * currentFile - Name of the currently loaded file
	 * wasSaving/wait/waitOn - Various waiting variables to give the player time to react to whats happening on screen
	 */
	private int inX = SCREENWIDTH/10-50, inY = 120;
	private int inLocation;
	private int sectionLoc;
	private int titleX = 530, titleY = 610;
	private int titleX2 = 340, titleY2 = 310;
	private int titleLocation;
	private String currentFile;
	private boolean wasSaving = false;
	private int wait;
	private boolean waitOn = false;
	
	/*----------- Game  -----------------
	 * SpriteSheets (To be split in to multiple smaller sprites)
	 */
	SpriteSheet extras1;
	SpriteSheet mainCharacter;
	
	//ImageEntitys (Basic pictures)
	ImageEntity inGameMenu;
	ImageEntity titleMenu;
	ImageEntity titleMenu2;
	
	//Menu classes
	TitleMenu title;
	InGameMenu inMenu;
	
	//Animated sprites
	AnimatedSprite titleArrow;
	
	//Player and NPCs
	Mob playerMob;
	Mob randomNPC;
	
	static int temp = 0;
	
	/*********************************************************************** 
	 * Constructor
	 * 
	 * Set up the super class Game and set the window to appear
	 **********************************************************************/
	public Judgement() 
	{
		super(130, SCREENWIDTH, SCREENHEIGHT);
		ImageIcon img = new ImageIcon("/textures/extras/extras1.png");
		setIconImage(img.getImage());
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		currentMap.accessTile(temp).drawBounds(Color.BLACK);
		currentOverlay.accessTile(temp).drawBounds(Color.ORANGE);
		Timer time = new Timer(30000, this);
		time.start(); 
	}
	
	/****************************************************************************
	 * Inherited Method
	 * This method is called only once by the 'Game.java' class, for startup
	 * Initialize all non-int variables here
	 *****************************************************************************/
	void gameStartUp() 
	{
		/****************************************************************
		 * The "camera" is the mapX and mapY variables. These variables 
		 * can be changed in order to move the map around, simulating the
		 * camera. The player is moved around ONLY when at an edge of a map,
		 * otherwise it simply stays at the center of the screen as the "camera"
		 * is moved around.
		 ****************************************************************/
		//****Initialize Misc Variables
		state = STATE.TITLE;
		option = OPTION.NONE;
		startPosX = -400; //TODO: Make a method that takes a tile index and spits back an x or y coordinate of that tile
		startPosY = -400;
		mapX = startPosX;
		mapY = startPosY;
		scale = 4;
		playerSpeed = 5;
		//****Initialize spriteSheets*********************************************************************
		extras1 = new SpriteSheet("/textures/extras/extras1.png", 8, 8, 32, scale);
		mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);

		//****Initialize and setup AnimatedSprites*********************************************************
		titleArrow = new AnimatedSprite(this, graphics(), extras1, 0, "arrow");
		titleArrow.loadAnim(4, 10);
		sprites().add(titleArrow);
		
		//****Initialize and setup image entities**********************************************************
		inGameMenu = new ImageEntity(this);
		titleMenu = new ImageEntity(this);
		titleMenu2 = new ImageEntity(this);
		inGameMenu.load("/menus/ingamemenu.png");
		titleMenu.load("/menus/titlemenu1.png");
		titleMenu2.load("/menus/titlemenu2.png");
		
		//*****Initialize Menus***************************************************************************
		title = new TitleMenu(titleMenu, titleMenu2, titleArrow, SCREENWIDTH, SCREENHEIGHT, simple, bold, bigBold);
		inMenu = new InGameMenu(inGameMenu, SCREENWIDTH, SCREENHEIGHT);
		
		//****Initialize and setup Mobs*********************************************************************
		playerMob = new Mob(this, graphics(), mainCharacter, 40, TYPE.PLAYER, "mainC", true);
		playerMob.setMultBounds(7, 50, 95, 37, 55, 62, 55, 50, 10);
		playerMob.setMoveAnim(32, 48, 40, 56, 3, 8);
		playerMob.addAttack("sword", 0, 5);
		playerMob.getAttack("sword").addMovingAnim(17, 25, 9, 1, 3, 8);
		playerMob.getAttack("sword").addAttackAnim(20, 28, 12, 4, 3, 6);
		playerMob.getAttack("sword").addInOutAnim(16, 24, 8, 0, 1, 10);
		playerMob.setCurrentAttack("sword"); //Starting attack
		playerMob.setHealth(35); //If you change the starting max health, dont forget to change it in inGameMenu.java max health also
		sprites().add(playerMob);
		
		//*****Initialize and setup first Map******************************************************************
		mapBase = new MapDatabase(this, graphics(), scale);
		
		//Get Map from the database
		for(int i = 0; i < mapBase.maps.length; i++)
		{
			// Identifies the rendering process
			if(i < 4)
			{
				System.out.println("Rendering map " + mapBase.getMap(i)._name);
			}
			
			// If the map equals nothing 
			if(mapBase.getMap(i) == null)
			{
				// Just continue on ahead
				continue;
			}
			// If the map database has the map name "city".. 
			if(mapBase.getMap(i).mapName() == "city")
			{
				// Set the map pointer to hold the correct map object
				currentMap = mapBase.getMap(i);
			}
			// If the map database has the map name "city0"...
			if(mapBase.getMap(i).mapName() == "cityO")
			{
				// Set the map pointer to hold the correct map object
				currentOverlay = mapBase.getMap(i);
			}
		}
		//Add the tiles from the map to be updated each system cycle
		for(int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++)
		{
			temp = i;
			// Add a tile to the map based upon the index of i
			addTile(currentMap.accessTile(i));
			
			// Add a tile to the currentOverlay map based upon the index of i 
			addTile(currentOverlay.accessTile(i));
			
			// Check if the current map has a mob object at that tile location
			if(currentMap.accessTile(i).hasMob()) 
			{
				// If so, 
				sprites().add(currentMap.accessTile(i).mob());
			}
			// Check if the currentMap overlay has a mob object at that tile location
			if(currentOverlay.accessTile(i).hasMob()) 
			{
				sprites().add(currentOverlay.accessTile(i).mob());
			}
			
			currentMap.accessTile(i).getEntity().setX(-300);
			
			currentOverlay.accessTile(i).getEntity().setX(-300);
		}
		
		requestFocus(); //Make sure the game is focused on
		start(); //Start the game loop
	}
	
	/**************************************************************************** 
	 * Inherited Method
	 * Method that updates with the default 'Game.java' loop method
	 * Add game specific elements that need updating here
	 *****************************************************************************/
	void gameTimedUpdate() 
	{
		// Checks for the user's input
		checkInput();
		
		//Update certain specifics based on certain game states
		if(state == STATE.TITLE)
		{
			title.update(option, titleLocation); //Title Menu update
		}
		if(state == STATE.INGAMEMENU)
		{
			inMenu.update(option, sectionLoc, playerMob.health()); //In Game Menu update
		}
		
		this.updateData(currentMap, currentOverlay, playerX, playerY);//Update the current file data for saving later
		//System.out.println("The framerate is : " + frameRate()); //Print the current frame rate to the console
		if(waitOn) wait--;
	}
	
	/**
	 * Inherited Method
	 * Obtain the 'graphics' passed down by the super class 'Game.java' and render objects on the screen
	 */
	void gameRefreshScreen() {		
		/*********************************************************************
		* Rendering images uses the java class Graphics2D
		* Each frame the screen needs to be cleared and an image is setup as a back buffer which is brought 
		* to the front as a full image at the time it is needed. This way the screen is NOT rendered pixel by 
		* pixel in front of the user, which would have made a strange lag effect.
		* 
		* 'graphics' objects have parameters that can be changed which effect what it renders, two are font and color
		**********************************************************************/
		Graphics2D g2d = graphics();
		g2d.clearRect(0, 0, SCREENWIDTH, SCREENHEIGHT); 
		g2d.setFont(simple);

		//GUI rendering for when a specific state is set, only specific groups of data is drawn at specific times
		if(state == STATE.GAME) 
		{
			//Render the map, the player, any NPCs or Monsters and the player health or status
			currentMap.render(this, g2d, mapX, mapY);
			currentOverlay.render(this, g2d, mapX, mapY);
			playerMob.renderMob(CENTERX - playerX, CENTERY - playerY);
			g2d.setColor(Color.RED);
			g2d.drawString("Health: " + inMenu.getHealth(), CENTERX - 780, CENTERY - 350);
			g2d.setColor(Color.BLUE);
			g2d.drawString("Magic: " + inMenu.getMagic(), CENTERX - 280, CENTERY - 350);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("Player: " + playerName, CENTERX + 200, CENTERY - 350);
		} 
		if(state == STATE.INGAMEMENU)
		{
			//Render the in game menu and specific text
			inMenu.render(this, g2d, inX, inY);
			g2d.setColor(Color.red);
			if(confirmUse) g2d.drawString("Use this?", CENTERX, CENTERY);
		}
		if(state == STATE.TITLE) 
		{
			//Render the title screen
			title.render(this, g2d);
		}
		
		//Render save time specific writing
		if(option == OPTION.SAVE)
		{
			save.saveState(mapX,mapY);
			g2d.setFont(new Font("TimesRoman", Font.BOLD, 110));
			drawString(g2d, (playerName + "'s game\n was saved." ), SCREENWIDTH/4*2, 450);
		}
	}
	
	/*******************************************************************
	 * The next four methods are inherited
	 * Currently these methods are not being used, but they have
	 * been set up to go off at specific times in a game as events.
	 * Actions that need to be done during these times can be added here.
	 ******************************************************************/
	void gameShutDown() {	
		save.saveState(mapX, mapY);
	}

	void spriteUpdate(AnimatedSprite sprite) 
	{		
	}

	void spriteDraw(AnimatedSprite sprite) 
	{		
	}

	void spriteDying(AnimatedSprite sprite) 
	{		
	}
	
	/*************************************************************************
	 * @param AnimatedSprite
	 * @param AnimatedSprite
	 * @param int
	 * @param int
	 * 
	 * Inherited Method
	 * Handling for when a SPRITE contacts a SPRITE
	 * 
	 * hitDir is the hit found when colliding on a specific bounding box on spr1 and hitDir2
	 * is the same thing applied to spr2
	 * hitDir is short for hit direction which can give the data needed to move the colliding sprites
	 * hitDir is a number between and including 0 and 3, these assignments are taken care of in 'Game.java'.
	 * What hitDir is actually referring to is the specific hit box that is on a multi-box sprite.
	 *****************************************************************************/
	void spriteCollision(AnimatedSprite spr1, AnimatedSprite spr2, int hitDir, int hitDir2) 
	{	
		// Only check for sprite collision if the user is not in the title screen
		if(state != STATE.TITLE)
		{
			//System.out.println("Sprite Collision Being Called");
			//System.out.println("Checking " + spr1._name + " sprite and " + spr2._name + " sprite");
			
			//Get the smallest possible overlap between the two problem sprites
			double leftOverlap = (spr1.getBoundX(hitDir) + spr1.getBoundSize() - spr2.getBoundX(hitDir2));
			double rightOverlap = (spr2.getBoundX(hitDir2) + spr2.getBoundSize() - spr1.getBoundX(hitDir));
			double topOverlap = (spr1.getBoundY(hitDir) + spr1.getBoundSize() - spr2.getBoundY(hitDir2));
			double botOverlap = (spr2.getBoundY(hitDir2) + spr2.getBoundSize() - spr1.getBoundY(hitDir));
			double smallestOverlap = Double.MAX_VALUE; 
			double shiftX = 1;
			double shiftY = 1;
			
			//System.out.println("Smallest Overlap : " + smallestOverlap);
			//System.out.println("Right Overlap : " + rightOverlap);
			//System.out.println("Top Overlap : " + topOverlap);
			//System.out.println("Bottun Overlap : " + botOverlap);
	
			if(leftOverlap < smallestOverlap) { //Left
				smallestOverlap = leftOverlap;
				shiftX -= leftOverlap; 
				//System.out.println("Thing 1");
				shiftY = 0;
			}
			if(rightOverlap < smallestOverlap){ //right
				smallestOverlap = rightOverlap;
				//System.out.println("Thing 2");
				shiftX = rightOverlap;
				shiftY = 0;
			}
			if(topOverlap < smallestOverlap){ //up
				smallestOverlap = topOverlap;
				//System.out.println("Thing 3");
				shiftX = 0;
				shiftY -= topOverlap;
			}
			if(botOverlap < smallestOverlap){ //down
				smallestOverlap = botOverlap;
				//System.out.println("Thing 4");
				shiftX = 0;
				shiftY = botOverlap;
			}
	
			//Handling very specific collisions
			if(spr1.spriteType() == TYPE.PLAYER && state == STATE.GAME)
			{
				if(spr2 instanceof Mob) ((Mob) spr2).stop();
				
				//This piece of code is commented out because I still need the capability of getting a tile from an xand y position
				/*if(((Mob) spr1).attacking() && currentOverlay.getFrontTile((Mob) spr1, playerX, playerY, CENTERX, CENTERY).getBounds().intersects(spr2.getBounds())){
					((Mob) spr2).takeDamage(25);
					//TODO: inside of take damage should be a number dependant on the current weapon equipped, change later
				}*/
				
				//Handle simple push back collision
				if(playerX != 0) playerX -= shiftX;
				if(playerY != 0) playerY -= shiftY;
				if(playerX == 0) mapX -= shiftX;
				if(playerY == 0) mapY -= shiftY;
			}
		}
	}
	
	/***********************************************************************
	* @param AnimatedSprite
	* @param Tile
	* @param int
	* @param int
	* 
	* Inherited Method
	* Set handling for when a SPRITE contacts a TILE, this is handy for
	* dealing with Tiles which contain Events. When specifying a new
	* collision method, check for the type of sprite and whether a tile is
	* solid or breakable, both, or even if it contains an event. This is
	* mandatory because the AxohEngine finds details on collision and then 
	* returns it for specific handling by the user.
	* 
	* For more details on this method, refer to the spriteCollision method above
	*************************************************************************/
	void tileCollision(AnimatedSprite spr, Tile tile, int hitDir, int hitDir2) 
	{
			//System.out.println("Hit direction was : " + spr.getBoundX(hitDir));
			double leftOverlap = (spr.getBoundX(hitDir) + spr.getBoundSize() - tile.getBoundX(hitDir2));
			double rightOverlap = (tile.getBoundX(hitDir2) + tile.getBoundSize() - spr.getBoundX(hitDir));
			double topOverlap = (spr.getBoundY(hitDir) + spr.getBoundSize() - tile.getBoundY(hitDir2));
			double botOverlap = (tile.getBoundY(hitDir2) + tile.getBoundSize() - spr.getBoundY(hitDir));
			double smallestOverlap = Double.MAX_VALUE; 
			double shiftX = 0;
			double shiftY = 0;
			
			//System.out.println("Smallest Overlap : " + smallestOverlap);
			//System.out.println("Left Overlap : " + leftOverlap);
			//System.out.println("Right Overlap : " + rightOverlap);
			//System.out.println("Top Overlap : " + topOverlap);
			//System.out.println("Button Overlap : " + botOverlap);
	
	
			if(leftOverlap < smallestOverlap) 
			{ //Left
				
				// Updates smallestOverlap to equal leftOverlap
				smallestOverlap = leftOverlap;
				
				// Updates the numerical value that the player must be shifted
				shiftX -= leftOverlap; 
				shiftY = 0;
			}
			if(rightOverlap < smallestOverlap)
			{ 
				// Updates smallestOverlap to equal rightOverlap
				smallestOverlap = rightOverlap;
				
				// Updates the numerical value that the player must be shifted
				shiftX = rightOverlap;
				shiftY = 0;
			}
			if(topOverlap < smallestOverlap)
			{ 
				// Updates smallestOverlap to equal TopOverlap
				smallestOverlap = topOverlap;
			
				// Updates the numerical value that the player must be shifted
				shiftX = 0;
				shiftY -= topOverlap;
			}
			if(botOverlap < smallestOverlap)
			{ 
				// Updates smallestOverlap to equal botOverlap
				smallestOverlap = botOverlap;
				
				// Updates the numerical value that the player must be shifted
				shiftX = 0;
				shiftY = botOverlap;
			}
			
			//Deal with a tiles possible event property
			if(tile.hasEvent()){
				if(spr.spriteType() == TYPE.PLAYER) 
				{
					//Warp Events(Doors)
					if(tile.event().getEventType() == TYPE.WARP)
					{
						tiles().clear();
						sprites().clear();
						sprites().add(playerMob);
						
						//Get the new map
						for(int i = 0; i < mapBase.maps.length; i++)
						{
							 if(mapBase.getMap(i) == null) continue;
							 if(tile.event().getMapName() == mapBase.getMap(i).mapName()) currentMap = mapBase.getMap(i);
							 if(tile.event().getOverlayName() == mapBase.getMap(i).mapName()) currentOverlay = mapBase.getMap(i);
						}
						//Load in the new maps Tiles and Mobs
						for(int i = 0; i < currentMap.getWidth() * currentMap.getHeight(); i++){
							addTile(currentMap.accessTile(i));
							addTile(currentOverlay.accessTile(i));
							if(currentMap.accessTile(i).hasMob()) sprites().add(currentMap.accessTile(i).mob());
							if(currentOverlay.accessTile(i).hasMob()) sprites().add(currentOverlay.accessTile(i).mob());
						}
						//Move the player to the new position
						playerX = tile.event().getNewX();
						playerY = tile.event().getNewY();
					}	
				} //end warp
				//Item exchange event
				if(spr.spriteType() == TYPE.PLAYER && tile.event().getEventType() == TYPE.ITEM && keyAction){
					if((tile._name).equals("chest")) tile.setFrame(tile.getSpriteNumber() + 1); //Chests should have opened and closed version next to each other
					inMenu.addItem(tile.event().getItem()); //Add item to inventory
					System.out.println(tile.event().getname());
					tile.endEvent();
				}
			}//end check events
			
			//If the tile is solid, move the player off of it and exit method immediately
			if(spr.spriteType() == TYPE.PLAYER && tile.solid() && state == STATE.GAME) {
				if(playerX != 0) playerX -= shiftX;
				if(playerY != 0) playerY -= shiftY;
				if(playerX == 0) mapX -= shiftX;
				if(playerY == 0) mapY -= shiftY;
				return;
			}
			//If an npc is intersecting a solid tile, move it off
			if(spr.spriteType() != TYPE.PLAYER && tile.solid() && state == STATE.GAME){
				if(spr instanceof Mob) {
					((Mob) spr).setLoc((int)shiftX, (int)shiftY);
					((Mob) spr).resetMovement();
				}
		}
	}//end tileCollision method
	
	/*****************************************************************
	 * @param int
	 * @param int
	 * 
	 *Method to call which moves the player. The player never moves apart from the map
	 *unless the player is at an edge of the generated map. Also, to simulate the movement
	 *of the space around the player like that, the X movement is flipped. 
	 *Which means to move right, you subtract from the X position.
	 ******************************************************************/
	void movePlayer(int xa, int ya) {
		if(xa > 0) {
			if(mapX + xa < currentMap.getMinX() && playerX < playerSpeed && playerX > -playerSpeed) mapX += xa;
			else playerX += xa; //left +#
		}
		if(xa < 0) {
			if(mapX + xa > currentMap.getMaxX(SCREENWIDTH) && playerX < playerSpeed && playerX > -playerSpeed) mapX += xa;
			else playerX += xa; //right -#
		}
		if(ya > 0) {
			if(mapY + ya < currentMap.getMinY() && playerY < playerSpeed && playerY > -playerSpeed) mapY += ya;
			else playerY += ya; //up +#
		}
		if(ya < 0) {
			if(mapY + ya > currentMap.getMaxY(SCREENHEIGHT) && playerY < playerSpeed && playerY > -playerSpeed) mapY += ya;
			else playerY += ya; //down -#
		}
		data.setLocation(playerX, playerY);
	}
	
	/**********************************************************
	 * Main
	 * 
	 * @param args
	 ********************************************************/
	public static void main(String[] args) { 
		
		new Judgement(); 
		}
	
	/**********************************************************
	 * The Depths of Judgement Lies Below
	 * 
	 *             Key events - Mouse events
	 *                            
	 ***********************************************************/
	
	/****************************************************************
	 * Check specifically defined key presses which do various things
	 ****************************************************************/
	public void checkInput() {
		int xa = 0;
		int ya = 0;
		
		/********************************************
		 * Special actions for In Game
		 *******************************************/
		if(state == STATE.GAME && inputWait < 0) { 
			//A or left arrow(move left)
			if(keyLeft) {
				xa = xa + 1 + playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			//D or right arrow(move right)
			if(keyRight) {
				xa = xa - 1 - playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			//W or up arrow(move up)
			if(keyUp) {
				ya = ya + 1 + playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			//S or down arrow(move down)
			if(keyDown) {
				ya = ya - 1 - playerSpeed;
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			if(keyEscape){
				save.saveState(mapX, mapY);
				System.out.println("Game saved successfully");
				System.exit(0);
			}
			if(keyAttack && playerMob.isTakenOut()){
				playerMob.attack();
			}
			//No keys are pressed
			if(!keyLeft && !keyRight && !keyUp && !keyDown) {
				playerMob.updatePlayer(keyLeft, keyRight, keyUp, keyDown);
			}
			movePlayer(xa, ya);
			//this.RenderBoundsInGame();
			playerMob.drawAllBounds(Color.red, Color.yellow, Color.pink, Color.green);
		
			//I(Inventory)
			if(keyInventory) {
				state = STATE.INGAMEMENU;
				inputWait = 10;
			}
			
			//SpaceBar(action button)
			if(keySpace) {
				playerMob.inOutItem();
				inputWait = 10;
			}
		}//end in game choices
		
		/*****************************************
		 * Special actions for the Title Menu
		 *****************************************/
		if(state == STATE.TITLE && inputWait < 0){
			//For when no initial choice has been made
			if(option == OPTION.NONE){
				//S or down arrow(Change selection)
				if(keyDown && titleLocation < 1) {
					
				}
				//W or up arrow(Change selection
				if(keyUp && titleLocation > 0){
					titleX += 105;
					titleY -= 100;
					titleLocation--;
					inputWait = 5;
				}
				//Enter key(Make a choice)
				if(keyEnter) {
					this.loadGame();
					state = STATE.GAME;
					setGameState(STATE.GAME);
				}
				if(keyEscape){
					save.saveState(mapX, mapY);
					System.exit(0);
				}
			}//end option none
			
			//After choosing an option
			if(option == OPTION.NEWGAME || option == OPTION.LOADGAME){
				//Backspace(Exit choice)
				if(keyBack && !title.isGetName()){
					if(option == OPTION.NEWGAME) titleLocation = 0;
					if(option == OPTION.LOADGAME) titleLocation = 1;
					inputWait = 5;
					titleX2 = 340;
					titleY2 = 310;
					option = OPTION.NONE;
				}
				//S or down arrow(Change selection)
				if(keyDown && titleLocation < 2 && !title.isGetName()) {
					titleLocation++;
					titleY2 += 160;
					inputWait = 7;
				}
				//W or up arrow(Change selection)
				if(keyUp && titleLocation > 0 && !title.isGetName()) {
					titleLocation--;
					titleY2 -= 160;
					inputWait = 7;
				}
				//Enter key(Make a choice)
				if(keyEnter && !title.isGetName()) {
					if(option == OPTION.NEWGAME) {
						if(title.files() != null){ //Make sure the location of a new game is greater than previous ones(Not overwriting)
							if(title.files().length - 1 < titleLocation) {
								title.enter();
								titleX2 += 40;
								inputWait = 5;
							}
						}
						if(title.files() == null) { //Final check if there are no files made yet, to make the file somewhere
							title.enter();
							titleX2 += 40;
							inputWait = 5;
						}
					}
					//Load the currently selected file
					if(option == OPTION.LOADGAME) {
						currentFile = title.enter();
						if(currentFile != "") { //File is empty
							loadGame();
							inputWait = 5;
							option = OPTION.NONE;
							state = STATE.GAME;
							setGameState(STATE.GAME);
						}
					}
				}//end enter key
				
				//The following is for when a new file needs to be created - Typesetting
				if(title.isGetName() == true) {
					title.setFileName(currentChar);
					currentChar = '\0'; //null
					//Back space(Delete last character)
					if(keyBack) {
						title.deleteChar();
						inputWait = 5;
					}
					//Back space(exit name entry if name has no characters)
					if(keyBack && title.getFileName().length() == 0) {
						title.setGetName(false);
						titleX2 -= 40;
						inputWait = 5;
					}
					//Enter key(Write the file using the currently typed name and save it)
					if(keyEnter && title.getFileName().length() > 0) {
						title.setGetName(false);
						currentFile = title.getFileName();
						state = STATE.GAME;
						option = OPTION.NONE;
						setGameState(STATE.GAME);
					}
				}//end get name
			}//end new/load option
		}//end title state
		
		
		/******************************************
		 * Special actions for In Game Menu
		 ******************************************/
		if(state == STATE.INGAMEMENU && inputWait < 0) {
			//I(Close inventory)
			if(keyInventory) {
				state = STATE.GAME;
				option = OPTION.NONE;
				inLocation = 0;
				inY = 130;
				inputWait = 8;
			}
			int boxChangeY = 130;
			//No option is chosen yet
			if(option == OPTION.NONE){ 
				if(wait == 0) wasSaving = false;
				//W or up arrow(Move selection)
				if(keyUp) {
					if(inLocation > 0) {
						inY -= boxChangeY;
						inLocation--;
						inputWait = 10;
					}
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(inLocation < 4) {
						inY += boxChangeY;
						inLocation++;
						inputWait = 10;
					}
				}
				//Enter key(Make a choice)
				if(keyEnter) {
					if(inLocation == 0){
						option = OPTION.ITEMS;
						inputWait = 5;
					}
					if(inLocation == 1){
						option = OPTION.EQUIPMENT;
						inputWait = 5;
					}
					if(inLocation == 2){
						option = OPTION.MAGIC;
						inputWait = 5;
					}
					if(inLocation == 3){
						option = OPTION.STATUS;
						inputWait = 5;
					}
					if(inLocation == 4){
						option = OPTION.SAVE;
						inputWait = 20;
					}
					keyEnter = false;
				}
			}
			
			//Set actions for specific choices in the menu
			//Items
			if(option == OPTION.ITEMS) {
				//W or up arrow(move selection)
				if(keyUp){
					if(sectionLoc == 0) inMenu.loadOldItems();
					if(sectionLoc - 1 != -1) sectionLoc--;
					inputWait = 8;
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(sectionLoc == 3) inMenu.loadNextItems();
					if(inMenu.getTotalItems() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
					inputWait = 8;
				}
				//Enter key(Make a choice)
				if(keyEnter){
					if(confirmUse) {
						inMenu.useItem(); //then use item
						confirmUse = false;
						keyEnter = false;
					}
					if(inMenu.checkCount() > 0 && keyEnter) confirmUse = true;
					inputWait = 10;
				}
				//Back space(Go back on your last choice)
				if(keyBack) confirmUse = false;
			}
			
			//Equipment
			if(option == OPTION.EQUIPMENT) {
				//W or up arrow(move selection)
				if(keyUp){
					if(sectionLoc == 0) inMenu.loadOldItems();
					if(sectionLoc - 1 != -1) sectionLoc--;
					inputWait = 8;
				}
				//S or down arrow(move selection)
				if(keyDown) {
					if(sectionLoc == 3) inMenu.loadNextEquipment();
					if(inMenu.getTotalEquipment() > sectionLoc + 1 && sectionLoc < 3) sectionLoc++;
					inputWait = 8;
				}
			}
			
			//Saving
			if(option == OPTION.SAVE){
				//Key enter(Save the file)
				if(keyEnter){
					save.saveState(mapX,mapY);
					inputWait = 20;
					wait = 200;
					waitOn = true;
					wasSaving = true;
					option = OPTION.NONE;
				}
			}
			
			//Backspace(if a choice has been made, this backs out of it)
			if(keyBack && option != OPTION.NONE) {
				option = OPTION.NONE;
				inMenu.setItemLoc(0);
				sectionLoc = 0;
				inputWait = 8;
				inY = 130;
				keyBack = false;
			}
			//Backspace(if a choice has not been made, this closes the inventory)
			if(keyBack && option == OPTION.NONE) {
				state = STATE.GAME;
				option = OPTION.NONE;
				inLocation = 0;
				sectionLoc = 0;
				inY = 130;
				inputWait = 8;
			}
		}
		inputWait--;
	}
	
	public void RenderBoundsInGame()
	{
		//Add the tiles from the map to be updated each system cycle
		for(int i = 0; i < currentMap.getHeight() * currentMap.getHeight(); i++)
		{	

				if(currentMap.accessTile(i).isSolid())
				{
					currentMap.accessTile(i).drawBounds(Color.red);
				}
			// Check if the currentMap overlay has a mob object at that tile location

				if(currentOverlay.accessTile(i).isSolid())
				{
					currentOverlay.accessTile(i).drawBounds(Color.blue);
				}
		}
	}
	
	/**
	 * Inherited method
	 * @param keyCode
	 * 
	 * Set keys for a new game action here using a switch statement
	 * Don't forget gameKeyUp
	 */
	void gameKeyDown(int keyCode) {
		switch(keyCode) {
	        case KeyEvent.VK_LEFT:
	            keyLeft = true;
	            break;
	        case KeyEvent.VK_A:
	        	keyAttack = true;
	        	break;
	        case KeyEvent.VK_RIGHT:
	            keyRight = true;
	            break;
	        case KeyEvent.VK_D:
	        	//keyRight = true;
	        	break;
	        case KeyEvent.VK_UP:
	            keyUp = true;
	            break;
	        case KeyEvent.VK_W:
	        	//keyUp = true;
	        	break;
	        case KeyEvent.VK_DOWN:
	            keyDown = true;
	            break;
	        case KeyEvent.VK_S:
	        	//keyDown = true;
	        	break;
	        case KeyEvent.VK_I:
	        	keyInventory = true;
	        	break;
	        case KeyEvent.VK_F:
	        	keyAction = true;
	        	break;
	        case KeyEvent.VK_ENTER:
	        	keyEnter = true;
	        	break;
	        case KeyEvent.VK_BACK_SPACE:
	        	keyBack = true;
	        	break;
	        case KeyEvent.VK_SPACE:
	        	keySpace = true;
	        	break;
	        case KeyEvent.VK_ESCAPE:
	        	keyEscape = true;
	        	break;
        }
	}

	/**
	 * Inherited method
	 * @param keyCode
	 * 
	 * Set keys for a new game action here using a switch statement
	 * Dont forget gameKeyDown
	 */
	void gameKeyUp(int keyCode) {
		switch(keyCode) {
        case KeyEvent.VK_LEFT:
            keyLeft = false;
            break;
        case KeyEvent.VK_A:
        	keyAttack = false;
        	break;
        case KeyEvent.VK_RIGHT:
            keyRight = false;
            break;
        case KeyEvent.VK_D:
        	keyRight = false;
        	break;
        case KeyEvent.VK_UP:
            keyUp = false;
            break;
        case KeyEvent.VK_W:
        	keyUp = false;
        	break;
        case KeyEvent.VK_DOWN:
            keyDown = false;
            break;
        case KeyEvent.VK_S:
        	keyDown = false;
        	break;
        case KeyEvent.VK_I:
	    	keyInventory = false;
	    	break;
	    case KeyEvent.VK_F:
	    	keyAction = false;
	    	break;
	    case KeyEvent.VK_ENTER:
	    	keyEnter = false;
	    	break;
	    case KeyEvent.VK_BACK_SPACE:
	    	keyBack = false;
	    	break;
	    case KeyEvent.VK_SPACE:
	    	keySpace = false;
	    	break;
	    case KeyEvent.VK_ESCAPE:
        	keyEscape = false;
        	break;
		}
	}

	/**
	 * Inherited method
	 * Currently unused
	 */
	void gameMouseDown() {	
	}

	/**
	 * Inherited method
	 * Currently if the game is running and the sword is out, the player attacks with it
	 */
	void gameMouseUp(double x, double y) {
		this.setFocusable(true);
		int temp;
		if(x < playerMob.getXLoc()){
		}
		else if(x > playerMob.getYLoc()){
			keyRight = true;
		}
		System.out.println("Player Location:" + playerMob.getXLoc() + "," + playerMob.getYLoc() + "\nMouseLocation:" + x + "," + y);
		//playerMob.move();
		keyLeft = false;
		keyRight = false;
	}

	/**
	 * Inherited Method
	 * Currently unused
	 */
	void gameMouseMove() {
	}
	 
	 //From the title screen, load a game file by having the super class get the data,
	 // then handling where the pieces of the data will be assigned here.
	/**
	 * Inherited Method
	 * 
	 * The title screen calls this method when a currently existing file is chosen
	 * Add new saved game details here as well as in the 'Data.java' class
	 * 
	 * Currently only the player x and y location and the current map is saved
	 */
	 void loadGame() {
		 File file = new File("Save.csv");
		 if(file.exists()){
		 String[] temp = new String[3];
		 int x = 0;
		 int y = 0;
		 try {
	        FileReader fileReader = new FileReader(file);
	        BufferedReader bufferedReader = new BufferedReader(fileReader);
	        String read = bufferedReader.readLine().trim();
	        bufferedReader.close();
	        
	        temp = read.split(",");
	        playerName = temp[0];
	        data.setName(playerName);
			mapX = Integer.parseInt(temp[1]);
			mapY = Integer.parseInt(temp[2]);
		 }
		  catch (FileNotFoundException e) {
			System.out.println("No save file exists. A new file will be created.");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 catch(NullPointerException e){
			System.out.println("NullPointerException: Player will be set to default location.");
		 }
		 }
		 else{
			    playerName = JOptionPane.showInputDialog("Enter your name:");
			    data.setName(playerName);
				mapX = -10;
		        mapY = -150;
		 }

	 } //end load method
	 
	 public int getPlayerX(){
		 return playerX;
	 }
	 public int getPlayerY(){
		 return playerY;
	 }

	@Override
	public void actionPerformed(ActionEvent e) {
		save.saveState(mapX, mapY);
		System.out.println("AutoSave");
	}	 
	public String getName(){
		return playerName;
	}
	
	
	
} //end class