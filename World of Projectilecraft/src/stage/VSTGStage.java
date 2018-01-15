/**
 * �ļ����ƣ�VSTGStage.java
 * ��·����stage
 * ������TODO Ӧ����STG�����Ϸ�еĹؿ���
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����09:59:25
 * �汾��Ver 1.0
 */
package stage;

import java.util.ArrayList;

import interfaces.*;
import controller.*;
import data.*;
import entities.*;
import fsm.FSMclass;
import fsm.FSMstate;
import global.*;
import system.*;
import timer.*;
import ui.VUI;
import ui.VUIManager;

/**
 * @author Demilichzz
 *
 */
public class VSTGStage extends VStage implements VKeyProcessorInterface{
	protected VLuaAction stageAction;	//�ؿ���Ϊ�������ڶ���ؿ��е������¼��������Ķ����ڸ���ʱ���д���
	//protected FSMclass fsm_sstage;	//�ؿ���״̬��
	
	public VSTGStage(){
		super();
	}
	public VSTGStage(GameState gs,String sid){
		super(gs,sid);
	}
	public void Init(){
		// TODO ��ʼ���ؿ�
		super.Init();
		InitFSM();
		//Debug.DebugSimpleMessage("STG�ؿ���ʼ�����");
		inited = true;
	}
	public void InitFSM(){
		fsm_sstage = new FSMclass(FSMconst.GS_GAME);
		FSMstate fsms;
		fsms = new FSMstate(FSMconst.GS_GAME, 2);
		fsms.AddTransition(FSMconst.INPUT_PAUSE, FSMconst.GS_PAUSE);
		fsms.AddTransition(FSMconst.INPUT_DIE,FSMconst.GS_DIE);
		fsm_sstage.AddState(fsms);
		fsms = new FSMstate(FSMconst.GS_PAUSE, 2);
		fsms.AddTransition(FSMconst.INPUT_CONTINUE, FSMconst.GS_GAME);
		fsms.AddTransition(FSMconst.INPUT_DIE,FSMconst.GS_DIE);
		fsm_sstage.AddState(fsms);
		fsms = new FSMstate(FSMconst.GS_DIE,1);
		fsm_sstage.AddState(fsms);
	}
	public void setAction(VLuaAction a){
		stageAction = a;
	}
	public void resetStageState(){
		super.resetStageState();
		updatecount = 0;
	}
	public void updateStage(){
		// TODO ����GS��״̬���µ�ǰ�ؿ�
		if(!inited){
			Init();
		}
		switch (fsm_sstage.GetCurrentState()) {
		case FSMconst.GS_GAME: {
			tp.process();
			if (stageAction != null) {
				stageAction.action();
			}
			GameData.updateSpriteList(); //������Ϸ�����е������б�
			GameData.updateUnitList(); //���µ�ǰ�ؿ���ս����λ�б�
			GameData.collisionDetection(); //������ײ���
			updatecount++;
			break;
		}
		case FSMconst.GS_PAUSE: {
			VUI menu = VEngine.gs.uiparent.getUIByID("ui_pause_menu");
			if(!menu.visible){			//��״̬������ͣʱ����һ����ʾ��ͣ�˵�
				menu.setActionVisible(true);
			}
			break;
		}
		case FSMconst.GS_DIE:{
			VUI menu = VEngine.gs.uiparent.getUIByID("ui_pause_menu");
			if(!menu.visible){			//��״̬��������ʱ����һ����ʾ��ͣ�˵�
				VUIManager.loadPauseMenu(VUIManager.PMENU_DIE);
				menu.setActionVisible(true);
			}
			break;
		}
		default: {

		}
		}
	}
	public void processKeyAction(int[] keystate) {
		// TODO Auto-generated method stub
		for(int i=0;i<keystate.length;i++){
			if(keystate[i]==1){	//�����Ӧ�����Ѱ���
				keyProcess(i);	//���ü����¼�������
			}
		}
		switch (fsm_sstage.GetCurrentState()) {
		case FSMconst.GS_GAME: {	//��Ϸ״̬
			GameData.getPlayer().processKeyAction(keystate);	//��Ұ�����Ӧ
			break;
		}
		case FSMconst.GS_PAUSE: {	//��ͣ״̬
			break;
		}
		case FSMconst.GS_DIE:{
			break;
		}
		}
	}
	public void fsmStateTransition(int fsm_action){
		// TODO �����״̬������״̬����״̬ת��
		fsm_sstage.SetCurrentState(fsm_sstage.StateTransition(fsm_action));
	}
	public void keyProcess(int index){
		// TODO ���ݴӼ�������ȡ�ļ�����Ϣ�������¼����������Ϊ������Ӧ����Ϸ��������
		switch(index){
		case GameListener.KEY_UP:{
			//GameData.pc.move(0, -3.5);
			break;
		}
		case GameListener.KEY_DOWN:{
			//GameData.pc.move(0, 3.5);
			break;
		}
		case GameListener.KEY_LEFT:{
			//GameData.pc.move(-3.5, 0);
			break;
		}
		case GameListener.KEY_RIGHT:{
			//GameData.pc.move(3.5, 0);
			break;
		}
		case GameListener.KEY_ENTER:{
			break;
		}
		case GameListener.KEY_SPACE:{
			break;
		}
		case GameListener.KEY_ESC:{
			break;
		}
		case GameListener.KEY_Z:{
			break;
		}
		case GameListener.KEY_X:{
			break;
		}
		case GameListener.KEY_C:{
			break;
		}
		}
	}
}
