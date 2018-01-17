/**
 * 	TODO ����������������������
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
	protected double forceangle=0;	//�������Ƕ�
	protected double forceaccel=0;	//���������ٶ�
	protected int endtime=0;			//������ֹͣʱ��(ms)
	
	public VSpriteGravity(){
		super();
	}
	public VSpriteGravity(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
	}
	public void spriteUpdate() {
		// TODO ��������״̬��ÿ�θ���״̬ʱ�����ײ��Ե
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			forcemove();	//���������������ǶȺ��ٶ�
			move();
			LifeMinus();
			if(restricted){	//������ӷ�Χ���ޣ����鷶Χ������ָ����Χ������
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
		// TODO ��������������
		forceangle=angle;
		forceaccel=accel;
		endtime=time;
	}
	private void forcemove() {
		// TODO ���������������������нǶȺ��ٶ�
		if(lifetime*1000-lifetime_f<endtime){
			double vx=Math.sin(angle)*speed+Math.sin(forceangle)*forceaccel;
			double vy=Math.cos(angle)*speed+Math.cos(forceangle)*forceaccel;	//���� xy����
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
