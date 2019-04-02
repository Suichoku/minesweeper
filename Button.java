package minesweeper;

import java.awt.Point;

/**
 * Holds information for menu element - Button
 * 
 * @author Tuomas Rautanen
 */
public class Button {

	private Point pos; // position of button in pixels (x,y)
	private int radius; // radius of the button in pixels
	private int[] color; // color of the button in RGB
	private String text; // button label text
	
	/**
	 * Holds information for menu element - Button
	 * 
	 * @param pos position of button in pixels (x,y)
	 * @param radius radius of the button in pixels
	 * @param color color of the button in RGB
	 * @param text button label text
	 * 
	 * @see MenuGraphics
	 */
	public Button(Point pos, int radius, int[] color, String text) {
		this.pos = pos;
		this.radius = radius;
		this.color = color;
		this.text = text;
	}
	/**
	 * Get position of the button in pixels (x,y)
	 * 
	 * @return position of the button
	 */
	public Point getPos() {
		return pos;
	}
	/**
	 * Set position of the button in pixels (x,y)
	 * 
	 * @param pos position of the button
	 */
	public void setPos(Point pos) {
		this.pos = pos;
	}
	/**
	 * Get radius of the button
	 * 
	 * @return radius of the button
	 */
	public int getRadius() {
		return radius;
	}
	/**
	 * Set radius of the button
	 * 
	 * @param radius radius of the button
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}
	/**
	 * Get button's color in RGB
	 * 
	 * @return button's color as RGB
	 */
	public int[] getColor() {
		return color;
	}
	/**
	 * Set button's color in RGB
	 * 
	 * @param color button's color as RGB
	 */
	public void setColor(int[] color) {
		this.color = color;
	}
	/**
	 * Get button's label text
	 * 
	 * @return button's label text
	 */
	public String getText() {
		return text;
	}
	/**
	 * Set button's label text
	 * 
	 * @param button's label text
	 */
	public void setText(String text) {
		this.text = text;
	}
}
