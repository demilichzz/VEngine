/**
 * @author Demilichzz
 *	UI关联接口，应用于有相应UI关联的游戏对象类
 * 2013-11-12
 */
package interfaces;

import ui.*;

/**
 * @author Demilichzz
 *
 * 2013-11-12
 */
public interface VUIBindingInterface {
	public void uiBindUpdate();		//绑定UI根据对象状态进行更新
	public void bindUI(VUI ui);	//绑定指定UI
}
