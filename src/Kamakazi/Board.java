package Kamakazi;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable, Commons {
	private static final long serialVersionUID = 1L; //generated with eclipse
	private Dimension d;
	private ArrayList<Kamakazi> kamakazis;
	private Player player;
	private Bomb shot;

	private int kamakaziX = 150;
	private int kamakaziY = 5;
	private int direction = -1;
	private int deaths = 0;

	private boolean ingame = true;
	private final String expl = "explosion.png";
	private final String kamakazipix = "kamakazi.png";
	private String message = "The objective was not to get shot...";
	public int high; //high score
	public int score;
	public int lvl; 
	public int plays;
	private Thread animator;

	public Board() throws IOException{

		addKeyListener(new TAdapter());
		setFocusable(true);
		d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
		setBackground(Color.black);

		gameStart(); // to change level
		setDoubleBuffered(true);
	}

	public void addNotify() {
		super.addNotify();
		gameLv1(); // to change level
	}

	public void gameStart() throws IOException
	{
		high = getHighScore();
		incrementPlays();
		plays = getPlays();
		System.out.println(plays);
		gameLv1();
		player = new Player();
		shot = new Bomb();
		if (animator == null || !ingame) {
			animator = new Thread(this);
			animator.start();
		}
	}
	
	public void gameLv1() {

		lvl = 1;
		kamakazis = new ArrayList<Kamakazi>();
		ImageIcon iIcon = new ImageIcon(this.getClass().getResource(kamakazipix));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				Kamakazi kamakazi = new Kamakazi(kamakaziX + 30 * j, kamakaziY + 30 * i);
				kamakazi.setImage(iIcon.getImage());
				kamakazis.add(kamakazi);
			}
		}
	}

	public void gameLv2() {

		kamakazis = new ArrayList<Kamakazi>();
		ImageIcon iIcon = new ImageIcon(this.getClass().getResource(kamakazipix));
		for (int i = 0; i < 9; i++) {// vertical
			for (int j = 0; j < 9; j++) {// horizontal
				Kamakazi kamakazi = new Kamakazi(kamakaziX + 18 * j, kamakaziY + 18 * i);
				kamakazi.setImage(iIcon.getImage());
				kamakazis.add(kamakazi);
			}
		}
		// getting rid of kamakazis
		int i[] = { 2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 18, 19, 20, 24, 25, 26, 27, 28, 34, 35, 36, 37, 40, 43, 44, 45,
				46, 52, 53, 54, 55, 56, 60, 61, 62, 65, 66, 67, 68, 69, 74, 75, 76, 77, 78 };
		for (int j = i.length - 1; j >= 0; j--)
			kamakazis.remove(i[j]);
	}

	public void gameLv3() {

		kamakazis = new ArrayList<Kamakazi>();
		ImageIcon iIcon = new ImageIcon(this.getClass().getResource(kamakazipix));
		for (int i = 0; i < 9; i++) {// vertical
			for (int j = 0; j < 9; j++) {// horizontal
				Kamakazi kamakazi = new Kamakazi(kamakaziX + 18 * j, kamakaziY + 18 * i);
				kamakazi.setImage(iIcon.getImage());
				kamakazis.add(kamakazi);
			}
		}
		// how to remove enemies
		int i[] = { 0, 2, 3, 4, 5, 6, 8, 10, 16, 18, 19, 21, 23, 25, 26, 27, 28, 30, 31, 32, 34, 35, 36, 39, 40, 41, 44,
				45, 46, 48, 49, 50, 52, 53, 54, 55, 57, 59, 61, 62, 64, 70, 72, 74, 75, 76, 77, 78, 80 };
		for (int j = i.length - 1; j >= 0; j--)
			kamakazis.remove(i[j]);
	}

	public void drawKamakazis(Graphics g) {
		Iterator<Kamakazi> it = kamakazis.iterator();

		while (it.hasNext()) {
			Kamakazi kamakazi = (Kamakazi) it.next();

			if (kamakazi.isVisible()) {
				g.drawImage(kamakazi.getImage(), kamakazi.getX(), kamakazi.getY(), this);
			}

			if (kamakazi.isDying()) {
				kamakazi.die();
			}
		}
	}

	public void drawPlayer(Graphics g) {

		if (player.isVisible()) {
			g.drawImage(player.getImage(), player.getX(), player.getY(), this);
		}

		if (player.isDying()) {
			player.die();
			ingame = false;
		}
	}

	public void drawShot(Graphics g) {
		if (shot.isVisible())
			g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
	}

	public void drawBombing(Graphics g) {

		Iterator<Kamakazi> i3 = kamakazis.iterator();

		while (i3.hasNext()) {
			Kamakazi a = (Kamakazi) i3.next();

			Kamakazi.Bomb b = a.getBomb();

			if (!b.isDestroyed()) {
				g.drawImage(b.getImage(), b.getX(), b.getY(), this);
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, d.width, d.height);
		g.setColor(Color.green);
		if (ingame) {
			g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
			drawKamakazis(g);
			drawPlayer(g);
			drawShot(g);
			drawBombing(g);
			Font small = new Font("Comic Sans MS", Font.PLAIN, 20);
			FontMetrics metr = this.getFontMetrics(small);
			g.setColor(Color.red);
			g.setFont(small);
			g.drawString("Score " + (score * lvl), 10, 20);
			g.drawString("High Score " + (high), 200, 20);
		}
		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public void gameOver() throws IOException{

		Graphics g = this.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);
		g.setColor(new Color(0, 32, 48));
		g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		g.setColor(Color.white);
		g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);
		g.setColor(Color.white);
		g.setFont(small);
		if(score > high)
		{
			setHighScore(score);
		}
		g.drawString(message, (BOARD_WIDTH - metr.stringWidth(message)) / 2, BOARD_WIDTH / 2);
		g.drawString("GAME OVER", ((BOARD_WIDTH - metr.stringWidth("Game Over"))/2), 20);
		g.drawString("Final Score: " + (score * lvl), ((BOARD_WIDTH - metr.stringWidth("Final Score"))/2), 100); //show user their final score when they lose
		g.drawString("High Score: " + (high), ((BOARD_WIDTH - metr.stringWidth("High Score"))/2), 150); //show user the failure they are for never getting the high score.
	}

	public void animationCycle() {

		if (deaths == Level_1_Kamakazis && lvl == 1) {
			gameLv2();
			lvl++;
		}
		if (deaths == Level_2_Kamakazis && lvl == 2) {
			gameLv3();
			lvl++;
		}
		if (deaths == Level_3_Kamakazis && lvl == 3) {
			ingame = false;
			message = "Game won!";
		}
		// player
		player.act();
		// shot
		if (shot.isVisible()) {
			Iterator<Kamakazi> it = kamakazis.iterator();
			int shotX = shot.getX();
			int shotY = shot.getY();
			while (it.hasNext()) {
				Kamakazi kamakazi = (Kamakazi) it.next();
				int kamakaziX = kamakazi.getX();
				int kamakaziY = kamakazi.getY();
				if (kamakazi.isVisible() && shot.isVisible()) {
					if (shotX >= (kamakaziX) && shotX <= (kamakaziX + KAMAKAZI_WIDTH) && shotY >= (kamakaziY)
							&& shotY <= (kamakaziY + KAMAKAZI_HEIGHT)) {
						ImageIcon iIcon = new ImageIcon(getClass().getResource(expl));
						kamakazi.setImage(iIcon.getImage());
						kamakazi.setDying(true);
						deaths++;
						score = deaths * 10;
						shot.die();
					}
				}
			}
			int y = shot.getY();
			y -= 4;
			if (y < 0)
				shot.die();
			else
				shot.setY(y);
		}
		// kamakazis
		Iterator<Kamakazi> it1 = kamakazis.iterator();

		while (it1.hasNext()) {
			Kamakazi a1 = (Kamakazi) it1.next();
			int x = a1.getX();

			if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
				direction = -1;
				Iterator<Kamakazi> i1 = kamakazis.iterator();
				while (i1.hasNext()) {
					Kamakazi a2 = (Kamakazi) i1.next();
					a2.setY(a2.getY() + GO_DOWN);
				}
			}

			if (x <= BORDER_LEFT && direction != 1) {
				direction = 1;

				Iterator<Kamakazi> i2 = kamakazis.iterator();
				while (i2.hasNext()) {
					Kamakazi a = (Kamakazi) i2.next();
					a.setY(a.getY() + GO_DOWN);
				}
			}
		}

		Iterator<Kamakazi> it = kamakazis.iterator();

		while (it.hasNext()) { //if they get to the base, gg.
			Kamakazi kamakazi = (Kamakazi) it.next();
			if (kamakazi.isVisible()) {

				int y = kamakazi.getY();

				if (y > GROUND - KAMAKAZI_HEIGHT) {
					ingame = false;
					message = "They just Kamakazi'd the Base. Nice one.";
				}

				kamakazi.act(direction);
			}
		}


		// bombs

		Iterator<Kamakazi> i3 = kamakazis.iterator();
		Random generator = new Random();

		while (i3.hasNext()) {
			int shot = generator.nextInt(15);
			Kamakazi a = (Kamakazi) i3.next();
			Kamakazi.Bomb b = a.getBomb();
			if (shot == CHANCE && a.isVisible() && b.isDestroyed()) {

				b.setDestroyed(false);
				b.setX(a.getX());
				b.setY(a.getY());
			}

			int bombX = b.getX();
			int bombY = b.getY();
			int playerX = player.getX();
			int playerY = player.getY();

			if (player.isVisible() && !b.isDestroyed()) {
				if (bombX >= (playerX) && bombX <= (playerX + PLAYER_WIDTH) && bombY >= (playerY)
						&& bombY <= (playerY + PLAYER_HEIGHT)) {
					ImageIcon iIcon = new ImageIcon(this.getClass().getResource(expl));
					player.setImage(iIcon.getImage());
					player.setDying(true);
					b.setDestroyed(true);
					;
				}
			}

			if (!b.isDestroyed()) {
				b.setY(b.getY() + 1);
				if (b.getY() >= GROUND - BOMB_HEIGHT) {
					b.setDestroyed(true);
				}
			}
		}
	}

	public void run() {

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (ingame) {
			repaint();
			animationCycle();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
				sleep = 2;
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				System.out.println("interrupted");
			}
			beforeTime = System.currentTimeMillis();
		}
		try {
			gameOver();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void setHighScore(int score) throws IOException //serialization for saving high score
	{
		high = score;
		File storage = new File("storedscore.txt");
        FileWriter write = new FileWriter(storage, false);
        PrintWriter print_line = new PrintWriter( write );
        print_line.printf("%s" + "%n", score);
        print_line.close();        
	}
	public int getHighScore() throws IOException
	{
		File storage = new File("storedscore.txt");
		Scanner scan = new Scanner(storage);
		return scan.nextInt();
	}
	public void incrementPlays() throws IOException
	{
		DataOutputStream output = new DataOutputStream(new FileOutputStream("myDat.dat", false));
		int temp = plays +1;
		output.writeInt(temp);
		output.close();
	}
	public int getPlays() throws IOException
	{
		DataInputStream input = new DataInputStream(new FileInputStream("myDat.dat"));
		int ret = input.readInt();
		input.close();
		return ret;
	}

	private class TAdapter extends KeyAdapter {

		public void keyReleased(KeyEvent e) {
			player.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) {

			player.keyPressed(e);

			int x = player.getX();
			int y = player.getY();

			if (ingame) {
				if (e.isControlDown()) {
					if (!shot.isVisible())
						shot = new Bomb(x, y);
				}
			}
		}
	}

	
}