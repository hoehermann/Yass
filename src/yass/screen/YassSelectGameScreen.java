package yass.screen;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;

/**
 *  Description of the Class
 *
 * @author     Saruta
 * @created    4. September 2006
 */
public class YassSelectGameScreen extends YassScreen {
	private static final long serialVersionUID = -8982467170029740808L;
	private int player[] = new int[MAX_PLAYERS];
	private boolean selected[] = new boolean[MAX_PLAYERS];
	private int selectedItem = 0;


	/**
	 *  Gets the iD attribute of the YassScoreScreen object
	 *
	 * @return    The iD value
	 */
	public String getID() {
		return "selectgame";
	}


	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String nextScreen() {
		return "selectcontrol";
	}


	/**
	 *  Description of the Method
	 */
	public void init() {
		loadBackgroundImage("plain_background.jpg");
	}


	/**
	 *  Description of the Method
	 */
	public void show() {
		loadBackgroundImage("plain_background.jpg");
		selectedItem = getProperties().getProperty("game_mode").equals("lines") ? 0 : 1;
		for (int t = 0; t < MAX_PLAYERS; t++) {
			selected[t] = false;
			player[t] = selectedItem;
		}
		startTimer(20);
	}


	/**
	 *  Description of the Method
	 */
	public void hide() {
		stopTimer();
	}


	/**
	 *  Description of the Method
	 */
	public void storeGame() {
		if (selectedItem == 0) {
			getProperties().put("game_mode", "lines");
		}
		if (selectedItem == 1) {
			getProperties().put("game_mode", "keyboard");
		}
		getProperties().store();
	}


	/**
	 *  Description of the Method
	 *
	 * @param  key  Description of the Parameter
	 * @return      Description of the Return Value
	 */
	public boolean keyPressed(int key) {
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (selected[t] && (key == UP[t] || key == LEFT[t] || key == DOWN[t] || key == RIGHT[t] || key == SELECT[t])) {
				return true;
			}
			if (key == UP[t] || key == LEFT[t]) {
				player[t]--;
				if (player[t] < 0) {
					player[t] = 0;
				}
				updateSelectedItem();
				getTheme().playSample("menu_navigation.wav", false);
				repaint();
				return true;
			}
			if (key == DOWN[t] || key == RIGHT[t]) {
				player[t]++;
				if (player[t] > 1) {
					player[t] = 1;
				}
				updateSelectedItem();
				getTheme().playSample("menu_navigation.wav", false);
				repaint();
				return true;
			}
			if (key == SELECT[t]) {
				selected[t] = true;
				getTheme().playSample("menu_selection.wav", false);
				repaint();

				for (int t2 = 0; t2 < MAX_PLAYERS; t2++) {
					if (isPlayerActive(t2) && !selected[t2]) {
						return true;
					}
				}
				storeGame();
				gotoScreen(nextScreen());
				return true;
			}
		}
		return false;
	}


	/**
	 *  Description of the Method
	 */
	public void updateSelectedItem() {
		int count[] = new int[2];
		count[0] = count[1] = 0;
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (isPlayerActive(t)) {
				count[player[t]]++;
			}
		}
		if (count[0] > count[1]) {
			selectedItem = 0;
		} else if (count[0] < count[1]) {
			selectedItem = 1;
		} else {
			//selectedItem = (int) (Math.random() * 2);
		}
	}


	/**
	 *  Description of the Method
	 *
	 * @param  g  Description of the Parameter
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		int w = getSize().width;
		int h = getSize().height;

		Font f = getTextFont();
		g2.setFont(f);
		g2.setStroke(getThinStroke());
		FontMetrics m = g2.getFontMetrics();

		int x = w / 3;
		int y = h / 2;

		String s = getString("lines");
		if (selectedItem != 0) {
			TextLayout txtLayout = new TextLayout(s, f, g2.getFontRenderContext());
			float sw = (float) txtLayout.getBounds().getWidth();
			AffineTransform transform = new AffineTransform();
			transform.setToTranslation(x - sw / 2, y);
			g2.setColor(getTheme().getColor(2));
			g2.draw(txtLayout.getOutline(transform));
		} else {
			int sw = m.stringWidth(s);
			g2.setColor(getTheme().getColor(3));
			g2.drawString(s, x - sw / 2, y);
		}
		int d = 0;
		int dc = 0;
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (isPlayerActive(t) && player[t] == 0) {
				dc++;
			}
		}
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (isPlayerActive(t) && player[t] == 0) {
				g.drawString(getTheme().getPlayerSymbol(t, selected[t]), x - dc * 15 + (d++) * 30, y + 30);
			}
		}

		x = w * 2 / 3;

		s = getString("keyboard");
		if (selectedItem != 1) {
			TextLayout txtLayout = new TextLayout(s, f, g2.getFontRenderContext());
			float sw = (float) txtLayout.getBounds().getWidth();
			AffineTransform transform = new AffineTransform();
			transform.setToTranslation(x - sw / 2, y);
			g2.setColor(getTheme().getColor(2));
			g2.draw(txtLayout.getOutline(transform));
		} else {
			int sw = m.stringWidth(s);
			g2.setColor(getTheme().getColor(3));
			g2.drawString(s, x - sw / 2, y);
		}
		d = 0;
		dc = 0;
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (isPlayerActive(t) && player[t] == 1) {
				dc++;
			}
		}
		for (int t = 0; t < MAX_PLAYERS; t++) {
			if (isPlayerActive(t) && player[t] == 1) {
				g.drawString(getTheme().getPlayerSymbol(t, selected[t]), x - dc * 15 + (d++) * 30, y + 30);
			}
		}
	}
}
