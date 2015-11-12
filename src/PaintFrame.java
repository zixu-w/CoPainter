import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static PaintPanel paintPanel;
	public static JMenuBar menuBar;
	PaintFrame() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(800, 700);
		this.setTitle("Collaborative Painter");
		this.setResizable(false);
		SpringLayout mainLayout = new SpringLayout();
		this.getContentPane().setLayout(mainLayout);
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("Control");
		JMenuItem clear = new JMenuItem("Clear");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("Load");
		JMenuItem exit = new JMenuItem("Exit");
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paintPanel.clear();
			}
		});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileSaver = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".pb Painting Board File", "pb");
				fileSaver.setFileFilter(filter);
				fileSaver.setSelectedFile(new File("Untitled.pb"));
				boolean done = false;
				do {
					if (fileSaver.showSaveDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
						try {
							File saveFile = fileSaver.getSelectedFile();
							if (saveFile.exists()) {
								if (JOptionPane.showConfirmDialog(rootPane, "File "
										+ saveFile.getName() + " already exists.\n"
										+ "Do you want to overwrite it?", "Overwrite?",
										JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
									continue;
							}
							FileOutputStream saverFileStream = new FileOutputStream(fileSaver.getSelectedFile());
							ObjectOutputStream saverObjectStream = new ObjectOutputStream(saverFileStream);
							saverObjectStream.writeObject(paintPanel.paths);
							saverObjectStream.writeObject(paintPanel.mousePoint);
							saverObjectStream.close();
							done = true;
						} catch (Exception fe) {
							JOptionPane.showMessageDialog(rootPane, fe.toString());
						}
					}
				} while (!done);
			}
		});
		ControlPanel controlPanel = new ControlPanel();
		paintPanel = new PaintPanel();
		menu.add(clear);
		menu.add(save);
		menu.add(load);
		menu.add(exit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		//menuBar.setLocation(0, 0);
		controlPanel.setPreferredSize(new Dimension(800, 100));
		//menuBar.setSize(700, 25);
		//paintPanel.setBounds(0,25,700, 605);
		paintPanel.setPreferredSize(new Dimension(800, 600));
		paintPanel.setBackground(Color.WHITE);
		//controlPanel.setBounds(0, 630,700,45);
		this.getContentPane().add(paintPanel);
		this.getContentPane().add(controlPanel);
		mainLayout.putConstraint(SpringLayout.NORTH, paintPanel, 0, SpringLayout.NORTH, this);
		mainLayout.putConstraint(SpringLayout.WEST, paintPanel, 0, SpringLayout.WEST, this);
		mainLayout.putConstraint(SpringLayout.NORTH, controlPanel, 0, SpringLayout.SOUTH, paintPanel);
		mainLayout.putConstraint(SpringLayout.WEST, controlPanel, 0, SpringLayout.WEST, this);
		//this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
