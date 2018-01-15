/**
 * 文件名称：VPointInterface.java
 * 类路径：entities
 * 描述：TODO 定义点的接口
 * 作者：Demilichzz
 * 时间：2012-4-16下午03:12:16
 * 版本：Ver 1.0
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