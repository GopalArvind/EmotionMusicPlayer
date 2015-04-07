import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class MusicPlayer {
	JFrame musicPlayer;
	JPanel musicControls;
	JButton playButton;
	JProgressBar progressBar;
	
	boolean isPlaying;
	
	AudioInputStream ais;
	Clip clip;
	File musicFile;
	
	Media media;
	MediaPlayer mp;
	
	public MusicPlayer() {
		JFXPanel fxPanel = new JFXPanel();	//Just to initialize Java FX.
		isPlaying = false;
		musicFile = new File("C:\\Temporary\\Input (From Phone or ext-HDD)\\Music\\Simple Plan\\Astronaut.mp3");
	}
	
	public static void main(String[] args) {
		new MusicPlayer().start();
	}
	
	public void start() {
		createFrame();
		
		musicControls = getNewPanel(BoxLayout.X_AXIS);
		playButton = getNewButton("Play", new PlayButtonListener());
		progressBar = getNewProgressBar();
		musicControls.add(playButton);
		musicControls.add(getNewButton("Stop",	new StopButtonListener()));
		musicControls.add(progressBar);
		
		musicPlayer.getContentPane().add(musicControls);
		musicPlayer.setVisible(true);
	}
	
	public JButton getNewButton(String buttonText, ActionListener listener) {
		JButton button = new JButton(buttonText);
		button.addActionListener(listener);
		
		return button;
	}
	
	public JProgressBar getNewProgressBar() {
		JProgressBar progress = new JProgressBar();
		progress.setValue(0);
		progress.addMouseListener(new SeekerListener());
		
		return progress;
	}
	
	public JPanel getNewPanel(int orientation) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, orientation));
		
		return panel;
	}
	
	public void createFrame() {
		musicPlayer = new JFrame("Emotion Music Player!");
		musicPlayer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		musicPlayer.setSize(400, 500);
		musicPlayer.setLocationRelativeTo(null);
	}
	
	public class PlayButtonListener implements ActionListener {
		public PlayButtonListener() {
			try {
//				ais = AudioSystem.getAudioInputStream(musicFile);
//				clip = AudioSystem.getClip();
//				clip.open(ais);
//				clip.setFramePosition(0);
				media = new Media(musicFile.toURI().toURL().toExternalForm());
				mp = new MediaPlayer(media);
				mp.seek(Duration.ZERO);
//				progressBar.setValue(0);
//				progressBar.setMaximum((int) (mp.getTotalDuration().toSeconds()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void actionPerformed(ActionEvent ae) {
			isPlaying = !isPlaying;
			
			if(isPlaying) {
				playButton.setText("Pause");
				mp.play();
//				clip.start();
			}
			else {
				playButton.setText("Play");
				mp.pause();
//				clip.stop();
			}
		}
	}
	
	public class StopButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
//			clip.close();
			playButton.setText("Play");
			mp.stop();
			mp.seek(Duration.ZERO);
		}
	}
	
	public class SeekerListener implements MouseListener {
		public void stateChanged(ChangeEvent ce) {
			int value = progressBar.getValue();
			System.out.println(value);
			mp.seek(new Duration(value));
		}
		
		public void mouseClicked(MouseEvent arg0) {
			
		}

		public void mouseEntered(MouseEvent arg0) {
			
		}

		public void mouseExited(MouseEvent arg0) {
			
		}

		public void mousePressed(MouseEvent arg0) {
			
		}

		public void mouseReleased(MouseEvent arg0) {
			
		}
	}
}
