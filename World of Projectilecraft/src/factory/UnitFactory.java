/**
 * 	TODO 用于生成战斗单位的工厂静态类
 */
package factory;

import entities.*;

/**
 * @author Demilichzz
 *
 * 2012-10-31
 */
public class UnitFactory {
	protected static String image="";
	protected static int life=100;
	protected static double lifetime=-1;
	protected static double rad=4;
	protected static boolean invincible=false;
	protected static boolean ifcolld=true;

	public static VCombatUnit creator(){
		VCombatUnit u=null;
		u=new VCombatUnit(image,life);
		u.setLifeTime(lifetime);
		u.setRad(rad);
		u.setInvincible(invincible);
		u.setIfCollD(ifcolld);
		return u;
	}
	public static void resetFactoryState(){
		// TODO 重置工厂内各项参数状态
		image="";
		life=100;
		lifetime=-1;
		rad=4;
		invincible=false;
		ifcolld=true;
	}
	public static void setImage(String str){
		image=str;
	}
	public static void setLife(int l){
		life=l;
	}
	public static void setLifeTime(int lt){
		lifetime=lt;
	}
	public static void setRad(double r){
		rad=r;
	}
	public static void setInvincible(boolean b){
		invincible=b;
	}
	public static void setIfCollD(boolean b){
		ifcolld=b;
	}
}
