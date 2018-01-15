/**
 * 文件名称：VSprite.java
 * 类路径：entities
 * 描述：TODO 基本弹幕粒子类，定义具有基础状态和参数的弹幕对象
 * 作者：Demilichzz
 * 时间：2012-8-7上午02:24:03
 * 版本：Ver 1.0
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
	//-------------粒子旋转状态常量------------------------
	public final static int ROTATEMODE_NOT=0;
	public final static int ROTATEMODE_ROTATE=1;
	public final static int ROTATEMODE_SELFPOSITIVE=2;	//顺时针自旋
	public final static int ROTATEMODE_SELFNEGATIVE=3;	//逆时针自旋	
	//-------------粒子死亡状态常量-------------------------
	public static final int DEATHSTATE_TIMEEND = 0;		//时间结束
	public static final int DEATHSTATE_COLLISION = 1;	//碰撞
	public static final int DEATHSTATE_CLEAR = 2;		//阶段清除
	public static final int DEATHSTATE_OUTBOUND = 3;	//超出范围
	//--------------------------------------------------------
	protected VLuaSPAction spaction;		//粒子行为
	protected VImageInterface image_top;	//上层图像
	protected VImageInterface anime_born=null ;	//生成动画
	protected double ab_x,ab_y;		//生成动画坐标
	protected VImageInterface anime_die;	//死亡动画
	protected int anime_state=0;		//动画状态
	protected int t_index=0,b_index=0;
	protected VImageInterface image_bottom;	//下层图像
	protected boolean ifrotate=true;	//是否可旋转
	protected int rotatemode=ROTATEMODE_ROTATE;			//旋转模式
	protected double rotatespeed=Math.PI;	//转速，单位rad/s
	protected double lifetime;		//生命周期，单位秒
	protected int lifetime_f;	//生命周期，单位毫秒
	//protected double c_rad = 6;		//碰撞半径
	//protected double angle = Math.PI * 0;	//角度,默认0度为正上方
	protected double speed = 0;			//速度，px/s
	protected double anglespeed = 0;	//角速度,角度/s
	protected VCombatObject owner;	//所有者
	protected VArea r_area = new VArea(0,0,400,600) ;		//限制区域
	protected boolean restricted=true;	//是否限定在区域内存活
	protected boolean invincible=false;	//粒子是否不因碰撞死亡
	protected double damage=1;		//粒子伤害
	protected boolean debugmark=false;	//调试标记
	
	protected ArrayList<VCombatObject> hitlist=new ArrayList<VCombatObject>();
	public boolean alive = false; 			//是否存活
	public boolean visible = true; 			//是否显示
	public boolean born = false;			//是否已激活
	protected int updatecount=0;			//更新计数
		
	public VSprite(){
		// TODO 设置为默认粒子参数
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
		// TODO 将新建粒子加入游戏数据中的粒子列表
		if(GameData.spritelock=false){
			GameData.spriteList.add(this);	
		}
		else{
			GameData.spriteList_temp.add(this);  //如果粒子列表正处于被调用的状态则加入临时列表
		}
	}
	public void die() {
		// TODO 粒子死亡
		born=true;
		alive=false;
	}
	public void die(int index){
		// TODO 基础粒子类型在死亡时不执行其他行为
		die();
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO 绘制粒子图像
		VImageInterface image = null;		//绘制的图像，根据参数i决定是上层还是下层图像
		int index=0;
		image=this.getImageByLayer(i);
		if(i==0){
			index = t_index;
		}
		else if(i==1){
			index = b_index;
		}
		// 获取真实粒子中心坐标
		int x = (int) (this.GetX()+plat.getRealX()+0.5);	//映射坐标四舍五入
		int y = (int) (this.GetY()+plat.getRealY()+0.5);
		int x_i = x-this.getImageByLayer(i).getWidth()/2;
		int y_i = y-this.getImageByLayer(i).getHeight()/2;
		Image ib = image.getImage(index);	//获取image
		if(rotatemode==1){	//如果旋转模式为跟随角度
			AffineTransform trans = new AffineTransform();
			trans.translate(x_i,y_i);	//位移矩阵
		    trans.rotate(getRotate(),x-x_i,y-y_i);	//旋转矩阵
		    Graphics2D g2d = (Graphics2D)g;
		    g2d.drawImage(ib,trans,p);
		}
		else if(rotatemode==0){			//不旋转则直接绘制原image
			if(image!=null){
				g.drawImage(ib, x_i, y_i, p);
			}
		}
		else{		//旋转模式为2/3，正/逆时针自旋
			AffineTransform trans = new AffineTransform();
			trans.translate(x_i,y_i);	//位移矩阵
			double rotate_self=0;
			/*if(rotatemode==2){
				rotate_self = (updatecount%100)*rotatespeed/100+Math.PI/2;
			}
			else if(rotatemode==3){
				rotate_self = -(updatecount%100)*rotatespeed/100+Math.PI/2;
			}*/
		    trans.rotate(getRotate(),x-x_i,y-y_i);	//旋转矩阵
		    Graphics2D g2d = (Graphics2D)g;
		    g2d.drawImage(ib,trans,p);
		}
		if(anime_state==1){		//绘制生成动画
			if(anime_born!=null){
				int x_anime=(int) (ab_x+plat.getRealX()-anime_born.getWidth()/2);
				int y_anime=(int) (ab_y+plat.getRealY()-anime_born.getWidth()/2);
				g.drawImage(anime_born.getImage(), x_anime, y_anime, p);
			}
		}
	}
	//------------以下是获取和设置粒子基础参数的函数-------------------------------------------------------
	public boolean getAlive(){
		return alive;
	}
	public double getASpeed() {
		// TODO 获取角速度
		return anglespeed;
	}
	public boolean getBound(){	
		// 获取粒子是否超出游戏区域
		return r_area.ifvalid(this.GetX(),this.GetY());
	}
	public double getDamage(){
		// 获取伤害值
		return damage;
	}
	public boolean getHited(VCombatObject tar){
		// TODO 获取是否已击中过目标
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
		// TODO 获取图像索引，0为上层，1为下层
		if(i==0){
			return t_index;
		}
		else{
			return b_index;
		}
	}
	public int getLife() {
		// TODO 获取粒子生命
		return lifetime_f;
	}
	public VCombatObject getOwner(){
		// TODO 获取粒子发射者
		return owner;
	}
	public double getSpeed(){
		return speed;
	}
	public int getUpdateCount(){
		// TODO 获取更新次数
		return updatecount;
	}
	public void setAngle(double a) {
		// TODO 重载设置角度函数，在自旋模式不在设置角度时同步旋转值
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
		// TODO 设置粒子图像是否旋转
		ifrotate = r;
		if(r){
			rotatemode = 1;
		}
		else{
			rotatemode = 0;
		}
	}
	public void setImage(String top,String bottom){
		// TODO 设置粒子图像
		image_top = Imageconst.GetImageByName(top);
		image_bottom = Imageconst.GetImageByName(bottom);
	}
	public void setImageIndex(int t,int b){
		// TODO 设置粒子图像索引
		t_index = t;
		b_index = b;
	}
	public void setInvincible(boolean b){
		invincible = b;
	}
	public void setLife(double f) {
		// TODO 设置粒子生命，不建议在生成后调用，否则会重置粒子生存时间
		lifetime = f;
		lifetime_f = (int) (lifetime * 1000);
	}
	public void setRestricted(boolean b){
		// TODO 设置粒子范围是否受限于显示区域
		restricted=b;
	}
	public void setRotateMode(int rm){
		// TODO 设置图像旋转模式
		rotatemode = rm;
	}
	public void setRotateSpeed(double a){
		// TODO 设置转速
		rotatespeed = a;
	}
	public void setSpeed(double d){
		// TODO 设置速度
		speed = d;
	}
	//---------------------------------------------------------------------------------------------------
	//------------以下是粒子的更新机制与渲染等功能函数-----------------------------------------------------
	public void hit(VCombatObject target){
		// TODO 与其它单位碰撞
		if(!invincible){		//粒子非无敌
			die(DEATHSTATE_COLLISION);
		}
		else{
			hitlist.add(target);
		}
	}
	public void LifeMinus() {
		// TODO 在每个游戏状态更改周期中粒子生命减1
		if(lifetime_f!=-1000){		//-1000则永久存活
			lifetime_f = lifetime_f - VEngine.gs.getMSecond();
		}
		if(lifetime_f<(int)(lifetime*1000)&&lifetime_f>(int)(lifetime*1000-500)){
			anime_state=1;		//播放生成动画
		}
		else{
			anime_state=0;
		}
	}
	public void move() {
		// TODO 粒子按速度和方向进行移动
		if(anglespeed!=0){		//如果角速度不为0则每帧改变角度
			this.setAngle(angle+anglespeed*VEngine.gs.getSecond());
		}
		if(speed!=0){			//位移
			PolarMove(angle,speed*VEngine.gs.getSecond());
		}
	}
	public void spBorn(){
		// TODO 粒子生成函数，激活在列表中的粒子使其可以被更新和渲染
		alive = true;										//设置存活性
		born = true;										//设置已生成
		lifetime_f = (int) (lifetime * 1000);				//设置剩余存活时间(ms)，不建议于生成后修改
		this.setCor((int)this.GetX(), (int)this.GetY());	//生成时使坐标为整数
		ab_x = this.GetX();									//将生成时坐标记录为动画坐标
		ab_y = this.GetY();
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态
		if (alive && (lifetime_f > 0||lifetime_f==-1000)) {	//存活且剩余时间>0
			/*if(spaction!=null){
				spaction.action(this);
			}*/
			move();
			LifeMinus();	//剩余时间-每帧时间
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
				if(!getBound()){
					die(DEATHSTATE_OUTBOUND);
				}
			}
			if(debugmark){	//如果该粒子是调试状态
				Debug.DebugSimpleMessage("X:"+this.GetX()+",Y:"+this.GetY());	//每帧输出其坐标
			}
		}
		else {
			die();
		}
		if(rotatemode==2||rotatemode==3){
			double rotate = VMath.StandardizationAngle(updatecount*rotatespeed/100+Math.PI/2);	//在更新过程中计算旋转值
			this.setRotate(rotate);
		}
		updatecount++;
	}
}
