package testing;

import application.Missile;
import javafx.geometry.Point2D;

public class MissileTestSuite extends TestSuite {
	private static Missile testMissile;
	private static Point2D testSpeed;
	
	public MissileTestSuite() {
		super("Missile");
		testMissile = new Missile(Point2D.ZERO);
		testSpeed = new Point2D(0.0, -1.0);
	}
	

	@Override
	public int run() {
		// Reset Failure Count
		this.failures = 0;
		
		// Test Static Missile Methods
		this.failures += testGetWidth();
		this.failures += testGetHeight();
		
		// Test Non-Static Missile Methods
		this.failures += testIsCollidingWith();
		this.failures += testIsAlive();
		this.failures += testKill();
		this.failures += testMove();
		
		return this.failures;
	}

	private int testGetWidth() {
		System.out.print("* Missile.getWidth()");
		
		// Test Static Constant Against Literal
		if (Missile.getWidth() != 15)
			return fail();
		
		return success();
	}
	
	
	private int testGetHeight() {
		System.out.print("* Missile.getHeight()");
		
		// Test Static Constant Against Literal
		if (Missile.getHeight() != 60)
			return fail();
		
		return success();			
	}
	
	private int testIsCollidingWith() {
		System.out.print("* missile.isCollidingWith(Entity)");
		Missile testNonColliding = new Missile(new Point2D(2.0 * Missile.getWidth(), 2.0 * Missile.getHeight()));
		Missile testColliding = new Missile(new Point2D(Missile.getWidth() / 2.0, Missile.getHeight() / 2.0));
		
		// Test Non-Colliding
		if (testMissile.isCollidingWith(testNonColliding))
			return fail();
		
		// Test Colliding
		if (!testMissile.isCollidingWith(testColliding))
			return fail();
		
		return success();
	}
	
	private int testIsAlive() {
		System.out.print("* missile.isAlive()");
		
		// Test That Newly Constructed Player is Alive
		testMissile = new Missile(Point2D.ZERO);
		if (!testMissile.isAlive())
			return fail();
		
		return success();	
	}
	
	private int testKill() {
		System.out.print("* missile.kill()");
		
		// Test That Newly Constructed Player is NOT Alive after kill()
		testMissile = new Missile(Point2D.ZERO);
		testMissile.kill();
		if (testMissile.isAlive())
			return fail();
		
		return success();
	}
	
	private int testMove() {
		System.out.print("* missile.move()");
		
		// Test Position After Move
		testMissile = new Missile(Point2D.ZERO);
		testMissile.move();
		if (testMissile.getPosition().distance(testSpeed) > FLOAT_TOLERANCE)
			return fail();
		
		return success();
		
	}
}
