package minesweeper;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PFont;

/* For documentation check javadocs*/
/**
 * @author Tuomas Rautanen
 * @see <a href="https://processing.org/">3rd party library used for graphics</a>
 */
public class Program extends PApplet {
	
	private enum Difficulty { // enum for game difficulties
		EASY, MEDIUM, HARD
	}
	Difficulty dif; // current difficulty

	TileGraphics tileRenderer; // renders tile related graphics
	MenuGraphics menuRenderer; // renders menu related graphics
	
	PFont font; // font that text uses
	
	RecordMapper records; // tracks records
	HashMap<String, Integer> recordTimes; // records for current runtime
	
	int time; // current processor time
	int gameTime; // tracks when game started
	
	boolean gameover = false; // has player lost game?
	boolean victory = false; // has player won game?
	boolean newgame = true; // has player started a new game?
	
	static Board gameBoard; // gameboard that contains all tiles
	
	int tileColumns; // // number of columns that gameboard has
	int tileRows; // // number of row that gameboard has
	
	int tileWidth; // width of tiles in pixels
	int tileHeight; // height of tiles in pixels
	
	int[] padding = new int[4]; // sets padding for borders in pixels, order [top, bottom, left, right]
	
	int mines; // mine count in current gameboard
	
	
	public static void main(String[] args) {
		
		PApplet.main("minesweeper.Program"); // Tells PApplet to use this class
	}
	
	public void setMineCount(Difficulty d) { // set mine count to match difficulty
		
		switch(dif) {
		case EASY:
			mines = (int)(tileRows * tileColumns * 0.1); // ~10% of tiles are mines
			break;
		case MEDIUM:
			mines = (int)(tileRows * tileColumns * 0.15); // ~15% of tiles are mines
			break;
		case HARD:
			mines = (int)(tileRows * tileColumns * 0.25); // ~25% of tiles are mines
			break;
		}
	}
	
	public void settings() { // screen size settings are done here
		
		tileColumns = 12; // number of columns that gameboard has
		tileRows = 15; // number of rows that gameboard has
		tileWidth = 50; //width of tiles in pixels
		tileHeight = 50; //height of tiles in pixels
		
		//setting paddings
		padding[0] = 60; // top
		padding[1] = 10; // bottom
		padding[2] = 10; // left
		padding[3] = 10; // right
		
		size(tileColumns * tileWidth + padding[2] + padding[3], tileRows * tileHeight + padding[0] + padding[1]); // set screen size	
	}
	
	public void setup() { // things that need to be done once, before game loop starts are done here.
		
		dif = Difficulty.EASY; // Setting game default difficulty
		setMineCount(dif); // set mine count to match difficulty
		records = new RecordMapper(); // tracks change in records and saves new records to file
		recordTimes = new HashMap<String, Integer>(); // track all records in current runtime
		
		gameBoard = new Board(new Point(tileWidth, tileHeight), new Point(tileColumns,tileRows), mines, padding); // create the gameboard with given values
		gameBoard.randomize(gameBoard.getResolution().x, gameBoard.getResolution().y); // Randomize mine locations in the gameboard
		gameBoard.setSurroundAll(); // find how many mines surround every non mine tile and save that information to Tile objects surround property
		
		background(20); // set background color to RGB value (20,20,20)
		
		try {
			
			recordTimes = records.readRecords("recordMapper.ser"); // reads records from file to current runtimes records
			
		} catch (ClassNotFoundException | IOException | NullPointerException e) {
			
			System.err.println("Couldn't read records, setting all records to default value.");
			
			for(Difficulty d : Difficulty.values()) { // set all records to default value of -1
				recordTimes.put(d.toString(), -1);
			}
		}
		
		tileRenderer = new TileGraphics(this, padding); // renderer for tile related graphics, this keyword is for passing the PApplet "frame" (so that engine knows "where" to draw stuff)
		tileRenderer.setTextSize(((tileWidth >= tileHeight) ? (int)(tileWidth * 0.7) : (int)(tileHeight * 0.7))); /// set text size for numbers and mine symbols
		
		menuRenderer = new MenuGraphics(this); // renderer for menu related graphics
		menuRenderer.setTextSize((int)(0.6 * padding[0])); // set text size for records
		
		// add buttons for choosing difficulties (E=Easy, M=Medium, H=Hard)
		menuRenderer.addButton(new Button(new Point((int)(width*0.82), (int)(padding[0]/2)), (int)(padding[0]*0.5), new int[] {0,220,0}, "E"));
		menuRenderer.addButton(new Button(new Point((int)(width*0.89), (int)(padding[0]/2)), (int)(padding[0]*0.5), new int[] {220,180,0}, "M"));
		menuRenderer.addButton(new Button(new Point((int)(width*0.96), (int)(padding[0]/2)), (int)(padding[0]*0.5), new int[] {220,0,0}, "H"));
	}
	
	public void draw() { // game loop (similar to while(true))
		
		background(20); // refreshes the screen with empty background
		
		menuRenderer.drawButtons(); // draw difficulty buttons
		
		int[] colorTemp; // set color for record text to indicate what difficulty is selected
		if(dif == Difficulty.EASY) {
			colorTemp = new int[] {100, 255, 100}; // RGB value (100,255,100)
		}
		else if(dif == Difficulty.MEDIUM) {
			colorTemp = new int[] {255, 200, 100}; // RGB value (255,200,100)
		}
		else {
			colorTemp = new int[] {255, 100, 100}; // RGB value (255,100,100)
		}
		
		if(recordTimes.get(dif.toString()) > 0) { // show selected difficulty's record
			
			int temp = recordTimes.get(dif.toString()); // get record time for selected difficulty
			int[] timeFormat = new int[3]; // holds minutes,seconds
			timeFormat[0] = (int)(temp / 60000); // convert milliseconds to minutes
			temp -= timeFormat[0] * 60000; // calculate remaining milliseconds
			timeFormat[1] = (int)(temp /  1000); // convert remaining milliseconds to seconds
			temp -= timeFormat[1] * 1000; // calculate remaining milliseconds
			
			// show selected difficulty's record time as: Record | minutes : seconds : milliseconds
			menuRenderer.drawText("Record | " + timeFormat[0] + " : " + timeFormat[1] + " : " + temp , new int[] {padding[2], 0, width - padding[3], padding[0]}, colorTemp);
		} 
		else { // show empty record as: Record |
			
			if(padding[2] <= 5) { // set minimum padding from left side (ie. if left side padding is under 5, set padding to 5)
				
				menuRenderer.drawText("Record | ", new int[] {5, 0, width - padding[3], padding[0]}, colorTemp);
			} 
			else { // set text left padding as given in settings
				
				menuRenderer.drawText("Record | ", new int[] {padding[2], 0, width - padding[3], padding[0]}, colorTemp);
			}
		}
				
		if(!gameover && !victory) { // if game is still running
			
			for(Tile[] outer : gameBoard.getTiles()) { // draw all tiles
				
				for(Tile inner : outer) {
					
					try {
						
						tileRenderer.drawTile(inner); // draw a tile
					} 
					catch (CustomException e) {
						
						e.printStackTrace();				
						if(e.getType() == ExType.TILESTATE) {	
							System.err.println("Error with tile state initialization");
						}
					}
				}
			}
		} 
		else { // if game is over (victory or defeat)
			
			for(Tile[] outer : gameBoard.getTiles()) { // show all mines
				
				for(Tile inner : outer) {
					
					try {
						
						if(inner.isMine()) inner.setState(State.REVEALED); // reveal tile if it is a mine
						
						tileRenderer.drawTile(inner); // draw a tile
					} 
					
					catch (CustomException e) {
						
						e.printStackTrace();
						if(e.getType() == ExType.TILESTATE) {	
							
							System.err.println("Error with tile state initialization");
						}
					}
				}
			}
			if(millis() - time >= 2000) { // wait 2 seconds before starting new game
				
				gameBoard.randomize(gameBoard.getResolution().x, gameBoard.getResolution().y); // Randomize mine locations in the gameboard
				gameBoard.setSurroundAll(); // find how many mines surround every non mine tile and save that information to Tile objects surround property
				
				//set booleans to indicate new game
				gameover = false;
				victory = false;
				newgame = true;
			}
		}
	}
	
	public void mousePressed() { // happens if mouse button is pressed (doesn't matter which one)
		
		if(newgame) { // start tracking time for new game
			
			gameTime = millis(); // set current processor millisecond time
			newgame = false; // set boolean to indicate that game has started
		}
		// see if mouse is clicked outside the gameboard (ie. buttons)
		if(mouseX < padding[2] || mouseX > gameBoard.getBoardSize().x * gameBoard.getResolution().x || mouseY < padding[0] || mouseY > gameBoard.getBoardSize().y * gameBoard.getResolution().y + padding[0]) {
			
			if(mouseButton == LEFT && menuRenderer.buttonFound(mouseX, mouseY) > -1) { // see if button was clicked
				
				int buttonIndex = menuRenderer.buttonFound(mouseX, mouseY); // check what button was clicked
				
				if(buttonIndex == 0) { // button for changing difficulty to EASY was clicked
					
					dif = Difficulty.EASY; // set game difficulty to EASY
				}
				else if(buttonIndex == 1) { // button for changing difficulty to MEDIUM was clicked
					
					dif = Difficulty.MEDIUM; // set game difficulty to MEDIUM
				}
				else if(buttonIndex == 2) { // button for changing difficulty to HARD was clicked
					
					dif = Difficulty.HARD; // set game difficulty to HARD
				}
				
				setMineCount(dif); // set mine count to match difficulty
				
				// restart game with new difficulty (or restart with same if same difficulty was selected)
				gameBoard.setMines(mines); // change mine count based to difficulty
				gameBoard.randomize(gameBoard.getResolution().x, gameBoard.getResolution().y); // Randomize mine locations in the gameboard
				gameBoard.setSurroundAll(); // find how many mines surround every non mine tile and save that information to Tile objects surround property
				
				//set booleans to indicate new game
				gameover = false;
				victory = false;
				newgame = true;
			}
		}
		else { // if click was within gameboard
			
			if(!gameover && !victory) { // if game is still running
				
				Point temp = gameBoard.normalizePosition(mouseX, mouseY); // normalize mouse position to match tile positions
				
				if(mouseButton == LEFT) { // check if mouse button pressed was left button
					
					if(gameBoard.getTile(temp.x, temp.y).getState() == State.REVEALED) { // check if tile was already revealed -> call quality of life feature to reveal surrounding tiles
						
						if(gameBoard.revealSurround(temp.x, temp.y)) { // check if any of the surrounding tiles were non flagged mines -> True if none were
							
							gameBoard.revealSurround(temp.x, temp.y); // reveal non-flagged surrounding tiles
						} 
						else { // if any of the tiles revealed were non-flagged mines
							
							time = millis(); // save current time to know when 2 seconds is passed
							gameover = true; // set boolean to indicate gameover to start new game
						}
						
					} else if(gameBoard.getTile(temp.x, temp.y).isMine()) { // check if clicked tile was a mine
						
						time = millis(); // save current time to know when 2 seconds is passed
						gameover = true; // set boolean to indicate gameover
						
					} else {
						
						if(gameBoard.getTile(temp.x, temp.y).getSurround() != 0) { // see if tile has any surrounding mines
							
							gameBoard.setTileState(temp.x, temp.y, State.REVEALED); // reveal clicked tile
							
						} else { // tile has no surrounding mines
							
							gameBoard.revealEmpty(temp.x, temp.y); // reveal all neighbouring tiles that don't have any surrounding mines
						}
					}
				} 
				else if (mouseButton == RIGHT) { // check if mouse button pressed was right button
					
					if(gameBoard.getTile(temp.x, temp.y).getState() == State.REVEALED) { // check if tile was already revealed -> call quality of life feature to flag surrounding hidden tiles
						
						gameBoard.flagSurround(temp.x, temp.y); //  flag all surrounding hidden tiles if hidden tile count equals to clicked tiles surround property
						
					} else if(gameBoard.getTile(temp.x, temp.y).getState() == State.FLAGGED) { // Toggle flag: Flagged -> Hidden
						
						gameBoard.setTileState(temp.x, temp.y, State.HIDDEN); // set tile state to hidden
						
					} else if (gameBoard.getTile(temp.x, temp.y).getState() == State.HIDDEN) { // Toggle flag: Hidden -> Flagged
						
						gameBoard.setTileState(temp.x, temp.y, State.FLAGGED); // set tile state to flagged
					}
				}	
			}
		}	
		
		if(gameBoard.gameWin()) { // check if player won the game
			
			time = millis(); // save processor time for calculating game time
			
			if(recordTimes.get(dif.toString()) > time - gameTime || recordTimes.get(dif.toString()) < 0) { // check if time is new record or first completed game
				
				recordTimes.put(dif.toString(), time - gameTime); // add record to runtime's list of records
				records.writeRecords(recordTimes, "recordMapper.ser"); // save record to file containing all records
			}
			victory = true; // set boolean to indicate victory, so that new game can start
		}
	}
}