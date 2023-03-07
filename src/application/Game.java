package application;
	
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Game extends Application {
	// JavaFX Constants
	private static final String GAME_NAME = "Space Blaster";
	private static final int WINDOW_WIDTH = 1024;
	private static final int WINDOW_HEIGHT = 768;
	private static final int GAME_LEFT_BOUND = 0;
	private static final int GAME_RIGHT_BOUND = WINDOW_WIDTH;
	private static final Color BACKGROUND_COLOR = Color.grayRgb(20);
	private static final Color PAUSE_OVERLAY_COLOR = Color.color(0.2, 0.2, 0.2, 0.6);
	private static final Font GUI_TEXT_FONT = Font.font(20);
	private static final Font GAME_OVER_FONT = Font.font(40);
	private static final Color GUI_TEXT_COLOR = Color.WHITE;
	
	// JavaFX Game Assets
	private static final Media BACKGROUND_MUSIC = new Media(new File("music/beyond-the-clouds.mp3").toURI().toString());
	private static final MediaPlayer MUSIC_PLAYER = new MediaPlayer(BACKGROUND_MUSIC);
	
	// JavaFX Data Fields
	private GraphicsContext context;
	
	// Gameplay Constants
	private static final int MAXIMUM_STARS = 200;
	private static final int INITIAL_STARS = 50;
	private static final int STAR_DENSITY = 20;
	private static final int MAXIMUM_ENEMIES = 10;
	private static final Point2D PLAYER_START_POSITION = new Point2D(WINDOW_WIDTH / 2.0, WINDOW_HEIGHT - Player.getHeight() - 10.0);
	private static final Point2D PLAYER_SPEED_LEFT = new Point2D(-7.0, 0.0);
	private static final Point2D PLAYER_SPEED_RIGHT = new Point2D(7.0, 0.0);
	private static final Point2D BASE_ENEMY_SPEED = new Point2D(0.0, 0.1);
	private static final double STAR_SPAWN_BOUND_LEFT = 0.0 + Star.getWidth();
	private static final double STAR_SPAWN_BOUND_RIGHT = WINDOW_WIDTH - Star.getWidth();
	private static final double ENEMY_SPAWN_BOUND_LEFT = 0.0 + Enemy.getWidth();
	private static final double ENEMY_SPAWN_BOUND_RIGHT = WINDOW_WIDTH - Enemy.getWidth();
	private static final double BACKGROUND_MUSIC_VOLUME = 0.5;
	
	// Gameplay Data Fields
	private int score;
	private double difficultyScalar;
	private int difficultyThreshold;
	private boolean paused;
	private Player player;
	private List<Star> starfield;
	private List<Enemy> enemies;
	private List<Missile> missiles;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Setup Canvas
			Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
			context = canvas.getGraphicsContext2D();

			// Setup Timeline
			Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), e -> { update(); draw(); }));
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
			
			// Setup Game Details
			setup();
			
			// Setup Scene with Canvas
			StackPane root = new StackPane(canvas);
			Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
			
			// Add Event Handler
			scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> handleKeyPress(event));
			scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> handleKeyRelease(event));
			
			// Add Scene to Stage
			primaryStage.setTitle(GAME_NAME);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/* setup()
	 * -- Set Game Data to Initial Conditions for a New Round
	 */
	private void setup() {
		// Setup Game Data
		score = 0;
		difficultyScalar = 0.0;
		difficultyThreshold = 10;
		paused = false;
		
		// Setup Entities
		player = new Player(PLAYER_START_POSITION);
		starfield = new ArrayList<>();
		enemies = new ArrayList<>();
		missiles = new ArrayList<>();
		
		// Set Entity Assets; Print Stack Trace if Assets Not Found
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
		
		// Initialize Star Field
		for (int i = 0; i < INITIAL_STARS; ++i)
			starfield.add(new Star(STAR_SPAWN_BOUND_LEFT, STAR_SPAWN_BOUND_RIGHT));
		for (Star star : starfield)
			star.shift();
		
		// Start Music Player
		MUSIC_PLAYER.setCycleCount(MediaPlayer.INDEFINITE);
		MUSIC_PLAYER.play();
		MUSIC_PLAYER.setVolume(BACKGROUND_MUSIC_VOLUME);
	}
	
	
	/* handleKeyPress(event)
	 * -- Respond to player input key events according to the game's state: over, paused, running
	 */
	private void handleKeyPress(KeyEvent event) {
		if (isOver()) {
			// Only Allow Exiting and Restarting While Game Over
			switch (event.getCode()) {
			case ESCAPE: quit(); break;
			case ENTER: setup(); break;
			default: break;
			}
		} else if (paused) {
			// Only Allow Unpausing and Quitting While Paused
			switch (event.getCode()) {
				case P: togglePause(); break;
				case ESCAPE: quit(); break;
				default: break;
			}
		} else {
			// Allow All Key Controls while Game is Running
			switch (event.getCode()) {
				case LEFT:  player.setSpeed(PLAYER_SPEED_LEFT); break;
				case RIGHT: player.setSpeed(PLAYER_SPEED_RIGHT); break;
				case SPACE: player.shoot(missiles); break;
				case P: togglePause(); break;
				case ESCAPE: quit(); break;
				default: break;
			}
		}
	}
	
	
	/* handleKeyRelease(KeyEvent event)
	 * -- Respond to player key release events:
	 * -- -- LEFT  -> Set Player Speed Back to Zero
	 * -- -- RIGHT -> Set Player Speed Back to Zero
	 */
	private void handleKeyRelease(KeyEvent event) {
		switch (event.getCode()) {
			case LEFT:  player.setSpeed(Point2D.ZERO); break;
			case RIGHT: player.setSpeed(Point2D.ZERO); break;
			default: break;
		}
	}
	
	
	/* update()
	 * -- Move entities, perform collision checking and entity culling
	 */
	private void update() {
		if (!paused && !isOver()) {
			// Move Player Ship
			player.move(GAME_LEFT_BOUND, GAME_RIGHT_BOUND);
			
			// Move Star Field
			for (Star star : starfield)
				star.move();
			
			// Move / Collide Enemies and Check for Game-Over by Crossing Bottom of Screen
			for (Enemy enemy : enemies) {
				enemy.move();
				if (enemy.isCollidingWith(player)) {
					enemy.kill();
					player.kill();
				}
				if ((enemy.getPosition().getY() + Enemy.getHeight()) > WINDOW_HEIGHT)
					player.kill();
			}
			
			// Move / Collide Missiles
			for (Missile missile : missiles) {
				missile.move();
				for (Enemy enemy : enemies) {
					if (missile.isCollidingWith(enemy)) {
						missile.kill();
						enemy.kill();
					}
				}
			}
			
			// Cull Off-Screen Stars
			cullStars();
			
			// Cull and Score Dead Enemies
			cullEnemies();
			
			// Cull Dead and Off-Screen Missiles
			cullMissiles();
			
			// Increase Difficulty if Necessary
			if (score / difficultyThreshold > difficultyScalar) {
				difficultyThreshold *= 2;
				difficultyScalar += 0.05;
			}
			
			// Create New Stars if Room Available
			spawnStars();
			
			// Create a New Enemy if Room Available
			spawnEnemies();
		}
	}
	
	
	/* draw()
	 * -- Draw player, enemies, missiles, and score to canvas on top of gray background
	 */
	private void draw() {
		// Draw Background
		context.setFill(BACKGROUND_COLOR);
		context.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Draw Scoreboard
		context.setTextAlign(TextAlignment.CENTER);
		context.setFont(GUI_TEXT_FONT);
		context.setFill(GUI_TEXT_COLOR);
		context.fillText("SCORE: " + score, 60, 20);
		
		// Draw Star Field
		for (Star star : starfield)
			star.draw(context);
		
		// Draw Missiles
		for (Missile missile : missiles)
			missile.draw(context);
		
		// Draw Enemies
		for (Enemy enemy : enemies)
			enemy.draw(context);
		
		// Draw Player
		if (player.isAlive())
			player.draw(context);
		
		// Draw Pause Overlay
		if (paused) {
			// Gray-Out the Game Board
			context.setFill(PAUSE_OVERLAY_COLOR);
			context.fillRect(0, 0, WINDOW_WIDTH,  WINDOW_HEIGHT);
			
			// Display "Paused" Message
			context.setTextAlign(TextAlignment.CENTER);
			context.setFont(GUI_TEXT_FONT);
			context.setFill(GUI_TEXT_COLOR);
			context.fillText("PAUSED", WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 5.0);
		}
		
		// Draw Game Over Screen
		if (isOver()) {
			// Gray-Out the Game Board
			context.setFill(PAUSE_OVERLAY_COLOR);
			context.fillRect(0, 0, WINDOW_WIDTH,  WINDOW_HEIGHT);
			
			// Display "Game Over" Message with Score
			context.setTextAlign(TextAlignment.CENTER);
			context.setFont(GAME_OVER_FONT);
			context.setFill(GUI_TEXT_COLOR);
			context.fillText("GAME OVER\nSCORE: " + score, WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 4.0);
			
			// Display Controls to Exit/Restart
			context.setFont(GUI_TEXT_FONT);
			context.setFill(GUI_TEXT_COLOR);
			context.fillText("Press ENTER to Restart\nPress ESCAPE to Quit", WINDOW_WIDTH / 2.0, WINDOW_HEIGHT / 2.0);
		}
	}
	
	
	/* cullStars()
	 * -- Removes stars that are off-screen and no longer visible.
	 */
	private void cullStars() {
		Iterator<Star> starIterator = starfield.iterator();
		while (starIterator.hasNext()) {
			Star star = starIterator.next();
			if (star.getPosition().getY() > WINDOW_HEIGHT)
				starIterator.remove();
		}
	}
	
	
	/* cullEnemies()
	 * -- Removes dead enemies from the enemies list and adjusts the player's
	 *    score for each enemy killed.
	 */
	private void cullEnemies() {
		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy enemy = enemyIterator.next();
			if (!enemy.isAlive()) {
				score += enemy.getScore();
				enemyIterator.remove();
			}
		}
	}
	
	
	/* cullMissiles()
	 * -- Removes dead and off-screen missiles from the missiles list.
	 */
	private void cullMissiles() {
		Iterator<Missile> missileIterator = missiles.iterator();
		while (missileIterator.hasNext()) {
			Missile missile = missileIterator.next();
			boolean isMissileOffscreen = (missile.getPosition().getY() < 0.0);
			if (!missile.isAlive() || isMissileOffscreen)
				missileIterator.remove();
		}
	}
	
	
	/* spawnStars()
	 * -- Spawn new stars in the star field if room is available.
	 */
	private void spawnStars() {
		// Do Not Spawn if Star Field is Full
		if (starfield.size() >= MAXIMUM_STARS)
			return;
		
		// Do Not Spawn If Too Many Stars Exist in Top Fifth of Window
		int starBufferCount = 0;
		double bufferRegionHeight = WINDOW_HEIGHT / 5.0;
		for (Star star : starfield)
			if (star.getPosition().getY() < bufferRegionHeight)
				starBufferCount += 1;
		if (starBufferCount >= STAR_DENSITY)
			return;
		
		// Create New Star
		starfield.add(new Star(STAR_SPAWN_BOUND_LEFT, STAR_SPAWN_BOUND_RIGHT));
	}
	
	
	/* spawnEnemies()
	 * -- Spawn new enemies with speed set according to the current value of the
	 *    difficulty scalar until the enemy list reaches the cap.
	 */
	private void spawnEnemies() {
		// Do Not Spawn if Enemies is Full
		if (enemies.size() >= MAXIMUM_ENEMIES)
			return;
		
		// Create New Enemy with Speed Based on Current Difficulty
		Point2D enemySpeed = BASE_ENEMY_SPEED.add(0, difficultyScalar);
		Enemy spawn = new Enemy(ENEMY_SPAWN_BOUND_LEFT, ENEMY_SPAWN_BOUND_RIGHT, enemySpeed);

		// Do Not Add Spawn to List if it Collides with Existing Enemy
		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy enemy = enemyIterator.next();
			if (spawn.isCollidingWith(enemy))
				return;
			}
		
		// No Collision; Add Enemy to List
		enemies.add(spawn);
	}
	
	
	/* isOver()
	 * -- Returns true if the player has died; returns false otherwise.
	 */
	private boolean isOver() {
		return !player.isAlive();
	}
	
	
	/* togglePause()
	 * -- Toggles the paused flag that controls game update and draw logic.
	 *    Reduces the volume of the background music when paused.
	 */
	private void togglePause() {
		if (paused) {
			paused = false;
			MUSIC_PLAYER.setVolume(BACKGROUND_MUSIC_VOLUME);
		} else {
			paused = true;
			MUSIC_PLAYER.setVolume(BACKGROUND_MUSIC_VOLUME / 3.0);
		}
	}
	
	
	/* quit()
	 * -- Ends the game timeline allowing the application to exit.
	 */
	private void quit() {
		Platform.exit();
	}
}