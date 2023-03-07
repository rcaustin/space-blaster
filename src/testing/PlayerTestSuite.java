package testing;

import java.util.ArrayList;
import java.util.List;

import application.Missile;
import application.Player;
import javafx.geometry.Point2D;

public class PlayerTestSuite extends TestSuite {
	private static Player testPlayer;
	
	public PlayerTestSuite() {
		super("Player");
		testPlayer = new Player(Point2D.ZERO);
	}
	
	
	@Override
	public int run() {
		// Reset Failure Count
		this.failures = 0;
		
		// Test Static Player Methods
		this.failures += testGetWidth();
		this.failures += testGetHeight();
		
		// Test Non-Static Player Methods
		this.failures += testShoot();
		this.failures += testIsCollidingWith();
		this.failures += testIsAlive();
		this.failures += testKill();
		this.failures += testMove();
		
		return this.failures;
	}

	
	private int testGetWidth() {
		System.out.print("* Player.getWidth()");
		
		// Test Static Constant Against Literal
		if (Player.getWidth() != 80)
			return fail();
		
		return success();
	}
	
	
	private int testGetHeight() {
		System.out.print("* Player.getHeight()");
		
		// Test Static Constant Against Literal
		if (Player.getHeight() != 80)
			return fail();
		
		return success();			
	}
	
	
	private int testShoot() {
		System.out.print("* player.shoot()");
		List<Missile> testMissiles = new ArrayList<>();
		
		// Check That shoot() Adds a Missile
		testPlayer.shoot(testMissiles);
		if (testMissiles.size() != 1)
			return fail();
		
		// Check That shoot() Does Not Add an 11th Missile
		for (int i = 0; i < 9; ++i)
			testPlayer.shoot(testMissiles);
		
		testPlayer.shoot(testMissiles);
		if (testMissiles.size() > 10)
			return fail();
		
		return success();
	}
	
	
	private int testIsCollidingWith() {
		System.out.print("* player.isCollidingWith(Entity)");
		Player testNonColliding = new Player(new Point2D(2.0 * Player.getWidth(), 2.0 * Player.getHeight()));
		Player testColliding = new Player(new Point2D(Player.getWidth() / 2.0, Player.getHeight() / 2.0));
		
		// Test Non-Colliding
		if (testPlayer.isCollidingWith(testNonColliding))
			return fail();
		
		// Test Colliding
		if (!testPlayer.isCollidingWith(testColliding))
			return fail();
		
		return success();
	}
	
	private int testIsAlive() {
		System.out.print("* player.isAlive()");
		
		// Test That Newly Constructed Player is Alive
		testPlayer = new Player(Point2D.ZERO);
		if (!testPlayer.isAlive())
			return fail();
		
		return success();	
	}
	
	private int testKill() {
		System.out.print("* player.kill()");
		
		// Test That Newly Constructed Player is NOT Alive after kill()
		testPlayer = new Player(Point2D.ZERO);
		testPlayer.kill();
		if (testPlayer.isAlive())
			return fail();
		
		return success();
	}
	
	private int testMove() {
		System.out.print("* player.move()");
		
		// Test Position After Move: DEFAULT SPEED=(0.0,0.0)
		testPlayer = new Player(Point2D.ZERO);
		testPlayer.move();
		if (testPlayer.getPosition().distance(Point2D.ZERO) > FLOAT_TOLERANCE)
			return fail();
		
		// Test Position After Move with NonZero Speed and Check within Bounds
		Point2D testSpeed = new Point2D(10.0, 0.0);
		int leftBoundTest = 0;
		int rightBoundTest = 1000;
		testPlayer = new Player(Point2D.ZERO);
		testPlayer.setSpeed(testSpeed);
		testPlayer.move(leftBoundTest, rightBoundTest);
		if (testPlayer.getPosition().distance(testSpeed) > FLOAT_TOLERANCE)
			return fail();
		if (testPlayer.getPosition().getX() < leftBoundTest || testPlayer.getPosition().getX() > rightBoundTest)
			return fail();
		
		return success();
		
	}
}
