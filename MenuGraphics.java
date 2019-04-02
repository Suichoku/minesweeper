package minesweeper;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Renders menu related graphics to PApplet frame.
 * 
 * @author Tuomas Rautanen
 */
public class MenuGraphics {

	private PApplet p; // parent PApplet which we draw stuff into
	private ArrayList<Button> buttons; // List of buttons in menu
	private int tSize; 
	
	/**
	 * Renders menu related graphics to PApplet frame.
	 * 
	 * @param p PApplet frame where graphics are drawn to
	 * 
	 * @see <a href="https://processing.org/">3rd party library used for graphics</a>
	 */
	public MenuGraphics(PApplet p) {
		
		this.p = p;
		this.buttons = new ArrayList<Button>();
	}
	/**
	 * Check if button is located at cursor location, return it's index in list of buttons.
	 * 
	 * @param x mouse x-coordinate
	 * @param y mouse y-coordinate
	 * 
	 * @return button index at list, if none found return -1
	 * 
	 * @see Button
	 */
	public int buttonFound(int x, int y) {
		
		for(int i = 0; i < buttons.size(); i++) {
			
			if(PApplet.dist(x, y, buttons.get(i).getPos().x, buttons.get(i).getPos().y) * 2 <= buttons.get(i).getRadius()) {
				
				return i;
			}
		}
		
		return -1;
	}
	/**
	 * Draw buttons to screen
	 * 
	 * @see Button
	 */
	public void drawButtons() {
		
		for(Button el : buttons) {
			
			int[] color = el.getColor(); // get buttons color
			
			p.strokeWeight(2); // set outer stroke lenght
			
			// decide what what fill setting to use based in length of color array
			// more info about this at: https://processing.org/reference/fill_.html
			if(color.length == 1) {
				
				p.stroke((int)(color[0] * 0.8));
				p.fill(color[0]);
			}
			else if(color.length == 2) {
				
				p.stroke((int)(color[0] * 0.8), color[1]);
				p.fill(color[0], color[1]);
			}
			else if(color.length == 3) {
				
				p.stroke((int)(color[0] * 0.8), (int)(color[1] * 0.8), (int)(color[2] * 0.8));
				p.fill(color[0], color[1], color[2]);
			}
			else if(color.length == 4) {
				
				p.stroke((int)(color[0] * 0.8), (int)(color[1] * 0.8), (int)(color[2] * 0.8), color[3]);
				p.fill(color[0], color[1], color[2], color[3]);
			}
			// color array size doesn't match any options -> set color to white
			else {
				
				System.err.println("Too many values for color, color accepts 1-4 values.");
				System.err.println("Setting value to default value of 255 (white).");
				p.fill(255); // set color to white
			}
			
			p.ellipseMode(PConstants.CENTER); // set how ellipse is drawn
			p.ellipse(el.getPos().x, el.getPos().y, el.getRadius(), el.getRadius()); // draw button as ellipse with previously determined color
			
			p.textAlign(PConstants.CENTER, PConstants.CENTER); // set text align
			p.textSize((int)(el.getRadius() * 0.6)); // set text size
			p.noStroke(); // no outer stroke for text
			p.fill(255); // text color as RGB
			p.text(el.getText(), el.getPos().x, el.getPos().y - (int)(el.getRadius()* 0.09)); // draw text button's text on top of the ellipse
		}
	}
	/**
	 * Draw text to screen at given position and with given color
	 * 
	 * @param text text to be drawn
	 * @param pos position of the text in pixels
	 * @param color color of the text
	 */
	public void drawText(String text, int[] pos, int[] color) {
		
		// decide what what fill setting to use based in length of color array
		// more info about this at: https://processing.org/reference/fill_.html
		if(color.length == 1) {
			
			p.fill(color[0]);
		}
		else if(color.length == 2) {
			
			p.fill(color[0], color[1]);
		}
		else if(color.length == 3) {
			
			p.fill(color[0], color[1], color[2]);
		}
		else if(color.length == 4) {
			
			p.fill(color[0], color[1], color[2], color[3]);
		}
		// color array size doesn't match any options -> set color to white
		else {
			
			System.err.println("Too many values for color, color accepts 1-4 values.");
			System.err.println("Setting value to default value of 255 (white).");
			p.fill(255);
		}
		
		p.textAlign(PConstants.LEFT, PConstants.CENTER); // set text align
		p.textSize(tSize); // set text size
		p.noStroke(); // no outer stroke for text
		p.text(text, pos[0], pos[1], pos[2], pos[3]); // draw text at given position with previously determined color
		
	}
	/**
	 * Add button to list of buttons
	 * 
	 * @param b button to be added
	 * 
	 * @see Button
	 */
	public void addButton(Button b) {
		
		this.buttons.add(b);
	}
	/**
	 * Get button at given index from list of buttons
	 * 
	 * @param i index of button in list
	 * 
	 * @return button at given index in list
	 * 
	 * @see Button
	 */
	public Button getButton(int i) {
	
		return this.buttons.get(i);
	}
	/**
	 * Modify button at given index
	 * 
	 * @param i index of button in list
	 * @param b modified button
	 * 
	 * @see Button
	 */
	public void setButton(int i, Button b) {
		
		this.buttons.set(i, b);
	}
	/**
	 * Get text size
	 * 
	 * @return text size
	 */
	public int getTextSize() {
		return tSize;
	}
	/**
	 * Set text size
	 * 
	 * @param tSize text size
	 */
	public void setTextSize(int tSize) {
		this.tSize = tSize;
	}
	/**
	 * Get list of all buttons
	 * 
	 * @return ArrayList containing all buttons
	 * 
	 * @see Button
	 */
	public ArrayList<Button> getButtons() {
		return buttons;
	}
	/**
	 * Set list of all buttons, useful for modification
	 * 
	 * @param buttons ArrayList containing all buttons
	 * 
	 * @see Button
	 */
	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}
	
}