/**
 * 	TODO ���Զ��������Ӧ��Ϊ��UI��������UI����׶λ�ȡ��ͣ����
 */
package ui;

import global.FSMconst;

import java.util.ArrayList;

import system.VEngine;
import controller.GameListener;

/**
 * @author Demilichzz
 *
 * 2012-11-16
 */
public class VResponseUI extends VUI{
	public VResponseUI(){
		super();
	}
	public VResponseUI(String str,String id){
		super(str,id);
	}
	public VResponseUI(int w, int h, String str) {
		// TODO Auto-generated constructor stub
		super(w,h,str);
	}
	public void uiKeyAction(int[] keystate) {
		// TODO UI������Ӧ��Ϊ
		if(keystate[GameListener.KEY_ESC]==1){	//ESC��ִ��ȡ����Ϊ
			VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_PAUSE);
			VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
			enable=false;
		}
		// �ݹ����UIִ�м�����Ϊ
		super.uiKeyAction(keystate);
	}
}
