/**
 * 文件名称：VLuaSPAction.java
 * 类路径：timer
 * 描述：TODO 粒子专用行为接口，行为函数含粒子自身索引作为参数
 * 作者：Demilichzz
 * 时间：2012-8-10上午02:57:06
 * 版本：Ver 1.0
 */
package timer;

import interfaces.VPointProxy;
import entities.VSprite;

/**
 * @author Demilichzz
 *
 */
public interface VLuaSPAction {
	public void action(VPointProxy sp);
}
