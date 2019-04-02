package minesweeper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * Game board object that contains all the tiles for current game.
 * 
 * @author Tuomas Rautanen
 */
public class Board {
	
	private int mines; // mine count in game board
	private Tile[][] tiles; // tiles array containing all tiles in game board
	private Point boardSize; // boardSize board width and height in tiles (columns, rows)
	private Point resolution; // resolution tile width and height in pixels
	private int[] padding; // padding around the board in pixels
	
	/**
	 * Game board object that contains all the tiles for current game.
	 * 
	 * @param resolution tile width and height in pixels
	 * @param boardSize board width and height in tiles (columns, rows)
	 * @param mines mine count in game board
	 * @param padding padding around the board in pixels
	 * 
	 * @see Tile
	 */
	public Board(Point resolution, Point boardSize, int mines, int[] padding) {
		
		this.resolution = resolution;
		this.boardSize = boardSize;
		this.padding = padding;
		
		if(mines < (boardSize.x * boardSize.y)) { // check mine count is less than amount of tiles
			
			this.mines = mines;
			
		} else { // if mine count exceeds tile count, set mine count to 90% of tiles
			
			System.err.println("Mine count exceeded tile count >> Set: Mine count = 0.9 * tile count");
			this.mines = (int)(0.9 * boardSize.x * boardSize.y);
		}
		
	}
	
	/**
	 * Creates board full of tiles and randomizes mine locations in the given board.
	 * 
	 * @param tileWidth width of the created tiles in pixels
	 * @param tileHeight height of the created tiles in pixels
	 * 
	 * @see Tile
	 */
	public void randomize(int tileWidth, int tileHeight) {
		
		Point size = this.getBoardSize(); // get board width and height
		
		// INITIALIZE BOARD
		Tile[][] tileTemp = new Tile[size.y][size.x]; // create array of tiles based on board size
		
		for(int y = 0; y < size.y; y++) { // Initialise tiles array
			for(int x = 0; x < size.x; x++) {
				
				Tile temp = new Tile(new Point(x, y), new Point(tileWidth,tileHeight)); // create blank tile in position (x,y) with resolution (tileWidth,tileHeight)
				
				temp.setState(State.HIDDEN); // initialise tile as hidden
				tileTemp[y][x] = temp;			
			}
		}
		// RANDOMIZE MINE LOCATIONS
		ArrayList<Integer> mineLocations = new ArrayList<Integer>(); // Create empty list where mine locations will be stored
		
		while(mineLocations.size() < mines) { // fill the list to contain all mine locations
			
			int pos = ThreadLocalRandom.current().nextInt(0, size.x * size.y - 1); // randomize location of mine
			
			if(!mineLocations.contains(pos)) { // check if mine is located at that position already -> true: skip, false: add the position
				
				mineLocations.add(pos);
			}
		}
		// PLACE MINES
		for(int index : mineLocations) { // set tiles at mine locations to have mines
			
			tileTemp[(int)(index / size.x)][index % size.x].setMine(true); // set tile as mine at (x,y) position
		}
		
		tiles = tileTemp; // save tiles into boards tile array
	}
	
	/**
	 * Check if player has won the current game.
	 * 
	 * @return has player won the game
	 */
	public boolean gameWin() {
		
		Tile[] temp = Stream.of(tiles).flatMap(Stream::of).toArray(Tile[]::new); // map 2D array of tiles to 1D array
		return Arrays.stream(temp).noneMatch(t -> t.getState() == State.HIDDEN) && // check that none of the tiles remain hidden
			   Arrays.stream(temp).noneMatch(t -> t.getState() == State.FLAGGED && !t.isMine()); // check that player hasn't cheated by flagging all tiles (even those that don't contain mines)
	}
	
	/** 
	 * Checks for how many mines surround tile at given position
	 * 
	 * @return count of mines surrounding the given tile
	 * 
	 * @see Tile
	 * **/
	public int findSurrounding(int x, int y) {
		
		int count = 0; // holds mine count
		
		for(int i = -1; i <= 1; i++) { // loop surrounding tiles to check if they are mines
			for(int j = -1; j <= 1; j++) {
				
				// checks if tile isn't outside of the board and it isn't the center tile
				if(!(i==0 && j==0) && (y+i >= 0 && y+i < this.getBoardSize().y) && (x+j >= 0 && x+j < this.getBoardSize().x)) {
					
					if(tiles[y+i][x+j].isMine()) count++; // increase mine count if tile was a mine
				}
			}
		}
		
		return count;
	}
	/** 
	 * Reveals all neighbouring empty tiles.
	 * 
	 * @param x x-coordinate of Tile
	 * @param y y-coordinate of Tile
	 * 
	 * @see <a href="https://en.wikipedia.org/wiki/Flood_fill">Source for algorithm used (Forest Fire algorithm)</a>
	 * @see Tile
	 * @see State
	 * **/
	public void revealEmpty(int x, int y) {
		
		tiles[y][x].setState(State.REVEALED); // set initial Tile as Revealed
		
		ArrayList<Tile> queue = new ArrayList<Tile>(); // create queue for tracking what Tiles have been visited
		queue.add(tiles[y][x]);
		
		while(!queue.isEmpty()) { // loop until no connected empty Tiles remain
			
			Tile temp = queue.get(0);
			queue.remove(0); // remove this Tile from queue (it is now "visited")
			
			for(int i = -1; i <= 1; i++) { // loop all surrounding tiles
				for(int j = -1; j <= 1; j++) {
					
					// check for vertical neighbours
					
					// checks if tile isn't outside of the board and it isn't the center tile
					if(!(i==0) && (temp.getPosition().y+i >= 0 && temp.getPosition().y+i < this.getBoardSize().y)) {
						
						// if tile is hidden and is empty -> reveal it and add it into queue
						if(tiles[temp.getPosition().y+i][temp.getPosition().x].getSurround() == 0 && tiles[temp.getPosition().y+i][temp.getPosition().x].getState() == State.HIDDEN) {
							
							tiles[temp.getPosition().y+i][temp.getPosition().x].setState(State.REVEALED);
							queue.add(tiles[temp.getPosition().y+i][temp.getPosition().x]);
						}
					}
					// check for horizontal neighbours
					
					// checks if tile isn't outside of the board and it isn't the center tile
					if(!(j==0) && (temp.getPosition().x+j >= 0 && temp.getPosition().x+j < this.getBoardSize().x)) {
						
						// if tile is hidden and is empty -> reveal it and add it into queue
						if(tiles[temp.getPosition().y][temp.getPosition().x+j].getSurround() == 0 && tiles[temp.getPosition().y][temp.getPosition().x+j].getState() == State.HIDDEN) {
							
							tiles[temp.getPosition().y][temp.getPosition().x+j].setState(State.REVEALED);
							queue.add(tiles[temp.getPosition().y][temp.getPosition().x+j]);
						}
					}
					// reveal edge Tiles (non empty ones)
					
					// checks if tile isn't outside of the board and it isn't the center tile
					if(!(i==0 && j==0) && (temp.getPosition().y+i >= 0 && temp.getPosition().y+i < this.getBoardSize().y) && (temp.getPosition().x+j >= 0 && temp.getPosition().x+j < this.getBoardSize().x)) {
						
						// checks if tile is hidden and isn't empty -> reveal it
						if(tiles[temp.getPosition().y+i][temp.getPosition().x+j].getSurround() > 0 && tiles[temp.getPosition().y+i][temp.getPosition().x+j].getState() == State.HIDDEN) {
							
							tiles[temp.getPosition().y+i][temp.getPosition().x+j].setState(State.REVEALED);
						}
					}					
				}		
			}	
		}
	}
	
	/** 
	 * Reveal all surrounding tiles at given position, if there is non flagged tile containing a mine return gameover boolean.
	 * (quality of life improvement for speeding up the gameplay)
	 * 
	 * @param x x-coordinate of Tile
	 * @param y y-coordinate of Tile
	 * 
	 * @return has player lost the game
	 * 
	 * @see Tile
	 * @see State
	 * **/
	public boolean revealSurround(int x, int y) {
		
		boolean mines = false; // does tile have non flagged mines around it?
		
		for(int i = -1; i <= 1; i++) { // check if there is any surrounding tiles that were mines and weren't flagged
			for(int j = -1; j <= 1; j++) {
				
				// checks if tile isn't outside of the board and it isn't the center tile
				if(!(i==0 && j==0) && (y+i >= 0 && y+i < this.getBoardSize().y) && (x+j >= 0 && x+j < this.getBoardSize().x)) {
					
					// check if tile is mine and not flagged -> set mines to true
					if(tiles[y+i][x+j].isMine() && tiles[y+i][x+j].getState() != State.FLAGGED) mines = true;
				}
			}
		}
		
		if(!mines) { // if no unflagged mines were found -> reveal all surrounding tiles
			
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					
					// checks if tile isn't outside of the board and it isn't the center tile
					if(!(i==0 && j==0) && (y+i >= 0 && y+i < this.getBoardSize().y) && (x+j >= 0 && x+j < this.getBoardSize().x)) {
						
						// reveal tile if it isn't flagged
						if(tiles[y+i][x+j].getState() != State.FLAGGED) this.setTileState(x+j, y+i, State.REVEALED);
						if(tiles[y+i][x+j].getSurround() == 0) this.revealEmpty(x+j, y+i);
					}
				}
			}
			
			return true; // Return true to indicate that no gameover happened
			
		} else {
			
			return false; // Return false to indicate that gameover happened
		}
	}
	
	/** 
	 * Flag all surrounding tiles at given position if surrounding hidden tiles plus flagged tiles equal to tiles surround value.
	 * (quality of life improvement for speeding up the gameplay)
	 * 
	 * @param x x-coordinate of Tile
	 * @param y y-coordinate of Tile
	 * 
	 * @see Tile
	 * @see State
	 * **/
	public void flagSurround(int x, int y) {
		
		int mines = this.getTile(x, y).getSurround(); // how many tiles surround tile at given position
		int count = 0;
		
		for(int i = -1; i <= 1; i++) { // counts how many hidden tiles and flagged tiles surround given tile
			for(int j = -1; j <= 1; j++) {
				
				// checks if tile isn't outside of the board and it isn't the center tile
				if(!(i==0 && j==0) && (y+i >= 0 && y+i < this.getBoardSize().y) && (x+j >= 0 && x+j < this.getBoardSize().x)) {
					
					if(tiles[y+i][x+j].getState().equals(State.HIDDEN) || tiles[y+i][x+j].getState().equals(State.FLAGGED)) count++;
				}
			}
		}
		if(count == mines) { // flag tiles only if count of hidden tiles + flagged tiles is equal to mine amount
			
			for(int i = -1; i <= 1; i++) { // loop all surrounding tiles
				for(int j = -1; j <= 1; j++) {
					
					// checks if tile isn't outside of the board and it isn't the center tile
					if(!(i==0 && j==0) && (y+i >= 0 && y+i < this.getBoardSize().y) && (x+j >= 0 && x+j < this.getBoardSize().x)) {
						
						if(!tiles[y+i][x+j].getState().equals(State.REVEALED)) { // flag tile if it isn't revealed
							
							this.setTileState(x+j, y+i, State.FLAGGED);
						}
					}
				}
			}
		}	
	}
	/**
	 * Check and set how many mines surround each Tile in gameboard.
	 * 
	 * @see Tile
	 */
	public void setSurroundAll() {
		
		for(Tile[] outer : tiles) { // loop all elements in tiles
			for(Tile inner : outer) {
				
				if(!inner.isMine()) { // if tile isn't a mine -> call setSurround to set how many mines surround given element
					
					inner.setSurround(this.findSurrounding(inner.getPosition().x, inner.getPosition().y));
				}
			}
		}
	}
	/**
	 * Normalize mouse position to match tile positions.
	 * 
	 * @param x mouse x-coordinate
	 * @param y mouse y-coordinate
	 * @return tile location in tiles array
	 */
	public Point normalizePosition(int x, int y) {
		
		int tempX, tempY;
		
		tempX = (int)((x - padding[2])/ this.resolution.x);
		tempY = (int)((y - padding[0])/ this.resolution.y);
		return new Point(tempX, tempY);
	}
	
	/**
	 * Get tile from tiles array at given position.
	 * 
	 * @param x x-coordinate of tile in tiles array (column)
	 * @param y y-coordinate of tile in tiles array (row)
	 * @return tile at given location
	 * 
	 * @see Tile
	 */
	public Tile getTile(int x, int y) {
		
		if(tiles.length > 0 && x < this.getBoardSize().x && y < this.getBoardSize().y) {
			return tiles[y][x];
		} else return null;
	}
	
	/**
	 * Set state of tile of tile in tiles array at given position.
	 * 
	 * @param x x-coordinate of tile in tiles array (column)
	 * @param y y-coordinate of tile in tiles array (row)
	 * @param state state of tile
	 * 
	 * @see Tile
	 * @see State
	 */
	public void setTileState(int x, int y, State state) {
		
		tiles[y][x].setState(state);
	}
	/**
	 * Get how many mines board has.
	 * 
	 * @return amount of mines
	 */
	public int getMines() {
		
		return mines;
	}
	/**
	 * Set how many mines board has.
	 * 
	 * @param mines amount of mines
	 */
	public void setMines(int mines) {
		
		this.mines = mines;
	}
	/**
	 * Get board size in tiles (columns, rows).
	 * 
	 * @return Point object containing how many columns and rows board has
	 * @see Tile
	 */
	public Point getBoardSize() {
		
		return boardSize;
	}
	/**
	 * Set board size in tiles (columns, rows).
	 * 
	 * @param boardSize Point object containing how many columns and rows board has
	 * @see Tile
	 */
	public void setBoardSize(Point boardSize) {
		
		this.boardSize = boardSize;
	}
	/**
	 * Get resolution of tiles in pixels.
	 * 
	 * @return pixel value for width and height of tiles
	 * @see Tile
	 */
	public Point getResolution() {
		
		return resolution;
	}
	/**
	 * Set resolution of tiles in pixels.
	 * 
	 * @param resolution
	 * @see Tile
	 */
	public void setResolution(Point resolution) {
		
		this.resolution = resolution;
	}
	/**
	 * Get all tiles in game board.
	 * 
	 * @return 2D array containing all tiles
	 * @see Tile
	 */
	public Tile[][] getTiles() {
		
		return tiles;
	}
}