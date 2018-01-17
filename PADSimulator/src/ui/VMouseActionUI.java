/**
 * @author Demilichzz
 *	进行鼠标响应的UI类
 * 2013-11-9
 */
package ui;

import system.VEngine;
import timer.VLuaAction;

/**
 * @author Demilichzz
 *
 * 2013-11-9
 */
public class VMouseActionUI extends VUI{
	protected boolean pressstate = false;	//表示该UI是否被鼠标点击选中
	protected int activeoffx = 0;
	protected int activeoffy = 0;	//鼠标点击点与UI坐标的偏移
	protected boolean allowdrag = false;
	protected boolean holdstate = true;		//是否允许悬停计数增加
	protected int holdtime = 0;
	protected int holdlimit = 100;	//默认悬停触发延迟1秒
	protected VLuaAction holdaction;		//鼠标指向UI并达到触发延迟的事件
	protected VLuaAction holdreleaseaction;		//鼠标离开UI的事件
	
	public VMouseActionUI(int width, int height, String ID) {
		super(width,height,ID);
	}
	public VMouseActionUI(String str, String ID){
		super(str,ID);
	}
	
	public void setDragState(boolean b){
		// TODO 设置UI是否可拖动
		allowdrag = b;
	}
	public void uiAction(String trigtype) {
		// TODO 参数为调用者类别字符串
		if (enable) {
			if(trigtype.equals("MousePress")){
				//System.out.print("press");
				if (action != null) {
					action.action();
				}
			}
			if(trigtype.equals("MouseMove")){
				
			}
			/*if (!trigtype.equals("MouseEvent")) {
				if (action != null) {
					action.action();
				}
			}*/
		}
	}
	public void addHoldAction(VLuaAction action){
		this.holdaction = action;
	}
	public void addHoldReleaseAction(VLuaAction action){
		this.holdreleaseaction = action;
	}
	public void uiHoldInc(){
		// TODO 在游戏更新时，若鼠标持续悬停在UI上，则增加UI的悬停计时直到设置的悬停时间
		if(holdstate){
			holdtime = holdtime+1;
			if(holdtime>=holdlimit){
				holdstate = false;
				if(holdaction!=null){
					holdaction.action();
				}
			}
		}
	}
	public void uiHoldRelease(){
		// TODO 鼠标不再悬停于此UI上
		holdtime = 0;
		holdstate = true;
		if(holdreleaseaction!=null){
			holdreleaseaction.action();
		}
	}
	/**
	 * @param x
	 * @param y
	 */
	public void uiDragPress(int x, int y) {
		// TODO 点击UI时调用，切换UI是否被拖拽的状态并根据点击位置设置偏移
		if(enable){
			if(!pressstate&&allowdrag){
				pressstate = true;
				VEngine.newgame.mcontroller.setActiveUI(this);
				activeoffx = (int)this.GetX()-x;
				activeoffy = (int)this.GetY()-y;
			}
		}
	}
	public void uiDragMoveTo(int x,int y){
		// TODO 将UI拖拽至指定位置，参数x,y为拖拽时的鼠标坐标
		if(enable){
			if(pressstate&&allowdrag){
				this.setLoc(x+activeoffx, y+activeoffy);
			}
		}
	}
	public void uiDragRelease(int x,int y){
		// TODO 释放UI拖拽
		if(enable){
			if(pressstate&&allowdrag){
				pressstate = false;
				activeoffx = 0;
				activeoffy = 0;
			}
		}
	}
}
