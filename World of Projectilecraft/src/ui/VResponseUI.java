/**
 * 	TODO 可自定义键盘响应行为的UI，用于在UI处理阶段获取暂停操作
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
		// TODO UI键盘响应行为
		if(keystate[GameListener.KEY_ESC]==1){	//ESC，执行取消行为
			VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_PAUSE);
			VEngine.glistener.resetKeystate(GameListener.KEY_ESC);
			enable=false;
		}
		// 递归对子UI执行键盘行为
		super.uiKeyAction(keystate);
	}
}
