/**
 * �ļ����ƣ�GameListener.java
 * ��·����controller
 * ������TODO ������Ϸ�����ļ�����
 * ���ߣ�Demilichzz
 * ʱ�䣺2011-12-2����04:07:29
 * �汾��Ver 1.0
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
	protected int[] keylist;	//������洢��λ���õİ���ӳ���
	protected String[] paramlist={"UP","DOWN","LEFT","RIGHT","ENTER","SPACE","ESC","Z","X","C","SHIFT"};	//����������
	protected int[] keystate;	//������洢��λ״̬��������GS�л�ȡ����GS����ʱִ�ж�Ӧ�ļ�����Ϊ
	
	public final static int KEY_UP = 0;
	public final static int KEY_DOWN = 1;
	public final static int KEY_LEFT = 2;
	public final static int KEY_RIGHT = 3;
	public final static int KEY_ENTER = 4;
	public final static int KEY_SPACE = 5;
	public final static int KEY_ESC = 6;
	public final static int KEY_Z = 7;
	public final static int KEY_X = 8;
	public final static int KEY_C = 9;	//���峣��ʹGS���¼���Ӧ�����������
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
		loadKeyConfig();	//���ذ�������
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
			keystate[i]=0;	//��ȫ������״̬��ԭΪδ����
		}
	}
	public void resetKeystate(int key){
		// TODO ����ԭָ������
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
		// TODO ����ָ����������,��������ini�ļ�
		for(int i=0;i<keylist.length;i++){
			if (i != index) {
				if (keylist[i] == key) { // ������Ҫ���õļ�λ
					keylist[i] = keylist[index]; // ԭ�м�λ=Ŀ������ԭ�а���
					VKeyConfigValueUI temp = (VKeyConfigValueUI) configPanel
							.getUIByID("ui_config_value" + i); // ��ȡԭ��λ��ӦUI
					temp.refreshText(this); // ��ԭ�м�λUI��������
					break;
				}
			}
		}
		keylist[index]=key;
	}*/
	public void saveKeyConfig(){
		// TODO ���浱ǰ��������
		try {
			ProjectConfig.saveCommonConfig("KeyConfig",keylist,paramlist,keyconfigurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadKeyConfig(){
		// TODO ���ذ������ã��޷��������ļ��л�ȡ�����Ĭ�����ã���д���ļ�
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
		// TODO ���̰�����Ӧ
		if (VEngine.developmode) {
			if (e.getKeyCode() == KeyEvent.VK_T) {
				VTimer tm = new VTimer(500, 500, false, new VLuaAction() {
					public void action() {
						// TODO Auto-generated method stub
						Debug.DebugSimpleMessage("Timer������Ϣ");
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
				keystate[i]=1;	//����Ӧ��λ״̬��Ϊ�Ѱ���
			}
		}
		//setKeyinUI(e.getKeyCode());	//����GS�к���Ϊ��ȡ����״̬�ļ�λ����UI��Ӱ���
	}

	/**
	 * @param keyCode
	 */
/*	private void setKeyinUI(int keyCode) {
		// TODO �ڼ�λ����UI��������ü�λ
		VUI tarui;
		VKeyConfigValueUI tarvalueui;
		for (tarui = configPanel.firstChild; tarui!= null&&tarui.firstChild!=null; tarui = tarui.nextUI) {
			tarvalueui = (VKeyConfigValueUI)tarui.firstChild;
			if(tarvalueui.isActive()){		//Ѱ�һUI
				setKey(tarvalueui.getKeyIndex(), keyCode);	//�ԻUI�ƶ����������ð���
				tarvalueui.refreshText(this);	//�����λ����Ӧ���»UI��ʾ������
				tarvalueui.uiAction("MouseEvent");
				return;
			}
		}
	}*/

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		for(int i=0;i<paramlist.length;i++){
			if(e.getKeyCode()==keylist[i]){	//�����λ����ָ�������б�
				//VEngine.gs.keyProcess(i);
				keystate[i]=0;	//����Ӧ��λ״̬��Ϊδ����
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
