/**
 * 文件名称：VSTGStage.java
 * 类路径：stage
 * 描述：TODO 应用于STG射击游戏中的关卡类
 * 作者：Demilichzz
 * 时间：2012-8-6下午09:59:25
 * 版本：Ver 1.0
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
	protected VLuaAction stageAction;	//关卡行为对象，用于定义关卡中的所有事件及关联的对象，在更新时进行处理
	//protected FSMclass fsm_sstage;	//关卡的状态机
	
	public VSTGStage(){
		super();
	}
	public VSTGStage(GameState gs,String sid){
		super(gs,sid);
	}
	public void Init(){
		// TODO 初始化关卡
		super.Init();
		InitFSM();
		//Debug.DebugSimpleMessage("STG关卡初始化完成");
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
		// TODO 随着GS的状态更新当前关卡
		if(!inited){
			Init();
		}
		switch (fsm_sstage.GetCurrentState()) {
		case FSMconst.GS_GAME: {
			tp.process();
			if (stageAction != null) {
				stageAction.action();
			}
			GameData.updateSpriteList(); //更新游戏数据中的粒子列表
			GameData.updateUnitList(); //更新当前关卡的战斗单位列表
			GameData.collisionDetection(); //进行碰撞检测
			updatecount++;
			break;
		}
		case FSMconst.GS_PAUSE: {
			VUI menu = VEngine.gs.uiparent.getUIByID("ui_pause_menu");
			if(!menu.visible){			//在状态进入暂停时调用一次显示暂停菜单
				menu.setActionVisible(true);
			}
			break;
		}
		case FSMconst.GS_DIE:{
			VUI menu = VEngine.gs.uiparent.getUIByID("ui_pause_menu");
			if(!menu.visible){			//在状态进入死亡时调用一次显示暂停菜单
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
			if(keystate[i]==1){	//如果对应按键已按下
				keyProcess(i);	//调用键盘事件处理函数
			}
		}
		switch (fsm_sstage.GetCurrentState()) {
		case FSMconst.GS_GAME: {	//游戏状态
			GameData.getPlayer().processKeyAction(keystate);	//玩家按键响应
			break;
		}
		case FSMconst.GS_PAUSE: {	//暂停状态
			break;
		}
		case FSMconst.GS_DIE:{
			break;
		}
		}
	}
	public void fsmStateTransition(int fsm_action){
		// TODO 对玩家状态机输入状态进行状态转换
		fsm_sstage.SetCurrentState(fsm_sstage.StateTransition(fsm_action));
	}
	public void keyProcess(int index){
		// TODO 根据从监听器获取的键盘信息处理按键事件，传入参数为按键对应的游戏操作索引
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
