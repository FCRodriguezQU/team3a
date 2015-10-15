package axohEngine2.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import axohEngine2.Game;
import axohEngine2.entities.AnimatedSprite;

/****************************************************************************************************
 * @author Travis R. Dewitt
 * @version 1.0
 * Date: June 15, 2015
 *  * 
 * Title: Saving
 * Description: This class is used to correctly serialize objects, specifically the 'Data.java' class
 *  It also creates new files when needed.
 *  
 * This work is licensed under a Attribution-NonCommercial 4.0 International
 * CC BY-NC-ND license. http://creativecommons.org/licenses/by-nc/4.0/
 ****************************************************************************************************/

public class Save {
	
	Game game;
	Data data;
	
	public Save(Data d,Game g){
		game = g;
		data = d;
	}
	public void saveState(int x, int y){
		try {
			File file = new File("Save.csv");
			PrintWriter write = new PrintWriter(file);
			write.println(data.getName() + "," + x + "," + y);
			write.close();
		} catch (FileNotFoundException e) {
			System.out.println("There was trouble saving the game.");
			e.printStackTrace();
		}
	}
}