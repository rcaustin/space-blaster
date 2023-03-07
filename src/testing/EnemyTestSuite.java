package testing;

import application.Enemy;
import application.Player;
import javafx.geometry.Point2D;

public class EnemyTestSuite extends TestSuite {
	private static double testLeftBound;
	private static double testRightBound;
	private static Point2D testSpeed;
	private static Enemy testEnemy;
	
	public EnemyTestSuite() {
		super("Enemy");
		testLeftBound = 100.0;
		testRightBound = 900.0;
		testSpeed = new Point2D(0.0, 10.0);
		testEnemy = new Enemy(testLeftBound, testRightBound, testSpeed);
	}
	

	@Override
	public int run() {	
		// Reset Failure Count
		this.failures = 0;
		
		// Test Static Enemy Methods
		this.failures += testGetWidth();
		this.failures += testGetHeight();
		
		// Test Non-Static Enemy Methods
		this.failures += testGetScore();
		this.failures += testIsCollidingWith();
		this.failures += testSpawnBounds();
		this.failures += testIsAlive();
		this.failures += testKill();
		this.failures += testMove();
		
		
		return this.failures;
	}
	

	private int testGetWidth() {
		System.out.print("* Enemy.getWidth()");
		
		// Test Static Constant Against Literal
		if (Enemy.getWidth() != 75)
			return fail();
		
		return success();
	}
	
	
	private int testGetHeight() {
		System.out.print("* Enemy.getHeight()");
		
		// Test Static Constant Against Literal
		if (Enemy.getHeight() != 75)
			return fail();
		
		return success();		
	}
	
	private int testGetScore() {
		System.out.print("* enemy.getScore()");
		
		// Test Score Getter *and* Constructor Score Calculation
		int testScore = (int)testSpeed.getY() * 100;
		if (testEnemy.getScore() != testScore)
			return fail();
		
		return success();
	}
	
	private int testIsCollidingWith() {
		System.out.print("* enemy.isCollidingWith(Entity)");
		Player testNonColliding = new Player(testEnemy.getPosition().add(new Point2D(Enemy.getWidth(), Enemy.getHeight())));
		Player testColliding = new Player(testEnemy.getPosition());
		
		// Test Non-Colliding
		if (testEnemy.isCollidingWith(testNonColliding))
			return fail();
		
		// Test Colliding
		if (!testEnemy.isCollidingWith(testColliding))
			return fail();
		
		return success();
	}
	
	private int testSpawnBounds() {
		System.out.print("* Enemy() Spawn Bounds Test");
		
		// Test That Enemy Position is within Bounds Passed to Constructor
		double positionX = testEnemy.getPosition().getX();
		if (positionX < testLeftBound || positionX > testRightBound)
			return fail();
		
		return success();
	}
	
	private int testIsAlive() {
		System.out.print("* enemy.isAlive()");
		
		// Test That Newly Constructed Player is Alive
		testEnemy = new Enemy(testLeftBound, testRightBound, testSpeed);
		if (!testEnemy.isAlive())
			return fail();
		
		return success();	
	}
	
	private int testKill() {
		System.out.print("* enemy.kill()");
		
		// Test That Newly Constructed Enemy is NOT Alive after kill()
		testEnemy = new Enemy(testLeftBound, testRightBound, testSpeed);
		testEnemy.kill();
		if (testEnemy.isAlive())
			return fail();
		
		return success();
	}
	
	private int testMove() {
		System.out.print("* enemy.move()");
		
		// Test Position After Move
		testEnemy = new Enemy(testLeftBound, testRightBound, testSpeed);
		Point2D initialPosition = testEnemy.getPosition();
		testEnemy.move();
		if (testEnemy.getPosition().distance(initialPosition.add(testSpeed)) > FLOAT_TOLERANCE)
			return fail();
		
		return success();
		
	}
}
