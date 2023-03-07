package application;

import java.io.File;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Star extends Entity {
	// Constants
	private static final Random RANDOMIZER = new Random();
	private static final String[] STAR_FILES = {
		new File("images/star1.png").toURI().toString(),
		new File("images/star2.png").toURI().toString(),
		new File("images/star3.png").toURI().toString()
	};
	private static final int MINIMUM_SIZE = 2;
	private static final int MAXIMUM_SIZE = 6;
	private static final double MINIMUM_SPEED = 0.1;
	private static final double MAXIMUM_SPEED = 0.5;
	
	// Data Fields
	private Image image;
	
	// Constructor
	public Star(double leftBound, double rightBound) {
		super(new Point2D(leftBound + RANDOMIZER.nextDouble() * (rightBound - leftBound), 0.0), 
			  new Point2D(0.0, RANDOMIZER.nextDouble(MINIMUM_SPEED, MAXIMUM_SPEED)));
		int imageChoice = RANDOMIZER.nextInt(3);
		int imageSize = RANDOMIZER.nextInt(MINIMUM_SIZE, MAXIMUM_SIZE);
		image = new Image(STAR_FILES[imageChoice], imageSize, imageSize, true, true);
	}
	
	// Accessors
	public static int getWidth() { return MAXIMUM_SIZE; }
	public static int getHeight() { return MAXIMUM_SIZE; }
	
	public void shift() {
		position = new Point2D(position.getX(), RANDOMIZER.nextDouble(768.0));
	}
	
	// Bounding Box Method for Collision Testing
	@Override
	protected Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}

	// AABB Collision Testing Method
	@Override
	public boolean isCollidingWith(Entity other) {
		return other.getBoundingBox().intersects(this.getBoundingBox());
	}
	
	//  Draw Method
	public void draw(GraphicsContext context) {
		context.drawImage(image, position.getX(), position.getY());
	}
}
