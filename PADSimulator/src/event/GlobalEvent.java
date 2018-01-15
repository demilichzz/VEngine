/**
 * @author Demilichzz
 *	全局事件静态类，包含在全局任何位置均可调用的事件函数
 * 2013-11-14
 */
package event;

import data.GameData;
import entities.VCharacter;
import interfaces.*;
import script.LuaScript;
import system.*;
import ui.*;

/**
 * @author Demilichzz
 *
 * 2013-11-14
 */
public class GlobalEvent {
	protected static GameState gs;
	
	public static void initEvent(){
		// TODO 初始化事件系统
		gs = VEngine.newgame.gs;
	}
	public static VUI getUIByID(String id){
		// TODO 按ID获取UI
		VUI tempui = null;
		tempui = gs.uiparent.getUIByID(id);
		return tempui;
	}
	public static VOrbUI getMouseHoldUI(){
		// TODO 获取转珠时鼠标控制的宝珠UI
		VOrbUI ui = (VOrbUI) gs.uiparent.getUIByID("ui_mousehold");
		return ui;
	}
	public static VArrowPanelUI getArrowUI(){
		// TODO 获取绘制转珠过程箭头的UI
		VArrowPanelUI ui = (VArrowPanelUI)gs.uiparent.getUIByID("ui_arrowpanel");
		return ui;
	}
	public static VGameAreaUI getGameAreaUI(){
		VGameAreaUI ui = (VGameAreaUI) gs.uiparent.getUIByID("ui_gamearea");
		return ui;
	}
	public static VPartyUI getPartyUI(){
		VPartyUI ui = (VPartyUI) gs.uiparent.getUIByID("ui_party");
		return ui;
	}
	public static VInstanceUI getInstanceUI(){
		VInstanceUI ui = (VInstanceUI) gs.uiparent.getUIByID("ui_instance");
		return ui;
	}
	public static void resetOrb(){
		// TODO 重置宝石分布
		GameData.ga.resetOrb();
	}

	public static LuaScript getLuaCore() {
		// TODO Auto-generated method stub
		return VEngine.newgame.gs.lua_core;
	}
	public static void movingLockUI(boolean b) {
		// TODO 在转珠过程锁定或解锁不允许操作的UI
		getUIByID("ui_gamearea").setEnable(!b);
		getUIByID("ui_party").setEnable(!b);
	}
	public static void showDisableCover(boolean b) {
		// TODO 显示禁止操作宝石面板的黑色半透明蒙版
		getGameAreaUI().setShowDisableCover(b);
	}
	public static void startInstance() {
		// TODO 开始进行副本战斗
		//GameData.instance.loadEnemyData();		//加载副本数据
		GameData.party.loadPartyData();			//加载队伍数据
		GlobalEvent.resetOrb();					//生成宝石面板
		VCharacter[]charlist = GameData.party.getCharList();
		int awoken_skillboost = GameData.party.getAvaliableAwoken(VCharacter.AWOKEN_SKILLBOOST);
		for(int i=0;i<charlist.length;i++){			//技能冷却觉醒
			charlist[i].processCoolDown(awoken_skillboost);
		}
		GlobalEvent.getMouseHoldUI().setValue(0, GameData.party.getMoveTime());	//设置转珠计时条UI最大值
		GameData.instance.gameStart();
	}
}
