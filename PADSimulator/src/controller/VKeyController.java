/**
 * @author Demilichzz
 *	键盘控制器
 * 2013-11-13
 */
package controller;

import java.awt.event.KeyEvent;

import global.Global;

import org.lwjgl.input.Keyboard;

import data.GameData;

import system.VEngine;

/**
 * @author Demilichzz
 *
 * 2013-11-13
 */
public class VKeyController {
	protected int[] keylist;	//用数组存储键位设置的按键映射表
	protected String keyconfigurl = "data/KeyConfig.ini";
	protected String[] paramlist={"UP","DOWN","LEFT","RIGHT","ENTER","SPACE","ESC","Z","X","C","ADD"};	//参数描述表
	protected int[] keystate;	//用数组存储的按键状态，-1表示未按下，0表示按下
	protected int keydelay;		//按键延迟
	
	public final static int KEY_UP = 0;
	public final static int KEY_DOWN = 1;
	public final static int KEY_LEFT = 2;
	public final static int KEY_RIGHT = 3;
	public final static int KEY_ENTER = 4;
	public final static int KEY_SPACE = 5;
	public final static int KEY_ESC = 6;
	public final static int KEY_Z = 7;
	public final static int KEY_X = 8;
	public final static int KEY_C = 9;	//定义常量使GS的事件响应函数易于理解
	public final static int KEY_ADD = 10;
	
	public VKeyController(){
		loadKeyConfig();
		keydelay = Global.keydelay;
	}

	/**
	 * 
	 */
	private void loadKeyConfig() {
		// TODO 加载按键配置
		if(false){
			
		}
		else{			//按默认配置初始化按键映射
			keylist = new int[11]; // 初始化按键映射表数组
			keystate = new int[11];
			resetKeystate();
			keylist[0] = Keyboard.KEY_UP;
			keylist[1] = Keyboard.KEY_DOWN;
			keylist[2] = Keyboard.KEY_LEFT;
			keylist[3] = Keyboard.KEY_RIGHT;
			keylist[4] = Keyboard.KEY_RETURN;		//enter
			keylist[5] = Keyboard.KEY_SPACE;
			keylist[6] = Keyboard.KEY_ESCAPE;
			keylist[7] = Keyboard.KEY_Z;
			keylist[8] = Keyboard.KEY_X;
			keylist[9] = Keyboard.KEY_C;
			keylist[10] = Keyboard.KEY_ADD;
		}
	}
	public int[] getKeystate(){
		return keystate;
	}
	public void resetKeystate(){
		for(int i=0;i<paramlist.length;i++){
			keystate[i]=-1;	//将全部按键状态还原为未按下
		}
	}
	public void resetKeystate(int key){
		// TODO 仅还原指定按键
		for(int i=0;i<paramlist.length;i++){
			if(i==key){
				keystate[i]=-1;
			}
		}
	}
	public void setKeydelay(int key){
		// TODO 将指定按键设置为延迟状态
		for(int i=0;i<paramlist.length;i++){
			if(i==key){
				keystate[i]=-keydelay;
			}
		}
	}
	/**
	 * @param keyindex
	 * @return
	 */

	public boolean isKeyDown(int key) {
		// TODO 获取指定索引的按键状态，并根据定义的按键响应类型确定是否弹起按键
		for(int i=0;i<paramlist.length;i++){
			if(i==key){
				if(keystate[i]==0){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 */
	public void keyAction() {
		// TODO 在GS更新时捕捉键盘状态
		for(int i=0;i<paramlist.length;i++){
			if(Keyboard.isKeyDown(keylist[i])){	//在当次更新中获取的按键状态为按下
				if(keystate[i]==-1){	//如果当前状态为未按下，则直接转换为已按下
					keystate[i]=0;
				}
				else if(keystate[i]<-1){	//如果当前状态为延迟中，则减少延迟剩余时间
					keystate[i] = keystate[i]+VEngine.newgame.gs.getMSecond();
				}
			}
			else{	//在当次更新中获取的按键状态为未按下
				keystate[i]=-1;	//状态直接转换为未按下
			}
		}
		//--------测试用命令-------------------------
	}
}
