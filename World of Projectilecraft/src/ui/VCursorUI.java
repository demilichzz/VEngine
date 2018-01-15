/**
 * �ļ����ƣ�VCursorUI.java
 * ��·����ui
 * ������TODO ���UI�������󶨹���UI���ڹ���UI���ƶ������ù���UI��Ϊ�ȹ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����04:17:36
 * �汾��Ver 1.0
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
	protected ArrayList<VUI> uilist;	//��UI�б�
	protected int bindindex=0;	//��ǰUI����
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
		// TODO ��Ӱ�UI
		if(ui!=null){
			uilist.add(ui);
		}
		if(uilist.size()>0){
			VUI firstUI = uilist.get(0);
			this.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void unbindUI(VUI ui){
		// TODO �Ƴ���UI
		if(uilist.contains(ui)){
			uilist.remove(ui);
		}
		else{
			Debug.DebugSimpleMessage("δ������UI");
		}
		if(uilist.size()>0){
			VUI firstUI = uilist.get(0);
			this.setLoc(firstUI.GetX()+x_offset, firstUI.GetY()+y_offset);
		}
	}
	public void moveCursor(int i){
		// TODO �ƶ����λ��
		if(!moveblock){
			if(i==1){	//ǰ��
				if(uilist.size()<=bindindex+1){
					bindindex=0;
				}
				else{
					bindindex++;
				}
			}
			else if(i==-1){	//����
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
		// TODO �ڹ��λ�ƺ����������UI����Ϊ���������ӳ�
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
		// TODO ����Ϊ����������ַ���
		if (enable) {
			VUI currentUI = uilist.get(bindindex);
			if(currentUI!=null){
				currentUI.uiAction(trigtype);
			}
		}
	}
	public void uiKeyAction(int[] keystate) {
		// TODO ����UI������Ϊ��ʵ�ֹ��λ�ƹ���
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
		// �ݹ����UIִ�м�����Ϊ
		super.uiKeyAction(keystate);
	}
}
