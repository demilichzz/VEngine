/**
 * �ļ����ƣ�VPointInterface.java
 * ��·����entities
 * ������TODO �����Ľӿ�
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-4-16����03:12:16
 * �汾��Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public interface VPointInterface {	
	public double GetX();
	public double GetY();
	public void setCor(double x,double y);
	public void moveCor(double x,double y);
	public void PolarMove(double angle,double d);
	public void setAngle(double d);
	public void addAngle(double d);
}