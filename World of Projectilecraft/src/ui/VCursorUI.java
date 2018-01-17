/**
 * 文件名称：VCursorUI.java
 * 类路径：ui
 * 描述：TODO 光标UI，包括绑定关联UI，在关联UI间移动及调用关联UI行为等功能
 * 作者：Demilichzz
 * 时间：2012-8-6下午04:17:36
 * 版本：Ver 1.0
 */
package ui;

import global.Debug;

import java.util.*;

import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;

import controller.GameListener;

/**
 * @author Demilichzz
 *
 */
public class VCursorUI extends VUI{
	protected ArrayList<VUI> uilist;	//绑定UI列表
	protected int bindindex=0;	//当前UI索引
	protected int x_offset=0,y_offset=0;
	protected boolean moveblock = false;
	
	public VCursorUI(){
		super();
		uilist = new ArrayList<VUI>();
	}
	public VCursorUI(String str,String id){
		super(str,id);
		uilist = new ArrayList<VUI>();
	}
	public void setOffset(int x,int y){
		x_offset = x;
		y_offset = y;
		if(uilist.size()>0){
			VUI firstUI = uilist.get(0);
			this.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void bindUI(VUI ui){
		// TODO 添加绑定UI
		if(ui!=null){
			uilist.add(ui);
		}
		if(uilist.size()>0){
			VUI firstUI = uilist.get(0);
			this.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void unbindUI(VUI ui){
		// TODO 移除绑定UI
		if(uilist.contains(ui)){
			uilist.remove(ui);
		}
		else{
			Debug.DebugSimpleMessage("未关联该UI");
		}
		if(uilist.size()>0){
			VUI firstUI = uilist.get(0);
			this.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void moveCursor(int i){
		// TODO 移动光标位置
		if(!moveblock){
			if(i==1){	//前移
				if(uilist.size()<=bindindex+1){
					bindindex=0;
				}
				else{
					bindindex++;
				}
			}
			else if(i==-1){	//后移
				if(bindindex-1<0){
					bindindex=uilist.size()-1;
				}
				else{
					bindindex--;
				}
			}
			this.setLoc(uilist.get(bindindex).GetX()+x_offset,uilist.get(bindindex).GetY()+y_offset);
		}
	}
	public void lockCursor(int time){
		// TODO 在光标位移后短暂锁定该UI，人为制造输入延迟
		this.moveblock = true;
		VTimer tm = new VTimer(time,time,false,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				moveblock = false;
			}
		});
		tm.timerStart();
	}
	public void uiAction(String trigtype) {
		// TODO 参数为调用者类别字符串
		if (enable) {
			VUI currentUI = uilist.get(bindindex);
			if(currentUI!=null){
				currentUI.uiAction(trigtype);
			}
		}
	}
	public void uiKeyAction(int[] keystate) {
		// TODO 重载UI键盘行为，实现光标位移功能
		if(keystate[GameListener.KEY_UP]==1){	//UP
			if(!moveblock){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_DOWN]==1){	//Down
			if(!moveblock){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			this.uiAction("Cursor");
			VEngine.glistener.resetKeystate();
		}
		// 递归对子UI执行键盘行为
		super.uiKeyAction(keystate);
	}
}
