/**
 * 文件名称：VCombatUnit.java
 * 类路径：entities
 * 描述：TODO 战斗单位类
 * 作者：Demilichzz
 * 时间：2012-8-7下午09:27:37
 * 版本：Ver 1.0
 */
package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import combat.*;
import jgui.GamePanel;
import data.GameData;
import system.VEngine;
import timer.*;
import ui.VCombatAreaUI;
import view.*;
import factory.BuffFactory;
import global.Debug;
import global.Imageconst;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VCombatUnit extends VPointProxy implements VCombatObject,VValueInterface{
	//protected int CType = 0;	//碰撞类型索引
	//protected double collrad = 4;	//圆形碰撞的碰撞半径 
	protected VImageInterface image;	//战斗单位图像
	protected double life;		//生命值
	public boolean alive=true;	//存活性
	
	protected ArrayList<VBuff> bufflist = new ArrayList<VBuff>();	//buff列表
	protected ArrayList<VBuff> trash = new ArrayList<VBuff>();
	
	protected double lifetime=-1;			//生命周期，为负则永久
	protected int lifetime_f=-1000;		//剩余生存时间(ms)
	protected boolean ifcolld=true;	//是否进行碰撞检测
	protected VLuaAction updateAction;	//更新时触发的行为
	protected VLuaSPAction d_Action;	//死亡时触发的行为
	protected boolean drawrad=true;		//是否绘制碰撞范围
	public boolean invincible=false;	//粒子是否不因碰撞死亡
	
	protected int mob_type = 0;		//怪物类型，为0则在转阶段时清除
	public final static int DEATH_BYPC=0;	//死亡类型：被玩家杀死
	public final static int DEATH_LIFETIME=1;	//死亡类型:时间耗尽
	public final static int DEATH_ACTION=2;	//死亡类型：附带行为
	public final static int DEATH_PHASE=3;	//死亡类型：附带行为
	
	public VCombatUnit(){
		
	}
	public VCombatUnit(String istr,int life){
		image = Imageconst.GetImageByName(istr);
		this.life = life;
		addToList();
	}
	protected void addToList(){
		// TODO 将新建单位加入游戏数据中的单位列表
		if(GameData.unitlock=false){
			GameData.unitlist.add(this);
		}
		else{
			GameData.unitlist_temp.add(this);
		}
	}
	public void setImage(String str){
		image = Imageconst.GetImageByName(str);
	}
	public void setLife(int life){
		this.life = life;
	}
	public void setLifeTime(double lt){
		this.lifetime=lt;
		this.lifetime_f=(int)(lt*1000);
	}
	public VImageInterface getImageByLayer(int layer) {
		// TODO Auto-generated method stub
		return image;
	}
	public void setIfCollD(boolean b){
		// TODO 设置是否进行碰撞
		ifcolld=b;
	}
	public void setInvincible(boolean b){
		// TODO 设置是否无敌
		invincible=b;
	}
	public boolean cDetection(VPointProxy target) {
		// TODO 碰撞检测
		if(ifcolld){
			return super.cDetection(target);
		}
		else{
			return false;
		}
	}
	public int getCType() {
		// TODO Auto-generated method stub
		return CType;
	}
	public void addAction(VLuaAction a){
		// TODO 设置单位的更新行为
		updateAction = a;
	}
	public void addDeathAction(VLuaSPAction death_a){
		this.d_Action = death_a;
	}
	public void appendBuff(String ID){
		// TODO 附加Buff
		boolean stack=false;
		for(VBuff buff:bufflist){
			if(buff.ID.equals(ID)){
				buff.addStack();
				stack=true;
			}
		}
		if(!stack){
			bufflist.add(BuffFactory.creator(ID));
		}
	}
	public void removeBuff(String ID){
		// TODO 移除Buff
		for(VBuff buff:bufflist){
			if(buff.ID.equals(ID)){
				buff.alive=false;
			}
		}
	}
	protected void LifeMinus(){
		if(lifetime_f>0){
			lifetime_f=lifetime_f - VEngine.gs.getMSecond();
			if(lifetime_f<=0){
				lifetime_f=0;
			}
		}
	}

	public void update() {
		if (alive) {
			if (life <= 0) {
				die(DEATH_BYPC);
			} else if (lifetime_f == 0) {
				die(DEATH_LIFETIME);
			}
			if (updateAction != null) {
				updateAction.action();
			}
			LifeMinus();
			for (VBuff buff : bufflist) {
				buff.lifeMinus(); // buff计时+1
				if (!buff.alive) {
					trash.add(buff); // buff列表维护,删除过期buff
				}
			}
			if (trash.size() > 0) {
				bufflist.removeAll(trash);
				trash = new ArrayList<VBuff>();
			}
		}
	}
	public void die(int index) {
		// TODO 战斗单位死亡时行为
		alive=false;
		switch(index){
		case DEATH_BYPC:{	//被玩家杀死
			break;
		}
		case DEATH_LIFETIME:{	//存活时间结束而死
			if(d_Action!=null){
				d_Action.action(this);
			}
			break;
		}
		case DEATH_ACTION:{	//关联附带行为的死亡
			break;
		}
		case DEATH_PHASE:{	//转阶段清除
			break;
		}
		}
	}

	public double getCollRad() {
		// TODO 获取圆形碰撞半径
		return super.getCollRad();
	}
	
	protected int hitcount=0;
	public void hit(VSprite sp) {
		// TODO 战斗单位被粒子击中的行为
		if(!invincible){
			double dr=1;
			for(VBuff b:bufflist){
				dr=dr*(1+b.getDR()/100);		//计算伤害修正
			}
			life=life-sp.damage*dr;
			if(GameData.pc.talent==0){		//如果玩家是奥术天赋
				hitcount=hitcount+1;
				if(hitcount>=20){
					hitcount=0;
					this.appendBuff("arcaneblast");
				}
			}
			if(life<=0){
				life=0;
			}
		}
		sp.hit(this);
	}
	public void drawMe(Graphics2D g2d, GamePanel p,VCombatAreaUI ui){
		int cx = (int) (GetX()+ui.getRealX());
		int cy = (int) (GetY()+ui.getRealY());
		int x = cx-getImageByLayer(0).getWidth()/2;
		int y = cy-getImageByLayer(0).getHeight()/2;
		g2d.drawImage(getImageByLayer(0).getImage(),x,y,p);
		g2d.setColor(Color.BLUE);
		if(drawrad){	//绘制碰撞范围
			g2d.drawOval((int)(cx-collrad),(int)(cy-collrad), (int)collrad*2, (int)collrad*2);
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case 0:{
			return (int) life;
		}
		case 1:{
			return mob_type;		//小怪则返回0
		}
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#setValue(int, int)
	 */
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		switch(index){
		case 1:{
			mob_type=value;
			break;
		}
		}
	}
}
