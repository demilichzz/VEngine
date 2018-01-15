/**
 * 	TODO 在死亡时执行行为的粒子
 */
package entities;

import timer.VLuaAction;
import timer.VLuaSPAction;
import interfaces.VCombatObject;

/**
 * @author Demilichzz
 *
 * 2012-10-26
 */
public class VSpriteCorpse extends VSprite{
	protected boolean restrictaction=false;	//是否在超出范围死亡时执行行为
	
	
	public VSpriteCorpse(){
		super();
	}
	public VSpriteCorpse(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态，每次更新状态时检测碰撞边缘
		if (alive && lifetime_f > 0) {
			move();
			LifeMinus();
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
				if(!r_area.ifvalid(this.GetX(),this.GetY())){
					if(!restrictaction){
						this.addAction(null);
					}
					die();
				}
			}
		}
		else {
			die();
		}
		updatecount++;
	}
	public void addAction(VLuaSPAction action){
		// TODO 设置行为
		//super.addAction(action);
	}
	public void setRAction(boolean b){
		restrictaction=b;
	}
	public void die(){
		if(spaction!=null){
			spaction.action(this);
		}
		super.die();
	}
}
