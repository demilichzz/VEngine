/**
 * 	TODO 淡入淡出特效，为特效基类的子类
 */
package view;

import java.awt.Color;
import java.awt.Graphics2D;
import system.VEngine;
import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 * 2012-11-14
 */
public class VGEffectFade extends VGraphicEffect{
	protected int fadetime;		//淡出过程中每一阶段持续的时间
	protected int x=0,y=0,w=800,h=600;		//范围坐标
	protected int alpha;	//透明度
	protected Color c;		//覆盖颜色
	
	public VGEffectFade(){
		super();
	}
	public VGEffectFade(int x,int y,int w,int h,int fadetime){
		super();
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.fadetime = fadetime/VEngine.gs.getMSecond();
	}
	public void update(int rt){
		// TODO 更新特效状态，传入参数为GS的运行计数
		super.update(rt);			//更新初始时间和当前时间
		int fadeindex = (currentTime-startTime)/fadetime;
		if(fadeindex<=10){
			c=new Color(0,0,0,fadeindex*25);
		}
		else if(fadeindex>20&&fadeindex<=30){
			c=new Color(0,0,0,(30-fadeindex)*25);
		}
		else if(fadeindex>30){
			destroyed=true;
		}		
	}
	public void drawEffect(Graphics2D g2d, GamePanel p2){
		// TODO 绘制特效
		g2d.setColor(c);
		g2d.fillRect(x,y,w,h);
	}
}
