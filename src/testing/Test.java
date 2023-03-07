package testing;

import java.io.File;

import application.Enemy;
import application.Missile;
import application.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

public class Test extends Application {
	@Override
	public void start(Stage primaryStage) {
		// Setup Scene with Canvas
		StackPane root = new StackPane();
		Scene scene = new Scene(root);
		
		// Load Entity Assets
		try {
			Player.setImage(new Image(new File("images/player.png").toURI().toString(), Player.getWidth(), Player.getHeight(), true, true));
			Player.setLaserSound(new AudioClip(new File("sfx/laser.wav").toURI().toString()));
			Player.setExplodeSound(new AudioClip(new File("sfx/explosion.wav").toURI().toString()));
			Enemy.setImage(new Image(new File("images/enemy.png").toURI().toString(), Enemy.getWidth(), Enemy.getHeight(), true, true));
			Enemy.setExplodeSound(new AudioClip(new File("sfx/explosion.wav").toURI().toString()));
			Missile.setImage(new Image(new File("images/missile.png").toURI().toString(), Missile.getWidth(), Missile.getHeight(), true, true));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// Run Test Suites
		runTestSuite(new EnemyTestSuite());
		runTestSuite(new MissileTestSuite());
		runTestSuite(new StarTestSuite());
		runTestSuite(new PlayerTestSuite());
		
		// Add Scene to Stage
		primaryStage.setTitle("Testing...");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void runTestSuite(TestSuite suite) {
		System.out.println("Running Test Suite...");
		suite.printHeader();
		int failureCount = suite.run();
		System.out.println("\nSuite Reported " + failureCount + " Total Failed Tests\n");
		System.out.println("**************************************************");
	}
}