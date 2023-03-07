package application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Missile extends Entity {
	// Constants
	private static final double DEFAULT_SPEED = -1.0;
	private static final int WIDTH = 15;
	private static final int HEIGHT = 60;
	
	// Data Fields
	private static Image image;
	
	// Constructor
	public Missile(Point2D position) {
		super(position, new Point2D(0.0, DEFAULT_SPEED));
	}

	// Accessors
	public static int getWidth() { return WIDTH; }
	public static int getHeight() { return HEIGHT; }
	
	// Asset Mutator
	public static void setImage(Image img) { image = img; }
	
	// Bounding Box Method for Collision Testing
	protected Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}
	
	// AABB Collision Testing Method
	public boolean isCollidingWith(Entity other) {
		return other.getBoundingBox().intersects(this.getBoundingBox());
	}
	
	//  Draw Method
	public void draw(GraphicsContext context) {
		context.drawImage(image, position.getX(), position.getY());
	}
}
