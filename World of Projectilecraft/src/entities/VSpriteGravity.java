/**
 * 	TODO 带附加作用力的粒子子类
 */
package entities;

import global.VMath;
import interfaces.VCombatObject;
import interfaces.VPointInterface;

/**
 * @author Demilichzz
 *
 * 2012-10-22
 */
public class VSpriteGravity extends VSprite{
	protected double forceangle=0;	//作用力角度
	protected double forceaccel=0;	//作用力加速度
	protected int endtime=0;			//作用力停止时间(ms)
	
	public VSpriteGravity(){
		super();
	}
	public VSpriteGravity(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态，每次更新状态时检测碰撞边缘
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			forcemove();	//根据作用力调整角度和速度
			move();
			LifeMinus();
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
				if(!r_area.ifvalid(this.GetX(),this.GetY())){
					die();
				}
			}
		}
		else {
			die();
		}
		updatecount++;
	}
	/**
	 * 
	 */
	public void setForce(double angle,double accel,int time){
		// TODO 设置作用力参数
		forceangle=angle;
		forceaccel=accel;
		endtime=time;
	}
	private void forcemove() {
		// TODO 根据作用力调整粒子运行角度和速度
		if(lifetime*1000-lifetime_f<endtime){
			double vx=Math.sin(angle)*speed+Math.sin(forceangle)*forceaccel;
			double vy=Math.cos(angle)*speed+Math.cos(forceangle)*forceaccel;	//计算 xy向量
			double newangle=0;
			if(vy!=0){
				newangle = Math.atan(vx/vy);
			}
			else{
				if(vx>0){
					newangle=Math.PI*0.5;
				}
				else{
					newangle=Math.PI*1.5;
				}
			}
			double newspeed=Math.sqrt(vx*vx+vy*vy);
			this.setAngle(newangle);
			this.setSpeed(newspeed);
		}
	}
}
