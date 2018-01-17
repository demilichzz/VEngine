/**
 * 文件名称：GameState.java
 * 类路径：system
 * 描述：TODO 游戏状态，用于控制所有游戏状态相关的更改
 * 作者：Demilichzz
 * 时间：2011-10-26上午08:34:38
 * 版本：Ver 1.0
 */
package system;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import xml.*;
import config.ProjectConfig;
import controller.*;
import data.*;
import jgui.*;
import entities.*;
import event.*;
import factory.*;
import fsm.*;
import global.*;
import script.*;
import stage.*;
import timer.*;
import ui.*;
import view.*;

/**
 * @author Demilichzz
 * 
 */
public class GameState {
	protected VEngine ve;	//引擎主体
	protected int time = 10; // 仿真器更新一次经过的时间,单位ms
	protected HashMap<String,VStage> stagelist = new HashMap<String,VStage>();
	protected String currentstage;
	protected ArrayList<VLuaAction> delayedAction;	//延迟到本次更新结束时执行的行为列表
	public int runTime = 0; // 活动游戏状态更新的次数
	public VUI uiparent;	//所有UI的最终父UI
	protected ArrayList<VGraphicEffect> effectlist = new ArrayList<VGraphicEffect>();	//图像特效列表
	protected ArrayList<VGraphicEffect> effectdrawlist = new ArrayList<VGraphicEffect>();
	public FSMclass fsm_GS;	//主体GS的状态机
	public LuaScript lua_core;	//lua核心
	public LuaScript lua_timer;	//lua计时器
	public VTimerProcessor gs_tp;
	//-------------------Game Parameters-------------------------
	
	public GameState(VEngine ve,int t) {
		this.ve = ve;
		setTime(t);
		// Init();
	}
	private void setTime(int t){
		if (t > 0) {
			time = t;
		} else
			time = 1;
	}
	public double getSecond(){ // 
		// TODO 返回仿真器每次更新花费的时间,单位为秒
		return time * 0.001;
	}
	public int getMSecond(){ // 
		// TODO 返回仿真器每次更新花费的时间,单位为毫秒
		return time;
	}
	public void Render(){
		// TODO 通知GamePanel重新绘制
		ve.p.repaint();
	}
	public void Init(){
		// TODO 初始化游戏状态,此初始化在游戏程序启动时进行,因此初始化的项目均为不会随着游戏进程改变的项目
		InitInGame();
	}
	public void InitInGame() {
		// TODO 在游戏进行时的初始化部分,初始化的项目为有可能随着游戏进程改变的项目,可以被反复调用以重置游戏开始时的状态
		InitFSM();
		InitLuaScript();
		InitUI();
		Soundconst.bgm.soundPlay(0);
		InitStage();
		gs_tp = new VTimerProcessor();
	}
	public void resetGameState(){
		// TOOD 重置游戏状态中的游戏相关参数
		fsm_GS.SetCurrentState(FSMconst.GS_GAME);
		InitStage();
		runTime = 0;
		effectlist.clear();
		effectdrawlist.clear();
		gs_tp = new VTimerProcessor();
		Animeconst.resetAnime();
	}
	private void InitStage() {
		// TODO 初始化关卡信息
		stagelist = new HashMap<String,VStage>();
		delayedAction = new ArrayList<VLuaAction>();
		effectlist = new ArrayList<VGraphicEffect>();
		effectdrawlist = new ArrayList<VGraphicEffect>();
		VStage v = new VStage(this,"stage_01");	//新建标准stage并加入到游戏状态中的关卡列表
		VSTGStage stg = new VSTGStage(this,"stage_stg_01");
		stg.Init();
		stg.setAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
			}	
		});
		SpriteFactory.setDefaultState();
		this.setCurrentStage("stage_01");
		//lua_core.runScriptFunction("initStage",null);	//加载Lua文件中定义的战斗单位更新行为
	}
	private void InitLuaScript(){
		// TODO 初始化Lua脚本
		//lua_timer = new LuaScript("data/Script/timer.lua");
		Debug.DebugSimpleMessage("载入Lua脚本资源");
		if(lua_core==null){
			lua_core=new LuaScript("data/Script/initStage.lua");
		}
		try {
			Thread.sleep(0);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//lua_core=new LuaScript("data/Script/th_initstage.lua");
	}

	private void InitUI() {
		// TODO 初始化UI
		//UILoader uil = new UILoader();
		uiparent = new VUI(800,600,"uiparent");		//建立空UI作为根UI
		uiparent.setLoc(0, 0);
		//-------------未使用UI--------------------------------------------------
		VUI ui_unused = new VUI(0,0,"ui_unused");
		ui_unused.setVisible(false);
		ui_unused.setParent(uiparent);
		//-------------主菜单----------------------------------------------------
		VUI uibg = new VUI("UI_title_bg.png","ui_bg");
		uibg.setParent(uiparent);
		uibg.addVisibleAction(new VLuaAction(){
			@Override
			public void action() {
				// TODO Auto-generated method stub
				uiparent.getUIByID("ui_titleconfigp").setVisible(false);
				uiparent.getUIByID("ui_gamehint").setVisible(false);
			}
		});
		VUI ui_author = new VUI("null.png","ui_author");
		ui_author.setParent(uibg);
		ui_author.setLoc(610,590);
		ui_author.addText("By：水晶骨架   20121202", 0, 0, Global.f, Global.c);
		VUI ui_version = new VUI("null.png","ui_version");
		ui_version.setParent(uibg);
		ui_version.setLoc(680,570);
		ui_version.addText(VEngine.version, 0, 0, Global.f, Global.c);
		final VUI ui_titlemenu = new VUI("UI_title_menu.png","ui_titlemenu");
		ui_titlemenu.setParent(uibg);
		ui_titlemenu.setLoc(325,360);
		VUI ui_btn_start = new VUI("UI_button_titlestart.png","ui_btn_start");
		ui_btn_start.setLoc(15, 10);
		ui_btn_start.setParent(ui_titlemenu);
		ui_btn_start.addAction(new VLuaAction(){
			public void action() {
				// TODO 开始游戏
				GlobalEvent.startGame();
			}
		});
		final VConfigPanelUI ui_configpanel = new VConfigPanelUI("UI_600x320_config.png","ui_titleconfigp");
		ui_configpanel.setParent(uibg);
		ui_configpanel.setLoc(100,250);
		ui_configpanel.setVisible(false);
		ui_configpanel.addCancelAction(new VLuaAction(){
			public void action() {
				ui_configpanel.setVisible(false);
				ui_titlemenu.setVisible(true);
				ProjectConfig.saveGameConfig();
			}
		});
		final VMenuUI ui_gamehint = new VMenuUI("UI_gamehint.png","ui_gamehint");
		ui_gamehint.setParent(uibg);
		ui_gamehint.setLoc(100,250);
		ui_gamehint.setVisible(false);
		ui_gamehint.addCancelAction(new VLuaAction(){
			public void action() {
				ui_gamehint.setVisible(false);
				ui_titlemenu.setVisible(true);
			}
		});
		VUI ui_btn_control = new VUI("UI_button_titleconfig.png","ui_btn_control");
		ui_btn_control.setLoc(15, 50);
		ui_btn_control.setParent(ui_titlemenu);
		ui_btn_control.addAction(new VLuaAction(){
			public void action(){
				ui_titlemenu.setVisible(false);
				ui_configpanel.setVisible(true);
			}
		});
		VUI ui_btn_hint = new VUI("UI_button_titlehint.png","ui_btn_hint");
		ui_btn_hint.setLoc(15, 90);
		ui_btn_hint.setParent(ui_titlemenu);
		ui_btn_hint.addAction(new VLuaAction(){
			public void action(){
				ui_titlemenu.setVisible(false);
				ui_gamehint.setVisible(true);
			}
		});
		VUI ui_btn_replay = new VUI("UI_button_titlereplay.png","ui_btn_replay");
		ui_btn_replay.setLoc(15, 130);
		ui_btn_replay.setParent(ui_titlemenu);
		ui_btn_replay.addAction(new VLuaAction(){
			public void action() {
				// TODO 载入录像
				VUIManager.loadUIState(VUIManager.UI_LOADREPLAY);	//载入播放录像UI
				/*VListUI ui_list = (VListUI) uiparent.getUIByID("ui_replaylist");
				ui_list.setVisible(true);*/
				GlobalEvent.loadReplayFile();
			}
		});
		VCursorUI ui_cursor = new VCursorUI("UI_cursor.png","ui_cursor");
		ui_cursor.setParent(ui_titlemenu);
		ui_cursor.bindUI(ui_btn_start);
		ui_cursor.bindUI(ui_btn_control);
		ui_cursor.bindUI(ui_btn_hint);
		ui_cursor.bindUI(ui_btn_replay);
		ui_cursor.setOffset(-40, 0);
		//-------------录像菜单列表-------------------------------------------
		VListUI ui_replaylist = new VListUI("UI_replay.png","ui_replaylist");
		ui_replaylist.setParent(uiparent);
		ui_replaylist.setVisible(false);
		//-------------开始游戏菜单-------------------------------------------------
		VUI ui_go_panel = new VUI(800,600,"ui_go_panel");	//游戏选项面板
		ui_go_panel.setParent(uiparent);
		ui_go_panel.setVisible(false);
		ui_go_panel.addVisibleAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				uiparent.getUIByID("ui_talent_menu").setVisible(false);
				uiparent.getUIByID("ui_mode_menu").setVisible(false);
				uiparent.getUIByID("ui_menu_describe").setVisible(false);
			}
		});
		VUI ui_instance_1 = new VUI("UI_instance_1.png","ui_instance_1");
		ui_instance_1.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.loadInstance(1);
			}
		});
		VUI ui_instance_2 = new VUI("UI_instance_2_close.png","ui_instance_2");
		ui_instance_2.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.loadInstance(2);
			}
		});
		VUI ui_instance_3 = new VUI("UI_instance_3_close.png","ui_instance_3");
		ui_instance_3.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.loadInstance(3);
			}
		});
		VUI ui_instance_4 = new VUI("UI_instance_4_close.png","ui_instance_4");
		ui_instance_4.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.loadInstance(4);
			}
		});
		VUI ui_instance_5 = new VUI("UI_instance_5_close.png","ui_instance_5");
		ui_instance_5.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.loadInstance(5);
			}
		});
		VScrollMenuUI ui_instance_menu = new VScrollMenuUI(800,600,"ui_instance_menu");
		ui_instance_menu.addCancelAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				VUIManager.loadUIState(VUIManager.UI_TITLE);
				//GlobalEvent.exitToMain();
			}
		});
		ui_instance_menu.bindUI(ui_instance_1);
		ui_instance_menu.bindUI(ui_instance_2);
		ui_instance_menu.bindUI(ui_instance_3);
		ui_instance_menu.bindUI(ui_instance_4);
		ui_instance_menu.bindUI(ui_instance_5);
		ui_instance_menu.setParent(ui_go_panel);
		ui_instance_menu.setVisible(false);
		VUI ui_talent_1 = new VUI("talent_fs_0.png","ui_talent_1");
		ui_talent_1.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setTalent(0);
			}
		});
		VUI ui_talent_2 = new VUI("talent_fs_1.png","ui_talent_2");
		ui_talent_2.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setTalent(1);
			}
		});
		VUI ui_talent_3 = new VUI("talent_fs_2.png","ui_talent_3");
		ui_talent_3.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setTalent(2);
			}
		});
		final VScrollMenuUI ui_talent_menu = new VScrollMenuUI(800,600,"ui_talent_menu");
		ui_talent_menu.addCancelAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.menuBackToInstance();
			}
		});
		ui_talent_menu.addSwitchAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				int index=ui_talent_menu.getIndex()+1;
				uiparent.getUIByID("ui_menu_describe").setImage("Menu_Scribe_"+index+".png");
			}
		});
		ui_talent_menu.setPeridic(true);
		ui_talent_menu.setDistribution(128, 0, 364, 264);
		ui_talent_menu.bindUI(ui_talent_1);
		ui_talent_menu.bindUI(ui_talent_2);
		ui_talent_menu.bindUI(ui_talent_3);
		ui_talent_menu.setParent(ui_go_panel);
		ui_talent_menu.setVisible(false);
		VUI ui_mode_1 = new VUI("Mode_Bronze.png","ui_mode_1");
		ui_mode_1.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setMode(0);
				VUIManager.loadPauseMenu(VUIManager.PMENU_STANDARD);
				GlobalEvent.startCombat();
			}
		});
		VUI ui_mode_2 = new VUI("Mode_Silver.png","ui_mode_2");
		ui_mode_2.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setMode(1);
				VUIManager.loadPauseMenu(VUIManager.PMENU_STANDARD);
				GlobalEvent.startCombat();
			}
		});
		VUI ui_mode_3 = new VUI("Mode_Gold.png","ui_mode_3");
		ui_mode_3.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GameData.setMode(2);
				VUIManager.loadPauseMenu(VUIManager.PMENU_STANDARD);
				GlobalEvent.startCombat();
			}
		});
		final VScrollMenuUI ui_mode_menu = new VScrollMenuUI(800,600,"ui_mode_menu");
		ui_mode_menu.setPeridic(true);
		ui_mode_menu.setDistribution(150, 0, 357, 257);
		ui_mode_menu.bindUI(ui_mode_1);
		ui_mode_menu.bindUI(ui_mode_2);
		ui_mode_menu.bindUI(ui_mode_3);
		ui_mode_menu.setIndex(1);		//默认模式为生存模式
		ui_mode_menu.setParent(ui_go_panel);
		ui_mode_menu.setVisible(false);
		ui_mode_menu.addCancelAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.menuBackToTalent();
			}
		});
		ui_mode_menu.addSwitchAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				int index=ui_mode_menu.getIndex()+1;
				uiparent.getUIByID("ui_menu_describe").setImage("mode_describe_"+index+".png");
			}
		});
		VUI ui_menu_describe = new VUI("Menu_Scribe_1.png","ui_menu_describe");
		ui_menu_describe.setParent(ui_go_panel);
		ui_menu_describe.setLoc(200,350);
		VUI ui_menu_cover = new VUI("UI_menu_cover.png","ui_menu_cover");
		ui_menu_cover.setParent(ui_go_panel);
		//------------战斗界面------------------------------------------------------
		VUI ui_gamepanel = new VUI(800,600,"ui_gamepanel");		//游戏面板UI
		ui_gamepanel.setParent(uiparent);
		ui_gamepanel.addVisibleAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				uiparent.getUIByID("ui_pause_menu").setVisible(false);	//关闭暂停菜单显示
			}
		});
		VResponseUI ui_responser = new VResponseUI(0,0,"ui_responser");	//键盘响应UI
		ui_responser.setParent(ui_gamepanel);
		VCombatAreaUI ui_gamearea = new VCombatAreaUI("UI_400x600_gamearea.png","ui_gamearea");	//游戏区域UI
		ui_gamearea.setLoc(100, 0);
		ui_gamearea.setParent(ui_gamepanel);
		VUI ui_ga_cover = new VUI("UI_gamepanel_cover.png","ui_ga_cover");	//面板背景覆盖UI
		ui_ga_cover.setParent(ui_gamepanel);
		VDBMPanelUI ui_dbm = new VDBMPanelUI("UI_220x200_DBM_panel.png","ui_dbm");	//DBMUI
		ui_dbm.setParent(ui_ga_cover);
		ui_dbm.setLoc(540,0);
		VThreatBarUI ui_threat = new VThreatBarUI("UI_20x200_Threatbar_bottom.png","ui_pcthreat");
		ui_threat.setParent(ui_ga_cover);
		ui_threat.setLoc(780,380);
		ui_threat.bindPlayer(GameData.pc);
		VDynamicBarUI ui_bosslife = new VDynamicBarUI("UI_380x20_lifebar_bottom.png","UI_380x20_lifebar_cover.png","UI_380x20_lifebar_stcover.png","ui_bosslife");	//Boss血条UI
		ui_bosslife.setMaxValue(1000);
		ui_bosslife.setParent(ui_gamearea);
		ui_bosslife.setLoc(10,0);
		VSkillUI pcskill = new VSkillUI("Spell_IceBlock.png","ui_pcskill");		//技能UI
		pcskill.setLoc(536,400);
		pcskill.setCDAnime("skillcd_anime");
		pcskill.setOffset(-3,-3);	
		pcskill.setParent(ui_ga_cover);
		pcskill.bindValue(GameData.pc, 2);
		final VUI ui_pchit = new VUI("null.png","ui_pchit");
		ui_pchit.setLoc(550,380);
		ui_pchit.setParent(ui_ga_cover);
		ui_pchit.addAction(new VLuaAction(){
			@Override
			public void action() {
				// TODO 更新时显示玩家被击中次数
				if(GameData.getPlayer()!=null){
					ui_pchit.addText("被击中次数："+GameData.getPlayer().hit, 0, 0, Global.f, Global.c);
				}
			}
		});
		VDynamicBarUI ui_pclife = new VDynamicBarUI("UI_pclb_bottom.png","UI_pclb_cover.png","UI_pclb_stcover.png","ui_pclife");		//玩家血条UI
		ui_pclife.setMaxValue(100);
		ui_pclife.setParent(ui_ga_cover);
		ui_pclife.setLoc(500, 580);
		ui_pclife.bindValue(GameData.pc, 0);
		VLastLifeUI ui_lastlife = new VLastLifeUI("UI_lifeleft.png","ui_lifeleft");
		ui_lastlife.bindValue(GameData.pc, 1);
		ui_lastlife.setLoc(500,580);
		ui_lastlife.setParent(ui_ga_cover);
		//----------------暂停菜单-----------------------
		final VMenuUI ui_pause_menu = new VMenuUI("UI_pausemenu.png","ui_pause_menu");
		ui_pause_menu.addVisibleAction(new VLuaAction(){
			public void action(){
				uiparent.getUIByID("ui_responser").setEnable(false);		//显示时关闭响应器
				uiparent.getUIByID("ui_savehint").setVisible(false);
			}
		});
		ui_pause_menu.addCancelAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				if(getCurrentStage().getfsmState()!=FSMconst.GS_DIE){	//关卡状态不能为已死亡
					ui_pause_menu.setVisible(false);
					uiparent.getUIByID("ui_responser").setEnable(true);		//重新激活响应器
					getCurrentStage().fsmStateTransition(FSMconst.INPUT_CONTINUE);
				}
			}
		});
		ui_pause_menu.setLoc(100,0);
		ui_pause_menu.setOffset(-20, 10);
		ui_pause_menu.setCursor("UI_cursor.png");
		ui_pause_menu.setResponseMode(VUI.MENU_RESPONSE_UD);
		VUI ui_continue = new VUI("UI_pausebutton_continue.png","ui_pb_continue");
		ui_continue.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				if (getCurrentStage().getfsmState() != FSMconst.GS_DIE) {
					ui_pause_menu.setVisible(false);
					uiparent.getUIByID("ui_responser").setEnable(true); // 重新激活响应器
					getCurrentStage().fsmStateTransition(
							FSMconst.INPUT_CONTINUE);
				}
			}
		});
		ui_continue.setLoc(100, 200);
		VUI ui_exittomain = new VUI("UI_pausebutton_exittomain.png","ui_pb_exittomain");
		ui_exittomain.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				if(GameData.gamemode!=0&&!GameData.replaymode){
					VUIManager.loadPauseMenu(VUIManager.PMENU_CONFIRMREPLAY);
				}
				else{
					GlobalEvent.exitToMain();
				}
			}
		});		
		ui_exittomain.setLoc(100, 300);
		VUI ui_exitreplay = new VUI("UI_pausebutton_exitreplay.png","ui_pb_exitreplay");
		ui_exitreplay.addAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.exitToMain();
				VUIManager.loadUIState(VUIManager.UI_LOADREPLAY);
			}
		});
		ui_exitreplay.setLoc(100,300);
		ui_exitreplay.setParent(ui_unused);
		final VUI ui_savehint = new VUI(1,1,"ui_savehint");
		ui_savehint.setParent(ui_pause_menu);
		ui_savehint.setLoc(60,240);
		ui_savehint.setVisible(false);
		final VUI ui_savereplay = new VUI("UI_pausebutton_savereplay.png","ui_pb_savereplay");
		ui_savereplay.addAction(new VLuaAction(){
			public void action(){
				VUIManager.loadPauseMenu(VUIManager.PMENU_SAVED);
				GlobalEvent.saveReplay();
				ui_savereplay.setEnable(false);
			}
		});	
		ui_savereplay.setLoc(100,200);
		ui_savereplay.setParent(ui_unused);
		VUI ui_notsave = new VUI("UI_pausebutton_notsave.png","ui_pb_notsave");
		ui_notsave.addAction(new VLuaAction(){
			public void action(){
				GlobalEvent.exitToMain();
				VUIManager.loadPauseMenu(VUIManager.PMENU_STANDARD);
			}
		});
		ui_notsave.setParent(ui_unused);
		ui_notsave.setLoc(100,300);
		ui_pause_menu.bindUI(ui_continue);
		ui_pause_menu.bindUI(ui_exittomain);
		ui_pause_menu.setIfResetCursor(true);
		ui_pause_menu.setParent(ui_gamepanel);
		//--------------------------------------
		ui_gamepanel.setVisible(false);
		//VUI ui_config =new VUITemplateConfigPanel().createConfigPanel();
		//ui_config.setParent(uiparent);
		//ui_config.setVisible(false);
		//lua_core.runScriptFunction("initUI",uiparent);
		/*UISaver uis = new UISaver(ui_title_bg);
		 try {
		 uis.SaveUI("xml/ui_title.xml");
		 } catch (IOException e) {
		 // TODO Auto-generated catch blockv
		 e.printStackTrace();
		 } catch (JDOMException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }*/
	}
	private void InitFSM() {
		// TODO 初始化GS的状态机
		fsm_GS = new FSMclass(FSMconst.GS_GAME);
		FSMstate fsms;
		fsms = new FSMstate(FSMconst.GS_GAME, 1);
		fsms.AddTransition(FSMconst.INPUT_PAUSE, FSMconst.GS_PAUSE);
		fsm_GS.AddState(fsms);
		fsms = new FSMstate(FSMconst.GS_PAUSE, 1);
		fsms.AddTransition(FSMconst.INPUT_PAUSE, FSMconst.GS_GAME);
		fsm_GS.AddState(fsms);
	}

	public void updateState() {
		// TODO 由仿真器控制的游戏状态更新函数
		runTime++;
		//processMouseAction(ve.mlistener); //处理鼠标事件对GS的影响
		uiparent.uiKeyAction(ve.glistener.getKeystate()); //包含键盘响应行为的UI进行捕获操作
		if(currentstage.equals("stage_stg_01")&&!GameData.replaymode&&stagelist.get(currentstage).getfsmState()==FSMconst.GS_GAME){
			//当前关卡是STG，且是游戏进行状态，且关卡状态机为游戏状态
			GameData.record(ve.glistener.getKeystate());
		}
		if(GameData.replaymode){		//如果是录像
			if(currentstage.equals("stage_stg_01")&&stagelist.get(currentstage).getfsmState()==FSMconst.GS_GAME){
				// TODO 当前关卡是STG且处于游戏进行状态
				int[] replayks = GameData.rep.getKeystate();		//获取录像按键信息
				if(replayks!=null){
					processKeyAction(replayks);		//处理录像记载的按键信息
				}
				else{		//录像已结束
					GlobalEvent.gameOver();
				}
			}
			else{	//暂停或死亡状态
				processKeyAction(ve.glistener.getKeystate());
			}
		}
		else{
			processKeyAction(ve.glistener.getKeystate()); //处理键盘事件
		}
		gs_tp.process(); //处理Timer
		switch (fsm_GS.GetCurrentState()) {
		case FSMconst.GS_GAME: {
			if (currentstage != null) {
				stagelist.get(currentstage).updateStage(); //处理stage更新
			}
			break;
		}
		case FSMconst.GS_PAUSE: {

			break;
		}
		default: {

		}
		}
		uiparent.updateUI(); //更新UI显示
		processGraphicEffect(runTime);		//处理图像特效的更新
		if(GameData.gamemode==0){
			uiparent.getUIByID("ui_pchit").uiAction("");
		}
		if(delayedAction.size()>0){		//执行延迟行为
			for(VLuaAction a:delayedAction){
				a.action();
			}
			delayedAction.clear();
		}
		//Soundconst.ProcessSound();		//处理循环模式音效
	}
	/**
	 * @param runTime2
	 */
	public void processGraphicEffect(int rt) {
		// TODO 通过传入当前运行时间处理图像特效的状态更新
		ArrayList<VGraphicEffect> trash = new ArrayList<VGraphicEffect>();
		for(VGraphicEffect ge:effectlist){
			ge.update(rt);			//进行更新
			if(ge.getDestoryed()){		//如果特效将被销毁
				trash.add(ge);		//则添加到销毁列表
			}
		}
		effectlist.removeAll(trash);
		trash.clear();
		trash=null;
		effectdrawlist=(ArrayList<VGraphicEffect>) effectlist.clone();	//复制到绘制列表
	}
	public void addDelayedAction(VLuaAction action){
		// TODO 将行为添加到延迟列表，使该行为在本次GS更新之后执行
		if(action!=null){
			delayedAction.add(action);
		}
	}
	public void addEffect(VGraphicEffect e){
		if(e!=null){
			effectlist.add(e);
		}
	}
	/**
	 * @param mlistener
	 */
	private void processMouseAction(GameMouseListener mlistener) {
		// TODO 处理鼠标事件对GS造成的影响,
		/*motionProcess(mlistener);
		if(mlistener.ifpress){
			pressProcess(mlistener.x,mlistener.y);
			mlistener.ifpress = false;
		}
		if(mlistener.ifdrag){
			if(mlistener.getDragTarget()!=null){
				mlistener.getDragTarget().dragTo(mlistener.x, mlistener.y);
			}
		}
		else{
			mlistener.setDragTarget(null);
		}*/
	}
	private void processKeyAction(int[] keystate){
		// TODO 在GS更新时处理键盘事件
		if(keystate!=null){
			this.getCurrentStage().processKeyAction(keystate);	//调用当前关卡的键盘响应
			for(int i=0;i<keystate.length;i++){
				if(keystate[i]==1){	//如果对应按键已按下
					keyProcess(i);	//调用键盘事件处理函数
				}
			}
		}
	}
	public void keyProcess(int index){
		// TODO 根据从监听器获取的键盘信息处理按键事件，传入参数为按键对应的游戏操作索引
		switch(index){
		case GameListener.KEY_UP:{
			break;
		}
		case GameListener.KEY_DOWN:{
			break;
		}
		case GameListener.KEY_LEFT:{
			break;
		}
		case GameListener.KEY_RIGHT:{
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
			//GlobalEvent.loadReplay();
			if(VEngine.developmode){		//开发模式则保存录像
				GlobalEvent.saveReplay();
				VEngine.glistener.resetKeystate(GameListener.KEY_C);
			}
			break;
		}
		case GameListener.KEY_SHIFT:{
			break;
		}
		}
	}
	/**
	 * @param x
	 * @param y
	 */
	private void motionProcess(GameMouseListener ml) {
		// TODO 处理鼠标移动事件,传入参数为鼠标监听器
		VUI temp = uiparent.getUIbyLoc(ml.x,ml.y);
/*		if(temp!=null&&!(temp instanceof VASizeUI)){
			ml.setPromptUI(temp);
		}*/
	}
	/**
	 * @param x
	 * @param y
	 */
	private void pressProcess(int x, int y) {
		// TODO 根据从监听器获取的点击信息处理鼠标点击事件，传入参数为鼠标点击坐标
		//Debug.DebugSimpleMessage(x+" "+y);
		if(false){
		/*if(GlobalEvent.ui_lock&&GlobalEvent.lockedUI!=null){	//如果UI状态为锁定且锁定对象不为空
			VMouseActionUI tmaUI = (VMouseActionUI) GlobalEvent.lockedUI.getMAUIbyLoc(x, y);
			if(tmaUI!=null){
				tmaUI.uiAction("MouseEvent");
			}
		}*/
		}
		else if(uiparent.getMAUIbyLoc(x,y)!=null){
			uiparent.getMAUIbyLoc(x,y).uiAction("MouseEvent");
		}
	}

	public void drawScene(Graphics2D g2d, GamePanel p) {
		// TODO 渲染游戏场景
		uiparent.drawUI(g2d, p);
		for(VGraphicEffect ge:effectdrawlist){
			ge.drawEffect(g2d,p);
		}
	}

	public void InputOperate(int i) {
		// TODO 根据GameListener获取的键盘输入,执行对应键位列表项的操作
		switch(i){
		case 0:{
			break;
		}
		case 1:{
			break;
		}
		}
	}

	public void addStage(VStage stage) throws Exception {
		// TODO 向游戏状态的关卡列表HashMap中添加关卡
		if(stagelist.containsKey(stage.getStageID())){
			throw(new Exception("已存在重复的关卡ID"));
		}
		else{
			stagelist.put(stage.getStageID(),stage);
		}
	}
	public void removeStage(String key){
		if(stagelist.containsKey(key)){
			stagelist.remove(key);
		}
		else{
			Debug.DebugSimpleMessage("不存在要移除的关卡");
		}
	}
	public void setCurrentStage(String id){
		// TODO 设置当前关卡索引
		currentstage = id;
	}
	public VStage getCurrentStage(){
		return stagelist.get(currentstage);
	}
}
