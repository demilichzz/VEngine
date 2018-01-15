/**
 * 	TODO SpriteFactory静态工厂的实例
 */
package factory;

import entities.*;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2012-10-26
 */
public class SpriteFactoryInstance {
	protected String image_top="";	//上层图像
	protected int t_index=0,b_index=0;
	protected String image_bottom="";	//下层图像
	protected double lifetime=0;		//生命周期，单位秒
	protected int lifetime_f=0;	//生命周期，单位毫秒
	protected double c_rad = 1;		//碰撞半径
	protected double angle = Math.PI * 0;	//角度,默认0度为正上方
	protected double speed = 0;
	protected double anglespeed = 0;
	protected VCombatObject owner=null;	//所有者
	protected double x=0,y=0;
	protected boolean restricted=false;
	protected boolean ifrotate=true;
	protected int rotatemode = 1;		//旋转模式
	protected double rotatespeed = Math.PI;	//转速
	protected boolean invincible=false;
	protected int spritemode=0;	//粒子类型
	protected double damage = 1;
	protected int warningtime = 1000;
	protected double width = 5;
	protected double length = 400;	
	//----------扩展粒子参数-------------------------------
	protected boolean mode_reflect=false;	//是否为边缘反弹类型扩展粒子
	protected boolean restrictaction=false;	//尸爆粒子越界死亡时执行行为
	protected int refmode = 0;		//反弹模式
	protected int reftime = 0;		//反弹次数
	protected int deathactionstate=0;		//死亡行为状态
	
	public SpriteFactoryInstance(){
		// TODO 定义工厂实例的构造函数
	}
	
	public SpriteFactoryInstance getInstance(){
		// TODO 获取一个复制全部工厂中定义的参数的工厂副本
		SpriteFactoryInstance sf = new SpriteFactoryInstance();
		sf.image_top = image_top;
		sf.t_index=t_index;
		sf.b_index=b_index;
		sf.image_bottom = image_bottom;
		sf.lifetime=lifetime;
		sf.lifetime_f=lifetime_f;
		sf.c_rad=c_rad;
		sf.angle=angle;
		sf.speed=speed;
		sf.anglespeed=anglespeed;
		sf.owner=owner;
		sf.x=x;
		sf.y=y;
		sf.restricted=restricted;
		sf.ifrotate=ifrotate;
		sf.invincible=invincible;
		sf.mode_reflect=mode_reflect;
		sf.spritemode=spritemode;
		sf.warningtime=warningtime;
		sf.width=width;
		sf.rotatemode=rotatemode;
		sf.refmode=refmode;
		sf.rotatespeed=rotatespeed;
		sf.reftime = reftime;
		sf.deathactionstate = deathactionstate;
		return sf;
	}
	public void setBaseSpriteParameter(){
		// TODO 设置为基础粒子参数，无附加额外行为
		mode_reflect=false;
		restrictaction=false;
		refmode = 0;
		reftime = 0;
		deathactionstate=0;
	}
	
	public VSprite creator(){
		// TODO 按工厂类中定义的参数自动创建一个粒子
		VSprite newsp;
		switch(spritemode){
		case SpriteFactory.MODE_DEFAULT:{
			newsp = new VSprite(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case SpriteFactory.MODE_EX:{
			VSpriteEx newsp_r = new VSpriteEx(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			newsp_r.setReflectMode(refmode);
			newsp_r.setReflectTime(reftime);
			newsp=newsp_r;
			break;
		}
		case SpriteFactory.MODE_GRAVITY:{
			newsp = new VSpriteEx(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case SpriteFactory.MODE_CORPSE:{
			VSpriteEx newsp_c = new VSpriteEx(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			if(restrictaction){
				newsp_c.setDeathActionState(9);	//时间结束或超范围
			}
			else{
				newsp_c.setDeathActionState(1);	//仅当时间结束执行死亡事件
			}
			newsp=newsp_c;
			break;
		}
		case SpriteFactory.MODE_TRACE:{
			newsp = new VSpriteTrace(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case SpriteFactory.MODE_LASER:{
			newsp = new VSpriteLaser(warningtime,width,length,t_index,b_index,lifetime,angle,owner); 
			newsp.setSpeed(speed);
			newsp.setASpeed(anglespeed);
			newsp.setCor(x, y);
			newsp.setDamage(damage);
			newsp.setRestricted(restricted);
			newsp.setInvincible(true);
			return(newsp);
		}
		case SpriteFactory.MODE_TAIL:{
			VSpriteTail newsp_t = new VSpriteTail(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			newsp_t.setOverallCor(x, y);
			newsp = newsp_t;
			break;
		}
		default:{
			newsp = new VSprite(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		}
		newsp.setSpeed(speed);
		newsp.setASpeed(anglespeed);
		newsp.setRotateSpeed(rotatespeed);
		newsp.setCor(x, y);
		newsp.setRotateMode(rotatemode);
		newsp.setRestricted(restricted);
		newsp.setInvincible(invincible);
		newsp.setDamage(damage);
		return newsp;
	}
	
	public void setImage(String top,String bottom){
		// TODO 设置工厂参数中的图像路径
		image_top = top;
		image_bottom = bottom;
	}
	public void setImageIndex(int top,int bottom){
		// TODO 设置图像索引
		t_index = top;
		b_index = bottom;
	}
	public void setLife(double l){
		lifetime = l;
	}
	public void setRad(double r){
		c_rad = r;
	}
	public void setAngle(double a){
		angle = a;
	}
	public void setSpeed(double s){
		speed = s;
	}
	public void setASpeed(double a){
		anglespeed = a;
	}
	public void setOwner(VCombatObject o){
		owner = o;
	}
	public void setLoc(double x,double y){
		this.x = x;
		this.y = y;
	}
	public void setIfRotate(boolean r){
		ifrotate = r;
		if(r){
			rotatemode=1;
		}
		else{
			rotatemode=0;
		}
	}
	public void setRotateMode(int rm){
		rotatemode = rm;
	}
	public void setRestricted(boolean b){
		restricted = b;
	}
	public void setInvincible(boolean b){
		invincible=b;
	}
	public void setMode(int i){
		// TODO 设置生成粒子的模式，即子类类型
		spritemode=i;
	}
	public void setDamage(double d){
		damage = d;
	}
	public void setWarningTime(int tm){
		// TODO 设置激光预警线时间
		warningtime = tm;
	}
	public void setWidth(double w){
		width = w;
	}
	public void setLength(double l){
		length = l;
	}
	public void setRotateSpeed(double r){
		// TODO 自旋粒子转速
		rotatespeed = r;
	}
	public void setMode_Reflect(boolean b){
		mode_reflect=b;
	}
	public void setRAction(boolean b){
		restrictaction=b;
	}
	public void setReflectMode(int i){
		refmode=i;
	}
	public void setReflectTime(int i){
		reftime = i;
	}
	public void setDeathActionState(int i){
		deathactionstate = i;
	}
}
