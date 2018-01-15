/**
 * 	TODO 滚动卷轴式菜单UI，继承单行菜单UI，用于游戏开始时的选择副本，天赋等
 */
package ui;

import global.Debug;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import jgui.GamePanel;

import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;
import config.ProjectConfig;
import controller.GameListener;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class VScrollMenuUI extends VMenuUI{
	protected int center_x=361,center_y=261;		//中心UI坐标
	protected int x_offset=200,y_offset=0;		//关联列表中UI的间距
	protected boolean perodic=false;		//UI是否循环显示

	public VScrollMenuUI(){
		super();
	}
	public VScrollMenuUI(String str,String id){
		super(str,id);
	}
	public VScrollMenuUI(int w, int h, String str) {
		super(w,h,str);
	}
	public void bindUI(VUI ui){
		super.bindUI(ui);
		resetUILoc();
	}
	public void setIndex(int i){
		bindindex = i;
		resetUILoc();
	}
	public void setDistribution(int x,int y,int cx,int cy){
		// TODO 设置UI分布，x,y为间隔，cx,cy为中心
		x_offset = x;
		y_offset = y;
		center_x = cx;
		center_y = cy;
		resetUILoc();
	}
	public void setPeridic(boolean b){
		perodic=b;
		resetUILoc();
	}
	public void resetUILoc(){
		// TODO 在子UI坐标相关参数改变时调用，以重新分布子UI位置
		if(perodic){
			int leftsize=uilist.size()/2;
			for(int i=0;i<uilist.size();i++){
				int newindex=(i+leftsize-bindindex+uilist.size())%uilist.size()-leftsize;	//偏移索引
				uilist.get(i).setLoc(center_x+x_offset*newindex,center_y+y_offset*newindex);
			}
		}
		else{
			for(int i=0;i<uilist.size();i++){
				uilist.get(i).setLoc(center_x+x_offset*(i-bindindex),center_y+y_offset*(i-bindindex));
			}	
		}		
	}
	public void moveCursor(int i){
		// TODO 移动光标位置，同时触发切换行为
		super.moveCursor(i);
		resetUILoc();
	}
}
