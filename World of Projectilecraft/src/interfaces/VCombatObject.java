/**
 * 文件名称：VCombatObject.java
 * 类路径：interfaces
 * 描述：TODO 战斗单位对象接口,实现此接口的战斗单位实体可通过getImage()函数获取其对应的战斗界面图形,并在战斗面板UI上显示
 * 作者：Demilichzz
 * 时间：2012-4-19下午12:16:50
 * 版本：Ver 1.0
 */
package interfaces;

import view.*;

/**
 * @author Demilichzz
 *
 */
public interface VCombatObject {
	public VImageInterface getImageByLayer(int layer);
}
