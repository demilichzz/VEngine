/**
 * 文件名称：GlobalEvent.java
 * 类路径：event
 * 描述：TODO 全局事件静态类，在此静态类预定义需要在全局范围内调用的事件类型为函数
 * 作者：Demilichzz
 * 时间：2012-8-6下午06:30:23
 * 版本：Ver 1.0
 */
package event;

import interfaces.VInt;

import java.io.*;
import java.util.*;

import data.GameData;
import entities.VCombatUnit;
import entities.VSprite;
import factory.SpriteFactory;
import global.FSMconst;
import global.Soundconst;
import global.VMath;
import script.LuaScript;
import system.*;
import timer.*;
import ui.*;
import view.VGEffectFade;

/**
 * @author Demilichzz
 *
 */
public class GlobalEvent {
	protected static GameState gs = VEngine.gs;
	
	public static void menuBackToInstance(){
		// TODO 选择天赋时取消，菜单回到选择副本
		VUI imenu=gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, 150);		//副本UI下移
		imenu.setEnable(true);		//激活副本UI
		gs.uiparent.getUIByID("ui_talent_menu").setVisible(false);
		gs.uiparent.getUIByID("ui_menu_describe").setVisible(false);
	}
	public static void menuBackToTalent() {
		// TODO 菜单返回到选择天赋
		VUI imenu=gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, 150);		//副本UI下移
		VScrollMenuUI m=(VScrollMenuUI) gs.uiparent.getUIByID("ui_talent_menu");
		m.moveLoc(0, 150);
		m.setEnable(true);
		m.moveCursor(0);
		gs.uiparent.getUIByID("ui_mode_menu").setVisible(false);
	}
	public static void gameOver(){
		// TODO 玩家死亡时调用的函数
		GameData.rep.setEndFrame(VEngine.gs.getCurrentStage().updatecount);
		VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_DIE);	//向关卡状态机输入死亡状态
	}
	public static void loadInstance(int index){
		// TODO 根据索引载入对应副本的弹幕脚本
		switch(index){
		case 1:{		//龙魂
			//gs.lua_core=new LuaScript("data/Script/initStage.lua");
			break;
		}
		case 2:{
			return;
		}
		case 3:{
			return;
		}
		case 4:{
			return;
		}
		case 5:{
			return;
		}
		}
		VUI imenu=gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, -150);
		imenu.setEnable(false);
		VScrollMenuUI m = (VScrollMenuUI) gs.uiparent.getUIByID("ui_talent_menu");
		m.setVisible(true);
		m.moveCursor(0);
		gs.uiparent.getUIByID("ui_menu_describe").setVisible(true);
	}
	public static void startGame(){
		// TODO 开始游戏事件
		VUIManager.loadUIState(VUIManager.UI_GAMEMODE);
	}
	public static void startCombat(){
		// TODO 开始游戏事件
		VEngine.gs.uiparent.getUIByID("ui_mode_menu").setEnable(false);
		VEngine.gs.uiparent.getUIByID("ui_replaylist").setEnable(false);
		setBackground("UI_400x600_gamearea_1.png");		//设置初始背景
		VGEffectFade fade = new VGEffectFade(0,0,800,600,50);	//创建淡出
		VTimer tm = new VTimer(600,600,false,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub	
				GameData.rep.InitReplay();		//初始化录像文件
				if(GameData.replaymode){
					setRandomSeed(GameData.rep.randomseed);		//如果是播放录像则将随机种子设为录像中记载的种子
				}
				else{											//否则取当前毫秒数为种子并在游戏数据中储存种子值
					long seed = System.currentTimeMillis();
					setRandomSeed(seed);
					GameData.rep.randomseed=seed;
				}
				gs.lua_core.runScriptFunction("initStage", null);	//载入副本脚本中定义的行为，包括Boss
				try {
					Thread.sleep(200);		//休眠一段时间令副本脚本能够创建boss对象
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Soundconst.bgm.soundPlay(GameData.boss.getBGM());
				VUIManager.loadUIState(VUIManager.UI_GAME0+GameData.gamemode);	//根据游戏模式加载UI
				gs.setCurrentStage("stage_stg_01");		//进入STG关卡
			}	//为确保转场特效全黑时再进行载入的计时器
		});
		tm.timerStart(0);
	}
	public static void phaseTransition(){
		// TODO 阶段转换，在阶段转换时，清空所有粒子和计时器
		GameData.spriteList=new ArrayList<VSprite>();
		GameData.spriteList_temp=new ArrayList<VSprite>();
		for(VCombatUnit unit:GameData.unitlist){
			if(unit.getValue(1)==0){
				unit.die(VCombatUnit.DEATH_PHASE);
			}
		}
		for(VCombatUnit unit:GameData.unitlist_temp){
			if(unit.getValue(1)==0){
				unit.die(VCombatUnit.DEATH_PHASE);
			}
		}
		//gs.gs_tp.reset();
		gs.getCurrentStage().tp.reset();	//清空关卡内Timer处理器的内容
	}
	public static void clearUnit(){
		// TODO 清除全部战斗单位
		if(GameData.boss.alive){
			GameData.boss.die(2);
		}
		for(VCombatUnit unit:GameData.unitlist){
			unit.die(VCombatUnit.DEATH_PHASE);
		}
		for(VCombatUnit unit:GameData.unitlist_temp){
			unit.die(VCombatUnit.DEATH_PHASE);
		}
	}
	public static void setBackground(String str){
		// TODO 设置背景
		VUI bg = VEngine.gs.uiparent.getUIByID("ui_gamearea");
		bg.setImage(str);
	}
	public static void activateNextBoss() {
		// TODO 激活下一个Boss
		clearUnit();
		phaseTransition();		//清空
		VGEffectFade fade = new VGEffectFade(100,0,400,600,50);
		if(GameData.getStage()<4){		//当前关卡还不是最终关卡
			VTimer tm = new VTimer(600,600,false,new VLuaAction(){	//转场之后先加载背景图像
				public void action(){
					int i = GameData.stage+1;
					if(i<5){
						setBackground("UI_400x600_gamearea_"+i+".png");
					}
				}
			});
			tm.timerStart(0);
			VTimer tma = new VTimer(3600,3600,false,new VLuaAction(){
				public void action() {
					// TODO Auto-generated method stub
					VEngine.gs.lua_core.runScriptFunction("activateNextBoss", null);//载入下一个Boss
				}	//为确保转场特效全黑时再进行载入的计时器
			});
			tma.timerStart(0);
		}	
		else{		//最终关卡则只等待600ms
			VTimer tma = new VTimer(600,600,false,new VLuaAction(){
				public void action() {
					// TODO Auto-generated method stub
					VEngine.gs.lua_core.runScriptFunction("activateNextBoss", null);//载入下一个Boss
				}	//为确保转场特效全黑时再进行载入的计时器
			});
			tma.timerStart(0);
		}
	}
	public static void stageEnd(){
		// TODO 通关结束关卡，在Lua中调用
		VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_DIE);
	}

	public static void exitToMain() {
		// TODO 放弃游戏并退回主菜单
		Soundconst.speech.soundStop();
		SpriteFactory.resetFactoryState();
		GameData.InitData();		//重新初始化游戏数据
		VEngine.gs.resetGameState();		//重置游戏状态
		VDynamicBarUI pclb = (VDynamicBarUI) VEngine.gs.uiparent.getUIByID("ui_pclife");	//重新绑定UI
		pclb.bindValue(GameData.pc, 0);
		VThreatBarUI pctt = (VThreatBarUI) VEngine.gs.uiparent.getUIByID("ui_pcthreat");
		if(pctt!=null){
			pctt.bindPlayer(GameData.pc);
		}
		VSkillUI pcskill = (VSkillUI)VEngine.gs.uiparent.getUIByID("ui_pcskill");
		pcskill.bindValue(GameData.pc, 2);
		VLastLifeUI pcll = (VLastLifeUI) VEngine.gs.uiparent.getUIByID("ui_lifeleft");
		pcll.bindValue(GameData.pc, 1);
		Soundconst.bgm.soundPlay(0);		//wowmain.mp3
		VUIManager.loadUIState(VUIManager.UI_TITLE);
	}
	public static void setRandomSeed(long rseed){
		// TODO 设置随机种子
		VMath.setRandomSeed(rseed);
		VInt iseed = new VInt(Math.abs((int) rseed));
		VEngine.gs.lua_core.runScriptFunction("setRandomSeed", iseed);
	}
	public static void loadReplay(String path) {
		// TODO Auto-generated method stub
		GameData.rep.loadFromFile(path);		//从rep文件载入
		GameData.replaymode=true;
		GameData.rep.setGameState();		//设置游戏参数
		VUIManager.loadPauseMenu(VUIManager.PMENU_REPLAY);		//加载录像模式暂停菜单UI
		GlobalEvent.startCombat();
	}
	public static void saveReplay(){
		GameData.rep.saveToFile();
	}
	public static void loadReplayFile() {
		// TODO 载入录像文件列表
		VListUI replist = (VListUI) VEngine.gs.uiparent.getUIByID("ui_replaylist");
		String reppath = "data/Replay";
		File file = new File(reppath);
		ArrayList<String> filenames = new ArrayList<String>();
		String suffix = "rep";				//后缀
		filenames = listFile(filenames,file,suffix,false);		//获取录像目录下的所有录像文件名列表
		replist.setStringList(filenames);
	}
	public static void DBMAddSkill(String name,double time){
		VDBMPanelUI dbm = (VDBMPanelUI) VEngine.gs.uiparent.getUIByID("ui_dbm");
		dbm.addSkill(name, time);
	}
	private static ArrayList<String> listFile(ArrayList<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// 若是目录, 采用递归的方法遍历子目录
		if (f.isDirectory()) {
			File[] t = f.listFiles();
			for (int i = 0; i < t.length; i++) {
				if (isdepth || t[i].isFile()) {
					listFile(lstFileNames, t[i], suffix, isdepth);
				}
			}
		} 
		else {
			String filePath = f.getAbsolutePath();
			if (!suffix.equals("")) {
				int begIndex = filePath.lastIndexOf("."); // 最后一个.(即后缀名前面的.)的索引
				String tempsuffix = "";

				if (begIndex != -1) {
					tempsuffix = filePath.substring(begIndex + 1, filePath
							.length());
					if (tempsuffix.equals(suffix)) {
						lstFileNames.add(f.getName());
					}
				}
			} 
			else {
				lstFileNames.add(f.getName());
			}
		}
		return lstFileNames;
	}
}
