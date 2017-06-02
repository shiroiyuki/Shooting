package Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Game {
	public static void main(String[] args) {
		new GameMenu();
	}
}

class GameMenu extends JFrame implements ActionListener {
	JLabel JL = new JLabel("Little Fighter");
	JButton JB1 = new JButton("遊戲開始");
	JButton JB2 = new JButton("操作教學");
	JButton JB3 = new JButton("離開遊戲");
	ImageIcon image = new ImageIcon("img//space1.jpg");
	BackgroundPanel BGP = new BackgroundPanel(image.getImage());
	Menu menu = new Menu();

	GameMenu() {
		super("Little Fighter");
		// set JFrame
		setLayout(null);
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setJMenuBar(menu);
		// setting
		JL.setForeground(new Color(255, 255, 255));
		JL.setFont(new Font("標楷體", Font.BOLD, 48));
		JL.setBounds(220, 100, 400, 100);
		BGP.setBounds(0, 0, 800, 600);
		JB1.setFont(new Font("SansSerif", Font.PLAIN, 18));
		JB1.setBounds(330, 250, 120, 50);
		JB2.setFont(new Font("SansSerif", Font.PLAIN, 18));
		JB2.setBounds(330, 310, 120, 50);
		JB3.setFont(new Font("SansSerif", Font.PLAIN, 18));
		JB3.setBounds(330, 370, 120, 50);
		// add to JFrame
		add(JB1);
		add(JB2);
		add(JB3);
		add(JL);
		add(BGP);
		// ActionLister
		JB1.addActionListener(this);
		JB2.addActionListener(this);
		JB3.addActionListener(this);
		menu.i4.addActionListener(this);
		// show JFrame
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == JB1) {
			new GameStart();
			dispose();
		} else if (e.getSource() == JB2) {
			new GameTeach();
			dispose();
		} else if (e.getSource() == JB3 || e.getSource() == menu.i4) {
			dispose();
		}
	}

}

class GameTeach extends JFrame implements ActionListener, KeyListener {
	ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	ArrayList<Bonus> bonus = new ArrayList<Bonus>();
	Clip play;
	Cursor cr = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon("").getImage(), new Point(16, 16), "MyCursor");
	Boolean pause = false, bonuscheck = true, pausecheck = true;
	Timer background = new Timer(50, this);
	Timer playermove = new Timer(30, this);
	Timer playershoot = new Timer(150, this);
	Timer collistion = new Timer(5, this);
	Timer bulletmove = new Timer(20, this);
	Timer Bonuscreat = new Timer(1000, this);
	int x1, x2;
	int next = 3;
	int z, count, check;
	Image bufferImage;
	ImageIcon imagebackground = new ImageIcon("img//Sky.jpg");
	ImageIcon player1 = new ImageIcon("img//player1.png");
	ImageIcon teachmode = new ImageIcon("img//teachmode.png");
	ImageIcon teachmode1 = new ImageIcon("img//teachmode1.png");
	ImageIcon teachmode2 = new ImageIcon("img//teachmode2.png");
	ImageIcon pause1 = new ImageIcon("img//pause1.png");
	ImageIcon move1 = new ImageIcon("img//move1.png");
	ImageIcon move2 = new ImageIcon("img//move2.png");
	ImageIcon move3 = new ImageIcon("img//move3.png");
	ImageIcon bonusimage1 = new ImageIcon("img//bonusimage1.png");
	ImageIcon bonus1 = new ImageIcon("img//bonus1.png");
	Player player = new Player(player1.getImage(), 350, 500, 40, 42, 5);

	GameTeach() {
		super("Teach mode");
		setLayout(null);
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setCursor(cr);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		x1 = 0;
		x2 = -getHeight();
		z = 0;
		count = 0;
		check = 0;
		background.start();
		playermove.start();
		try {
			Gamesound();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(true);
	}

	public void Gamesound() throws Exception {
		File file = new File("music//k19.wav");
		// 取得音效黨的輸入串流
		AudioInputStream sound = AudioSystem.getAudioInputStream(file);
		// 將取得的輸入串留在入記憶體Clip
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		// 取得指定的Clip撥放棄
		play = (Clip) AudioSystem.getLine(info);
		play.open(sound);
		play.start();
		play.loop(-1);
	}

	public void shootsound() throws Exception {
		File file = new File("music//hit.wav");
		// 取得音效黨的輸入串流
		AudioInputStream sound = AudioSystem.getAudioInputStream(file);
		// 將取得的輸入串留在入記憶體Clip
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		// 取得指定的Clip撥放棄
		Clip shootsound1 = (Clip) AudioSystem.getLine(info);
		shootsound1.open(sound);
		shootsound1.start();
	}

	@Override
	public void keyPressed(KeyEvent e) {

		// TODO Auto-generated method stub
		Graphics g = getGraphics();
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			playermove.stop();
			z = 0;
			count = 0;
			check = 0;
			bonuscheck = true;
			pausecheck = true;
			player.x = 350;
			player.y = 500;
			player.weaponlevel = 1;
			bonus.clear();
			bullet.clear();
			playermove.restart();
		} else if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
			if (pause == false && pausecheck == true) {
				g.drawImage(pause1.getImage(), 280, 300, this);
				background.stop();
				playermove.stop();
				playershoot.stop();
				Bonuscreat.stop();
				bulletmove.stop();
				collistion.stop();
				pause = true;
			} else if (pause == true && pausecheck == true) {
				background.restart();
				playermove.restart();
				playershoot.restart();
				Bonuscreat.restart();
				bulletmove.restart();
				collistion.restart();
				pause = false;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			background.stop();
			playermove.stop();
			playershoot.stop();
			Bonuscreat.stop();
			bulletmove.stop();
			collistion.stop();
			bonus.clear();
			bullet.clear();
			play.stop();
			new GameMenu();
			dispose();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub
		Graphics g = getGraphics();
		update(g);
		if (e.getSource() == background) {
			x1++;
			x2++;
			if (x1 == getHeight())
				x1 = -getHeight();
			if (x2 == getHeight())
				x2 = -getHeight();
			// bonusMove
			for (int i = 0; i < bonus.size(); i++) {
				bonus.get(i).y += bonus.get(i).vy;
				bonus.get(i).x += bonus.get(i).vx;
			}
			for (int i = 0; i < bonus.size(); i++)
				if (bonus.get(i).y > getHeight())
					bonus.remove(i);
		}
		if (e.getSource() == playermove) {
			if (z == 1)
				player.x += next;
			else if (z == 2)
				player.x -= next;
			else if (z == 3)
				player.y -= next;
			else if (z == 4)
				player.y += next;
			else if (z == 5) {
				player.x += 2;
				player.y -= 2;
			} else if (z == 6) {
				player.x -= 2;
				player.y -= 2;
			} else if (z == 7) {
				player.x -= 2;
				player.y += 2;
			}

			else if (z == 8) {
				player.x += 2;
				player.y += 2;
			} else if (z == 9) {
				playermove.stop();
				playershoot.start();
				Bonuscreat.start();
				bulletmove.start();
				collistion.start();
			}
			count++;
			if (count % 60 == 0)
				z++;
		}
		if (e.getSource() == playershoot) {
			if (z == 9) {
				if (player.weaponchange == 1) {
					try {
						shootsound();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (player.weaponlevel == 1) {
						Bullet temp = new Bullet(player.x + 16, player.y, 6,
								10, 1, 0, 10, 1);
						bullet.add(temp);
					} else if (player.weaponlevel == 2)
						for (int i = 0; i < 2; i++) {
							Bullet temp = new Bullet(
									player.x + 16 - 8 + i * 16, player.y, 6,
									10, 1, 0, 10, 1);
							bullet.add(temp);
						}
					check++;
					if (check == 150) {
						background.stop();
						playermove.stop();
						Bonuscreat.stop();
						bulletmove.stop();
						collistion.stop();
						pausecheck = false;
						g.drawImage(teachmode1.getImage(), 250, 220, this);
						g.drawImage(teachmode2.getImage(), 230, 270, this);
						playershoot.stop();
					}
				}
			}
		}
		if (e.getSource() == bulletmove) {
			for (int i = 0; i < bullet.size(); i++) {
				bullet.get(i).x -= bullet.get(i).vx;
				bullet.get(i).y -= bullet.get(i).vy;
			}
			for (int i = 0; i < bullet.size(); i++)
				if (bullet.get(i).y < 0) {
					bullet.remove(i);
					break;
				}
		}
		if (e.getSource() == Bonuscreat) {
			if (z == 9 && bonuscheck == true) {
				Bonus temp = new Bonus(bonusimage1.getImage(), 1, 350, 0, 0, 3);
				bonus.add(temp);
				bonuscheck = false;
				Bonuscreat.stop();
			}
		}
		if (e.getSource() == collistion) {
			for (int i = 0; i < bonus.size(); i++)
				if ((bonus.get(i).x + 20 > player.x)
						&& (bonus.get(i).y + 10 > player.y)
						&& (bonus.get(i).x + 20 < player.x + player.width)
						&& (bonus.get(i).y + 10 < player.y + player.height)) {
					player.weaponlevel++;
					bonus.remove(i);
					break;
				}
		}
	}

	public void paint(Graphics g) {

		g.drawImage(imagebackground.getImage(), 0, x1, getWidth(), getHeight(),
				this);
		g.drawImage(imagebackground.getImage(), 0, x2, getWidth(), getHeight(),
				this);
		g.setColor(Color.yellow);
		for (int i = 0; i < bullet.size(); i++)
			g.fillRect(bullet.get(i).x, bullet.get(i).y, bullet.get(i).width,
					bullet.get(i).height);
		for (int i = 0; i < bonus.size(); i++)
			g.drawImage(bonus.get(i).image, bonus.get(i).x, bonus.get(i).y, 40,
					30, this);
		g.drawImage(player.image, player.x, player.y, this);
		if (z == 0) {
			g.drawImage(teachmode.getImage(), 280, 520, this);
			g.drawImage(teachmode1.getImage(), 250, 220, this);
			g.drawImage(teachmode2.getImage(), 230, 270, this);
		} else if (z == 1 || z == 2)
			g.drawImage(move1.getImage(), 280, 520, this);
		else if (z == 3 || z == 4)
			g.drawImage(move2.getImage(), 280, 520, this);
		else if (z == 5 || z == 6 || z == 7 || z == 8)
			g.drawImage(move3.getImage(), 280, 520, this);
		else if (z == 9)
			g.drawImage(bonus1.getImage(), 280, 520, this);
		if (z == 9 && check == 150) {
			g.drawImage(teachmode1.getImage(), 250, 220, this);
			g.drawImage(teachmode2.getImage(), 230, 270, this);
		}
		if (pause == true)
			g.drawImage(pause1.getImage(), 280, 300, this);
	}

	public void update(Graphics g)// double buffer
	{
		bufferImage = createImage(getWidth(), getHeight());
		Graphics gBuffer = bufferImage.getGraphics();
		if (gBuffer != null)
			paint(gBuffer);
		else
			paint(g);
		gBuffer.dispose();
		g.drawImage(bufferImage, 0, 0, null);
	}

}

class GameStart extends JFrame implements ActionListener, KeyListener {
	Image bufferImage;
	Random r1 = new Random();
	Random r2 = new Random();
	int score;
	int bomb;
	static Clip play;
	final Cursor cr = Toolkit.getDefaultToolkit().createCustomCursor(
			new ImageIcon("").getImage(), new Point(16, 16), "MyCursor");
	boolean up = false, down = false, left = false, right = false,
			bulletCheck = false, pause = false, pausecheck = true,
			bombcheck = true;
	ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	ArrayList<Bullet> ebullet = new ArrayList<Bullet>();
	ArrayList<Enemy> enemy = new ArrayList<Enemy>();
	ArrayList<Bonus> bonus = new ArrayList<Bonus>();
	int x1, x2;
	final int next = 7;
	int z;
	int ex, ey;
	Timer background = new Timer(50, this);
	Timer playermove = new Timer(15, this);
	Timer playershoot = new Timer(150, this);
	Timer enemyshoot = new Timer(500, this);
	Timer collistion = new Timer(5, this);
	Timer enemycreat = new Timer(100, this);
	Timer bulletmove = new Timer(20, this);
	// Timer tExplosion = new Timer(20, this);
	Timer Bonuscreat = new Timer(15000, this);
	Timer enemymove = new Timer(22, this);
	Timer random = new Timer(5, this);
	ImageIcon imagebackground = new ImageIcon("img//space3.jpg");
	ImageIcon player1 = new ImageIcon("img//player1.png");
	ImageIcon enemyimage = new ImageIcon("img//0.png");
	ImageIcon enemyimage1 = new ImageIcon("img//1.png");
	ImageIcon enemyimage2 = new ImageIcon("img//2.png");
	ImageIcon enemyimage3 = new ImageIcon("img//3.png");
	ImageIcon bonusimage1 = new ImageIcon("img//bonusimage1.png");
	ImageIcon pause2 = new ImageIcon("img//pause2.png");
	ImageIcon gameover = new ImageIcon("img//gameover.png");
	Player player = new Player(player1.getImage(), 280, 550, 40, 42, 50);

	GameStart() {
		super("Stage One");
		setLayout(null);
		setSize(800, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setCursor(cr);
		addKeyListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		x1 = 0;
		x2 = -getHeight();
		ex = 100;
		ey = 200;
		z=0;
		score = 0;
		bomb = 3;
		background.start();
		playermove.start();
		enemymove.start();
		bulletmove.start();
		enemycreat.start();
		random.start();
		collistion.start();
		playershoot.start();
		enemyshoot.start();
		Bonuscreat.start();
		setVisible(true);
		try {
			Gamesound();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shootsound() throws Exception {
		File file = new File("music//hit.wav");
		// 取得音效黨的輸入串流
		AudioInputStream sound = AudioSystem.getAudioInputStream(file);
		// 將取得的輸入串留在入記憶體Clip
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		// 取得指定的Clip撥放棄
		Clip shootsound1 = (Clip) AudioSystem.getLine(info);
		shootsound1.open(sound);
		shootsound1.start();
	}

	public void Gamesound() throws Exception {
		File file = new File("music//k19.wav");
		// 取得音效黨的輸入串流
		AudioInputStream sound = AudioSystem.getAudioInputStream(file);
		// 將取得的輸入串留在入記憶體Clip
		DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
		// 取得指定的Clip撥放棄
		play = (Clip) AudioSystem.getLine(info);
		play.open(sound);
		play.start();
		play.loop(-1);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Graphics g = getGraphics();
		update(g);
		int ptempx;
		int ptempy;
		int tempx = 0;
		int tempy = 10;
		if (e.getSource() == random) {
			ex = r1.nextInt(709) + 1;
			ey = r2.nextInt(200) + 1;
			for (int i = 0; i < ebullet.size(); i++) {
				if (ebullet.get(i).type == 2) {
					if (player.y - ebullet.get(i).y > 0) {
						ebullet.get(i).vx = (player.x + 20 - ebullet.get(i).x) / 100;
						ebullet.get(i).vy = (player.y + 20 - ebullet.get(i).y) / 100;
					}
					if ((player.y + 20 - ebullet.get(i).y) / 100 <= 0) {
						ebullet.get(i).vy = 3;
					}
					if ((player.x + 20 - ebullet.get(i).x) / 100 <= 0) {
						if (ebullet.get(i).x < player.x + 23)
							ebullet.get(i).vx = 2;
						else
							ebullet.get(i).vx = -2;
					}
				}
			}
		}
		if (e.getSource() == background)// background
		{
			x1++;
			x2++;
			if (x1 == getHeight())
				x1 = -getHeight();
			if (x2 == getHeight())
				x2 = -getHeight();
			// bonusMove
			for (int i = 0; i < bonus.size(); i++)
				bonus.get(i).y += 10;
			for (int i = 0; i < bonus.size(); i++)
				if (bonus.get(i).y > getHeight())
					bonus.remove(i);
		}
		if (e.getSource() == enemymove) {
			for (int i = 0; i < enemy.size(); i++) {
				enemy.get(i).x += enemy.get(i).vx;
				enemy.get(i).y += enemy.get(i).vy;
			}
			for (int i = 0; i < enemy.size(); i++) {
				if (enemy.get(i).x < 0 || enemy.get(i).x + 70 > 800
						|| enemy.get(i).y < 0 || enemy.get(i).y + 60 > 270) {
					int temp = enemy.get(i).vx;
					enemy.get(i).vx = -temp;
					temp = enemy.get(i).vy;
					enemy.get(i).vy = -temp;
				}
			}
		}
		if (e.getSource() == playermove) {
			ptempx = player.x;
			ptempy = player.y;
			if (up == true && left == false && right == false && down == false)
				player.y -= next;
			if (down == true && right == false && left == false && up == false)
				player.y += next;
			if (left == true && up == false && down == false && right == false)
				player.x -= next;
			if (right == true && up == false && down == false && left == false)
				player.x += next;
			if (up == true && left == true && down == false && right == false) {
				player.y -= 5;
				player.x -= 5;
			}
			if (up == true && right == true && down == false && left == false) {
				player.y -= 5;
				player.x += 5;
			}
			if (down == true && left == true && up == false && right == false) {
				player.y += 5;
				player.x -= 5;
			}
			if (down == true && right == true && up == false && left == false) {
				player.y += 5;
				player.x += 5;
			}
			if (player.x < 0 || player.x > 760 || player.y < 270
					|| player.y > 560) {// can't move
				player.x = ptempx;
				player.y = ptempy;
			}

		}
		if (e.getSource() == enemyshoot) {
			for (int i = 0; i < enemy.size(); i++) {
				if (enemy.get(i).type == 1) {
					Bullet temp = new Bullet(enemy.get(i).x + 40,
							enemy.get(i).y + 70, 6, 10, 2, 0, 5, 1);
					ebullet.add(temp);
				} else if (enemy.get(i).type == 2) {
					Bullet temp = new Bullet(enemy.get(i).x + 40,
							enemy.get(i).y + 70, 6, 10, 1, 0, 3, 2);
					ebullet.add(temp);
				} else if (enemy.get(i).type == 3) {
					if (enemy.get(i).x + 40 < player.x + 20) {
						tempx = 6;
						tempy = 8;
					} else if (enemy.get(i).x + 40 > player.x + 20) {
						tempx = -6;
						tempy = 8;
					}
					Bullet temp = new Bullet(enemy.get(i).x + 40,
							enemy.get(i).y + 70, 6, 10, 4, tempx, tempy, 3);
					ebullet.add(temp);
				}
			}
		}
		if (e.getSource() == playershoot) {
			if (bulletCheck == true) {
				if (player.weaponchange == 1) {
					try {
						shootsound();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (player.weaponlevel == 1) {
						Bullet temp = new Bullet(player.x + 16, player.y, 6,
								10, 1, 0, 10, 1);
						bullet.add(temp);
					} else if (player.weaponlevel == 2)
						for (int i = 0; i < 2; i++) {
							Bullet temp = new Bullet(
									player.x + 16 - 8 + i * 16, player.y, 6,
									10, 1, 0, 10, 1);
							bullet.add(temp);
						}
					else if (player.weaponlevel == 3) {
						for (int i = 0; i < 2; i++) {
							Bullet temp = new Bullet(
									player.x + 16 - 8 + i * 16, player.y, 6,
									10, 1, 0, 10, 1);
							bullet.add(temp);
						}
						Bullet temp = new Bullet(player.x + 16 - 12,
								player.y + 10, 6, 10, 1, 6, 8, 1);
						bullet.add(temp);
						temp = new Bullet(player.x + 16 - 12 + 24,
								player.y + 10, 6, 10, 1, -6, 8, 1);
						bullet.add(temp);
					}
				}
			}
		}
		if (e.getSource() == bulletmove) {
			for (int i = 0; i < bullet.size(); i++) {
				bullet.get(i).x -= bullet.get(i).vx;
				bullet.get(i).y -= bullet.get(i).vy;
			}
			for (int i = 0; i < bullet.size(); i++)
				if (bullet.get(i).y < 0) {
					bullet.remove(i);
					break;
				}
			for (int i = 0; i < ebullet.size(); i++) {
				ebullet.get(i).x += ebullet.get(i).vx;
				ebullet.get(i).y += ebullet.get(i).vy;
			}
			for (int i = 0; i < ebullet.size(); i++)
				if (ebullet.get(i).y > 600) {
					ebullet.remove(i);
					break;
				}

		}
		if (e.getSource() == collistion) {
			for (int i = 0; i < enemy.size(); i++)
				for (int j = 0; j < bullet.size(); j++) {
					if (bullet.get(j).x + bullet.get(j).width / 2 < enemy
							.get(i).x + enemy.get(i).width
							&& bullet.get(j).x + bullet.get(j).width / 2 > enemy
									.get(i).x
							&& bullet.get(j).y + bullet.get(j).height / 2 > enemy
									.get(i).y
							&& bullet.get(j).y + bullet.get(j).height / 2 < enemy
									.get(i).y + enemy.get(i).height) {
						enemy.get(i).hp = enemy.get(i).hp
								- bullet.get(j).damage;
						bullet.remove(j);
						if (enemy.get(i).hp <= 0) {
							// enemy.get(i).image = enemyimage1.getImage();
							if (enemy.get(i).type == 1)
								score += 100;
							else if (enemy.get(i).type == 2)
								score += 300;
							else if (enemy.get(i).type == 3)
								score += 500;
							enemy.remove(i);
							break;
						}
						break;
					}

				}
			for (int i = 0; i < bonus.size(); i++)
				if ((bonus.get(i).x + 20 > player.x)
						&& (bonus.get(i).y + 10 > player.y)
						&& (bonus.get(i).x + 20 < player.x + player.width)
						&& (bonus.get(i).y + 10 < player.y + player.height)) {
					if (player.weaponlevel < 3)
						player.weaponlevel++;
					bonus.remove(i);
					break;
				}
			// GameOver
			for (int i = 0; i < ebullet.size(); i++) {
				if (ebullet.get(i).x + ebullet.get(i).width / 2 < player.x + 40
						&& ebullet.get(i).x + ebullet.get(i).width / 2 > player.x
						&& ebullet.get(i).y + ebullet.get(i).height / 2 > player.y
						&& ebullet.get(i).y + ebullet.get(i).height / 2 < player.y + 42) {
					player.hp = player.hp - ebullet.get(i).damage;
					ebullet.remove(i);
					break;
				}
			}
			if (player.hp <= 0) {
				player.hp=0;
				pausecheck = false;
				background.stop();
				playermove.stop();
				enemymove.stop();
				bulletmove.stop();
				enemycreat.stop();
				playershoot.stop();
				enemyshoot.stop();
				Bonuscreat.stop();
				random.stop();
				g.drawImage(gameover.getImage(), 240, 250, this);
				g.setColor(Color.ORANGE);
				g.setFont(new Font("標楷體", Font.BOLD, 30));
				g.drawString("Your score is : " + score, 260, 400);
				//collistion.stop();
			}
		}
		if (e.getSource() == Bonuscreat) {
			Bonus temp = new Bonus(bonusimage1.getImage(), 1, 300, 0, 0, 5);
			bonus.add(temp);
		}
		if (e.getSource() == enemycreat) {
			if (z % 10 == 0) {
				Enemy temp = new Enemy(enemyimage.getImage(), ex, ey, 80, 80,
						8, 2, 0, 1);
				enemy.add(temp);
			}
			if (z >= 120) {
				if (z % 70 == 0) {
					Enemy temp = new Enemy(enemyimage1.getImage(), ex, ey, 80,
							80, 3, 2, 0, 2);
					enemy.add(temp);
				}
			}
			if (z >= 300) {
				if (z % 100 == 0) {
					Enemy temp = new Enemy(enemyimage2.getImage(), ex, ey, 80,
							80, 16, 1, 0, 3);
					enemy.add(temp);
				}
			}
			z++;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		Graphics g = getGraphics();
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w')
			up = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's')
			down = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a')
			left = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd')
			right = true;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			bulletCheck = true;
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (pause == false && pausecheck == true) {
				background.stop();
				playermove.stop();
				enemymove.stop();
				bulletmove.stop();
				enemycreat.stop();
				collistion.stop();
				playershoot.stop();
				enemyshoot.stop();
				Bonuscreat.stop();
				random.stop();
				g.drawImage(pause2.getImage(), 240, 250, this);
				pause = true;
			} else if (pause == true && pausecheck == true) {
				background.restart();
				playermove.restart();
				enemymove.restart();
				bulletmove.restart();
				enemycreat.restart();
				collistion.restart();
				playershoot.restart();
				enemyshoot.restart();
				Bonuscreat.restart();
				random.restart();
				pause = false;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			background.stop();
			playermove.stop();
			enemymove.stop();
			bulletmove.stop();
			enemycreat.stop();
			collistion.stop();
			playershoot.stop();
			enemyshoot.stop();
			Bonuscreat.stop();
			random.stop();
			bullet.clear();
			ebullet.clear();
			enemy.clear();
			bonus.clear();
			play.stop();
			pausecheck = true;
			new GameMenu();
			dispose();
		}
		if (e.getKeyCode() == KeyEvent.VK_CONTROL && bombcheck == true
				&& bomb > 0) {
			bomb--;
			ebullet.clear();
			enemy.clear();
			playermove.stop();
			enemymove.stop();
			bulletmove.stop();
			enemycreat.stop();
			collistion.stop();
			playershoot.stop();
			enemyshoot.stop();
			Bonuscreat.stop();
			random.stop();
			bombcheck = false;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w')
			up = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's')
			down = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a')
			left = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd')
			right = false;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			bulletCheck = false;
		if (e.getKeyCode() == KeyEvent.VK_CONTROL && bombcheck == false) {
			playermove.restart();
			enemymove.restart();
			bulletmove.restart();
			enemycreat.restart();
			collistion.restart();
			playershoot.restart();
			enemyshoot.restart();
			Bonuscreat.restart();
			random.restart();
			bombcheck = true;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		g.setFont(new Font("標楷體", Font.BOLD, 30));
		g.drawImage(imagebackground.getImage(), 0, x1, getWidth(), getHeight(),
				this);
		g.drawImage(imagebackground.getImage(), 0, x2, getWidth(), getHeight(),
				this);
		for (int i = 0; i < ebullet.size(); i++) {
			if (ebullet.get(i).type == 1) {
				g.setColor(Color.white);
				g.fillRect(ebullet.get(i).x, ebullet.get(i).y,
						ebullet.get(i).width, ebullet.get(i).height);
			} else if (ebullet.get(i).type == 2) {
				g.setColor(new Color(55, 243, 29));
				g.fillRect(ebullet.get(i).x, ebullet.get(i).y,
						ebullet.get(i).width, ebullet.get(i).height);
			} else if (ebullet.get(i).type == 3) {
				g.setColor(Color.blue);
				g.fillRect(ebullet.get(i).x, ebullet.get(i).y,
						ebullet.get(i).width, ebullet.get(i).height);
			}

		}
		g.setColor(Color.yellow);
		for (int i = 0; i < bullet.size(); i++)
			g.fillRect(bullet.get(i).x, bullet.get(i).y, bullet.get(i).width,
					bullet.get(i).height);
		for (int i = 0; i < bonus.size(); i++)
			g.drawImage(bonus.get(i).image, bonus.get(i).x, bonus.get(i).y, 40,
					30, this);

		for (int i = 0; i < enemy.size(); i++)
			g.drawImage(enemy.get(i).image, enemy.get(i).x, enemy.get(i).y,
					enemy.get(i).width, enemy.get(i).height, this);

		g.drawImage(player.image, player.x, player.y, this);
		if (pause == true)
			g.drawImage(pause2.getImage(), 240, 250, this);
		else if (pausecheck == false) {
			g.drawImage(gameover.getImage(), 240, 250, this);
			g.setColor(Color.ORANGE);
			g.drawString("Your score is : " + score, 260, 400);
		}
		if (bombcheck == false) {
			g.setColor(Color.white);
			g.fillRect(0, 0, 800, 600);
		}
		g.drawString("HP : " + player.hp, 20, 500);
		g.drawString("BOMB : " + bomb, 20, 550);
	}

	public void update(Graphics g)// double buffer
	{
		bufferImage = createImage(getWidth(), getHeight());
		Graphics gBuffer = bufferImage.getGraphics();
		if (gBuffer != null)
			paint(gBuffer);
		else
			paint(g);
		gBuffer.dispose();
		g.drawImage(bufferImage, 0, 0, null);
	}
}

class Bonus {
	int weaponchange;
	Image image;
	int x, y;
	int vx, vy;

	Bonus(Image image, int weaponchange, int x, int y, int vx, int vy) {
		this.image = image;
		this.weaponchange = weaponchange;
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
}

class Enemy {
	int x, y, width, height, hp, vx, vy, type;
	Image image;

	Enemy(Image image, int x, int y, int width, int height, int hp, int vx,
			int vy, int type) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hp = hp;
		this.vx = vx;
		this.vy = vy;
		this.type = type;
	}
}

class Bullet {
	int x, y, width, height, damage, vx, vy, type;
	Image image;

	Bullet(int x, int y, int width, int height, int damage, int vx, int vy,
			int type) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.damage = damage;
		this.vx = vx;
		this.vy = vy;
		this.type = type;
	}
}

class Player {
	int x, y, width, height, hp, weaponchange, weaponlevel;
	Image image;

	Player(Image image, int x, int y, int width, int height, int hp) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hp = hp;
		weaponchange = 1;
		weaponlevel = 1;
	}
}

class Menu extends JMenuBar {
	JMenu system = new JMenu("System");
	JMenuItem i1 = new JMenuItem("Save");
	JMenuItem i2 = new JMenuItem("Load");
	JMenuItem i3 = new JMenuItem("High Score");
	JMenuItem i4 = new JMenuItem("Exit");

	Menu() {
		system.add(i1);
		system.add(i2);
		system.addSeparator();
		system.add(i3);
		system.add(i4);
		add(system);
		setOpaque(false);
		setBackground(null);
	}
}

class BackgroundPanel extends JPanel {
	Image im;

	public BackgroundPanel(Image im) {
		this.im = im;
		this.setOpaque(true);
	}

	// Draw the back ground.
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		g.drawImage(im, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
