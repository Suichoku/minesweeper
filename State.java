package minesweeper;

/**
 * States that tile can have
 * <li>{@link #REVEALED}</li>
 * <li>{@link #HIDDEN}</li>
 * <li>{@link #FLAGGED}</li>
 * <li>{@link #NONE}</li>
 */
public enum State {
	/**
	 * Tile is revealed: it shows how many tiles surround it
	 */
	REVEALED,
	/**
	 * Tile is hidden: it is at its initial game state
	 */
	HIDDEN,
	/**
	 * Tile is flagged: player thinks that tile contains a mine
	 */
	FLAGGED,
	/**
	 * Tile doesn't have set state: it has its default value
	 */
	NONE
}