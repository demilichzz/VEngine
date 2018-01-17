/**
 * �ļ����ƣ�VPointProxy.java
 * ��·����combat
 * ������TODO ��ӿڵĴ���,ʵ�������л�������
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-4-18����08:27:08
 * �汾��Ver 1.0
 */
package interfaces;

import entities.VSpriteLaser;
import entities.VSpriteTail;
import global.VMath;

/**
 * @author Demilichzz
 *
 */
public class VPointProxy implements VPointInterface,VCollisionObject{
	protected double x,y;
	protected double angle;			//����Ƕ�
	protected double rotate;		//��תֵ
	protected double x1,y1,x2,y2;		//���ر�Ե
	protected boolean limit=false;		//�Ƿ�Ϊ�������ƶ�ģʽ
	protected boolean rotaterelated=true;	//��ת�����������ƶ������Ƿ����
	
	protected int CType = 0;	//��ײ��������
	protected double collrad = 1;	//Բ����ײ����ײ�뾶��Ϊ0�򲻽��м��
	
	public VPointProxy(){
		setCor(0,0);
		angle=0;
	}
	public VPointProxy(double x,double y){
		setCor(x,y);
		angle=0;
	}
	public void addAngle(double a) {
		// TODO Auto-generated method stub
		this.setAngle(this.angle+a);
	}

	public boolean cDetection(VPointProxy target) {
		// TODO ��ײ���
		boolean result = false;
		switch(target.getCType()){
		case 0:{		//Բ����ײ
			if(this.getCollRad()==0||target.getCollRad()==0){
				// TODO ���������Բ����ײ����뾶Ϊ0���򲻽��м��
				return false;
			}
			double simpd=VMath.GetSimpleMaxDistance(this,target);	//��ȡxy����������
			if(simpd>this.getCollRad()+target.getCollRad()){	//���xy��������>��ײ�뾶֮��
				result = false;	//�򲻻���ײ
			}
			else{	//����ִ�о�ȷ����
				double dist = VMath.GetDistanceBetween2Points(this, target);
				if(dist<this.getCollRad()+target.getCollRad()){
					result = true;
				}
				else{
					result = false;
				}
			}
			break;
		}
		case 1:{		//ֱ�߼�����ײ
			if(target instanceof VSpriteLaser){
				VSpriteLaser t = (VSpriteLaser) target;
				if(t.getWarningMode()){
					return false;
				}
				else{
					if(VMath.GetDistanceBetweenPointAndLine(this, t, t.angle)<t.getWidth()/2+1.5){	//����Ŀ�������߾��룬��С�����߿��
						double angle = VMath.GetAngleBetween2Points(t,this)-t.angle;	//��ȡĿ�������߽Ƕ�
						angle = VMath.StandardizationAngle(angle+Math.PI/2);
						if(angle>=0&&angle<=Math.PI){	//Ŀ�������߷���180�ȷ�Χ��
							double disty = VMath.GetDistanceBetweenPointAndLine(this, t, t.angle+Math.PI/2);	//����Ŀ������������ϵy�����
							if(disty>t.getLength()){	//���볬�����ⳤ����δ��ײ
								return false;
							}
							else{
								return true;
							}
						}
						else{
							return false;
						}
					}
					else{	//�����Զ��δ��ײ
						return false;
					}
				}
			}
			break;
		}
		case 2:{		//ҷβ����ײ
			if(target instanceof VSpriteTail){
				VSpriteTail t = (VSpriteTail)target;
				result = false;
				result = this.cDetection(new VPointProxy(t.x,t.y));
				double[]xlist = t.getTail(0);
				double[]ylist = t.getTail(1);
				for(int i=0;i<xlist.length;i++){
					if(getRoundCD(xlist[i],ylist[i],t.getCollRad())){
						return true;
					}
				}
				return result;
			}
			break;
		}
		}
		boolean col=false;
		if(!result){
			col = result&&target.cDetection(this);
		}
		else{
			col = result;
		}
		return col;
	}

	public double getAngle(){
		return angle;
	}

	public double getCollRad() {
		// TODO ��ȡԲ����ײ�뾶
		return collrad;
	}

	public int getCType() {
		// TODO ��ȡ��ײ����
		return CType;
	}
	public double getRotate(){
		return rotate;
	}

	public boolean getRoundCD(double x,double y,double rad){
		// TODO ��������ȡԲ����ײ���
		double dist = VMath.GetDistanceBetween2Points(this.GetX(),this.GetY(), x,y);
		if(dist<this.getCollRad()+rad){
			return true;
		}
		else{
			return false;
		}
	}

	public double GetX() {
		// TODO Auto-generated method stub
		return x;
	}
	public double GetY() {
		// TODO Auto-generated method stub
		return y;
	}
	public void moveCor(double x, double y) {
		// TODO Auto-generated method stub
		this.x = this.x + x;
		this.y = this.y + y;
		if(limit){
			if(this.x>x2){
				this.x=x2;
			}
			if(this.x<x1){
				this.x=x1;
			}
			if(this.y>y2){
				this.y=y2;
			}
			if(this.y<y1){
				this.y=y1;
			}
		}
	}
	public void PolarMove(double angle, double d) {
		// TODO ������λ��
		double x_move = d * Math.cos(angle);
		double y_move = d * Math.sin(angle);
		moveCor(x_move, y_move);		
	}
	public void setAngle(double a) {
		// TODO Auto-generated method stub
		this.angle = VMath.StandardizationAngle(a);
		rotate = angle + Math.PI/2;
		/*int temp = (int) ((angle + Math.PI/16)/(Math.PI/8));
		if(rotaterelated=true){
			rotate = temp*Math.PI/8+Math.PI/2;
		}*/
	}

	public void setCor(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public void setLimit(double x1,double y1,double x2,double y2,boolean l){
		// TODO �л��õ�����Ƿ�Ϊ�����ƶ�ģʽ������Ϊ��������
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		limit=l;
	}
	
	public void setRad(double r){
		// TODO ������ײ�뾶
		this.collrad=r;
	}

	public void setRotate(double r){
		rotate = r;
	}
	public void setRotateRelated(boolean b){
		rotaterelated=b;
	}
}
