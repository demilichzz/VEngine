/**
 * �ļ����ƣ�VPlayer.java
 * ��·����entities
 * ������TODO ��ҽ�ɫ��
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-6����07:56:44
 * �汾��Ver 1.0
 */
package entities;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.RescaleOp;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

import jgui.GamePanel;

import config.ProjectConfig;
import controller.*;
import data.GameData;
import event.GlobalEvent;
import system.*;
import timer.*;
import ui.VCombatAreaUI;
import view.*;
import factory.*;
import fsm.*;
import global.*;
import interfaces.*;
/**
 * @author Demilichzz
 *
 */
public class VPlayer extends VPointProxy implements VCombatObject,VKeyProcessorInterface,VValueInterface{
	protected VImageInterface image_top;	//����ͼ��
	protected VImageInterface image_bottom;	//�ײ�ͼ��
	protected VAnime anime_ib;				//���䶯��
	protected SpriteFactoryInstance spf;		//������ҵ�Ļ�����ӹ�������
	protected SpriteFactoryInstance spf_sub_a;
	protected SpriteFactoryInstance spf_sub_b;
	//-------------�����״̬��ص����Բ���---------------------------------------
	protected double life;
	//protected int CType = 0;	//��ײ��������
	//protected double collrad = 4;	//Բ����ײ����ײ�뾶
	public int hit=0;			//��ұ����д�������
	protected boolean displayCP = false;		//�Ƿ���ʾ�ж���
	protected FSMclass fsm_p;	//��ҵ�λ״̬��
	protected int talent=0;		//����츳
	protected int threat=0;		//��ҳ��ֵ
	protected boolean slowmode = false;	//����ģʽ
	protected double movespeed = 200;	//�ƶ��ٶ�px/s
	protected int hitsound = 0;		//��������Ч������
	protected String[] soundpack_name = new String[]{"HumanMale","HumanFemale","Biu"}; //��Ƶ������
	protected static int invincibletime_value = 100;	//�����е��޵�ʱ��
	protected static int threatbuffer_value = 200;	//ֹͣ��������޽��͵Ļ���ʱ�䣬Ĭ��ֵΪ2��
	protected static int threatcap_value = 12000;		//���ֵ����
	protected static int threatdecay_value = -20;	//�ڳ��˥��״̬ÿ�θ��¼��ٵĳ��ֵ
	protected static int threatinc_value = 30;		//����״̬ÿ�θ������ӵĳ��ֵ
	//-------------ʵ����Ϸ�߼�ʹ�õĲ���--------------------------------------------
	protected int threatbuffer=0;		//��ֹͣ��������޿�ʼ���͵Ļ���ʱ��
	protected int invinciblecount=0;		//�޵�ʱ�����
	protected int updatecount=0;		//���¼��� 
	
	public VPlayer(){
		super();
		life = 100;
		threat=0;
		initFSM();
		collrad=2.5;
		image_top = Imageconst.GetImageByName("Char_mage_40x70.png");
		anime_ib = Animeconst.GetAnimeInstance("pc_iceblock_anime");
		spf=SpriteFactory.getInstance();
		this.setCor(200, 500);
		this.setLimit(10, 60, 390, 580, true);	//�����ƶ���������Ϊ��Ϸ����
		this.setHitSound(ProjectConfig.getConfigValue(ProjectConfig.HIT_SOUND));
	}

	private void initFSM() {
		// TODO ��ʼ�����״̬��
		fsm_p = new FSMclass(FSMconst.CU_NORMAL);
		FSMstate fsms;
		fsms = new FSMstate(FSMconst.CU_NORMAL, 3);
		fsms.AddTransition(FSMconst.INPUT_CUMOVE, FSMconst.CU_MOVING);
		fsms.AddTransition(FSMconst.INPUT_CUINVINCIBLE,FSMconst.CU_INVINCIBLE);
		fsms.AddTransition(FSMconst.INPUT_ICEBLOCK,FSMconst.CU_ICEBLOCK);
		fsm_p.AddState(fsms);
		fsms = new FSMstate(FSMconst.CU_INVINCIBLE, 3);
		fsms.AddTransition(FSMconst.INPUT_CUINVINCIBLE,FSMconst.CU_NORMAL);
		fsms.AddTransition(FSMconst.INPUT_DEINVINCIBLE,FSMconst.CU_NORMAL);
		fsms.AddTransition(FSMconst.INPUT_ICEBLOCK, FSMconst.CU_ICEBLOCK);
		fsm_p.AddState(fsms);
		fsms = new FSMstate(FSMconst.CU_ICEBLOCK,3);
		fsms.AddTransition(FSMconst.INPUT_ICEBLOCK, FSMconst.CU_NORMAL);
		fsms.AddTransition(FSMconst.INPUT_REMOVEBLOCK, FSMconst.CU_NORMAL);
		fsm_p.AddState(fsms);
	}
	public void setTalent(int i){
		this.talent=i;
		spf.setOwner(this);
		spf.setRad(8);
		switch(talent){
		case 0:{		//����
			spf.setSpeed(800);
			spf.setInvincible(false);
			spf.setIfRotate(true);
			spf.setLife(3);
			spf.setImage("player_sprite_arcane.png","null");
			spf.setImageIndex(0, 0);
			spf.setDamage(2);
			break;
		}
		case 1:{		//����
			spf.setSpeed(300);
			spf.setInvincible(false);
			spf.setIfRotate(false);
			spf.setLife(6);
			spf.setImage("player_sprite_firestar.png","null");
			spf.setImageIndex(0, 0);
			spf.setDamage(1.5);
			spf_sub_a=spf.getInstance();
			spf_sub_a.setSpeed(400);
			spf_sub_a.setLife(3);
			spf_sub_a.setMode(4);
			spf_sub_a.setImage("player_sprite_pyro.png","null");
			spf_sub_a.setDamage(4);
			spf_sub_b=spf.getInstance();
			spf_sub_b.setSpeed(500);
			spf_sub_b.setLife(3);
			spf_sub_b.setMode(4);
			break;
		}
		case 2:{		//��˪
			spf.setSpeed(500);
			spf.setInvincible(false);
			spf.setIfRotate(true);
			spf.setLife(3);
			spf.setImage("player_sprite_icearrow.png","null");
			spf.setImageIndex(0, 0);
			spf.setDamage(1.2);			//��ͨ��
			spf_sub_a=spf.getInstance();
			spf_sub_a.setSpeed(600);
			spf_sub_a.setLife(1);
			spf_sub_a.setImage("player_sprite_ice.png","null");
			spf_sub_a.setDamage(5);		//���ⵯ
			break;
		}
		}
	}

	public int getTalent(){
		return talent;
	}

	public void move(double x,double y){
		// TODO ���λ�ƹ��ܺ����������״̬��Ӱ��
		switch(fsm_p.GetCurrentState()){
		case FSMconst.CU_ICEBLOCK:{
			break;
		}
		default:{
			this.moveCor(x, y);
			break;
		}
		}
		//Debug.DebugSimpleMessage("X:"+this.GetX()+" Y:"+this.GetY());
	}
	public void update(){
		// TODO ���״̬����
		if(invinciblecount>0){	//�����޵�״̬ʱ��
			invinciblecount--;
			if(invinciblecount==0){
				this.fsmStateTransition(FSMconst.INPUT_DEINVINCIBLE);	//�޵м�ʱ����
			}
		}
		if(threatbuffer>0){	//������״̬���������Ϊ0��������2��δ���й�������޿�ʼ����
			threatbuffer--;
		}
		else{
			addThreat(threatdecay_value);
		}
		if(iceblockcd>0){	//�������CD
			iceblockcd--;
			if(IB_CD-iceblockcd==IB_TIME){	//�����׼CD-ʣ��CD=����ʱ�䣬˵������ʱ���ѵ�
				removeIceBlock();	//����״̬����
			}
		}
		updatecount++;
	}

	public void resetState(){
		// TODO �������״̬
		switch(GameData.gamemode){
		case 1:{
			life=100;
			break;
		}
		}
		invinciblecount=0;
		iceblockcd=0;
		fsm_p.SetCurrentState(FSMconst.CU_NORMAL);	//����״̬��
		
	}
	public void hit(VSprite sp){
		// TODO �����ӻ���ʱ���õĺ���
		if(fsm_p.GetCurrentState()==FSMconst.CU_NORMAL){
			//Debug.DebugSimpleMessage("PC Hit at:"+VEngine.gs.getCurrentStage().updatecount);
			GameData.rep.addHitState(VEngine.gs.getCurrentStage().updatecount);
			Soundconst.GetSoundPack(soundpack_name[hitsound]).soundPlay(0);
			switch(GameData.gamemode){
			case 0:{			//��ϰģʽ
				hit=hit+1;	//��������+1
				//Debug.DebugSimpleMessage(""+VEngine.gs.getCurrentStage().updatecount);
				break;
			}
			case 1:{			//����ģʽ
				hit=hit+1;
				life=life-sp.getDamage();
				if(life<=0){
					die();
				}
				break;
			}
			case 2:{			//��սģʽ
				hit=hit+1;
				if(hit>5){
					die();
				}
				break;
			}
			}
			fsm_p.SetCurrentState(fsm_p.StateTransition(FSMconst.INPUT_CUINVINCIBLE));	//�����޵�״̬
			invinciblecount=invincibletime_value;
		}
		else{
			
		}
		sp.hit(this);
	}
	public void die(){
		// TODO �������
		Soundconst.GetSoundPack(soundpack_name[hitsound]).soundPlay(1);	//����������Ч
		GlobalEvent.gameOver();
	}
	private int lastshoot = 0;
	private static final int SHOOT_RATE = 10;	//���� �ӵ���/��
	protected int shotcount=0;
	public void fire(){
		// TODO ��ҽ������
		if(this.fsm_p.GetCurrentState()!=FSMconst.CU_ICEBLOCK){
			if((VEngine.gs.getCurrentStage().updatecount+1-lastshoot)*VEngine.gs.getMSecond()>=(1000/SHOOT_RATE)||lastshoot==0){
				lastshoot = VEngine.gs.getCurrentStage().updatecount+1;
				shoot(talent);
			}
			if(!slowmode){	//�Ǽ���ģʽ��
				threatbuffer=threatbuffer_value;	//��������򻺳����¿�ʼ��ʱ
				addThreat(threatinc_value);
			}
		}
	}
	public void shoot(int index){
		// TODO ���츳���з���
		Soundconst.GetSoundByName("se_plst00.wav").soundPlay();
		switch(index){
		case 0:{	//����
			for(int i=0;i<2;i++){
				VSprite b = spf.creator();
				b.setAngle(Math.PI*-0.5);
				b.setCor(this.GetX()-10+i*20, this.GetY());
				b.spBorn();
			}
			double offset=10-Math.abs(shotcount%20-10);		//offset=10~0~10
			VSprite b = spf.creator();
			b.setAngle(Math.PI*-0.5-offset*Math.PI*0.008);
			b.setCor(this.GetX()-10, this.GetY()+20);
			b.spBorn();
			b = spf.creator();
			b.setAngle(Math.PI*-0.5+offset*Math.PI*0.008);
			b.setCor(this.GetX()+10, this.GetY()+20);
			b.spBorn();
			break;
		}
		case 1:{	//����
			for(int i=0;i<3;i++){
				VSprite b = spf.creator();
				b.setAngle(Math.PI*-0.6+Math.PI*0.1*i);
				b.setCor(this.GetX()-20+i*20, this.GetY()-20);
				b.spBorn();
			}
			if(VMath.GetRandomInt(1, 6)>4){
				for(int i=0;i<3;i++){
					VSprite b = spf_sub_a.creator();
					b.setAngle(Math.PI*-0.55+Math.PI*0.05*i);
					b.setCor(this.GetX(), this.GetY());
					b.spBorn();
				}
			}
			for(int i=0;i<2;i++){
				VSpriteTrace b = (VSpriteTrace) spf_sub_b.creator();
				b.setAngle(Math.PI*-0.5);
				b.setCor(this.GetX()-20+i*40, this.GetY());
				b.setTarget(GameData.getClosetUnit());
				b.spBorn();
			}
			break;
		}
		case 2:{	//��˪
			for(int i=0;i<7;i++){
				VSprite b = spf.creator();
				b.setAngle(Math.PI*-0.8+Math.PI*0.1*i);
				b.setCor(this.GetX(), this.GetY());
				b.spBorn();
			}
			if(shotcount%3==0){
				for(int i=0;i<3;i++){
					VSprite b = spf_sub_a.creator();
					b.setAngle(-Math.PI/2+Math.PI/32-Math.PI/32*i);
					b.setCor(this.GetX()-20+i*20, this.GetY());
					b.spBorn();
				}
			}
			break;
		}
		}
		shotcount=shotcount+1;
	}
	
	protected int iceblockcd = 0;
	protected final int IB_CD = 6000;	//����CD60s����λΪStage����´���
	protected final int IB_TIME = 500;	//�������ʱ��5s
	public void bomb(){
		// TODO ը��
		if(iceblockcd==0){
			iceBlock();
		}
		else{
			removeIceBlock();
		}
	}
	public void iceBlock(){
		// TODO ����
		iceblockcd = IB_CD;		//���������ȴ
		anime_ib.animeReset();	//���ñ��䶯��
		collrad = 35;
		Soundconst.GetSoundByName("IceBarrirerState.mp3").soundPlay();
		fsmStateTransition(FSMconst.INPUT_ICEBLOCK);	//����״̬
	}
	public void removeIceBlock(){
		// TODO �������
		collrad = 2;		//��ԭ��ײ���
		fsmStateTransition(FSMconst.INPUT_REMOVEBLOCK);
	}
	public void addThreat(int value){
		// TODO ���ӻ���ٳ��ֵ
		threat = threat + value;
		if(threat<0){
			threat=0;
		}
		else if(threat>threatcap_value){
			threat=threatcap_value;
		}
	}
	public void drawMe(Graphics2D g,GamePanel p,VCombatAreaUI g_area){
		// TODO ������ҵ�λ
		int x= (int)(g_area.getRealX()+GetX());
		int y= (int)(g_area.getRealY()+GetY());
		int x_i = x-(getImageByLayer(0).getWidth()/2);
		int y_i = y-(getImageByLayer(0).getHeight()/2);
		if(fsm_p.GetCurrentState()==FSMconst.CU_INVINCIBLE){
			float alpha = Math.abs(20-updatecount%40);
			alpha=alpha/20;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);	//͸���� 
			g.setComposite(ac);
		}
		if(g_area.area.ifvalid(x, y)){
			for(int i=0;i<2;i++){
				if(getImageByLayer(1-i)!=null){
					g.drawImage(getImageByLayer(1-i).getImage(),x_i,y_i,p);
				}
			}
		}
		if(fsm_p.GetCurrentState()==FSMconst.CU_INVINCIBLE){
			g.setPaintMode();		//����Graphicģʽ
		}
		if(displayCP){		//��ʾ�ж���
			int x_cp_i = x-8;
			int y_cp_i = y-8;
			if(g_area.area.ifvalid(x, y)){
				g.drawImage(Imageconst.GetImageByName("Char_CPoint.png").getImage(),x_cp_i,y_cp_i,p);
			}
		}
		if(fsm_p.GetCurrentState()==FSMconst.CU_ICEBLOCK){	//״̬��Ϊ����״̬
			int x_a = x-anime_ib.getWidth()/2;
			int y_a = y-anime_ib.getHeight()/2;
			g.drawImage(anime_ib.getImage(),x_a,y_a,p);
		}
	}

	public void processKeyAction(int[] keystate) {
		// TODO ������Ӧ�ӿ�
		for(int i=0;i<keystate.length;i++){
			if(keystate[i]==1){	//�����Ӧ�����Ѱ���
				keyProcess(i,1);	//���ü����¼�������
			}
			else{
				keyProcess(i,0);
			}
		}
	}

	private void keyProcess(int index,int state) {
		// TODO Auto-generated method stub
		double fspeed=movespeed;		//ʵ���ٶ�
		if(slowmode){
			fspeed=fspeed*0.4;
		}
		if (state == 1) {			//��������ʱ����Ӧ
			switch (index) {
			case GameListener.KEY_UP: {
				move(0, -fspeed/100);
				break;
			}
			case GameListener.KEY_DOWN: {
				move(0, fspeed/100);
				break;
			}
			case GameListener.KEY_LEFT: {
				move(-fspeed/100, 0);
				break;
			}
			case GameListener.KEY_RIGHT: {
				move(fspeed/100, 0);
				break;
			}
			case GameListener.KEY_ENTER: {
				break;
			}
			case GameListener.KEY_SPACE: {
				break;
			}
			case GameListener.KEY_Z: {
				fire();		//���
				break;
			}
			case GameListener.KEY_X: {
				bomb();		//����
				VEngine.glistener.resetKeystate(GameListener.KEY_X);
				break;
			}
			case GameListener.KEY_C: {
				break;
			}
			case GameListener.KEY_SHIFT:{
				displayCP = true;
				slowmode = true;
				break;
			}
			}
		}
		else if(state==0){		//����δ����
			switch(index){
			case GameListener.KEY_Z:{
				break;
			}
			case GameListener.KEY_SHIFT:{
				displayCP = false;
				slowmode = false;
				break;
			}
			}
		}
	}

	public boolean cDetection(VPointProxy target) {
		// TODO ��ײ���
		return super.cDetection(target);
	}

	public void fsmStateTransition(int fsm_action){
		// TODO �����״̬������״̬����״̬ת��
		fsm_p.SetCurrentState(fsm_p.StateTransition(fsm_action));
	}
	public int getCType() {
		// TODO ��ȡ��ײ����
		return CType;
	}

	public double getCollRad() {
		// TODO ��ȡ��ײ�뾶
		return super.getCollRad();
	}
	public VImageInterface getImageByLayer(int layer) {
		// TODO ��ȡָ�����ͼ��
		switch(layer){
		case 0:{
			return image_top;
		}
		case 1:{
			return image_bottom;
		}
		default:{
			return null;
		}
		}
	}

	public void setImageByLayer(VImageInterface v,int layer){
		// TODO ����ָ�����ͼ��
		switch(layer){
		case 0:{
			image_top = v;
		}
		case 1:{
			image_bottom = v;
		}
		default:{
			Debug.DebugSimpleMessage("�����ͼ������");
		}
		}
	}

	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	public int getThreatState(){
		// TODO ���ݳ��ֵ��ȡ��ǰ�ĳ��״̬
		if(threat<7000){
			return 0;		//��ɫ��ȫ
		}
		else if(threat<10000){
			return 1;		//��ɫΣ��
		}
		else{
			return 2;		//��ɫOT
		}
	}
	@Override
	public int getValue(int index) {
		// TODO �����ʵ�ֶ�ֵ̬�ӿڵĻ�ȡֵ���������ݲ�����ȡ��ͬ��״̬����
		switch(index){
		case 0:{			//��������ֵ
			return (int) life;
		}
		case 1:{			//����ʣ��л�
			if(hit<-3){	
				return 8;
			}
			else if(hit>5){
				return 0;
			}
			else{
				return 5-hit;
			}
		}
		case 2:{			//���ر�����ȴ
			return iceblockcd;
		}
		case 3:{			//���س��ֵ
			return threat;
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
		case 0:{
			life=value;
			break;
		}
		}
	}

	/**
	 * @param configValue
	 */
	public void setHitSound(int cv) {
		// TODO ���ñ����е���Ƶ
		hitsound = cv;
	}
}
