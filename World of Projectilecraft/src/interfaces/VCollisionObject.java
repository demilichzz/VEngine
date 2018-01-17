/**
 * @author Demilichzz
 *	碰撞检测对象接口
 * 2012-12-5
 */
package interfaces;

/**
 * @author Demilichzz
 *
 * 2012-12-5
 */
public interface VCollisionObject {
	public boolean cDetection(VPointProxy target);	//返回与目标相碰撞的结果，返回的是两个对象分别检测对方的结果求或
	public int getCType();		//获取碰撞类型：0-圆形,1-激光,2-曳尾弹
}
