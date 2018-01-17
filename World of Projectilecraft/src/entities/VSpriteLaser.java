/**
 * 	TODO 激光粒子类
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
	protected boolean warningmode = true;		//预警线模式，控制激光判定是否为预警线状态
	protected boolean drawwarningline = true;			//绘制时是否为预警线模式
	protected int warningtime=0;			//预警线持续时间(ms)
	protected double width=0;				//激光宽度
	protected double length=400;
	protected double drawwidth=0;
	protected double sidewidth=0;
	protected boolean hasCenter = false;		//起始点是否有球
	
	protected int x,y,x_a,y_a;		//在每次LifeMinus中计算激光的实际始末坐标
	
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
		// TODO 粒子状态更新
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			move();
			//--------测试时间开始-------------
			LifeMinus();
			//-------测试时间结束-----------
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
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
		// TODO 在每个游戏状态更改周期中粒子生命减1
		super.LifeMinus();
		if((int)(lifetime*1000)-lifetime_f>warningtime){	//时间超过预警时间
			warningmode=false;
			drawwarningline=false;
		}
		else{		//预警时间剩余250ms时激光图像扩展
			int timeremain = warningtime+lifetime_f-(int)(lifetime*1000);	//计算预警剩余时间
			if(timeremain<250){
				drawwarningline=false;
				drawwidth=width*(int)((250-timeremain)/25)/10;
				sidewidth=(int)(drawwidth/10)*2+5;
			}
		}
		if(lifetime_f<250){			//剩余最后250ms时激光图像收缩
			//warningmode=true;
			drawwidth=width*(int)(lifetime_f/25)/10;
			sidewidth=(int)(drawwidth/10)*2+5;
		}
		/*int x = (int) (this.GetX()+plat.getRealX());	//起始点
		int y = (int) (this.GetY()+plat.getRealY());*/
		x = (int) (this.GetX()+100);	//起始点
		y = (int) (this.GetY()+0);
		x_a = (int) (x+length*Math.cos(angle));		//结束点
		y_a = (int) (y+length*Math.sin(angle));
		//System.out.println("X:"+x+",Y:"+y+",Xa:"+x_a+",Ya:"+y_a);
	}
	public boolean getWarningMode(){
		// TODO 获取激光是否为预警线
		return warningmode;
	}
	public double getWidth(){
		return width;
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO 绘制粒子图像
		// 获取真实粒子中心坐标
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
			if(i==1){		//底层激光
				g.setColor(Global.Color_LaserBottom[t_index]);
				g.setStroke(new BasicStroke((float)(drawwidth+sidewidth*2),BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND));
				g.drawLine(x, y, x_a, y_a);
				//g2d.setColor(Global.Color_LaserBottom[t_index]);
				//g2d.setStroke(new BasicStroke((float)(drawwidth+sidewidth),BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				//g2d.drawLine(x, y, x_a, y_a);
				//g2d.fillOval(x-10, y-10, 20, 20);
			}
			else if(i==0){	//顶层激光
				g.setColor(Color.white);
				g.setStroke(new BasicStroke((float)drawwidth,BasicStroke.CAP_SQUARE,BasicStroke.JOIN_ROUND));
				g.drawLine(x, y, x_a, y_a);
				//g2d.setStroke( new BasicStroke((float) drawwidth));
				//g2d.fillOval(x-5, y-5, 10, 10);
			}
		}
	}
	public boolean getBound(){	
		// 获取是否满足可见区域，激光永远可见
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
