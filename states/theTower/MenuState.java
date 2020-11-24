package theTower;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import theTower.Engine;

public class MenuState extends JFrame implements ActionListener, MouseListener {
	private static final long serialVersionUID = 1L;
	private static int width, height, currentBest;
	private static JFrame f;
	private static ArrayList<JButton> buttons = new ArrayList<>();
	private static ArrayList<JLabel> stars = new ArrayList<>();
	private static int spacingWidth = 100, spacingHeight = 5, borderWidth = 2;
	private static int bestWaveNumber = 0;
	private boolean bIsFirstGame = true;
	static boolean playing;
	static JLabel title, scoreLbl, score;
	static BufferedImage sPic, sPicR;
	JLabel instruct1, instruct2, instruct3, princess;
	JLabel fullStar;
	JLabel emptyStar, emptyStar2;
	JLabel playerMove, playerHit;
	static JPanel starPanel = new JPanel();
	static JPanel scorePanel, mainPanel, titlePanel;
	JPanel instructionPanel, instructLvl1, instructLvl2, instructLvl3, instructLvl4;
	Color grey = Color.decode("#333333");
	Color white = Color.decode("#FFFFFF");
	Color purple = Color.decode("#9400D3");
	Color color5 = Color.decode("#FF0000");
	GameState G;
	Dimension d;
	Color defaultGrey;
	Border mouseHoverBorder, greyBorder, noBorder;
	Font centurySmall, centuryLarge;
	JButton gotIt;

	public MenuState() {
		System.out.println("MenuState has been opened.");

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		width = screen.width;
		height = screen.height;
		currentBest = 0;
		d = new Dimension(350, 100);
		centurySmall = new Font("Century Gothic", Font.PLAIN, 25);
		centuryLarge = new Font("Century Gothic", Font.BOLD, 60);
		noBorder = new LineBorder(white, borderWidth);
		greyBorder = new LineBorder(grey, borderWidth);
		// Create menu frame
		f = new JFrame("The Tower");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loadImages();
		createMainPanel();
		createTitlePanel();
		makeScorePanel();
		makeButtons();

		// Load the number of stars that the user has achieved
		loadStars();

		// Construction
		mainPanel.add(Box.createRigidArea(new Dimension(spacingWidth, 30)));

		try {
			// Add flame gif
			Icon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("src/Images/fire.gif"));
			JLabel fireIcon = new JLabel(icon);

			// Get playerMove gif
			Icon move = new ImageIcon(Toolkit.getDefaultToolkit().createImage("src/Images/playerMove.gif"));
			playerMove = new JLabel(move, SwingConstants.CENTER);

			// Get playerHit gif
			Icon hit = new ImageIcon(Toolkit.getDefaultToolkit().createImage("src/Images/playerHit.gif"));
			playerHit = new JLabel(hit, SwingConstants.CENTER);

			titlePanel.add(Box.createRigidArea(new Dimension(40, 90)));
			titlePanel.add(fireIcon, BorderLayout.CENTER);
		} catch (Exception e) {
			System.out.println("Could not load gif.");
		}

		mainPanel.add(titlePanel);
		mainPanel.add(Box.createRigidArea(new Dimension(100, 30)));
		mainPanel.add(scorePanel);
		mainPanel.add(Box.createRigidArea(new Dimension(100, 30)));

		// Add buttons to menu panel
		for (JButton j : buttons) {
			mainPanel.add(j);
			mainPanel.add(Box.createRigidArea(new Dimension(spacingWidth, spacingHeight)));
		}

		defaultGrey = title.getForeground();

		f.add(mainPanel);
		fullScreen(f);

		f.setVisible(true);
		makeInstructionPanel();
	}

	private void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBackground(white);
	}

	private void createTitlePanel() {
		titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setMaximumSize(new Dimension(width, 75));
		title = new JLabel("The Tower");
		title.setSize(200, 50);
		title.setFont(centuryLarge);
		title.setForeground(grey);
		titlePanel.setBackground(white);
		try {
			// Add flame gif
			Icon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage("src/Images/fire.gif"));
			JLabel center = new JLabel(icon);

			titlePanel.add(center, BorderLayout.CENTER);
			titlePanel.add(Box.createRigidArea(new Dimension(40, 90)));
		} catch (Exception e) {
			System.out.println("Could not load gif.");
		}

		titlePanel.add(title);
	}

	private void makeScorePanel() {
		scorePanel = new JPanel();
		scorePanel.setMaximumSize(new Dimension(width, 90));
		scoreLbl = new JLabel("Best Score: ");
		score = new JLabel(Integer.toString(currentBest));

		scorePanel.add(scoreLbl);
		scorePanel.add(score);

		score.setFont(new Font("Century Gothic", Font.BOLD, 25));
		scoreLbl.setFont(new Font("Century Gothic", Font.PLAIN, 25));
		score.setForeground(Color.decode("#9400D3"));
	}

	private void loadImages() {
		try {
			sPic = ImageIO.read(new File("src/Images/fullMenuStar.png"));
			sPicR = ImageIO.read(new File("src/Images/emptyMenuStar.png"));
			BufferedImage in1 = ImageIO.read(new File("src/Images/instruct1.png"));
			BufferedImage in2 = ImageIO.read(new File("src/Images/instruct2.png"));
			BufferedImage in3 = ImageIO.read(new File("src/Images/instruct3.png"));
			BufferedImage prin = ImageIO.read(new File("src/Images/princess.png"));

			fullStar = new JLabel(new ImageIcon(sPic));
			emptyStar = new JLabel(new ImageIcon(sPicR));
			instruct1 = new JLabel(new ImageIcon(in1));
			instruct2 = new JLabel(new ImageIcon(in2));
			instruct3 = new JLabel(new ImageIcon(in3));
			princess = new JLabel(new ImageIcon(prin));
		} catch (Exception e) {
			System.out.println("Could not load menu images.");
		}
	}

	private static void loadStars() {
		int max = bestWaveNumber / 3;

		starPanel.removeAll();
		stars.clear();
		for (int i = 0; i < max; i++) {
			// Make full stars
			stars.add(new JLabel(new ImageIcon(sPic)));
		}
		for (int i = 0; i < 5 - max; i++) {
			// Make empty stars
			stars.add(new JLabel(new ImageIcon(sPicR)));
		}
		starPanel.add(Box.createRigidArea(new Dimension(50, 65)));
		for (JLabel s : stars) {
			starPanel.add(s);
		}
		scorePanel.add(starPanel);
	}

	private void makeButtons() {
		buttons.add(new JButton("Play Game"));
		buttons.add(new JButton("How To Play"));
		buttons.add(new JButton("Exit"));
		for (JButton j : buttons) {
			j.addActionListener(this);
			j.addMouseListener(this);
			j.setFocusable(false);
			j.setMaximumSize(d);
			j.setAlignmentX(Component.CENTER_ALIGNMENT);
			j.setBackground(white);
			j.setBorder(noBorder);
			if (j.getText() == "Exit") {
				j.setFont(new Font("Century Gothic", Font.BOLD, 25));
			} else {
				j.setFont(centurySmall);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand() == "Exit") {
			System.exit(0);
		} else if (e.getActionCommand() == "How To Play") {

			// Remove unnecessary items from JFrame
			mainPanel.remove(scorePanel);
			for (JButton b : buttons) {
				b.setForeground(defaultGrey);
				b.setBorder(noBorder);
				b.setBackground(white);
				mainPanel.remove(b);
			}
			// Display instructions
			mainPanel.add(instructionPanel);

			mainPanel.revalidate();
			f.repaint();

		} else if (e.getActionCommand() == "Play Game") {
			boolean bPlayIntro = bIsFirstGame;
			bIsFirstGame = false;
			new GameState(bPlayIntro);
		} else if (e.getActionCommand() == "Got it!") {
			// Add back removed items
			mainPanel.remove(instructionPanel);
			mainPanel.add(scorePanel);
			for (JButton b : buttons) {
				mainPanel.add(b);
			}
			if (e.getSource() == gotIt) {
				gotIt.setForeground(defaultGrey);
				gotIt.setBorder(noBorder);
				gotIt.setBackground(white);
			}
			f.repaint();
		}
	}

	private void makeInstructionPanel() {

		instructionPanel = new JPanel();
		instructionPanel.setBackground(white);

		instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
		instructionPanel.setMaximumSize(new Dimension(width, height / 2 + (height / 4)));
		instructLvl1 = new JPanel();
		instructLvl1.setBackground(white);
		instructLvl2 = new JPanel();
		instructLvl2.setBackground(white);
		instructLvl3 = new JPanel();
		instructLvl3.setBackground(white);

		// Add instructions
		try {
			// First instructions
			instructLvl1.add(instruct1);
			instructLvl1.add(Box.createRigidArea(new Dimension(50, 20)));
			instructLvl1.add(playerMove);
			instructionPanel.add(instructLvl1);
			instructionPanel.add(Box.createRigidArea(new Dimension(width, 100)));

			// Second instructions
			instructLvl2.add(instruct2);
			instructLvl2.add(Box.createRigidArea(new Dimension(50, 20)));
			instructLvl2.add(playerHit);
			instructionPanel.add(instructLvl2);
			instructionPanel.add(Box.createRigidArea(new Dimension(width, 100)));

			// Third instructions
			instructLvl3.add(instruct3);
			instructLvl3.add(Box.createRigidArea(new Dimension(50, 20)));
			instructLvl3.add(princess);
			instructionPanel.add(instructLvl3);
			instructionPanel.add(Box.createRigidArea(new Dimension(width, 100)));

			// Exit button
			gotIt = new JButton("Got it!");
			gotIt.addActionListener(this);
			gotIt.addMouseListener(this);
			gotIt.setFocusable(false);
			gotIt.setMaximumSize(d);
			gotIt.setPreferredSize(d);

			gotIt.setAlignmentX(Component.CENTER_ALIGNMENT);
			gotIt.setBackground(white);
			gotIt.setBorder(noBorder);
			gotIt.setFont(centurySmall);

			instructionPanel.add(gotIt);
			instructionPanel.add(Box.createRigidArea(new Dimension(width, 100)));
		} catch (Exception e) {
			System.out.println("Could not display instructions.");
			System.out.println(e);
		}
	}

	public static void getCurrentScore(int score, int waveNumberAchieved) {
		if (score > currentBest) {
			currentBest = score;
			MenuState.score.setText(Integer.toString(currentBest));
			bestWaveNumber = waveNumberAchieved;
			loadStars();
		}
	}
		public static void fullScreen(JFrame j) {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		j.setSize(screen.width, screen.height);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == buttons.get(0)) {
			buttons.get(0).setBorder(greyBorder);
			buttons.get(0).setForeground(purple);
		} else if (e.getSource() == buttons.get(1)) {
			buttons.get(1).setBorder(greyBorder);
			buttons.get(1).setForeground(purple);
		} else if (e.getSource() == buttons.get(2)) {
			buttons.get(2).setBorder(new LineBorder(color5, borderWidth));
			buttons.get(2).setForeground(color5);
		} else if (e.getSource() == gotIt) {
			gotIt.setBorder(new LineBorder(purple, borderWidth));
			gotIt.setForeground(purple);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		for (JButton j : buttons) {
			j.setForeground(defaultGrey);
			j.setBorder(noBorder);
			j.setBackground(white);
		}
		if (e.getSource() == gotIt) {
			gotIt.setForeground(defaultGrey);
			gotIt.setBorder(noBorder);
			gotIt.setBackground(white);
		}
	}
}
