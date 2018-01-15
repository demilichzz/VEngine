/**
 * �ļ����ƣ�GlobalEvent.java
 * ��·����event
 * ������TODO ȫ���¼���̬�࣬�ڴ˾�̬��Ԥ������Ҫ��ȫ�ַ�Χ�ڵ��õ��¼�����Ϊ����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����06:30:23
 * �汾��Ver 1.0
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
		// TODO ѡ���츳ʱȡ�����˵��ص�ѡ�񸱱�
		VUI imenu=gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, 150);		//����UI����
		imenu.setEnable(true);		//�����UI
		gs.uiparent.getUIByID("ui_talent_menu").setVisible(false);
		gs.uiparent.getUIByID("ui_menu_describe").setVisible(false);
	}
	public static void menuBackToTalent() {
		// TODO �˵����ص�ѡ���츳
		VUI imenu=gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, 150);		//����UI����
		VScrollMenuUI m=(VScrollMenuUI) gs.uiparent.getUIByID("ui_talent_menu");
		m.moveLoc(0, 150);
		m.setEnable(true);
		m.moveCursor(0);
		gs.uiparent.getUIByID("ui_mode_menu").setVisible(false);
	}
	public static void gameOver(){
		// TODO �������ʱ���õĺ���
		GameData.rep.setEndFrame(VEngine.gs.getCurrentStage().updatecount);
		VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_DIE);	//��ؿ�״̬����������״̬
	}
	public static void loadInstance(int index){
		// TODO �������������Ӧ�����ĵ�Ļ�ű�
		switch(index){
		case 1:{		//����
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
		// TODO ��ʼ��Ϸ�¼�
		VUIManager.loadUIState(VUIManager.UI_GAMEMODE);
	}
	public static void startCombat(){
		// TODO ��ʼ��Ϸ�¼�
		VEngine.gs.uiparent.getUIByID("ui_mode_menu").setEnable(false);
		VEngine.gs.uiparent.getUIByID("ui_replaylist").setEnable(false);
		setBackground("UI_400x600_gamearea_1.png");		//���ó�ʼ����
		VGEffectFade fade = new VGEffectFade(0,0,800,600,50);	//��������
		VTimer tm = new VTimer(600,600,false,new VLuaAction(){
			public void action() {
				// TODO Auto-generated method stub	
				GameData.rep.InitReplay();		//��ʼ��¼���ļ�
				if(GameData.replaymode){
					setRandomSeed(GameData.rep.randomseed);		//����ǲ���¼�������������Ϊ¼���м��ص�����
				}
				else{											//����ȡ��ǰ������Ϊ���Ӳ�����Ϸ�����д�������ֵ
					long seed = System.currentTimeMillis();
					setRandomSeed(seed);
					GameData.rep.randomseed=seed;
				}
				gs.lua_core.runScriptFunction("initStage", null);	//���븱���ű��ж������Ϊ������Boss
				try {
					Thread.sleep(200);		//����һ��ʱ������ű��ܹ�����boss����
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Soundconst.bgm.soundPlay(GameData.boss.getBGM());
				VUIManager.loadUIState(VUIManager.UI_GAME0+GameData.gamemode);	//������Ϸģʽ����UI
				gs.setCurrentStage("stage_stg_01");		//����STG�ؿ�
			}	//Ϊȷ��ת����Чȫ��ʱ�ٽ�������ļ�ʱ��
		});
		tm.timerStart(0);
	}
	public static void phaseTransition(){
		// TODO �׶�ת�����ڽ׶�ת��ʱ������������Ӻͼ�ʱ��
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
		gs.getCurrentStage().tp.reset();	//��չؿ���Timer������������
	}
	public static void clearUnit(){
		// TODO ���ȫ��ս����λ
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
		// TODO ���ñ���
		VUI bg = VEngine.gs.uiparent.getUIByID("ui_gamearea");
		bg.setImage(str);
	}
	public static void activateNextBoss() {
		// TODO ������һ��Boss
		clearUnit();
		phaseTransition();		//���
		VGEffectFade fade = new VGEffectFade(100,0,400,600,50);
		if(GameData.getStage()<4){		//��ǰ�ؿ����������չؿ�
			VTimer tm = new VTimer(600,600,false,new VLuaAction(){	//ת��֮���ȼ��ر���ͼ��
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
					VEngine.gs.lua_core.runScriptFunction("activateNextBoss", null);//������һ��Boss
				}	//Ϊȷ��ת����Чȫ��ʱ�ٽ�������ļ�ʱ��
			});
			tma.timerStart(0);
		}	
		else{		//���չؿ���ֻ�ȴ�600ms
			VTimer tma = new VTimer(600,600,false,new VLuaAction(){
				public void action() {
					// TODO Auto-generated method stub
					VEngine.gs.lua_core.runScriptFunction("activateNextBoss", null);//������һ��Boss
				}	//Ϊȷ��ת����Чȫ��ʱ�ٽ�������ļ�ʱ��
			});
			tma.timerStart(0);
		}
	}
	public static void stageEnd(){
		// TODO ͨ�ؽ����ؿ�����Lua�е���
		VEngine.gs.getCurrentStage().fsmStateTransition(FSMconst.INPUT_DIE);
	}

	public static void exitToMain() {
		// TODO ������Ϸ���˻����˵�
		Soundconst.speech.soundStop();
		SpriteFactory.resetFactoryState();
		GameData.InitData();		//���³�ʼ����Ϸ����
		VEngine.gs.resetGameState();		//������Ϸ״̬
		VDynamicBarUI pclb = (VDynamicBarUI) VEngine.gs.uiparent.getUIByID("ui_pclife");	//���°�UI
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
		// TODO �����������
		VMath.setRandomSeed(rseed);
		VInt iseed = new VInt(Math.abs((int) rseed));
		VEngine.gs.lua_core.runScriptFunction("setRandomSeed", iseed);
	}
	public static void loadReplay(String path) {
		// TODO Auto-generated method stub
		GameData.rep.loadFromFile(path);		//��rep�ļ�����
		GameData.replaymode=true;
		GameData.rep.setGameState();		//������Ϸ����
		VUIManager.loadPauseMenu(VUIManager.PMENU_REPLAY);		//����¼��ģʽ��ͣ�˵�UI
		GlobalEvent.startCombat();
	}
	public static void saveReplay(){
		GameData.rep.saveToFile();
	}
	public static void loadReplayFile() {
		// TODO ����¼���ļ��б�
		VListUI replist = (VListUI) VEngine.gs.uiparent.getUIByID("ui_replaylist");
		String reppath = "data/Replay";
		File file = new File(reppath);
		ArrayList<String> filenames = new ArrayList<String>();
		String suffix = "rep";				//��׺
		filenames = listFile(filenames,file,suffix,false);		//��ȡ¼��Ŀ¼�µ�����¼���ļ����б�
		replist.setStringList(filenames);
	}
	public static void DBMAddSkill(String name,double time){
		VDBMPanelUI dbm = (VDBMPanelUI) VEngine.gs.uiparent.getUIByID("ui_dbm");
		dbm.addSkill(name, time);
	}
	private static ArrayList<String> listFile(ArrayList<String> lstFileNames, File f,
			String suffix, boolean isdepth) {
		// ����Ŀ¼, ���õݹ�ķ���������Ŀ¼
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
				int begIndex = filePath.lastIndexOf("."); // ���һ��.(����׺��ǰ���.)������
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
