package minesweeper;

import java.awt.Point;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Renders tile related graphics to PApplet frame
 * 
 * @author Tuomas Rautanen
 */
public class TileGraphics {
	
	private PApplet p; // parent PApplet which we draw stuff into
	private int xOff; // x offset in pixels
	private int yOff; // y offset in pixels
	private int tSize; // text size
	
	/**
	 * Renders tile related graphics to PApplet frame
	 * 
	 * @param p PApplet frame where graphics are drawn to
	 * @param padding padding around the board in pixels
	 * 
	 * @see <a href="https://processing.org/">3rd party library used for graphics</a>
	 */
	public TileGraphics(PApplet p, int[] padding) {
		
		this.p = p;
		this.xOff = padding[2];
		this.yOff = padding[0];
	}
	/**
	 * Draw value of tile to the center of the given tile (value=surround value or mine symbol)
	 * 
	 * @param t tile which information is drawn
	 * 
	 * @see Tile
	 */
	public void drawValue(Tile t) {
		
		Point tPos = t.getPosition(); // position of tile in columns / rows
		Point tRes = t.getResolution(); // width and height of tile in pixels
		
		float xPos = tPos.x * tRes.x + tRes.x/(float)3.5; // base x-coordinate of text
		float yPos = tPos.y * tRes.y + tRes.y/(float)10.0; // base y-coordinate of text
		
		p.textAlign(PConstants.LEFT, PConstants.TOP); // text alignment
		p.textSize(tSize); // size of the font
		p.noStroke(); // no outer stroke
		
		if(!t.isMine()) { // tile isn't a mine -> draw surround value
			if(t.getSurround() != 0) { // if tile has surrounding mines -> draw surround value
				int[] cVals = this.getColor(t.getSurround()); // get color based on surround value
				int c = p.color(cVals[0], cVals[1], cVals[2]); // value as color type
				
				p.fill(c); // color of the text
				p.text(Integer.toString(t.getSurround()), xPos + xOff, yPos + yOff); // draw surround value as text
			}
		} else {
			
			p.fill(255, 22, 22); // color of mine symbol
			p.text("X", xPos + xOff, yPos + yOff); // draw mine symbol as text
		}
	}
	
	/**
	 * Draw given tile based on its state
	 * 
	 * @param t tile which is drawn
	 * @throws CustomException tile hasn't been initialised (State is {@link State#NONE})
	 * 
	 * @see Tile
	 * @see State
	 */
	public void drawTile(Tile t) throws CustomException {
		
		Point tPos = t.getPosition(); // position of tile in columns / rows
		Point tRes = t.getResolution(); // width and height of tile in pixels
		
		float xPos = tPos.x * tRes.x + (float)0.01; // base x-coordinate of tile
		float yPos = tPos.y * tRes.y + (float)0.01; // base y-coordinate of tile
		
		p.strokeWeight(1); // outer stroke width in pixels
		p.stroke(0); // outer stroke color in RGB (0,0,0)
		
		if(t.getState() == State.FLAGGED) { // if tile is flagged
			
			p.fill(200,0,10); // RGB color of tile
			p.rect(xPos + xOff,  yPos + yOff, tRes.x - (float)0.02, tRes.y - (float)0.02); // draw tile as rectangle
		}
		else if(t.getState() == State.HIDDEN) { // if tile is hidden
			
			p.fill(220); // RGB color of tile (220,220,220)
			p.rect(xPos + xOff, yPos + yOff, tRes.x - (float)0.02, tRes.y - (float)0.02); // draw tile as rectangle
		}
		else if(t.getState() == State.REVEALED && !t.isMine()) { // if tile is revealed but isn't a mine
			
			p.fill(20); // RGB color of tile (20,20,20)
			p.rect(xPos + xOff, yPos + yOff, tRes.x - (float)0.02, tRes.y - (float)0.02); // draw tile as rectangle
			this.drawValue(t); // draw text value on top of the tile
		} 
		else if(t.getState() == State.REVEALED && t.isMine()) { // if tile is revealed and is mine
			
			p.fill(40, 8, 1); // RGB color of tile
			p.rect(xPos + xOff, yPos + yOff, tRes.x - (float)0.02, tRes.y - (float)0.02); // draw tile as rectangle
			this.drawValue(t); // draw mine symbol on top of the tile
		} 
		else {
			
			throw(new CustomException("Tile incorrectly initialized", ExType.TILESTATE)); // throw exception because tile isn't initialised correctly
		}
	}
	/**
	 * Get RGB value based on amount of surrounding mines
	 * 
	 * @param value amount of mines
	 * @return RGB color as array
	 */
	public int[] getColor(int value) {
		
		switch(value) { // check color value based on amount of mines
			case 1:  return new int[] {88, 218, 244};
			case 2:  return new int[] {87, 244, 197};
			case 3:  return new int[] {87, 244, 134};
			case 4:  return new int[] {141, 239, 71};
			case 5:  return new int[] {213, 239, 71};
			case 6:  return new int[] {239, 219, 71};
			case 7:  return new int[] {242, 184, 48};
			default: return new int[] {241, 138, 47};
		}
	}
	/**
	 * Get current text size
	 * 
	 * @return text size
	 */
	public int getTextSize() {
		return tSize;
	}
	/**
	 * Set current text size
	 * 
	 * @param tSize text size
	 */
	public void setTextSize(int tSize) {
		this.tSize = tSize;
	}
	/**
	 * Get x-offset caused by padding
	 * 
	 * @return x-offset in pixels
	 */
	public int getxOff() {
		return xOff;
	}
	/**
	 * Set x-offset caused by padding
	 * 
	 * @param x-offset in pixels
	 */
	public void setxOff(int xOff) {
		this.xOff = xOff;
	}
	/**
	 * Get y-offset caused by padding
	 * 
	 * @return y-offset in pixels
	 */
	public int getyOff() {
		return yOff;
	}
	/**
	 * Set y-offset caused by padding
	 * 
	 * @param y-offset in pixels
	 */
	public void setyOff(int yOff) {
		this.yOff = yOff;
	}
	
}