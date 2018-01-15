/**
 * 文件名称：VPointProxy.java
 * 类路径：combat
 * 描述：TODO 点接口的代理,实现其所有基础函数
 * 作者：Demilichzz
 * 时间：2012-4-18下午08:27:08
 * 版本：Ver 1.0
 */
package interfaces;

//import entities.VSpriteLaser;
//import entities.VSpriteTail;
import global.VMath;

/**
 * @author Demilichzz
 *
 */
public class VPointProxy implements VPointInterface{
	protected double x,y;
	protected double angle;			//朝向角度
	protected double rotate;		//旋转值
	protected double x1,y1,x2,y2;		//场地边缘
	protected boolean limit=false;		//是否为受限制移动模式
	protected boolean rotaterelated=true;	//旋转方向与粒子移动方向是否关联
	
	protected int CType = 0;	//碰撞类型索引
	protected double collrad = 1;	//圆形碰撞的碰撞半径，为0则不进行检测
	
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

/*	public boolean cDetection(VPointProxy target) {
		// TODO 碰撞检测
		boolean result = false;
		switch(target.getCType()){
		case 0:{		//圆形碰撞
			if(this.getCollRad()==0||target.getCollRad()==0){
				// TODO 如果有任意圆形碰撞对象半径为0，则不进行检测
				return false;
			}
			double simpd=VMath.GetSimpleMaxDistance(this,target);	//获取xy方向最大距离
			if(simpd>this.getCollRad()+target.getCollRad()){	//如果xy轴最大距离>碰撞半径之和
				result = false;	//则不会碰撞
			}
			else{	//否则执行精确计算
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
		case 1:{		//直线激光碰撞
			if(target instanceof VSpriteLaser){
				VSpriteLaser t = (VSpriteLaser) target;
				if(t.getWarningMode()){
					return false;
				}
				else{
					if(VMath.GetDistanceBetweenPointAndLine(this, t, t.angle)<t.getWidth()/2+1.5){	//计算目标与射线距离，如小于射线宽度
						double angle = VMath.GetAngleBetween2Points(t,this)-t.angle;	//获取目标与射线角度
						angle = VMath.StandardizationAngle(angle+Math.PI/2);
						if(angle>=0&&angle<=Math.PI){	//目标在射线方向180度范围内
							double disty = VMath.GetDistanceBetweenPointAndLine(this, t, t.angle+Math.PI/2);	//计算目标与射线坐标系y轴距离
							if(disty>t.getLength()){	//距离超过激光长度则未碰撞
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
					else{	//距离过远则未碰撞
						return false;
					}
				}
			}
			break;
		}
		case 2:{		//曳尾弹碰撞
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
	}*/

	public double getAngle(){
		return angle;
	}

	public double getCollRad() {
		// TODO 获取圆形碰撞半径
		return collrad;
	}

	public int getCType() {
		// TODO 获取碰撞类型
		return CType;
	}
	public double getRotate(){
		return rotate;
	}

/*	public boolean getRoundCD(double x,double y,double rad){
		// TODO 按参数获取圆形碰撞结果
		double dist = VMath.GetDistanceBetween2Points(this.GetX(),this.GetY(), x,y);
		if(dist<this.getCollRad()+rad){
			return true;
		}
		else{
			return false;
		}
	}
*/
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
		// TODO 极坐标位移
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
		// TODO 切换该点对象是否为受限移动模式，参数为限制区域
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		limit=l;
	}
	
	public void setRad(double r){
		// TODO 设置碰撞半径
		this.collrad=r;
	}

	public void setRotate(double r){
		rotate = r;
	}
	public void setRotateRelated(boolean b){
		rotaterelated=b;
	}
}
