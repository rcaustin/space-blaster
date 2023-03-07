package application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

public abstract class Entity {
	// Data Fields
	protected Point2D position;
	protected Point2D speed;
	protected boolean alive;
	
	// Constructor
	protected Entity(Point2D position, Point2D speed) {
		this.position = position;
		this.speed = speed;
		this.alive = true;
	}
	
	// Accessors / Mutators
	public Point2D getPosition() { return position; }
	public boolean isAlive() { return alive; }
	public void kill() { alive = false; }
	
	public void move() {
		position = position.add(speed);
	}
	
	// Abstract Bounding Box Method for Collision Testing
	abstract protected Rectangle2D getBoundingBox();
	
	// Abstract AABB Collision Testing Method
	abstract public boolean isCollidingWith(Entity other);
}
