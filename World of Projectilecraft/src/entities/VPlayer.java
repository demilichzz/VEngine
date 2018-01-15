/**
 * 文件名称：VPlayer.java
 * 类路径：entities
 * 描述：TODO 玩家角色类
 * 作者：Demilichzz
 * 时间：2012-8-6下午07:56:44
 * 版本：Ver 1.0
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
	protected VImageInterface image_top;	//顶层图像
	protected VImageInterface image_bottom;	//底层图像
	protected VAnime anime_ib;				//冰箱动画
	protected SpriteFactoryInstance spf;		//生成玩家弹幕的粒子工厂对象
	protected SpriteFactoryInstance spf_sub_a;
	protected SpriteFactoryInstance spf_sub_b;
	//-------------与玩家状态相关的属性参数---------------------------------------
	protected double life;
	//protected int CType = 0;	//碰撞类型索引
	//protected double collrad = 4;	//圆形碰撞的碰撞半径
	public int hit=0;			//玩家被击中次数计数
	protected boolean displayCP = false;		//是否显示判定点
	protected FSMclass fsm_p;	//玩家单位状态机
	protected int talent=0;		//玩家天赋
	protected int threat=0;		//玩家仇恨值
	protected boolean slowmode = false;	//减速模式
	protected double movespeed = 200;	//移动速度px/s
	protected int hitsound = 0;		//被击中音效的索引
	protected String[] soundpack_name = new String[]{"HumanMale","HumanFemale","Biu"}; //音频包名称
	protected static int invincibletime_value = 100;	//被击中的无敌时间
	protected static int threatbuffer_value = 200;	//停止攻击到仇恨降低的缓冲时间，默认值为2秒
	protected static int threatcap_value = 12000;		//仇恨值上限
	protected static int threatdecay_value = -20;	//在仇恨衰减状态每次更新减少的仇恨值
	protected static int threatinc_value = 30;		//攻击状态每次更新增加的仇恨值
	//-------------实现游戏逻辑使用的参数--------------------------------------------
	protected int threatbuffer=0;		//从停止攻击到仇恨开始降低的缓冲时间
	protected int invinciblecount=0;		//无敌时间计数
	protected int updatecount=0;		//更新计数 
	
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
		this.setLimit(10, 60, 390, 580, true);	//设置移动限制区域为游戏区域
		this.setHitSound(ProjectConfig.getConfigValue(ProjectConfig.HIT_SOUND));
	}

	private void initFSM() {
		// TODO 初始化玩家状态机
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
		case 0:{		//奥术
			spf.setSpeed(800);
			spf.setInvincible(false);
			spf.setIfRotate(true);
			spf.setLife(3);
			spf.setImage("player_sprite_arcane.png","null");
			spf.setImageIndex(0, 0);
			spf.setDamage(2);
			break;
		}
		case 1:{		//火焰
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
		case 2:{		//冰霜
			spf.setSpeed(500);
			spf.setInvincible(false);
			spf.setIfRotate(true);
			spf.setLife(3);
			spf.setImage("player_sprite_icearrow.png","null");
			spf.setImageIndex(0, 0);
			spf.setDamage(1.2);			//普通弹
			spf_sub_a=spf.getInstance();
			spf_sub_a.setSpeed(600);
			spf_sub_a.setLife(1);
			spf_sub_a.setImage("player_sprite_ice.png","null");
			spf_sub_a.setDamage(5);		//特殊弹
			break;
		}
		}
	}

	public int getTalent(){
		return talent;
	}

	public void move(double x,double y){
		// TODO 玩家位移功能函数，受玩家状态机影响
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
		// TODO 玩家状态更新
		if(invinciblecount>0){	//计算无敌状态时间
			invinciblecount--;
			if(invinciblecount==0){
				this.fsmStateTransition(FSMconst.INPUT_DEINVINCIBLE);	//无敌计时结束
			}
		}
		if(threatbuffer>0){	//计算仇恨状态，如果缓冲为0，则已有2秒未进行攻击，仇恨开始降低
			threatbuffer--;
		}
		else{
			addThreat(threatdecay_value);
		}
		if(iceblockcd>0){	//计算冰箱CD
			iceblockcd--;
			if(IB_CD-iceblockcd==IB_TIME){	//冰箱标准CD-剩余CD=持续时间，说明持续时间已到
				removeIceBlock();	//冰箱状态结束
			}
		}
		updatecount++;
	}

	public void resetState(){
		// TODO 重置玩家状态
		switch(GameData.gamemode){
		case 1:{
			life=100;
			break;
		}
		}
		invinciblecount=0;
		iceblockcd=0;
		fsm_p.SetCurrentState(FSMconst.CU_NORMAL);	//重置状态机
		
	}
	public void hit(VSprite sp){
		// TODO 被粒子击中时调用的函数
		if(fsm_p.GetCurrentState()==FSMconst.CU_NORMAL){
			//Debug.DebugSimpleMessage("PC Hit at:"+VEngine.gs.getCurrentStage().updatecount);
			GameData.rep.addHitState(VEngine.gs.getCurrentStage().updatecount);
			Soundconst.GetSoundPack(soundpack_name[hitsound]).soundPlay(0);
			switch(GameData.gamemode){
			case 0:{			//练习模式
				hit=hit+1;	//被击中数+1
				//Debug.DebugSimpleMessage(""+VEngine.gs.getCurrentStage().updatecount);
				break;
			}
			case 1:{			//生存模式
				hit=hit+1;
				life=life-sp.getDamage();
				if(life<=0){
					die();
				}
				break;
			}
			case 2:{			//挑战模式
				hit=hit+1;
				if(hit>5){
					die();
				}
				break;
			}
			}
			fsm_p.SetCurrentState(fsm_p.StateTransition(FSMconst.INPUT_CUINVINCIBLE));	//进入无敌状态
			invinciblecount=invincibletime_value;
		}
		else{
			
		}
		sp.hit(this);
	}
	public void die(){
		// TODO 玩家死亡
		Soundconst.GetSoundPack(soundpack_name[hitsound]).soundPlay(1);	//播放死亡音效
		GlobalEvent.gameOver();
	}
	private int lastshoot = 0;
	private static final int SHOOT_RATE = 10;	//射速 子弹数/秒
	protected int shotcount=0;
	public void fire(){
		// TODO 玩家进行射击
		if(this.fsm_p.GetCurrentState()!=FSMconst.CU_ICEBLOCK){
			if((VEngine.gs.getCurrentStage().updatecount+1-lastshoot)*VEngine.gs.getMSecond()>=(1000/SHOOT_RATE)||lastshoot==0){
				lastshoot = VEngine.gs.getCurrentStage().updatecount+1;
				shoot(talent);
			}
			if(!slowmode){	//非减速模式下
				threatbuffer=threatbuffer_value;	//进行射击则缓冲重新开始计时
				addThreat(threatinc_value);
			}
		}
	}
	public void shoot(int index){
		// TODO 按天赋进行发射
		Soundconst.GetSoundByName("se_plst00.wav").soundPlay();
		switch(index){
		case 0:{	//奥术
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
		case 1:{	//火焰
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
		case 2:{	//冰霜
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
	protected final int IB_CD = 6000;	//冰箱CD60s，单位为Stage活动更新次数
	protected final int IB_TIME = 500;	//冰箱持续时间5s
	public void bomb(){
		// TODO 炸弹
		if(iceblockcd==0){
			iceBlock();
		}
		else{
			removeIceBlock();
		}
	}
	public void iceBlock(){
		// TODO 冰箱
		iceblockcd = IB_CD;		//冰箱进入冷却
		anime_ib.animeReset();	//重置冰箱动画
		collrad = 35;
		Soundconst.GetSoundByName("IceBarrirerState.mp3").soundPlay();
		fsmStateTransition(FSMconst.INPUT_ICEBLOCK);	//输入状态
	}
	public void removeIceBlock(){
		// TODO 解除冰箱
		collrad = 2;		//还原碰撞体积
		fsmStateTransition(FSMconst.INPUT_REMOVEBLOCK);
	}
	public void addThreat(int value){
		// TODO 增加或减少仇恨值
		threat = threat + value;
		if(threat<0){
			threat=0;
		}
		else if(threat>threatcap_value){
			threat=threatcap_value;
		}
	}
	public void drawMe(Graphics2D g,GamePanel p,VCombatAreaUI g_area){
		// TODO 绘制玩家单位
		int x= (int)(g_area.getRealX()+GetX());
		int y= (int)(g_area.getRealY()+GetY());
		int x_i = x-(getImageByLayer(0).getWidth()/2);
		int y_i = y-(getImageByLayer(0).getHeight()/2);
		if(fsm_p.GetCurrentState()==FSMconst.CU_INVINCIBLE){
			float alpha = Math.abs(20-updatecount%40);
			alpha=alpha/20;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);	//透明度 
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
			g.setPaintMode();		//重置Graphic模式
		}
		if(displayCP){		//显示判定点
			int x_cp_i = x-8;
			int y_cp_i = y-8;
			if(g_area.area.ifvalid(x, y)){
				g.drawImage(Imageconst.GetImageByName("Char_CPoint.png").getImage(),x_cp_i,y_cp_i,p);
			}
		}
		if(fsm_p.GetCurrentState()==FSMconst.CU_ICEBLOCK){	//状态机为冰箱状态
			int x_a = x-anime_ib.getWidth()/2;
			int y_a = y-anime_ib.getHeight()/2;
			g.drawImage(anime_ib.getImage(),x_a,y_a,p);
		}
	}

	public void processKeyAction(int[] keystate) {
		// TODO 按键响应接口
		for(int i=0;i<keystate.length;i++){
			if(keystate[i]==1){	//如果对应按键已按下
				keyProcess(i,1);	//调用键盘事件处理函数
			}
			else{
				keyProcess(i,0);
			}
		}
	}

	private void keyProcess(int index,int state) {
		// TODO Auto-generated method stub
		double fspeed=movespeed;		//实际速度
		if(slowmode){
			fspeed=fspeed*0.4;
		}
		if (state == 1) {			//按键按下时的响应
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
				fire();		//射击
				break;
			}
			case GameListener.KEY_X: {
				bomb();		//冰箱
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
		else if(state==0){		//按键未按下
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
		// TODO 碰撞检测
		return super.cDetection(target);
	}

	public void fsmStateTransition(int fsm_action){
		// TODO 对玩家状态机输入状态进行状态转换
		fsm_p.SetCurrentState(fsm_p.StateTransition(fsm_action));
	}
	public int getCType() {
		// TODO 获取碰撞类型
		return CType;
	}

	public double getCollRad() {
		// TODO 获取碰撞半径
		return super.getCollRad();
	}
	public VImageInterface getImageByLayer(int layer) {
		// TODO 获取指定层的图像
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
		// TODO 设置指定层的图像
		switch(layer){
		case 0:{
			image_top = v;
		}
		case 1:{
			image_bottom = v;
		}
		default:{
			Debug.DebugSimpleMessage("错误的图像层参数");
		}
		}
	}

	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	public int getThreatState(){
		// TODO 根据仇恨值获取当前的仇恨状态
		if(threat<7000){
			return 0;		//绿色安全
		}
		else if(threat<10000){
			return 1;		//橙色危险
		}
		else{
			return 2;		//红色OT
		}
	}
	@Override
	public int getValue(int index) {
		// TODO 玩家类实现动态值接口的获取值函数，根据参数获取不同的状态参数
		switch(index){
		case 0:{			//返回生命值
			return (int) life;
		}
		case 1:{			//返回剩余残机
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
		case 2:{			//返回冰箱冷却
			return iceblockcd;
		}
		case 3:{			//返回仇恨值
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
		// TODO 设置被击中的音频
		hitsound = cv;
	}
}
