/**
 * 	TODO ׷������
 */
package entities;

import global.VMath;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class VSpriteTrace extends VSprite{
	protected VCombatUnit target; 
	
	public VSpriteTrace(){
		super();
	}
	public VSpriteTrace(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public void setTarget(VCombatUnit t){
		target=t;
	}
	public void spriteUpdate() {
		// TODO ��������״̬
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			if(target!=null&&target.alive){
				double ta = VMath.GetAngleBetween2Points(this, target);
				setAngle(ta);
			}
			move();
			LifeMinus();
			if(restricted){	//������ӷ�Χ���ޣ����鷶Χ������ָ����Χ������
				if(!getBound()){
					die();
				}
			}
		}
		else {
			die();
		}
	}
	
}
