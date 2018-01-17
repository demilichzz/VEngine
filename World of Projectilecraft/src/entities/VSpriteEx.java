/**
 * @author Demilichzz
 *	��չ�����࣬�̳л����������ͬʱ����ԭGravity,Corpse,Reflect���Ӻ�ʱ������Ϊ������
 * 2012-12-5
 */
package entities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import jgui.GamePanel;

import timer.*;
import ui.VCombatAreaUI;
import view.VImageInterface;
import global.Debug;
import global.VMath;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2012-12-5
 */
public class VSpriteEx extends VSprite{
	protected VLuaSPAction deathaction = null;
	protected VLuaSPAction reflectaction = null;
	protected VLuaSPAction updateaction = null;
	protected int deathactionstate=1;		//ִ��������Ϊ��������ֵΪ����������Ӧ��booleanֵ���ϣ�
											//Ĭ��״̬Ϊ����ʱ�����ʱִ��
	protected double forceangle=0;	//�������Ƕ�
	protected double forceaccel=0;	//���������ٶ�
	protected int forceendframe=0;	//������ֹͣ֡��Ĭ��Ϊ0�������������
	protected int refmode = 0;	//�����Ʒ��ߵ���λ��ʾ�������ұ��Ƿ񷴵�,ȡֵ0-15��Ĭ�ϲ�����
								//ȡֵ11ʱ�²�����
	protected int reftime = 0;	//����������-1Ϊ���ޣ�Ĭ��Ϊ0������
	protected int refx,refy,refw,refh;	//������Ե
	protected boolean invisiblemode=false;	//����ģʽ��Ϊ����ģʽʱ��͸����ʾ�����ж�
	protected double sprad;		//��¼���Ӱ뾶
	protected ArrayList<int[]> invisibletimeline = new ArrayList<int[]>();
	protected ArrayList<int[]> actiontimeline = new ArrayList<int[]>();	//ʱ���ߣ�ÿ��ʱ��ڵ��¼��Ϊִ��֡�ĳ������࣬�����ޣ����ڿ��Ƽ��
	
	public VSpriteEx(){
		super();
	}
	public VSpriteEx(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
		sprad=c_rad;
	}	
	public void addDeathAction(VLuaSPAction action){
		deathaction = action;
	}
	public void addReflectAction(VLuaSPAction action){
		reflectaction = action;
	}
	public void addUpdateAction(VLuaSPAction action){
		updateaction = action;
	}
	public void addTimeNode(int divide,int mod,int min,int max){
		// TODO ��ʱ�������ִ�и�����Ϊ��ʱ��ڵ�
		int[] node = new int[]{divide,mod,min,max};
		actiontimeline.add(node);
	}
	public void addInvTimeNode(int divide,int mod,int min,int max){
		// TODO �������ʱ����
		int[] node = new int[]{divide,mod,min,max};
		invisibletimeline.add(node);
	}
	public void setRad(double rad){
		// TODO ���Ǹ������ð뾶�����õ�ͬʱ����һ��������ĵı����м�¼���Ա��л�͸��ģʽʹ��
		this.collrad=rad;
		this.sprad=rad;
	}
	public void clearTimeLine(){
		// TODO ���ʱ����
	}
	public void setDeathActionState(int i){
		// TODO ����������Ϊ����
		deathactionstate = i;
	}
	public void setReflectArea(int x,int y,int w,int h){
		refx = x;
		refy = y;
		refw = w;
		refh = h;
	}
	public void setReflectMode(int mode){
		refmode = mode;
	}
	public void setReflectTime(int rt) {
		// TODO Auto-generated method stub
		this.reftime = rt;
	}
	public void setForce(double angle,double accel,int f){
		// TODO ��������������
		forceangle=angle;
		forceaccel=accel;
		forceendframe=f;
	}
	//---------------------------------------------------------------------------------------------------
	//------------���������ӵĸ��»�������Ⱦ�ȹ��ܺ���-----------------------------------------------------
	
	public void die() {
		// TODO ��������
		born=true;
		alive=false;
	}
	public void die(int index){
		if(VMath.getBinaryValue(deathactionstate, index)==1){	//�����Ϊ��������
			if(deathaction!=null){
				deathaction.action(this);		//ִ��������Ϊ
			}
		}
		die();
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO ��������ͼ��
		if(invisiblemode){
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2);	//͸���� 
			g.setComposite(ac);
		}
		super.drawImage(g, p, i, plat);
		g.setPaintMode();		//����Graphicģʽ
	}
	public void spriteUpdate() {
		// TODO ��������״̬
		if (alive && (lifetime_f > 0||lifetime_f==-1000)) {	//�����ʣ��ʱ��>0
			forcemove();	//������ǰʩ��������
			reflect();		//������ǰ���㷴��
			timelineprocess();	//����ʱ�����¼�
			invisibleprocess();	//��������ʱ����
			move();			//�ƶ�		
			LifeMinus();	//ʣ��ʱ��-ÿ֡ʱ��
			if(restricted){	//������ӷ�Χ���ޣ����鷶Χ������ָ����Χ������
				if(!getBound()){
					die(DEATHSTATE_OUTBOUND);
				}
			}
			if(debugmark){	//����������ǵ���״̬
				Debug.DebugSimpleMessage("X:"+this.GetX()+",Y:"+this.GetY());	//ÿ֡���������
			}
		}
		else {
			die(DEATHSTATE_TIMEEND);
		}
		if(rotatemode==2||rotatemode==3){
			double rotate = VMath.StandardizationAngle(updatecount*rotatespeed/100+Math.PI/2);	//�ڸ��¹����м�����תֵ
			this.setRotate(rotate);
		}
		updatecount++;
	}

	private void invisibleprocess() {
		// TODO ��������ʱ����
		boolean mark=false;
		if(invisibletimeline.size()>0){
			for(int[] node:invisibletimeline){	//����ʱ���߽ڵ��ȡ��ǰʱ��������״̬
				if(checknode(node)){
					mark=true;
				}
			}
		}
		if(mark){
			invisiblemode=true;
			collrad=0;			//��ײΪ0��������ײ
		}
		else{
			invisiblemode=false;
			collrad=sprad;		//�ָ���ײ
		}
	}
	private void timelineprocess() {
		// TODO ����ʱ������Ϊ
		if(actiontimeline.size()>0&&updateaction!=null){		//ʱ���߷ǿ�����Ϊ�ǿ�
			for(int[] node:actiontimeline){
				if(checknode(node)){	//����ڵ�Ϸ��ҵ�ǰ����֡������ڵ�
					updateaction.action(this);
				}
			}
		}
	}
	private boolean checknode(int[] node) {
		// TODO ���ڵ��Ƿ�Ϸ�
		if(node.length==4){
			if(node[0]>0&&node[1]<node[0]&&node[2]<node[3]){	//���>0������<���
				if(updatecount>=node[2]&&updatecount<=node[3]){	//���´������㷶Χ
					if(updatecount%node[0]==node[1]){	//���´����Լ��������������
						return true;		//������ʱ��ڵ���������ʱ���ִ����Ϊ
					}
				}
			}
		}
		return false;
	}
	private void forcemove() {
		// TODO ���������������������нǶȺ��ٶ�
		//setAngle(getAngle());		//��׼���Ƕ�
		if(updatecount<forceendframe||forceendframe==-1){
			if (forceaccel != 0) {
				double vx = Math.cos(angle) * speed + Math.cos(forceangle)
						* forceaccel;
				double vy = Math.sin(angle) * speed + Math.sin(forceangle)
						* forceaccel; // ���� xy����
				double newangle = 0;
				if (vx != 0) {
					newangle = Math.atan2(vy,vx);	//atan2���ػ���ֵ
				}
				else {
					if (vy > 0) {
						newangle = Math.PI * 0.5;
					} else {
						newangle = Math.PI * 1.5;
					}
				}
				double newspeed = Math.sqrt(vx * vx + vy * vy);
				this.setAngle(newangle);
				this.setSpeed(newspeed);
			}
		}
	}
	protected void reflect(){
		// TODO ���㷴���Ƕ�
		if(reftime!=0&&(x<5||y<5||x>395||y>595)){
			VPointInterface vtar=VMath.PolarMove(this,angle, speed/100);	//�����´��ƶ���Ŀ��λ��
			double xt=vtar.GetX();
			double yt=vtar.GetY();
			int ref_up = VMath.getBinaryValue(refmode, 3);
			int ref_down = VMath.getBinaryValue(refmode, 2);
			int ref_left = VMath.getBinaryValue(refmode, 1);
			int ref_right = VMath.getBinaryValue(refmode, 0);
			if(xt<1&&ref_left!=0){
				this.reflect(Math.PI-angle);
			}
			else if(xt>399&&ref_right!=0){
				this.reflect(Math.PI-angle);
			}
			if(yt<1&&ref_up!=0){
				this.reflect(-angle);
			}
			else if(yt>599&&ref_down!=0){
				this.reflect(-angle);
			}
		}
	}
	protected void reflect(double angle){
		// TODO �������ýǶȣ�ͬʱ����
		if(reftime!=0){
			if(reflectaction!=null){
				reflectaction.action(this);
			}
			this.setAngle(angle);
		}
		if(reftime>0){
			reftime--;
		}
	}
}
