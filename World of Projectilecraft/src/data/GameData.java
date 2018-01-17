/**
 * 文件名称：GameData.java
 * 类路径：data
 * 描述：TODO
 * 作者：Demilichzz
 * 时间：2012-8-6下午11:19:36
 * 版本：Ver 1.0
 */
package data;

import java.util.*;

import system.VEngine;
import timer.VLuaSPAction;
import ui.*;

import entities.*;
import event.GlobalEvent;
import factory.*;
import global.*;

/**
 * @author Demilichzz
 *
 */
public class GameData {
	public static VPlayer pc;
	public static int gamemode=0;		//游戏模式
	public static VReplay rep=new VReplay();			//录像
	public static VCUBoss boss;
	public static int stage=0;			//关卡
	public static boolean spritelock=false;		//粒子列表是否为锁定状态
	public static ArrayList<VSprite> spriteList_temp = new ArrayList<VSprite>();	//锁定时使用的临时列表
	public static ArrayList<VSprite> spriteList = new ArrayList<VSprite>();
	public static ArrayList<VSprite> drawList = new ArrayList<VSprite>();	//玩家粒子绘制表
	public static boolean unitlock=false;		//单位列表是否为锁定状态
	public static ArrayList<VCombatUnit>unitlist_temp = new ArrayList<VCombatUnit>();
	public static ArrayList<VCombatUnit>unitlist = new ArrayList<VCombatUnit>();	//关卡战斗单位列表
	public static ArrayList<VCombatUnit>unitdrawlist = new ArrayList<VCombatUnit>();
	public static boolean replaymode=false;		//游戏是否在播放录像
	
	public static void InitData() {
		// TODO 初始化游戏数据
		pc = new VPlayer();
		BuffFactory.initFactory();
		spritelock=false;
		unitlock=false;
		boss=null;
		spriteList = new ArrayList<VSprite>();
		spriteList_temp = new ArrayList<VSprite>();
		drawList = new ArrayList<VSprite>();
		unitlist = new ArrayList<VCombatUnit>();
		unitdrawlist = new ArrayList<VCombatUnit>();
		unitlist_temp = new ArrayList<VCombatUnit>();
		replaymode=false;
	}
	public static void record(int[] keystate){
		// TODO 向录像类中记录玩家操作信息
		rep.addKeyState(keystate);
	}
	public static void setSpListAction(VLuaSPAction spa){
		// TODO 对粒子列表中全部粒子实施指定的行为
		for(VSprite sp:spriteList){
			spa.action(sp);
		}
	}
	public static VPlayer getPlayer(){
		// TODO 获取当前玩家对象
		return pc;
	}
	public static VCombatUnit getClosetUnit(){
		// TODO 获取离玩家最近非无敌单位
		VCombatUnit u=null;
		double dist=10000;
		for(VCombatUnit unit:unitlist){
			if(VMath.GetSimpleMaxDistance(pc, unit)<dist&&!unit.invincible){
				u=unit;
				dist=VMath.GetSimpleMaxDistance(pc, unit);
			}
		}
		return u;
	}
	public static void setBoss(VCUBoss unit){
		boss=unit;
		VDynamicBarUI bosslife = (VDynamicBarUI) VEngine.gs.uiparent.getUIByID("ui_bosslife");
		bosslife.setMaxValue(boss.getValue(0));	//设置BOSS血条UI最大值为HP最大值
		bosslife.bindValue(boss, 0);			//绑定BOSS血条UI于当前BOSS
		Soundconst.bgm.soundPlay(boss.getBGM());
	}
	public static void setStage(int i){
		// TODO 设置关卡值，载入关卡背景和BGM
		stage=i;
	}
	public static void updateSpriteList(){
		//	---------锁定粒子列表-----
		spritelock=true;
		//	---------更新粒子状态-----
		for(VSprite sp:spriteList){
			if(sp!=null){
				//if(sp.born&&sp.alive){	//粒子已出生并存活
				sp.spriteUpdate();	//才进行状态更新
				//}
			}
		}
		//	---------销毁死亡粒子-----
		ArrayList<VSprite> delList = new ArrayList<VSprite>();	//删除列表
		for(VSprite sp:spriteList){
			if(sp!=null){
				if(!sp.alive&&sp.born){	//Alive为false且born为true时,才能确定粒子死亡
					delList.add(sp);	//遍历搜索死亡粒子，并添加到删除列表
				}
				else{
				}
			}
		}
		spriteList.removeAll(delList);	//删除死亡粒子
		drawList = new ArrayList<VSprite>();	//新建绘制列表并复制粒子列表的全部单位
		drawList.addAll(spriteList);		//在图像更新时，绘制绘制列表中的粒子，以防止在图像更新时游戏状态同时更新造成并发处理异常
		spritelock=false;	//处理完毕解锁粒子列表
		if(spriteList_temp.size()>0){		//将临时列表加入粒子列表
			spriteList.addAll(spriteList_temp);
			spriteList_temp=new ArrayList<VSprite>();
		}
	}

	public static void updateUnitList() {
		// TODO 更新战斗单位状态，原理同更新粒子状态
		unitlock=true;
		pc.update();		//更新玩家
		boss.update();		//更新当前Boss
		ArrayList<VCombatUnit> delList = new ArrayList<VCombatUnit>();
		for(VCombatUnit unit:unitlist){
			if(unit!=null){
				if(unit.alive){
					unit.update();
				}
				if(!unit.alive){
					delList.add(unit);
				}
			}
		}
		unitlist.removeAll(delList);
		unitdrawlist=new ArrayList<VCombatUnit>();
		unitdrawlist.addAll(unitlist);
		unitlock=false;
		if(unitlist_temp.size()>0){
			unitlist.addAll(unitlist_temp);
			unitlist_temp=new ArrayList<VCombatUnit>();
		}
	}
	public static void collisionDetection(){
		// TODO 进行碰撞检测
		ArrayList<VSprite> enemylist = new ArrayList<VSprite>(); //敌对粒子列表
		ArrayList<VSprite> playerlist = new ArrayList<VSprite>(); //玩家粒子列表
		for (VSprite sp : spriteList) {
			if (sp.getOwner() instanceof VPlayer) {
				playerlist.add(sp);
			} else {
				enemylist.add(sp);
			}
		}
		for (VSprite sp : enemylist) { //遍历敌对粒子判断是否与自机相撞
			if (pc.cDetection(sp)) {
				//Debug.DebugSimpleMessage("X: "+sp.GetX()+" Y: "+sp.GetY());
				pc.hit(sp);
			}
		}
		for (VSprite sp : playerlist) { //遍历自机粒子
			if(GameData.boss.alive&&GameData.boss.cDetection(sp)&&!sp.getHited(GameData.boss)){
				GameData.boss.hit(sp);
			}
			for (VCombatUnit unit : unitlist) {
				if (unit.cDetection(sp) && !sp.getHited(unit)) {
					unit.hit(sp);
				}
			}
		}
	}

	public static void setTalent(int i) {
		// TODO 设置玩家天赋
		pc.setTalent(i);
		VUI imenu=VEngine.gs.uiparent.getUIByID("ui_instance_menu");
		imenu.moveLoc(0, -150);
		imenu.setEnable(false);
		imenu=VEngine.gs.uiparent.getUIByID("ui_talent_menu");
		imenu.moveLoc(0, -150);
		imenu.setEnable(false);
		VScrollMenuUI m = (VScrollMenuUI) VEngine.gs.uiparent.getUIByID("ui_mode_menu");
		m.setVisible(true);
		m.moveCursor(0);
		//GlobalEvent.startCombat();		//选择完毕后开始战斗
	}
	public static void setMode(int i){
		// TODO 设置游戏模式
		gamemode=i;
	}
	/**
	 * @return
	 */
	public static int getStage() {
		// TODO 获取关卡
		return stage;
	}
}
