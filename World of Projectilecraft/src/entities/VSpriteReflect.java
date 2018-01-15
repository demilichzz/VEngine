/**
 * 	TODO 触边反弹粒子
 */
package entities;

import data.GameData;
import global.Imageconst;
import global.VMath;
import interfaces.VCombatObject;
import interfaces.VPointInterface;
import interfaces.VPointProxy;

/**
 * @author Demilichzz
 *
 * 2012-10-15
 */
public class VSpriteReflect extends VSprite{
	protected int refmode = 11;	//二进制法高到低位表示上下左右壁是否反弹,取值0-15，默认下不反弹
	protected int reftime = -1;	//反弹次数
	
	public VSpriteReflect(){
		super();
	}
	public VSpriteReflect(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public VSpriteReflect(VSprite newsp) {
		// TODO 使用基础粒子类的所有参数构造扩展类
		this.image_top = newsp.image_top;
		this.image_bottom = newsp.image_bottom;
		this.t_index = newsp.t_index;
		this.b_index = newsp.b_index;
		this.lifetime = newsp.lifetime;
		this.setAngle(newsp.getAngle());
		this.owner = newsp.owner;
		GameData.spriteList.add(this);
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态，每次更新状态时检测碰撞边缘
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			if((x<5||y<5||x>395||y>595)&&reftime!=0){
				VPointInterface vtar=VMath.PolarMove(this,angle, speed/100);	//计算下次移动的目标位置
				double xt=vtar.GetX();
				double yt=vtar.GetY();
				int ref_up = refmode/8;
				int ref_down = (refmode%8)/4;
				int ref_left = (refmode%4)/2;
				int ref_right = refmode%2;
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
	protected void reflect(double angle){
		// TODO 反弹设置角度，同时计数
		if(reftime!=0){
			this.setAngle(angle);
		}
		if(reftime>0){
			reftime--;
		}
	}
	public void setReflectMode(int i){
		// TODO 设置反射模式
		refmode = i;
	}
	public void setReflectTime(int i){
		// TODO 设置反射次数，-1为无限
		reftime = i;
	}
}
