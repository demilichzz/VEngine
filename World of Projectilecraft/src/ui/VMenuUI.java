/**
 * 	TODO 单行菜单UI类，继承基础UI类并实现所有基本的单行菜单操作函数
 */
package ui;

import global.Debug;
import global.Imageconst;

import java.awt.Graphics;
import java.util.ArrayList;

import jgui.*;
import controller.*;
import system.*;
import timer.*;
/**
 * @author Demilichzz
 *
 * 2012-11-16
 */
public class VMenuUI extends VUI{
	protected ArrayList<VUI> uilist;	//绑定UI列表
	protected VLuaAction cancel_action;	//菜单取消行为
	protected VLuaAction switch_action;	//菜单切换行为
	protected int bindindex=0;	//当前UI索引
	protected boolean ifreset=false;	//显示菜单时是否重置光标
	protected boolean moveblock = false;	//菜单当前状态是否禁止光标移动，用于控制光标速度
	protected int responsemode = VUI.MENU_RESPONSE_LR;	//按键响应模式，默认为左右键
	protected VUI cursor = null;		//光标UI
	protected int x_offset=0,y_offset=0;//光标偏移
	
	
	public VMenuUI(){
		super();
		uilist = new ArrayList<VUI>();
	}
	public VMenuUI(String str,String id){
		super(str,id);
		uilist = new ArrayList<VUI>();
	}
	public VMenuUI(int w, int h, String str) {
		// TODO Auto-generated constructor stub
		super(w,h,str);
		uilist = new ArrayList<VUI>();
	}
	public void setCursor(String image){
		// TODO 设置菜单光标
		if(Imageconst.GetImageByName(image)!=null){
			cursor = new VUI(image,uiID+"_cursor");
			cursor.setParent(this);
		}
		else{
			cursor = null;
		}
	}
	public void setVisible(boolean v){
		super.setVisible(v);
		if(v){
			moveblock = false;
			if(ifreset){		//如果显示时重置光标
				resetCursor();
			}
		}
	}
	public void setIfResetCursor(boolean b){
		ifreset = b;
	}
	public void setOffset(int x,int y){
		x_offset = x;
		y_offset = y;
		if(uilist.size()>0){
			VUI cuui = uilist.get(bindindex);
			cursor.setLoc(cuui.GetX()+x_offset, cuui.GetY()+y_offset);
		}
	}
	public int getIndex(){
		// TODO 获取当前绑定UI索引
		return bindindex;
	}
	public void setIndex(int i){
		// TODO 设置索引
		bindindex = i;
		if(uilist.size()>0){
			VUI cuui = uilist.get(bindindex);
			cursor.setLoc(cuui.GetX()+x_offset, cuui.GetY()+y_offset);
		}
	}
	public void addCancelAction(VLuaAction ca){
		// TODO 添加菜单取消时的行为
		cancel_action=ca;
	}
	public void addSwitchAction(VLuaAction sa) {
		// TODO 添加在菜单选项之间切换时触发的行为
		switch_action=sa;
	}
	public void bindUI(VUI ui){
		// TODO 添加绑定UI
		if(ui!=null){
			uilist.add(ui);
			ui.setParent(this);		//绑定UI时将其父UI设置为菜单UI，从而在draw函数中控制是否显示
		}
		if(uilist.size()>0&&cursor!=null){	//重置光标
			VUI firstUI = uilist.get(0);
			cursor.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
		bindindex=0;
	}
	public void unbindUI(VUI ui){
		// TODO 移除绑定UI，并将移除的UI加入未使用UI组
		if(uilist.contains(ui)){
			uilist.remove(ui);
			ui.setParent(VEngine.gs.uiparent.getUIByID("ui_unused"));
		}
		else{
			Debug.DebugSimpleMessage("未关联该UI");
		}
		if(uilist.size()>0&&cursor!=null){	//重置光标
			VUI firstUI = uilist.get(0);
			cursor.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void clearBind(){
		// TODO 清空所有绑定UI，在菜单加载不同菜单项时先清空再绑定需要的菜单项UI
		for(VUI ui:uilist){
			ui.setParent(VEngine.gs.uiparent.getUIByID("ui_unused"));
		}
		uilist.clear();
	}
	public void setResponseMode(int i){
		// TODO 设置按键响应模式
		responsemode = i;
	}
	public void moveCursor(int i){
		// TODO 移动光标位置，同时触发切换行为
		if (!moveblock&&uilist.size()>0) { // 非禁止移动状态
			if (i == 1) { // 前移
				if (uilist.size() <= bindindex + 1) {
					bindindex = 0;
				} else {
					bindindex++;
				}
			} else if (i == -1) { // 后移
				if (bindindex - 1 < 0) {
					bindindex = uilist.size() - 1;
				} else {
					bindindex--;
				}
			}
			if (switch_action != null) {	//执行切换行为
				switch_action.action();
			}
			if(cursor!=null){	//移动光标位置
				cursor.setLoc(uilist.get(bindindex).GetX()+x_offset,uilist.get(bindindex).GetY()+y_offset);
			}
		}
		//resetUILoc();
	}
	public void resetCursor(){
		// TODO 重置光标
		bindindex=0;
		if(cursor!=null){	//移动光标位置
			cursor.setLoc(uilist.get(bindindex).GetX()+x_offset,uilist.get(bindindex).GetY()+y_offset);
		}
	}
	public void lockCursor(int time){
		// TODO 在光标位移后短暂锁定该UI，人为制造输入延迟
		moveblock = true;
		VTimer tm = new VTimer(time,time,false,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				moveblock=false;
			}
		});
		tm.timerStart(0);
	}
	public void uiAction(String trigtype) {
		// TODO 执行UI行为，参数为调用者类别字符串
		if (enable) {
			if(uilist.size()>0){
				VUI currentUI = uilist.get(bindindex);
				if(currentUI!=null){
					currentUI.uiAction(trigtype);
				}
			}
		}
	}
	public void uiKeyAction(int[] keystate) {
		// TODO 重载UI键盘行为，实现光标位移功能
		if(keystate[GameListener.KEY_UP]==1){	//UP
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_UD||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_DOWN]==1){	//DOWN
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_UD||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_RIGHT]==1){	//RIGHT
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_LR||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_LEFT]==1){	//LEFT
			if(!moveblock&&(responsemode==VUI.MENU_RESPONSE_LR||responsemode==VUI.MENU_RESPONSE_LRUD)){
				this.moveCursor(-1);
				lockCursor(200);
			}
		}
		if(keystate[GameListener.KEY_ESC]==1){	//ESC，执行取消行为
			if(cancel_action!=null){
				cancel_action.action();
				VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
			}
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			this.uiAction("Cursor");
			VEngine.glistener.resetKeystate(GameListener.KEY_ENTER);
		}
		// 递归对子UI执行键盘行为
		super.uiKeyAction(keystate);
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制菜单UI，根据参数控制绑定列表UI的显示与否
		super.drawUI(g, p);
	}
}
