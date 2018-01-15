/**
 * �ļ����ƣ�GameData.java
 * ��·����data
 * ������TODO
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����11:19:36
 * �汾��Ver 1.0
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
	public static int gamemode=0;		//��Ϸģʽ
	public static VReplay rep=new VReplay();			//¼��
	public static VCUBoss boss;
	public static int stage=0;			//�ؿ�
	public static boolean spritelock=false;		//�����б��Ƿ�Ϊ����״̬
	public static ArrayList<VSprite> spriteList_temp = new ArrayList<VSprite>();	//����ʱʹ�õ���ʱ�б�
	public static ArrayList<VSprite> spriteList = new ArrayList<VSprite>();
	public static ArrayList<VSprite> drawList = new ArrayList<VSprite>();	//������ӻ��Ʊ�
	public static boolean unitlock=false;		//��λ�б��Ƿ�Ϊ����״̬
	public static ArrayList<VCombatUnit>unitlist_temp = new ArrayList<VCombatUnit>();
	public static ArrayList<VCombatUnit>unitlist = new ArrayList<VCombatUnit>();	//�ؿ�ս����λ�б�
	public static ArrayList<VCombatUnit>unitdrawlist = new ArrayList<VCombatUnit>();
	public static boolean replaymode=false;		//��Ϸ�Ƿ��ڲ���¼��
	
	public static void InitData() {
		// TODO ��ʼ����Ϸ����
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
		// TODO ��¼�����м�¼��Ҳ�����Ϣ
		rep.addKeyState(keystate);
	}
	public static void setSpListAction(VLuaSPAction spa){
		// TODO �������б���ȫ������ʵʩָ������Ϊ
		for(VSprite sp:spriteList){
			spa.action(sp);
		}
	}
	public static VPlayer getPlayer(){
		// TODO ��ȡ��ǰ��Ҷ���
		return pc;
	}
	public static VCombatUnit getClosetUnit(){
		// TODO ��ȡ�����������޵е�λ
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
		bosslife.setMaxValue(boss.getValue(0));	//����BOSSѪ��UI���ֵΪHP���ֵ
		bosslife.bindValue(boss, 0);			//��BOSSѪ��UI�ڵ�ǰBOSS
		Soundconst.bgm.soundPlay(boss.getBGM());
	}
	public static void setStage(int i){
		// TODO ���ùؿ�ֵ������ؿ�������BGM
		stage=i;
	}
	public static void updateSpriteList(){
		//	---------���������б�-----
		spritelock=true;
		//	---------��������״̬-----
		for(VSprite sp:spriteList){
			if(sp!=null){
				//if(sp.born&&sp.alive){	//�����ѳ��������
				sp.spriteUpdate();	//�Ž���״̬����
				//}
			}
		}
		//	---------������������-----
		ArrayList<VSprite> delList = new ArrayList<VSprite>();	//ɾ���б�
		for(VSprite sp:spriteList){
			if(sp!=null){
				if(!sp.alive&&sp.born){	//AliveΪfalse��bornΪtrueʱ,����ȷ����������
					delList.add(sp);	//���������������ӣ�����ӵ�ɾ���б�
				}
				else{
				}
			}
		}
		spriteList.removeAll(delList);	//ɾ����������
		drawList = new ArrayList<VSprite>();	//�½������б����������б��ȫ����λ
		drawList.addAll(spriteList);		//��ͼ�����ʱ�����ƻ����б��е����ӣ��Է�ֹ��ͼ�����ʱ��Ϸ״̬ͬʱ������ɲ��������쳣
		spritelock=false;	//������Ͻ��������б�
		if(spriteList_temp.size()>0){		//����ʱ�б���������б�
			spriteList.addAll(spriteList_temp);
			spriteList_temp=new ArrayList<VSprite>();
		}
	}

	public static void updateUnitList() {
		// TODO ����ս����λ״̬��ԭ��ͬ��������״̬
		unitlock=true;
		pc.update();		//�������
		boss.update();		//���µ�ǰBoss
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
		// TODO ������ײ���
		ArrayList<VSprite> enemylist = new ArrayList<VSprite>(); //�ж������б�
		ArrayList<VSprite> playerlist = new ArrayList<VSprite>(); //��������б�
		for (VSprite sp : spriteList) {
			if (sp.getOwner() instanceof VPlayer) {
				playerlist.add(sp);
			} else {
				enemylist.add(sp);
			}
		}
		for (VSprite sp : enemylist) { //�����ж������ж��Ƿ����Ի���ײ
			if (pc.cDetection(sp)) {
				//Debug.DebugSimpleMessage("X: "+sp.GetX()+" Y: "+sp.GetY());
				pc.hit(sp);
			}
		}
		for (VSprite sp : playerlist) { //�����Ի�����
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
		// TODO ��������츳
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
		//GlobalEvent.startCombat();		//ѡ����Ϻ�ʼս��
	}
	public static void setMode(int i){
		// TODO ������Ϸģʽ
		gamemode=i;
	}
	/**
	 * @return
	 */
	public static int getStage() {
		// TODO ��ȡ�ؿ�
		return stage;
	}
}
