/**
 * �ļ����ƣ�VSprite.java
 * ��·����entities
 * ������TODO ������Ļ�����࣬������л���״̬�Ͳ����ĵ�Ļ����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-7����02:24:03
 * �汾��Ver 1.0
 */
package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import jgui.GamePanel;
import data.GameData;
import system.*;
import timer.VLuaAction;
import timer.VLuaSPAction;
import ui.VCombatAreaUI;
import view.*;
import global.*;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VSprite extends VPointProxy implements VCombatObject{
	//-------------������ת״̬����------------------------
	public final static int ROTATEMODE_NOT=0;
	public final static int ROTATEMODE_ROTATE=1;
	public final static int ROTATEMODE_SELFPOSITIVE=2;	//˳ʱ������
	public final static int ROTATEMODE_SELFNEGATIVE=3;	//��ʱ������	
	//-------------��������״̬����-------------------------
	public static final int DEATHSTATE_TIMEEND = 0;		//ʱ�����
	public static final int DEATHSTATE_COLLISION = 1;	//��ײ
	public static final int DEATHSTATE_CLEAR = 2;		//�׶����
	public static final int DEATHSTATE_OUTBOUND = 3;	//������Χ
	//--------------------------------------------------------
	protected VLuaSPAction spaction;		//������Ϊ
	protected VImageInterface image_top;	//�ϲ�ͼ��
	protected VImageInterface anime_born=null ;	//���ɶ���
	protected double ab_x,ab_y;		//���ɶ�������
	protected VImageInterface anime_die;	//��������
	protected int anime_state=0;		//����״̬
	protected int t_index=0,b_index=0;
	protected VImageInterface image_bottom;	//�²�ͼ��
	protected boolean ifrotate=true;	//�Ƿ����ת
	protected int rotatemode=ROTATEMODE_ROTATE;			//��תģʽ
	protected double rotatespeed=Math.PI;	//ת�٣���λrad/s
	protected double lifetime;		//�������ڣ���λ��
	protected int lifetime_f;	//�������ڣ���λ����
	//protected double c_rad = 6;		//��ײ�뾶
	//protected double angle = Math.PI * 0;	//�Ƕ�,Ĭ��0��Ϊ���Ϸ�
	protected double speed = 0;			//�ٶȣ�px/s
	protected double anglespeed = 0;	//���ٶ�,�Ƕ�/s
	protected VCombatObject owner;	//������
	protected VArea r_area = new VArea(0,0,400,600) ;		//��������
	protected boolean restricted=true;	//�Ƿ��޶��������ڴ��
	protected boolean invincible=false;	//�����Ƿ�����ײ����
	protected double damage=1;		//�����˺�
	protected boolean debugmark=false;	//���Ա��
	
	protected ArrayList<VCombatObject> hitlist=new ArrayList<VCombatObject>();
	public boolean alive = false; 			//�Ƿ���
	public boolean visible = true; 			//�Ƿ���ʾ
	public boolean born = false;			//�Ƿ��Ѽ���
	protected int updatecount=0;			//���¼���
		
	public VSprite(){
		// TODO ����ΪĬ�����Ӳ���
		image_top = Imageconst.GetImageByName("Sprite_01.png");
		lifetime = 10;
		addToList();
		collrad=3;
	}

	public VSprite(String image_t,String image_b,int t_index,int b_index,
				   double lifetime,double c_rad,double angle,VCombatObject owner){
		image_top = Imageconst.GetImageByName(image_t);
		image_bottom = Imageconst.GetImageByName(image_b);
		this.t_index = t_index;
		this.b_index = b_index;
		this.lifetime = lifetime;
		//this.c_rad = c_rad;
		this.collrad = c_rad;
		this.setAngle(angle);
		this.owner = owner;
		addToList();
	}
	private void addToList() {
		// TODO ���½����Ӽ�����Ϸ�����е������б�
		if(GameData.spritelock=false){
			GameData.spriteList.add(this);	
		}
		else{
			GameData.spriteList_temp.add(this);  //��������б������ڱ����õ�״̬�������ʱ�б�
		}
	}
	public void die() {
		// TODO ��������
		born=true;
		alive=false;
	}
	public void die(int index){
		// TODO ������������������ʱ��ִ��������Ϊ
		die();
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO ��������ͼ��
		VImageInterface image = null;		//���Ƶ�ͼ�񣬸��ݲ���i�������ϲ㻹���²�ͼ��
		int index=0;
		image=this.getImageByLayer(i);
		if(i==0){
			index = t_index;
		}
		else if(i==1){
			index = b_index;
		}
		// ��ȡ��ʵ������������
		int x = (int) (this.GetX()+plat.getRealX()+0.5);	//ӳ��������������
		int y = (int) (this.GetY()+plat.getRealY()+0.5);
		int x_i = x-this.getImageByLayer(i).getWidth()/2;
		int y_i = y-this.getImageByLayer(i).getHeight()/2;
		Image ib = image.getImage(index);	//��ȡimage
		if(rotatemode==1){	//�����תģʽΪ����Ƕ�
			AffineTransform trans = new AffineTransform();
			trans.translate(x_i,y_i);	//λ�ƾ���
		    trans.rotate(getRotate(),x-x_i,y-y_i);	//��ת����
		    Graphics2D g2d = (Graphics2D)g;
		    g2d.drawImage(ib,trans,p);
		}
		else if(rotatemode==0){			//����ת��ֱ�ӻ���ԭimage
			if(image!=null){
				g.drawImage(ib, x_i, y_i, p);
			}
		}
		else{		//��תģʽΪ2/3����/��ʱ������
			AffineTransform trans = new AffineTransform();
			trans.translate(x_i,y_i);	//λ�ƾ���
			double rotate_self=0;
			/*if(rotatemode==2){
				rotate_self = (updatecount%100)*rotatespeed/100+Math.PI/2;
			}
			else if(rotatemode==3){
				rotate_self = -(updatecount%100)*rotatespeed/100+Math.PI/2;
			}*/
		    trans.rotate(getRotate(),x-x_i,y-y_i);	//��ת����
		    Graphics2D g2d = (Graphics2D)g;
		    g2d.drawImage(ib,trans,p);
		}
		if(anime_state==1){		//�������ɶ���
			if(anime_born!=null){
				int x_anime=(int) (ab_x+plat.getRealX()-anime_born.getWidth()/2);
				int y_anime=(int) (ab_y+plat.getRealY()-anime_born.getWidth()/2);
				g.drawImage(anime_born.getImage(), x_anime, y_anime, p);
			}
		}
	}
	//------------�����ǻ�ȡ���������ӻ��������ĺ���-------------------------------------------------------
	public boolean getAlive(){
		return alive;
	}
	public double getASpeed() {
		// TODO ��ȡ���ٶ�
		return anglespeed;
	}
	public boolean getBound(){	
		// ��ȡ�����Ƿ񳬳���Ϸ����
		return r_area.ifvalid(this.GetX(),this.GetY());
	}
	public double getDamage(){
		// ��ȡ�˺�ֵ
		return damage;
	}
	public boolean getHited(VCombatObject tar){
		// TODO ��ȡ�Ƿ��ѻ��й�Ŀ��
		for(VCombatObject unit:hitlist){
			if(tar==unit){
				return true;
			}
		}
		return false;
	}
	public VImageInterface getImageByLayer(int layer) {
		// TODO Auto-generated method stub
		if(layer==0){
			return image_top;
		}
		else{
			return image_bottom;
		}
	}
	public int getImageIndex(int i){
		// TODO ��ȡͼ��������0Ϊ�ϲ㣬1Ϊ�²�
		if(i==0){
			return t_index;
		}
		else{
			return b_index;
		}
	}
	public int getLife() {
		// TODO ��ȡ��������
		return lifetime_f;
	}
	public VCombatObject getOwner(){
		// TODO ��ȡ���ӷ�����
		return owner;
	}
	public double getSpeed(){
		return speed;
	}
	public int getUpdateCount(){
		// TODO ��ȡ���´���
		return updatecount;
	}
	public void setAngle(double a) {
		// TODO �������ýǶȺ�����������ģʽ�������ýǶ�ʱͬ����תֵ
		this.angle = VMath.StandardizationAngle(a);
		if(rotatemode!=2&&rotatemode!=3){
			rotate = angle + Math.PI/2;
		}
		/*int temp = (int) ((angle + Math.PI/16)/(Math.PI/8));
		if(rotaterelated=true){
			rotate = temp*Math.PI/8+Math.PI/2;
		}*/
	}
	public void setASpeed(double a){
		anglespeed = a;
	}
	public void setBornAnime(String anime){
		anime_born = Animeconst.GetAnimeInstance(anime);
	}
	public void setDamage(double d){
		damage = d;
	}
	public void setDebugMark(boolean b){
		debugmark=b;
	}
	public void setIfRotate(boolean r){
		// TODO ��������ͼ���Ƿ���ת
		ifrotate = r;
		if(r){
			rotatemode = 1;
		}
		else{
			rotatemode = 0;
		}
	}
	public void setImage(String top,String bottom){
		// TODO ��������ͼ��
		image_top = Imageconst.GetImageByName(top);
		image_bottom = Imageconst.GetImageByName(bottom);
	}
	public void setImageIndex(int t,int b){
		// TODO ��������ͼ������
		t_index = t;
		b_index = b;
	}
	public void setInvincible(boolean b){
		invincible = b;
	}
	public void setLife(double f) {
		// TODO �������������������������ɺ���ã������������������ʱ��
		lifetime = f;
		lifetime_f = (int) (lifetime * 1000);
	}
	public void setRestricted(boolean b){
		// TODO �������ӷ�Χ�Ƿ���������ʾ����
		restricted=b;
	}
	public void setRotateMode(int rm){
		// TODO ����ͼ����תģʽ
		rotatemode = rm;
	}
	public void setRotateSpeed(double a){
		// TODO ����ת��
		rotatespeed = a;
	}
	public void setSpeed(double d){
		// TODO �����ٶ�
		speed = d;
	}
	//---------------------------------------------------------------------------------------------------
	//------------���������ӵĸ��»�������Ⱦ�ȹ��ܺ���-----------------------------------------------------
	public void hit(VCombatObject target){
		// TODO ��������λ��ײ
		if(!invincible){		//���ӷ��޵�
			die(DEATHSTATE_COLLISION);
		}
		else{
			hitlist.add(target);
		}
	}
	public void LifeMinus() {
		// TODO ��ÿ����Ϸ״̬��������������������1
		if(lifetime_f!=-1000){		//-1000�����ô��
			lifetime_f = lifetime_f - VEngine.gs.getMSecond();
		}
		if(lifetime_f<(int)(lifetime*1000)&&lifetime_f>(int)(lifetime*1000-500)){
			anime_state=1;		//�������ɶ���
		}
		else{
			anime_state=0;
		}
	}
	public void move() {
		// TODO ���Ӱ��ٶȺͷ�������ƶ�
		if(anglespeed!=0){		//������ٶȲ�Ϊ0��ÿ֡�ı�Ƕ�
			this.setAngle(angle+anglespeed*VEngine.gs.getSecond());
		}
		if(speed!=0){			//λ��
			PolarMove(angle,speed*VEngine.gs.getSecond());
		}
	}
	public void spBorn(){
		// TODO �������ɺ������������б��е�����ʹ����Ա����º���Ⱦ
		alive = true;										//���ô����
		born = true;										//����������
		lifetime_f = (int) (lifetime * 1000);				//����ʣ����ʱ��(ms)�������������ɺ��޸�
		this.setCor((int)this.GetX(), (int)this.GetY());	//����ʱʹ����Ϊ����
		ab_x = this.GetX();									//������ʱ�����¼Ϊ��������
		ab_y = this.GetY();
	}
	public void spriteUpdate() {
		// TODO ��������״̬
		if (alive && (lifetime_f > 0||lifetime_f==-1000)) {	//�����ʣ��ʱ��>0
			/*if(spaction!=null){
				spaction.action(this);
			}*/
			move();
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
			die();
		}
		if(rotatemode==2||rotatemode==3){
			double rotate = VMath.StandardizationAngle(updatecount*rotatespeed/100+Math.PI/2);	//�ڸ��¹����м�����תֵ
			this.setRotate(rotate);
		}
		updatecount++;
	}
}
