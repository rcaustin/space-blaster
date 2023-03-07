package testing;

import application.Player;
import application.Star;
import javafx.geometry.Point2D;

public class StarTestSuite extends TestSuite {
	private static Star testStar;
	private static double testBoundLeft = 0.0;
	private static double testBoundRight = 1000.0;
	
	public StarTestSuite() {
		super("Star");
		testStar = new Star(testBoundLeft, testBoundRight);
	}
	
	
	@Override
	public int run() {
		// Reset Failure Count
		this.failures = 0;
		
		// Test Static Player Methods
		this.failures += testGetWidth();
		this.failures += testGetHeight();
		
		// Test Non-Static Player Methods
		this.failures += testShift();
		this.failures += testIsCollidingWith();
		this.failures += testIsAlive();
		this.failures += testKill();
		this.failures += testMove();
		
		return this.failures;
	}

	
	private int testGetWidth() {
		System.out.print("* Star.getWidth()");
		
		// Test Static Constant Against Literal
		if (Star.getWidth() != 6)
			return fail();
		
		return success();
	}
	
	
	private int testGetHeight() {
		System.out.print("* Star.getHeight()");
		
		// Test Static Constant Against Literal
		if (Star.getHeight() != 6)
			return fail();
		
		return success();			
	}
	
	private int testShift() {
		System.out.print("* star.shift()");
		
		// Test that Shift Does Not Move in X
		Star star = new Star(testBoundLeft, testBoundRight);
		Point2D initialPosition = star.getPosition();
		star.shift();
		if (initialPosition.getX() != star.getPosition().getX())
			return fail();
		
		// Test that Shift Only Keeps Y or Increases Y
		star = new Star(testBoundLeft, testBoundRight);
		initialPosition = star.getPosition();
		star.shift();
		if (initialPosition.getY() > star.getPosition().getY())
			return fail();
		
		return success();
	}
	
	
	private int testIsCollidingWith() {
		System.out.print("* star.isCollidingWith(Entity)");
		
		Star star = new Star(testBoundLeft, testBoundRight);
		
		// Test Non-Colliding
		Player nonColliding = new Player(star.getPosition().add(200.0, 200.0));
		if (star.isCollidingWith(nonColliding))
			return fail();
		
		// Test Colliding
		Player colliding = new Player(star.getPosition());
		if (!star.isCollidingWith(colliding))
			return fail();
		
		return success();
	}
	
	private int testIsAlive() {
		System.out.print("* star.isAlive()");
		
		// Test That Newly Constructed Player is Alive
		testStar = new Star(testBoundLeft, testBoundRight);
		if (!testStar.isAlive())
			return fail();
		
		return success();	
	}
	
	private int testKill() {
		System.out.print("* star.kill()");
		
		// Test That Newly Constructed Player is NOT Alive after kill()
		testStar = new Star(testBoundLeft, testBoundRight);
		testStar.kill();
		if (testStar.isAlive())
			return fail();
		
		return success();
	}
	
	private int testMove() {
		System.out.print("* star.move()");
		

		
		return success();
		
	}
}
