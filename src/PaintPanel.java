import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaintPanel extends JPanel implements MouseMotionListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	public static ArrayList<Path> paths;
	public static Path currentPath;
	private NetworkManager networkManager;
	PaintPanel (NetworkManager n) {
		networkManager = n;
		new Thread(networkManager).start();
		paths=new ArrayList<Path>();
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	public void clear() {
		paths = new ArrayList<Path>();
	}
	
	public void updateNetwork() {
		networkManager.write(new ArrayList<Path>(paths));
	}
	
	public void paintComponent(Graphics g) {
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		this.setCursor(blankCursor);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		for (Path path:paths) {
			g.setColor(path.color);
			if (path.points.size()==1)
				g.fillOval(path.points.get(0).x-path.radius,path.points.get(0).y-path.radius,path.radius*2,path.radius*2);
			else {
				if(g instanceof Graphics2D) {
					Graphics2D g2D=(Graphics2D) g;
					g2D.setStroke(new BasicStroke(path.radius*2,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				}
				Point prevPoint=null;
				for (Point p:path.points) {
					if(prevPoint!=null) {
						g.drawLine(prevPoint.x, prevPoint.y, p.x, p.y);
					}
					prevPoint=p;
				}
			}
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		currentPath.points.add(e.getPoint());
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		try {
			ControlPanel.current.setLocation(e.getX()-ControlPanel.current.getRadius(),e.getY()-ControlPanel.current.getRadius());
		} catch (Exception ex) {
		}
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		ControlPanel.current.setSize(ControlPanel.current.getRadius()*2, ControlPanel.current.getRadius()*2);
		this.add(ControlPanel.current);
		try {
			ControlPanel.current.setLocation(arg0.getX()-ControlPanel.current.getRadius(),arg0.getY()-ControlPanel.current.getRadius());
		} catch (Exception ex) {
		}
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		remove(ControlPanel.current);
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		currentPath = new Path(ControlPanel.current.getColor(),ControlPanel.current.getRadius());
		paths.add(currentPath);
		//currentPath.points.clear();
		currentPath.points.add(getMousePosition());
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		updateNetwork();
	}
}
