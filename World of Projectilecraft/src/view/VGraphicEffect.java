/**
 * 	TODO 图像效果基类，在游戏面板进行绘制之后进行绘制某些特殊效果
 */
package view;

import java.awt.Graphics2D;

import system.VEngine;
import jgui.*;

/**
 * @author Demilichzz
 *
 * 2012-11-13
 */
public class VGraphicEffect {
	protected GamePanel p = null;		//面板
	protected boolean destroyed = false;
	protected int startTime=0;		//特效运行起始时间点
	protected int currentTime=0;	//当前时间
	protected boolean started=false;
	
	public VGraphicEffect(){
		p = VEngine.p;
		VEngine.gs.addEffect(this);		//初始化时加入GS的特效列表中
	}
	
	public void drawEffect(Graphics2D g2d, GamePanel p2){
		// TODO 绘制特效
		
	}

	public boolean getDestoryed() {
		// TODO 获取是否应被销毁 
		return destroyed;
	}
	public void update(int rt){
		// TODO 更新特效状态，传入参数为GS的运行计数
		if(!started){
			startTime = rt;
			started=true;
		}
		currentTime = rt;
	}
}
