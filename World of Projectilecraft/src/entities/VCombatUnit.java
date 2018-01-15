/**
 * �ļ����ƣ�VCombatUnit.java
 * ��·����entities
 * ������TODO ս����λ��
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-7����09:27:37
 * �汾��Ver 1.0
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
	//protected int CType = 0;	//��ײ��������
	//protected double collrad = 4;	//Բ����ײ����ײ�뾶 
	protected VImageInterface image;	//ս����λͼ��
	protected double life;		//����ֵ
	public boolean alive=true;	//�����
	
	protected ArrayList<VBuff> bufflist = new ArrayList<VBuff>();	//buff�б�
	protected ArrayList<VBuff> trash = new ArrayList<VBuff>();
	
	protected double lifetime=-1;			//�������ڣ�Ϊ��������
	protected int lifetime_f=-1000;		//ʣ������ʱ��(ms)
	protected boolean ifcolld=true;	//�Ƿ������ײ���
	protected VLuaAction updateAction;	//����ʱ��������Ϊ
	protected VLuaSPAction d_Action;	//����ʱ��������Ϊ
	protected boolean drawrad=true;		//�Ƿ������ײ��Χ
	public boolean invincible=false;	//�����Ƿ�����ײ����
	
	protected int mob_type = 0;		//�������ͣ�Ϊ0����ת�׶�ʱ���
	public final static int DEATH_BYPC=0;	//�������ͣ������ɱ��
	public final static int DEATH_LIFETIME=1;	//��������:ʱ��ľ�
	public final static int DEATH_ACTION=2;	//�������ͣ�������Ϊ
	public final static int DEATH_PHASE=3;	//�������ͣ�������Ϊ
	
	public VCombatUnit(){
		
	}
	public VCombatUnit(String istr,int life){
		image = Imageconst.GetImageByName(istr);
		this.life = life;
		addToList();
	}
	protected void addToList(){
		// TODO ���½���λ������Ϸ�����еĵ�λ�б�
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
		// TODO �����Ƿ������ײ
		ifcolld=b;
	}
	public void setInvincible(boolean b){
		// TODO �����Ƿ��޵�
		invincible=b;
	}
	public boolean cDetection(VPointProxy target) {
		// TODO ��ײ���
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
		// TODO ���õ�λ�ĸ�����Ϊ
		updateAction = a;
	}
	public void addDeathAction(VLuaSPAction death_a){
		this.d_Action = death_a;
	}
	public void appendBuff(String ID){
		// TODO ����Buff
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
		// TODO �Ƴ�Buff
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
				buff.lifeMinus(); // buff��ʱ+1
				if (!buff.alive) {
					trash.add(buff); // buff�б�ά��,ɾ������buff
				}
			}
			if (trash.size() > 0) {
				bufflist.removeAll(trash);
				trash = new ArrayList<VBuff>();
			}
		}
	}
	public void die(int index) {
		// TODO ս����λ����ʱ��Ϊ
		alive=false;
		switch(index){
		case DEATH_BYPC:{	//�����ɱ��
			break;
		}
		case DEATH_LIFETIME:{	//���ʱ���������
			if(d_Action!=null){
				d_Action.action(this);
			}
			break;
		}
		case DEATH_ACTION:{	//����������Ϊ������
			break;
		}
		case DEATH_PHASE:{	//ת�׶����
			break;
		}
		}
	}

	public double getCollRad() {
		// TODO ��ȡԲ����ײ�뾶
		return super.getCollRad();
	}
	
	protected int hitcount=0;
	public void hit(VSprite sp) {
		// TODO ս����λ�����ӻ��е���Ϊ
		if(!invincible){
			double dr=1;
			for(VBuff b:bufflist){
				dr=dr*(1+b.getDR()/100);		//�����˺�����
			}
			life=life-sp.damage*dr;
			if(GameData.pc.talent==0){		//�������ǰ����츳
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
		if(drawrad){	//������ײ��Χ
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
			return mob_type;		//С���򷵻�0
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
