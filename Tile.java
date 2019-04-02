package minesweeper;

import java.awt.Point;

/**
 * Tile object that has information of given tile in gameboard.
 * 
 * @author Tuomas Rautanen
 */
public class Tile {
	
	private Point position; // position of tile in board (column, row)
	private Point resolution; // width and height of tile in pixels
	private int surround = -1; // how many mines surround tile
	private boolean mine = false; // is tile a mine
	private State state = State.NONE; // current state of tile
	
	/**
	 * Tile object that has information of given tile in gameboard.
	 * 
	 * @param position position of tile in board (column, row)
	 * @param resolution width and height of tile in pixels
	 * 
	 * @see Board
	 */
	public Tile(Point position, Point resolution) {
		
		this.resolution = resolution;
		this.position = position;
	}
	/**
	 * Get position of tile in board (column, row).
	 * 
	 * @return position of tile in board
	 * 
	 * @see Board
	 */
	public Point getPosition() {
		
		return position;
	}
	/**
	 * Set position of given tile in (column, row).
	 * 
	 * @param x column where tile is in board
	 * @param y row where tile is in board
	 * 
	 * @see Board
	 */
	public void setPosition(int x, int y) {
		
		this.position = new Point(x,y);
	}
	/**
	 * Get how many mines surround the tile.
	 * 
	 * @return amount of mines
	 */
	public int getSurround() {
		
		return surround;
	}
	/**
	 * Set how many mines surround the tile.
	 * 
	 * @param surround amount of mines
	 */
	public void setSurround(int surround) {
		
		this.surround = surround;
	}
	/**
	 * Check if tile contains a mine.
	 * 
	 * @return does tile contain a mine
	 */
	public boolean isMine() {
		
		return mine;
	}
	/**
	 * Set if tile contains a mine.
	 * 
	 * @param mine does tile contain a mine
	 */
	public void setMine(boolean mine) {
		
		this.mine = mine;
	}
	/**
	 * Get state of tile.
	 * 
	 * @return state of tile
	 * 
	 * @see State
	 */
	public State getState() {
		
		return this.state;
	}
	/**
	 * Set state of tile.
	 * 
	 * @param state state of tile
	 * 
	 * @see State
	 */
	public void setState(State state) {
		
		this.state = state;
	}
	/**
	 * Get resolution of tile in pixels.
	 * 
	 * @return width and height of tile in pixels
	 */
	public Point getResolution() {
		
		return resolution;
	}
	/**
	 * Set resolution of tile in pixels.
	 * 
	 * @param resolution width and height of tile in pixels
	 */
	public void setResolution(Point resolution) {
		
		this.resolution = resolution;
	}
}