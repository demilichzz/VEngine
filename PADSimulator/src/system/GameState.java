/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
package system;

import java.util.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import data.*;
import display.*;
import entities.GameArea;
import entities.VEnemy;
import event.GlobalEvent;
import script.*;
import stage.*;
import timer.*;
import ui.*;
import view.*;
import fsm.*;
import global.*;

/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
public class GameState {
	protected VEngine ve;	//引擎主体
	protected int time = 10; // 仿真器更新一次经过的时间,单位ms
	public int runTime = 0; // 活动游戏状态更新的次数
	protected HashMap<String,VStage> stagelist = new HashMap<String,VStage>();
	protected String currentstage;
	public FSMclass fsm_GS;	//主体GS的状态机
	public VTimerProcessor gs_tp;
	public int renderCount = 0;
	public VUI uiparent;
	public LuaScript lua_core;	//lua核心
	
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
	public void Init() {
		// TODO Auto-generated method stub
		InitInGame();
	}
	public void InitInGame() {
		// TODO Auto-generated method stub
		VMath.setRandomSeed(0);
/*		int[] randB = new int[6];
		randB = VMath.GetRandomInts(1, 33, 6);
		int randR = VMath.GetRandomInt(1, 16);
		for(int i=0;i<6;i++){
			System.out.print(randB[i]);
		}
		System.out.println();
		System.out.println(randR);*/
		gs_tp = new VTimerProcessor();
		GameData.preinitGameData();
		InitFSM();
		InitLuaScript();
		InitUI();

		InitStage();
		
		GameData.initGameData();
		initLuaGetEntities();
		GlobalEvent.startInstance();
	}
	public void resetGameState(){
		// TOOD 重置游戏状态中的游戏相关参数
		fsm_GS.resetState();
		InitStage();
		runTime = 0;
		gs_tp = new VTimerProcessor();
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
	private void InitLuaScript(){
		Debug.DebugTestTimeStart();
		//Debug.DebugSimpleMessage("载入Lua脚本资源");
		if (lua_core == null) {
			lua_core = new LuaScript("data/Script/initStage.lua");
		}
		try {
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lua_core.runScriptFunction("initStage", null);
		Debug.DebugTestTimeEnd("载入Lua脚本资源", true);
	}
	private void initLuaGetEntities(){
		lua_core.runScriptFunction("initEntities", null);
	}
	private void InitUI(){
		Debug.DebugTestTimeStart();
		//Debug.DebugSimpleMessage("初始化UI");
		uiparent = new VUI(Global.windowX, Global.windowY, "uiparent");
		VUI gaui = GameArea.createUI("ui_gamearea"); //转珠区域UI
		gaui.setLoc(0, 0);
		gaui.setParent(uiparent);
		VArrowPanelUI uiarrowpanel = new VArrowPanelUI(510, 425,
				"ui_arrowpanel");
		uiarrowpanel.setLoc(0, 0);
		uiarrowpanel.setParent(gaui);
		VUI editbuttonui = new VMouseActionUI("button_200x100.png",
				"ui_editbutton");
		editbuttonui.setParent(uiparent);
		editbuttonui.setLoc(700, 100);
		VText editbtntext = new VText("编辑面板");
		editbtntext.setFont("font_big");
		editbtntext.setLayout(VText.Layout_CENTER);
		editbtntext.setLoc(100, 50);
		editbuttonui.addText(editbtntext);
		editbuttonui.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO 点击编辑面板按钮触发的行为
				GlobalEvent.getArrowUI().resetArrowList();
				VEngine.newgame.editframe.showframe(); //显示宝石面板编辑窗体
				VEngine.newgame.editframe.loadGemMatrix(GameData.ga.orbs);
			}
		});
		VUI resetbuttonui = new VMouseActionUI("button_200x100.png",
				"ui_resetbutton");
		resetbuttonui.setParent(uiparent);
		resetbuttonui.setLoc(700, 200);
		VText resetbuttontext = new VText("重置宝石分布");
		resetbuttontext.setFont("font_big");
		resetbuttontext.setLayout(VText.Layout_CENTER);
		resetbuttontext.setLoc(100, 50);
		resetbuttonui.addText(resetbuttontext);
		resetbuttonui.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO Auto-generated method stub
				GameData.pathAI.bindUI(uiparent.getUIByID("ui_arrowpanel")); //遗传算法AI接管箭头绘制面板显示
				GlobalEvent.getArrowUI().resetArrowList();
				GlobalEvent.resetOrb();
			}
		});
		VUI playbackbtn = new VMouseActionUI("button_200x100.png",
				"ui_playback");
		playbackbtn.setParent(uiparent);
		playbackbtn.setLoc(700, 300);
		VText playbackbtntext = new VText("转珠回放");
		playbackbtntext.setFont("font_big");
		playbackbtntext.setLayout(VText.Layout_CENTER);
		playbackbtntext.setLoc(100, 50);
		playbackbtn.addText(playbackbtntext);
		playbackbtn.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO Auto-generated method stub
				GlobalEvent.getArrowUI().resetArrowList();
				//GameData.pathAI.bindUI(uiparent.getUIByID("ui_arrowpanel"));
				GameData.currentAI.playback();
			}
		});
		VUI aipf = new VMouseActionUI("button_200x100.png", "ui_aipf");
		aipf.setParent(uiparent);
		aipf.setLoc(700, 400);
		VText aipftext = new VText("自动生成路径");
		aipftext.setFont("font_big");
		aipftext.setLayout(VText.Layout_CENTER);
		aipftext.setLoc(100, 50);
		aipf.addText(aipftext);
		aipf.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO 自动生成路径按钮
				GlobalEvent.getArrowUI().resetArrowList(); //清除箭头显示
				GameData.gPathAI.bindUI(uiparent.getUIByID("ui_arrowpanel")); //遗传算法AI接管箭头绘制面板显示
				GameData.gPathAI.getStartState(); //获取宝石面板
				GameData.gPathAI.generatePathGroup(); //生成路径组
			}
		});
		VUI ui_simmode_buttonlayer = new VUI(0, 0, "ui_simmode_buttonlayer"); //模拟模式按钮总面板
		ui_simmode_buttonlayer.setLoc(620, 0);
		ui_simmode_buttonlayer.setParent(uiparent);
		ui_simmode_buttonlayer.setVisible(false);
		//	-------------------右侧面板按钮-----------------------------
		VUI ui_simmode_button1 = new VMouseActionUI("button_100x50.png",
				"ui_simmode_button1");
		ui_simmode_button1.setParent(ui_simmode_buttonlayer);
		ui_simmode_button1.setLoc(50, 0);
		ui_simmode_button1.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO 测试用命令 
				VEnemy enemy = GameData.instance.getChosenEnemy();
				if(enemy!=null){
					enemy.getDamageEvent(400000, 1);
				}
			}
		});
		VText btn1_text = new VText("测试用按钮");
		btn1_text.setFont("font_default");
		btn1_text.setLayout(VText.Layout_CENTER);
		btn1_text.setLoc(50, 25);
		ui_simmode_button1.addText(btn1_text);
		VUI ui_simmode_button2 = new VMouseActionUI("button_100x50.png",
				"ui_simmode_button2");
		ui_simmode_button2.setParent(ui_simmode_buttonlayer);
		ui_simmode_button2.setLoc(50, 50);
		ui_simmode_button2.addAction(new VLuaAction() {
			@Override
			public void action() {
				// TODO 测试用命令 
				GlobalEvent.getLuaCore().runScriptFunction("initInstances", null);
				GlobalEvent.startInstance();
			}
		});
		VText btn2_text = new VText("初始化副本");
		btn2_text.setFont("font_default");
		btn2_text.setLayout(VText.Layout_CENTER);
		btn2_text.setLoc(50, 25);
		ui_simmode_button2.addText(btn2_text);
		//-----------------------------------------------------------------
		VOrbUI mouseUI = new VOrbUI("fire.png", "ui_mousehold"); //跟随鼠标移动的临时宝石UI
		mouseUI.setParent(uiparent);
		mouseUI.setVisible(false);
		VDynamicBarUI timerdbui = new VDynamicBarUI(
				"movetimerbar_background.png", "movetimerbar_cover.png",
				"movetimerbar_staticcover.png", "ui_movetimerdbui");
		mouseUI.addTimerDBUI(timerdbui);
		timerdbui.setLoc(0, 80);
		Debug.DebugTestTimeEnd("初始化UI", true);
	}
	public void Render() {
		// TODO 每帧进行的渲染
		//System.out.println(System.currentTimeMillis());
		ve.renderer.render();		//绘制场景
		Display.update();		//更新lwjgl的显示区域
		renderCount++;			//渲染计数+1
	}
	public void drawScene(){
		uiparent.drawUI();
	}
	public void renderGL() {
/*		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// R,G,B,A Set The Color To Blue One Time Only
		GL11.glColor3f(0.5f, 0.5f, 1.0f);
		// draw quad
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-x, -y, 0);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x - 50, y - 50);
		GL11.glVertex2f(x + 50, y - 50);
		GL11.glVertex2f(x + 50, y + 50);
		GL11.glVertex2f(x - 50, y + 50);
		GL11.glEnd();
		GL11.glPopMatrix();*/
	}
	public void updateState() {
		// TODO 由仿真器控制的游戏状态更新过程
		runTime++;
		if(runTime%100==0){	//每100次更新则计算FPS
			Display.setTitle("FPS:"+renderCount);
			renderCount = 0;
		}
		gs_tp.process(); //处理Timer
		processInput();
		switch (fsm_GS.GetCurrentState()) {
		case FSMconst.GS_GAME: {
			if (currentstage != null) {
				stagelist.get(currentstage).updateStage(); //处理当前stage更新
			}
			break;
		}
		case FSMconst.GS_PAUSE: {

			break;
		}
		default: {

		}
		}
		uiparent.updateUI();
		//--------GameState Update-------------------------------
		
		//-------------------------------------------------------
	}
	//------------Input------------------------------------------------------------------
	public void processInput(){
		// TODO 在游戏状态每次更新时调用以处理键盘或鼠标输入
		VStage currentstage = this.getCurrentStage();
		if(currentstage!=null){
			currentstage.processInput();	//调用当前关卡的输入响应
		}
	}
	//------------Stage------------------------------------------------------------------
	private void InitStage() {
		// TODO 初始化关卡信息
		Debug.DebugTestTimeStart();
		stagelist = new HashMap<String, VStage>();
		//VStage v = new VStage(this,"stage_01");	//新建标准stage并加入到游戏状态中的关卡列表
		VAssistStage va = new VAssistStage(this, "stage_assist");
		VSimulatorStage vs = new VSimulatorStage(this, "stage_simulator");
		/*VSTGStage stg = new VSTGStage(this,"stage_stg_01");
		stg.Init();
		stg.setAction(new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub
			}	
		});*/
		this.setCurrentStage("stage_simulator");
		//lua_core.runScriptFunction("initStage",null);	//加载Lua文件中定义的战斗单位更新行为
		Debug.DebugTestTimeEnd("初始化关卡", true);
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
		stagelist.get(currentstage).initStage();
	}
	public VStage getCurrentStage(){
		return stagelist.get(currentstage);
	}
	//-----------------------------------------------------------------------------------
}
