/**
 * 文件名称：VCollisionInterface.java
 * 类路径：interfaces
 * 描述：TODO 碰撞模型接口，实现该接口的碰撞检测函数以对两个碰撞模型对象进行碰撞检测
 * 作者：Demilichzz
 * 时间：2012-8-7下午09:58:25
 * 版本：Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public interface VCollisionInterface {
	public boolean cDetection(VCollisionInterface target);
	public int getCType();	//获取碰撞模型类型
	public double getCollRad();	//获取圆形碰撞的半径
}
