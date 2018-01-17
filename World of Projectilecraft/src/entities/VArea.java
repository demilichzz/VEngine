/**
 * �ļ����ƣ�VArea.java
 * ��·����entities
 * ������TODO ʹ������������������궨��ľ�������������UI�༭����ʾ��򼰽�������¼������
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-10-26����07:55:59
 * �汾��Ver 1.0
 */
package entities;

import java.awt.Color;
import java.awt.Graphics2D;

import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 */
public class VArea {
	public int x, y, w, h;		//�洢���������������������ı���
	public boolean boldborder = true;	//������ʾʱ�Ƿ�Ϊ�����

	public VArea(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public VArea() {
		// TODO Auto-generated constructor stub
		x = 0;
		y = 0;
		w = 0;
		h = 0;
	}
	public void setLoc(double x,double y){
		this.w = (int) (this.w + x - this.x);
		this.h = (int) (this.h + y - this.y);
		this.x = (int) (x);
		this.y = (int) (y);
	}

	public boolean ifvalid(double x, double y) {
		// TODO ��ȡĿ���x,y�Ƿ��ڴ�����Χ��
		if (this.x == 0 && this.y == 0 && this.w == 0 && this.h == 0) {
			return true;
		}
		if (x > this.x && x < this.w && y > this.y && y < this.h) {
			return true;
		} else
			return false;
	}

	public void drawMe(Graphics2D g2d, GamePanel p) {
		// TODO ��������߿�
		g2d.setColor(Color.BLUE);
		g2d.drawLine(this.x, this.y, this.x, this.h);
		g2d.drawLine(this.x, this.y, this.w, this.y);
		g2d.drawLine(this.x, this.h, this.w, this.h);
		g2d.drawLine(this.w, this.y, this.w, this.h);
		if(boldborder){
			g2d.drawLine(x+1,y+1, x+1, h-1);
			g2d.drawLine(x+1,y+1, w-1,y+1);
			g2d.drawLine(x+1, h-1, w-1, h-1);
			g2d.drawLine(w-1, y+1, w-1, h-1);
		}
	}
}
