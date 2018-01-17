/**
 * 文件名称：SpriteFactory.java
 * 类路径：factory
 * 描述：TODO 粒子工厂，用于在工厂中设置粒子相关参数以便于大量创建性质相似的弹幕粒子
 * 作者：Demilichzz
 * 时间：2012-8-7下午08:17:27
 * 版本：Ver 1.0
 */
package factory;
import interfaces.VCombatObject;
import view.VImageInterface;
import entities.*;
import data.*;
/**
 * @author Demilichzz
 *
 */
public class SpriteFactory {
	public final static int MODE_DEFAULT = 0;
	public final static int MODE_EX = 1;
	public final static int MODE_GRAVITY = 2;
	public final static int MODE_CORPSE = 3;
	public final static int MODE_TRACE = 4;
	public final static int MODE_LASER = 5;
	public final static int MODE_TAIL = 6;
	
	//-------------工厂类的静态参数----------------------------------------------
	protected static String image_top="";	//上层图像
	protected static int t_index=0,b_index=0;
	protected static String image_bottom="";	//下层图像
	protected static double lifetime=0;		//生命周期，单位秒
	protected static int lifetime_f=0;	//生命周期，单位毫秒
	protected static double c_rad = 3;		//碰撞半径
	protected static double angle = Math.PI * 0;	//角度,默认0度为正上方
	protected static double speed = 0;
	protected static VCombatObject owner=null;	//所有者
	protected static double x=0,y=0;
	protected static boolean restricted=false;
	protected static boolean ifrotate=true;
	protected static boolean invincible=false;
	protected static boolean mode_reflect=false;	//是否为边缘反弹类型扩展粒子
	protected static int spritemode=0;	//粒子类型
	protected static double damage=1;
	private static int warningtime;
	private static double width=5;
	private static double length=400;
	
	public static SpriteFactoryInstance getInstance(){
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
		sf.owner=owner;
		sf.x=x;
		sf.y=y;
		sf.restricted=restricted;
		sf.ifrotate=ifrotate;
		sf.invincible=invincible;
		sf.mode_reflect=mode_reflect;
		sf.spritemode=spritemode;
		return sf;
	}
	
	public static VSprite creator(){
		// TODO 按工厂类中定义的参数自动创建一个粒子
		VSprite newsp;
		switch(spritemode){
		case MODE_DEFAULT:{
			newsp = new VSprite(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case MODE_EX:{
			newsp = new VSpriteReflect(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case MODE_GRAVITY:{
			newsp = new VSpriteGravity(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case MODE_CORPSE:{
			newsp = new VSpriteCorpse(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case MODE_TRACE:{
			newsp = new VSpriteTrace(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		case MODE_LASER:{
			newsp = new VSpriteLaser(warningtime,width,length,t_index,b_index,lifetime,angle,owner);
			break;
		}
		default:{
			newsp = new VSprite(image_top,image_bottom,t_index,b_index,lifetime,c_rad,angle,owner);
			break;
		}
		}
		newsp.setSpeed(speed);
		newsp.setCor(x, y);
		newsp.setIfRotate(ifrotate);
		newsp.setRestricted(restricted);
		newsp.setInvincible(invincible);
		newsp.setDamage(damage);
		return newsp;
	}
	public static void resetFactoryState(){
		// TODO 重置工厂中所有粒子参数的状态
		image_top = "";
		image_bottom = "";
		t_index = 0;
		b_index = 0;
		lifetime = 0;
		lifetime_f = 0;
		c_rad = 1;
		angle = Math.PI*0;
		speed = 0;
		owner = null;
		x=0;
		y=0;
		restricted=true;
		ifrotate=true;
	}
	public static void setDefaultState(){
		image_top = "Sprite_A_top.png";
		image_bottom = "Sprite_A_bottom.png";
		t_index = 3;
		b_index = 3;
		lifetime = 10;
		lifetime_f = 0;
		c_rad = 3;
		angle = Math.PI*0;
		speed = 100;
		owner = null;
		x=200;
		y=100;
		restricted=true;
		ifrotate=true;
	}
	public static void setImage(String top,String bottom){
		// TODO 设置工厂参数中的图像路径
		image_top = top;
		image_bottom = bottom;
	}
	public static void setImageIndex(int top,int bottom){
		// TODO 设置图像索引
		t_index = top;
		b_index = bottom;
	}
	public static void setLife(double l){
		lifetime = l;
	}
	public static void setRad(double r){
		c_rad = r;
	}
	public static void setAngle(double a){
		angle = a;
	}
	public static void setSpeed(double s){
		speed = s;
	}
	public static void setOwner(VCombatObject o){
		owner = o;
	}
	public static void setLoc(double x,double y){
		SpriteFactory.x = x;
		SpriteFactory.y = y;
	}
	public static void setIfRotate(boolean r){
		ifrotate = r;
	}
	public static void setRestricted(boolean b){
		restricted = b;
	}
	public static void setMode_Reflect(boolean b){
		mode_reflect=b;
	}
	public static void setInvincible(boolean b){
		invincible=b;
	}
	public static void setMode(int i){
		// TODO 设置生成粒子的模式，即子类类型
		spritemode=i;
	}
	public static void setDamage(double d){
		// TODO 设置伤害值
		damage = d;
	}
}
