/**
 * 	TODO ������ʱִ����Ϊ������
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
	protected boolean restrictaction=false;	//�Ƿ��ڳ�����Χ����ʱִ����Ϊ
	
	
	public VSpriteCorpse(){
		super();
	}
	public VSpriteCorpse(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public void spriteUpdate() {
		// TODO ��������״̬��ÿ�θ���״̬ʱ�����ײ��Ե
		if (alive && lifetime_f > 0) {
			move();
			LifeMinus();
			if(restricted){	//������ӷ�Χ���ޣ����鷶Χ������ָ����Χ������
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
		// TODO ������Ϊ
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
