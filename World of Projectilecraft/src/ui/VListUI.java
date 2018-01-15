/**
 * 	TODO 列表UI，应用于选择录像文件载入
 */
package ui;

import java.awt.Color;
import java.util.ArrayList;

import system.VEngine;
import controller.GameListener;

import entities.VPlayer;
import event.GlobalEvent;
import global.Global;

/**
 * @author Demilichzz
 *
 * 2012-11-9
 */
public class VListUI extends VUI{
	protected ArrayList<String> strlist;		//UI包含的字符串列表
	protected int page=1;		//列表显示的页数
	protected int index=0;		//列表光标停留的项目索引
	protected VUI[]items = new VUI[20];			//列表项UI
	protected VUI listcursor;
	protected int x_offset=-30,y_offset=-5;		//光标偏移
	
	public VListUI(){
		super();
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public VListUI(String str,String id){
		super(str,id);
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public VListUI(int w, int h, String str) {
		// TODO Auto-generated constructor stub
		super(w,h,str);
		strlist = new ArrayList<String>();
		initSubUI();
	}
	public void setVisible(boolean b){
		super.setVisible(b);
		page = 1;
		index = 0;
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);	//设置光标位置
	}
	public void initSubUI(){
		// TODO 初始化列表中的项目子UI，并将它们的父UI设置为列表本身
		for(int i=0;i<20;i++){
			items[i]=new VUI("ph_block.png",uiID+"_listitem_"+i);
			items[i].setParent(this);
			items[i].setLoc(50, 20+i*28);
		}
		listcursor=new VUI("UI_cursor.png",uiID+"_cursor");		//列表光标
		listcursor.setParent(this);
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);
	}
	public void setStringList(ArrayList<String> str){
		// TODO 设置字符串列表
		if(str!=null){
			strlist=str;
		}
		else{
			strlist.clear();
		}
		page=1;
		index=0;
	}
	public void updateUI(){
		// TODO 更新列表项UI
		for(int i=0;i<20;i++){
		    if((page-1)*20+i<strlist.size()){
		    	String str = strlist.get((page-1)*20+i);
		    	items[i].addText(str,30,10,Global.f,Global.c);		//更新列表项文字
		    }
		    else{
		    	items[i].addText("",30,10,Global.f,Global.c);
		    }
		    items[i].text.setStroke(false, new Color(0,0,0));
		}
		//setVisible(this.visible);	//因为包含的玩家条UI是重新建立的对象,因此要将其按当前UI的可视性设置
		super.updateUI();
	}
	public void moveCursor(int value){
		index=index+value;
		if(index>19){
			index=index-20;
		}
		else if(index<0){
			index=index+20;
		}
		listcursor.setLoc(items[index].GetX()+x_offset, items[index].GetY()+y_offset);	//设置光标位置
	}
	public void page(int i){
		// TODO 翻页,i为正则页数增加
		page = page + i;
		adjustPage();
	}
	public void setPage(int i){
		page = i;
		adjustPage();
	}
	private void adjustPage(){
		// TODO 调整页数使其合法
		if(page<1||strlist.size()<=20){
			page = 1;
		}
		else if((page-1)*20>=strlist.size()){	//超限
			page = (strlist.size()-1)/20+1;
		}
		// 根据调整后的页数重新更新UI
		updateUI();
	}
	public void uiKeyAction(int[] keystate) {
		// TODO 重载UI键盘行为，实现光标位移功能
		if(keystate[GameListener.KEY_LEFT]==1){	
			this.page(-1);
			VEngine.glistener.resetKeystate(GameListener.KEY_LEFT);
		}
		if(keystate[GameListener.KEY_RIGHT]==1){	
			this.page(1);
			VEngine.glistener.resetKeystate(GameListener.KEY_RIGHT);
		}
		if(keystate[GameListener.KEY_DOWN]==1){	
			this.moveCursor(1);
			VEngine.glistener.resetKeystate(GameListener.KEY_DOWN);
		}
		if(keystate[GameListener.KEY_UP]==1){	
			this.moveCursor(-1);
			VEngine.glistener.resetKeystate(GameListener.KEY_UP);
		}
		if(keystate[GameListener.KEY_ESC]==1){	//ESC，执行取消行为
			VEngine.gs.uiparent.getUIByID("ui_bg").setActionVisible(true);
			VEngine.gs.uiparent.getUIByID("ui_replaylist").setVisible(false);
			VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
		}
		if(keystate[GameListener.KEY_ENTER]==1){	//Enter
			if(items[index].getText()!=""&&items[index].getText()!=null){			
				GlobalEvent.loadReplay("data/Replay/"+items[index].getText());
			}
			VEngine.glistener.resetKeystate(GameListener.KEY_ENTER);
		}
		// 递归对子UI执行键盘行为
		super.uiKeyAction(keystate);
	}
}
