/**
 * 文件名称：GameListener.java
 * 类路径：controller
 * 描述：TODO 监听游戏操作的监听器
 * 作者：Demilichzz
 * 时间：2011-12-2上午04:07:29
 * 版本：Ver 1.0
 */
package controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.util.*;

import config.ProjectConfig;
import event.GlobalEvent;
import global.*;
import system.*;
import timer.VLuaAction;
import timer.VTimer;
import ui.VUI;

/**
 * @author Demilichzz
 *
 */
public class GameListener implements KeyListener{
	protected VEngine ve;
	protected VUI configPanel;
	protected String keyconfigurl = "data/KeyConfig.ini";
	protected int[] keylist;	//用数组存储键位设置的按键映射表
	protected String[] paramlist={"UP","DOWN","LEFT","RIGHT","ENTER","SPACE","ESC","Z","X","C","SHIFT"};	//参数描述表
	protected int[] keystate;	//用数组存储键位状态，用于在GS中获取并在GS更新时执行对应的键盘行为
	
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
	public final static int KEY_SHIFT = 10;
	
	/*	[0]:UP
		[1]:DOWN
		[2]:LEFT
		[3]:RIGHT
		[4]:ENTER
		[5]:SPACE
		[6]:ESC
		[7]:Z
		[8]:X
		[9]:C
	*/
	public GameListener(VEngine ve){
		this.ve = ve;
		loadKeyConfig();	//加载按键配置
		keystate = new int[11];
	}
	
	public void setConfigPanelUI(VUI p){
		configPanel = p;
	}
	public int[] getKeystate(){
		return keystate;
	}
	public void resetKeystate(){
		for(int i=0;i<paramlist.length;i++){
			keystate[i]=0;	//将全部按键状态还原为未按下
		}
	}
	public void resetKeystate(int key){
		// TODO 仅还原指定按键
		for(int i=0;i<paramlist.length;i++){
			if(i==key){
				keystate[i]=0;
			}
		}
	}
	public int[] getKeylist() {
		return keylist;
	}
	public String[] getParamlist(){
		return paramlist;
	}
/*	public void setKey(int index,int key){
		// TODO 设置指定索引按键,并保存至ini文件
		for(int i=0;i<keylist.length;i++){
			if (i != index) {
				if (keylist[i] == key) { // 若已有要设置的键位
					keylist[i] = keylist[index]; // 原有键位=目标索引原有按键
					VKeyConfigValueUI temp = (VKeyConfigValueUI) configPanel
							.getUIByID("ui_config_value" + i); // 获取原键位对应UI
					temp.refreshText(this); // 对原有键位UI更新文字
					break;
				}
			}
		}
		keylist[index]=key;
	}*/
	public void saveKeyConfig(){
		// TODO 保存当前按键配置
		try {
			ProjectConfig.saveCommonConfig("KeyConfig",keylist,paramlist,keyconfigurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadKeyConfig(){
		// TODO 加载按键配置，无法从配置文件中获取则加载默认配置，并写入文件
		try {
			keylist = ProjectConfig.loadKeyConfig(paramlist);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Debug.DebugSimpleMessage(e.getMessage());
		}
	}
	public void loadKeyDefaultConfig() {
		// TODO Auto-generated method stub
		try {
			keylist = ProjectConfig.loadKeyDefaultConfig(paramlist);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO 键盘按键响应
		if (VEngine.developmode) {
			if (e.getKeyCode() == KeyEvent.VK_T) {
				VTimer tm = new VTimer(500, 500, false, new VLuaAction() {
					public void action() {
						// TODO Auto-generated method stub
						Debug.DebugSimpleMessage("Timer测试信息");
					}
				});
				tm.timerStart();
			}
			/*
			 * if(e.getKeyCode()==KeyEvent.VK_S){
			 * GlobalEvent.saveGame(GameData.getSaveurl()); } else
			 * if(e.getKeyCode()==KeyEvent.VK_L){
			 * GlobalEvent.loadGame("xml/TEST_Save.xml"); } else
			 * if(e.getKeyCode()==KeyEvent.VK_P){
			 * GameData.currentboss.combatStart(); } else
			 * if(e.getKeyCode()==KeyEvent.VK_O){
			 * GlobalEvent.combatLoadNewBoss(new VCUBossMorchok()); } else
			 */
			if (e.getKeyCode() == KeyEvent.VK_F1) {
				Debug.DebugSpriteNum();
			}
			if (e.getKeyCode() == KeyEvent.VK_F2) {
				GlobalEvent.activateNextBoss();
			}
		}
		for(int i=0;i<paramlist.length;i++){
			if(e.getKeyCode()==keylist[i]){
				//Debug.DebugSimpleMessage(KeyEvent.getKeyText(e.getKeyCode()));
				//VEngine.gs.keyProcess(i);
				keystate[i]=1;	//将对应键位状态改为已按下
			}
		}
		//setKeyinUI(e.getKeyCode());	//调用GS中函数为获取按键状态的键位设置UI添加按键
	}

	/**
	 * @param keyCode
	 */
/*	private void setKeyinUI(int keyCode) {
		// TODO 在键位设置UI面板中设置键位
		VUI tarui;
		VKeyConfigValueUI tarvalueui;
		for (tarui = configPanel.firstChild; tarui!= null&&tarui.firstChild!=null; tarui = tarui.nextUI) {
			tarvalueui = (VKeyConfigValueUI)tarui.firstChild;
			if(tarvalueui.isActive()){		//寻找活动UI
				setKey(tarvalueui.getKeyIndex(), keyCode);	//对活动UI制定的索引设置按键
				tarvalueui.refreshText(this);	//重设键位后相应更新活动UI显示的文字
				tarvalueui.uiAction("MouseEvent");
				return;
			}
		}
	}*/

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		for(int i=0;i<paramlist.length;i++){
			if(e.getKeyCode()==keylist[i]){	//如果键位码在指定按键列表
				//VEngine.gs.keyProcess(i);
				keystate[i]=0;	//将对应键位状态改为未按下
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
