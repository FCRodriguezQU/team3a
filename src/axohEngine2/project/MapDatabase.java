/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: July 5, 2015
 * 
 * Title: Map Database
 * Description: A data handling class used for large projects. This class contains all of the spritesheets,
 * tiles, events, items, mobs and map creations since they all interlock together.
 * 
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/
//Package
package axohEngine2.project;

//Imports
import java.awt.Graphics2D;

import javax.swing.JFrame;

import axohEngine2.entities.Mob;
import axohEngine2.entities.SpriteSheet;
import axohEngine2.map.Event;
import axohEngine2.map.Map;
import axohEngine2.map.Tile;

public class MapDatabase {

	//SpriteSheets
	SpriteSheet misc;
	SpriteSheet buildings;
	SpriteSheet environment32;
	SpriteSheet extras2;
	SpriteSheet mainCharacter;
	
	//Maps
	Map city;
	Map cityO;
	Map houses;
	Map housesO;
	
	//Tiles - Names are defined in the constructor for better identification
	Tile d;
	Tile g;
	Tile f;
	Tile b;
	Tile r;
	Tile e;
	Tile ro;
	Tile h;
	Tile hf;
	Tile c;
	Tile t1;
	Tile t2;
	Tile t3;
	Tile t4;
	Tile p1l;
	Tile p1m;
	Tile p1r;
	Tile p2l;
	Tile p2r;
	Tile p3l;
	Tile p3m;
	Tile p3r;
	Tile p4l;
	Tile p4m;
	Tile p4r;
	Tile psl;
	Tile psr;
	Tile s;
	Tile pc;
	
	//Events
	Event warp1;
	Event warp2;
	Event getPotion;
	Event getMpotion;
	
	//Items
	Item potion;
	Item mpotion;
	
	//NPC's and Monsters
	Mob npc;
	
	//Array of maps
	public Map[] maps;
	
	/****************************************************************
	 * Constructor
	 * Instantiate all variables for the game
	 * 
	 * @param frame - JFrame Window for the map to be displayed on
	 * @param g2d - Graphics2D object needed to display images
	 * @param scale - Number to be multiplied by each image for correct on screen display
	 *******************************************************************/
	public MapDatabase(JFrame frame, Graphics2D g2d, int scale) {
		//Currently a maximum of 200 maps possible(Can be changed if needed)
		maps = new Map[200];
		
		//Set up spriteSheets
		misc = new SpriteSheet("/textures/environments/environments1.png", 16, 16, 16, scale);
		buildings = new SpriteSheet("/textures/environments/4x4buildings.png", 4, 4, 64, scale);
		environment32 = new SpriteSheet("/textures/environments/32SizeEnvironment.png", 8, 8, 32,scale);
		extras2 = new SpriteSheet("/textures/extras/extras2.png", 16, 16, 16, scale);
		mainCharacter = new SpriteSheet("/textures/characters/mainCharacter.png", 8, 8, 32, scale);
		
		//Set up tile blueprints and if they are animating
		d = new Tile(frame, g2d, "door", environment32, 0);
		f = new Tile(frame, g2d, "flower", misc, 1);
		g = new Tile(frame, g2d, "grass", misc, 0);
		b = new Tile(frame, g2d, "bricks", misc, 16, true);
		r = new Tile(frame, g2d, "walkWay", misc, 6);
		e = new Tile(frame, g2d, "empty", misc, 7);
		ro = new Tile(frame, g2d, "rock", misc, 2);
		h = new Tile(frame, g2d, "house", buildings, 0, true);
		hf = new Tile(frame, g2d, "floor", misc, 8);
		c = new Tile(frame, g2d, "chest", extras2, 0, true);
		p1l = new Tile(frame, g2d, "platform1left", misc, 32, true);
		p1m = new Tile(frame, g2d, "platform1mid", misc, 33, true);
		p1r = new Tile(frame, g2d, "platform1right", misc, 34, true);
		p2l = new Tile(frame, g2d, "platform2left", misc, 35, true);
		p2r = new Tile(frame, g2d, "platform2right", misc, 36, true);
		p3l = new Tile(frame, g2d, "platform3left", misc, 37, true);
		p3m = new Tile(frame, g2d, "platform3mid", misc, 38, true);
		p3r = new Tile(frame, g2d, "platform3right", misc, 39, true);
		p4l = new Tile(frame, g2d, "platform4left", misc, 40, true);
		p4m = new Tile(frame, g2d, "platform4mid", misc, 41, true);
		p4r = new Tile(frame, g2d, "platform4right", misc, 42, true);
		psl = new Tile(frame, g2d, "platformstairleft", misc, 44, true);
		psr = new Tile(frame, g2d, "platformstairright", misc, 45, true);
		s = new Tile(frame, g2d, "stair", misc, 43);
		pc = new Tile(frame, g2d, "platformcorner", misc, 46, true);
		
		
		//Set the tile blueprints in an array for the Map
		Tile[] cityTiles = {b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b,
			    b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, p1l, p1m, p1m, p1m, p1m, p1m, p1r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, f, g, g, g, g, g, p2l, g, g, g, g, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, p2l, g, g, g, g, g, p2r, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, p3l, psr, g, psl, pc, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, f, g, g, p4l, p4r, g, p4l, p2l, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, p2r, g, g, g, g, g, g, g, g, f, f, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p2l, g, p2r, g, g, g, g, g, g, g, g, f, f, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p2l, f, p2r, f, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p2l, f, p2r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, f, f, g, g, g, g, g, p2l, g, p2r, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, f, f, g, g, g, g, g, p3l, p3m, p3r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, p4l, p4m, p4r, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, f, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, f, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, g, b, b,
			    b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b,
			    b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b, b};

		Tile[] cityOTiles = {e, e, h, e, e, e, e, h, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, d, e, e, e, e, d, e, e, e, c, c, c, c, c, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, c, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, c, e, ro, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, ro, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, c, c, c, c, ro, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e,
							 e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e};

		Tile[] houseTiles = {hf, hf, hf, hf, hf, hf,
							 hf, hf, hf, hf, hf, hf,
							 hf, hf, hf, hf, hf, hf,
							 hf, hf, hf, hf, hf, hf,
							 hf, hf, hf, hf, hf, hf,
							 hf, hf, hf, hf, hf, hf};
		
		Tile[] houseOTiles = {hf, hf, hf, hf, hf, hf,
						 	  hf, hf, hf, hf, hf, hf,
						 	  hf, hf, hf, hf, hf, hf,
						 	  hf, hf, hf, hf, hf, hf,
						 	  hf, hf, hf, hf, hf, hf,
						 	  hf, hf, hf, hf, hf, hf};	
		
		
		//Put the maps together and add them to the array of possible maps
		city = new Map(frame, g2d, cityTiles, 40, 40, "city");
		maps[0] = city;
		cityO = new Map(frame, g2d, cityOTiles, 40, 40, "cityO");
		maps[1] = cityO;
		houses = new Map(frame, g2d, houseTiles, 6, 6, "houses");
		maps[2] = houses;
		housesO = new Map(frame, g2d, houseOTiles, 6, 6, "housesO");
		maps[3] = housesO;
		
		//Put together all items (Dont forget to add these to the count and setup methods in inGameMenu.java)
		potion = new Item(frame, g2d, extras2, 2, "Potion", false);
		potion.setHealItem(25, false, "");
		mpotion = new Item(frame, g2d, extras2, 2, "Mega Potion", false);
		potion.setHealItem(50, false, "");
		
		//Put together all events
		//Warping events
		warp1 = new Event("fromHouse", TYPE.WARP);
		warp1.setWarp("city", "cityO", 200, -50);
		warp2 = new Event("toHouse", TYPE.WARP);
		warp2.setWarp("houses", "housesO", 620, 250);
		
		//Item events
		getPotion = new Event("getPotion", TYPE.ITEM);
		getPotion.setItem(potion);
		getMpotion = new Event("getMpotion", TYPE.ITEM);
		getMpotion.setItem(mpotion);
		
		//Add the events to their specific tiles and maps
		houses.accessTile(5).addEvent(warp1);
		cityO.accessTile(92).addEvent(getPotion);
		cityO.accessTile(242).addEvent(getPotion);
		cityO.accessTile(328).addEvent(getPotion);
		cityO.accessTile(327).addEvent(getMpotion);
		cityO.accessTile(326).addEvent(getMpotion);
		cityO.accessTile(325).addEvent(getMpotion);
		cityO.accessTile(93).addEvent(getMpotion);
		cityO.accessTile(94).addEvent(getMpotion);
		cityO.accessTile(95).addEvent(getMpotion);
		cityO.accessTile(96).addEvent(getMpotion);
		
		//Set up Monsters and NPCs
		npc = new Mob(frame, g2d, mainCharacter, 40, TYPE.RANDOMPATH, "npc", false);
		npc.setMultBounds(6, 50, 92, 37, 88, 62, 92, 62, 96);
		npc.setMoveAnim(32, 48, 40, 56, 3, 8);
		npc.setHealth(60);
		
		//Add the mobs to their tile home
		cityO.accessTile(98).addMob(npc);
	}
	
	/************************************************************
	 * Get a map back  based on its index in the array of maps
	 * 
	 * @param index - Position in the maps array
	 * @return - Map
	 *************************************************************/
	public Map getMap(int index) {
		return maps[index];
	}
}