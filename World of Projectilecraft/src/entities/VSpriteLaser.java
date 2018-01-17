/**
 * 	TODO ����������
 */
package entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

import jgui.GamePanel;
import system.VEngine;
import ui.VCombatAreaUI;
import view.VImageInterface;
import global.Debug;
import global.Global;
import interfaces.VCombatObject;

/**
 * @author Demilichzz
 *
 * 2012-11-14
 */
public class VSpriteLaser extends VSprite{
	protected boolean warningmode = true;		//Ԥ����ģʽ�����Ƽ����ж��Ƿ�ΪԤ����״̬
	protected boolean drawwarningline = true;			//����ʱ�Ƿ�ΪԤ����ģʽ
	protected int warningtime=0;			//Ԥ���߳���ʱ��(ms)
	protected double width=0;				//������
	protected double length=400;
	protected double drawwidth=0;
	protected double sidewidth=0;
	protected boolean hasCenter = false;		//��ʼ���Ƿ�����
	
	protected int x,y,x_a,y_a;		//��ÿ��LifeMinus�м��㼤���ʵ��ʼĩ����
	
	public VSpriteLaser(){
		super();
		CType = 1;
	}
	public VSpriteLaser(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
		CType = 1;
	}
	public VSpriteLaser(int warningtime,double width,double length,int t_index,int b_index,double lifetime,double angle,VCombatObject owner){
		super("Sprite_laser_top.png","Sprite_laser_bottom.png",t_index,b_index,lifetime,0,angle,owner);
		CType=1;
		this.warningtime = warningtime;
		this.width = width;
		this.length = length;
		this.drawwidth=width;
		this.sidewidth=(int)(drawwidth/10)*2+5;
	}
	public void spriteUpdate(){
		// TODO ����״̬����
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			move();
			//--------����ʱ�俪ʼ-------------
			LifeMinus();
			//-------����ʱ�����-----------
			if(restricted){	//������ӷ�Χ���ޣ����鷶Χ������ָ����Χ������
				if(!getBound()){
					die();
				}
			}
		}
		else {
			die();
		}
		updatecount++;
	}
	public void LifeMinus(){
		// TODO ��ÿ����Ϸ״̬��������������������1
		super.LifeMinus();
		if((int)(lifetime*1000)-lifetime_f>warningtime){	//ʱ�䳬��Ԥ��ʱ��
			warningmode=false;
			drawwarningline=false;
		}
		else{		//Ԥ��ʱ��ʣ��250msʱ����ͼ����չ
			int timeremain = warningtime+lifetime_f-(int)(lifetime*1000);	//����Ԥ��ʣ��ʱ��
			if(timeremain<250){
				drawwarningline=false;
				drawwidth=width*(int)((250-timeremain)/25)/10;
				sidewidth=(int)(drawwidth/10)*2+5;
			}
		}
		if(lifetime_f<250){			//ʣ�����250msʱ����ͼ������
			//warningmode=true;
			drawwidth=width*(int)(lifetime_f/25)/10;
			sidewidth=(int)(drawwidth/10)*2+5;
		}
		/*int x = (int) (this.GetX()+plat.getRealX());	//��ʼ��
		int y = (int) (this.GetY()+plat.getRealY());*/
		x = (int) (this.GetX()+100);	//��ʼ��
		y = (int) (this.GetY()+0);
		x_a = (int) (x+length*Math.cos(angle));		//������
		y_a = (int) (y+length*Math.sin(angle));
		//System.out.println("X:"+x+",Y:"+y+",Xa:"+x_a+",Ya:"+y_a);
	}
	public boolean getWarningMode(){
		// TODO ��ȡ�����Ƿ�ΪԤ����
		return warningmode;
	}
	public double getWidth(){
		return width;
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO ��������ͼ��
		// ��ȡ��ʵ������������
		//Graphics2D g2d = (Graphics2D) g;
		if(drawwarningline){
			if(i==1){
				//g2d.setColor(new Color(200,200,255,50));
				//g2d.fillOval(x-10, y-10, 20, 20);
			}
			if(i==0){
				g.setColor(Color.white);
				g.setStroke(new BasicStroke(1));
				g.drawLine(x, y, x_a, y_a);
				//g2d.fillOval(x-5, y-5, 10, 10);
			}
		}
		else{
			if(i==1){		//�ײ㼤��
				g.setColor(Global.Color_LaserBottom[t_index]);
				g.setStroke(new BasicStroke((float)(drawwidth+sidewidth*2),BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND));
				g.drawLine(x, y, x_a, y_a);
				//g2d.setColor(Global.Color_LaserBottom[t_index]);
				//g2d.setStroke(new BasicStroke((float)(drawwidth+sidewidth),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				//g2d.drawLine(x, y, x_a, y_a);
				//g2d.fillOval(x-10, y-10, 20, 20);
			}
			else if(i==0){	//���㼤��
				g.setColor(Color.white);
				g.setStroke(new BasicStroke((float)drawwidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND));
				g.drawLine(x, y, x_a, y_a);
				//g2d.setStroke( new BasicStroke((float) drawwidth));
				//g2d.fillOval(x-5, y-5, 10, 10);
			}
		}
	}
	public boolean getBound(){	
		// ��ȡ�Ƿ�����ɼ����򣬼�����Զ�ɼ�
		return true;
	}
	/**
	 * @return
	 */
	public double getLength() {
		// TODO Auto-generated method stub
		return length;
	}
}
