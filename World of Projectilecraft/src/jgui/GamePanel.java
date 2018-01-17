/**
 * �ļ����ƣ�GamePanel.java
 * ��·����jgui
 * ������TODO UI�༭���༭�������
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-10-26����07:36:14
 * �汾��Ver 1.0
 */
package jgui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;

import javax.swing.JPanel;

import entities.*;
import global.*;
import system.*;
import view.*;

/**
 * @author Demilichzz
 * 
 */
public class GamePanel extends JPanel {
	protected VEngine ve;
	public static Toolkit tk = Toolkit.getDefaultToolkit();
	public VArea area = new VArea(0, 0, 799, 599);

	public GamePanel(VEngine ve) {
		// TODO Auto-generated constructor stub
		super();
		this.ve = ve;
	}

	//--------ԭ��-------------------------------
	/*public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.DARK_GRAY);
		g2d.fillRect(0, 0, 800, 600); // ��ɫ����
		area.drawMe(g2d, this);
		ve.gs.drawScene(g2d, this);
		g.dispose();
		try {
			Thread.sleep(0);			//�ͷŻ����߳�
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public void paintComponent(Graphics g) {
		//--------����ʱ�俪ʼ-------------
		Image img = createImage(800, 600); //��������ͼ��
		Graphics2D graph = (Graphics2D) img.getGraphics(); //��ȡ������graphic		
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);	//�����
		graph.setColor(Color.DARK_GRAY);
		graph.fillRect(0, 0, 800, 600); // ��ɫ����
		area.drawMe(graph, this);
		VEngine.gs.drawScene(graph, this);
		Graphics2D g2d = (Graphics2D) g;
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);	//�����
		g2d.drawImage(img, 0, 0, this); //��������ͼ����Ƶ���Ļ
		g2d.dispose();
		try {
			Thread.sleep(0); //�ͷŻ����߳�
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
