package application;

import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public class Player extends Entity {
	// Constants
	private static final int MAXIMUM_MISSILES = 10;
	private static final int SIZE = 80;
	
	// Data Fields
	private static Image image;
	private static AudioClip laserSFX;
	private static AudioClip deathSFX;
	
	// Constructor
	public Player(Point2D position) {
		super(position, Point2D.ZERO);
	}
	
	// Accessors / Mutators
	public static int getWidth() { return SIZE; }
	public static int getHeight() { return SIZE; }
	public void setSpeed(Point2D speed) { this.speed = speed; }
	
	// Asset Mutators
	public static void setImage(Image img) { image = img; }
	public static void setLaserSound(AudioClip sound) { laserSFX = sound; }
	public static void setExplodeSound(AudioClip sound) { deathSFX = sound; }
	
	// Shoot Method
	public void shoot(List<Missile> missiles) {
		if (missiles.size() < MAXIMUM_MISSILES) {
			laserSFX.play(0.5);
			missiles.add(new Missile(position.add(image.getWidth() / 2.0, -image.getHeight() / 2.0)));
		}
	}
	
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
	
	// Overloaded Move Method to Keep Player within Game Board
	public void move(int leftBound, int rightBound) {
		int x = (int)position.add(speed).getX();
		if ((x >= leftBound) && (x + image.getWidth() <= rightBound))
			position = position.add(speed);
	}
	
	//  Draw Method
	public void draw(GraphicsContext context) {
		context.drawImage(image, position.getX(), position.getY());
	}
}