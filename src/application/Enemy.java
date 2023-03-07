package application;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Enemy extends Entity {
	// Constants
	private static final Random RANDOMIZER = new Random();
	private final int SCORE;
	private static final int SCORE_MULTIPLIER = 100;
	private static final int SIZE = 75;
	
	// Data Fields
	private static Image image;
	private static AudioClip deathSFX;
	
	// Constructor
	public Enemy(double leftBound, double rightBound, Point2D speed) {
		super(new Point2D(leftBound + RANDOMIZER.nextDouble() * (rightBound - leftBound),
				          0.0 - (RANDOMIZER.nextDouble(3.0) * SIZE)),
			  speed);
		this.SCORE = (int)(speed.getY() * SCORE_MULTIPLIER);
	}

	// Accessors
	public static int getWidth() { return SIZE; }
	public static int getHeight() { return SIZE; }
	public int getScore() { return SCORE; }
	public static int getSize() { return SIZE; }
	
	// Asset Mutators
	public static void setImage(Image img) { image = img; }
	public static void setExplodeSound(AudioClip sound) { deathSFX = sound; }
	
	// Bounding Box Method for Collision Testing
	protected Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight());
	}
	
	// AABB Collision Testing Method
	public boolean isCollidingWith(Entity other) {
		return other.getBoundingBox().intersects(this.getBoundingBox());
	}
	
	// Overriden Kill Method to Play Explosion
	@Override
	public void kill() {
		super.kill();
		deathSFX.play(0.5);
	}
	
	//  Draw Method
	public void draw(GraphicsContext context) {
		context.drawImage(image, position.getX(), position.getY());
	}
}
