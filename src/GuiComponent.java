import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GuiComponent {
	GuiComponent gui;
	
	public GuiComponent() {
		gui = this;
	}
	
	public void showGui() {
		final JFrame frame = new JFrame("Choose an option.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setLocationRelativeTo(null);
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		//Adding buttons.
		JButton train = new JButton("Training Mode");
		train.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		train.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				EmotionDetector.mode = "Training";
				System.out.println(EmotionDetector.mode);
				frame.dispose();
				
				selectImage();
				
				//New frame
				final JFrame emotionFrame = new JFrame("Training frame.");
				emotionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				emotionFrame.setSize(400, 200);
				emotionFrame.setLocationRelativeTo(null);
				JPanel p = new JPanel();
				p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
				p.add(new JLabel("Enter the emotion of image"));
				final JTextField text = new JTextField(10);
				text.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
				text.addKeyListener(new KeyListener() {

					@Override
					public void keyPressed(KeyEvent arg0) {
						
					}

					@Override
					public void keyReleased(KeyEvent arg0) {
						if(arg0.getKeyChar() == '\n') {
							EmotionDetector.emotion = text.getText();
							System.out.println(EmotionDetector.emotion);
							emotionFrame.dispose();
							synchronized(gui) {
								gui.notify();
							}
							
						}
					}

					@Override
					public void keyTyped(KeyEvent arg0) {
						
					}
					
				});
				p.add(text);
				JButton b = new JButton("Okay");
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent ae) {
						EmotionDetector.emotion = text.getText();
						System.out.println(EmotionDetector.emotion);
						emotionFrame.dispose();
						synchronized(gui) {
							gui.notify();
						}
					}
				});
				p.add(b);
				emotionFrame.getContentPane().add(p);
				emotionFrame.setVisible(true);
			}
			
		});
		JButton test = new JButton("Testing Mode");
		test.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
		test.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EmotionDetector.mode = "Testing";
				System.out.println(EmotionDetector.mode);
				frame.dispose();
				
				selectImage();
				synchronized(gui) {
					gui.notify();
				}
			}
		});
		
		panel.add(train);
		panel.add(Box.createRigidArea(new Dimension(5,0)));
		panel.add(test);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
	}
	
	public void showResult(String emotion) {  
		JFrame result = new JFrame("Result.");
		result.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		result.setSize(400, 200);
		result.setLocationRelativeTo(null);
		
		JLabel l = new JLabel("The emotion is: " + emotion);
		l.setFont(new Font("Serif", Font.BOLD, 30));
		result.getContentPane().add(l);
		result.setVisible(true);
	}
	
	public void selectImage() {
		JFrame select = new JFrame();
		FileDialog fd = new FileDialog(select, "Chooose an image.", FileDialog.LOAD);
		fd.setDirectory("C:\\Users\\Akash\\workspace\\OpenCV\\MusicPlayer\\MusicPlayer\\TestImages\\");
		fd.setVisible(true);
		
		String filename = fd.getFile();
		if(filename == null) {
			System.out.println("No file selected");
			System.exit(0);
		}
		else {
			System.out.println("Selected image: " + filename);
			EmotionDetector.imagePath = filename;
		}
		
		select.dispose();
		System.out.println("Done");
	}
	
//	public static void main(String[] args) {
//		new GuiComponent().showGui();
//	}
}
