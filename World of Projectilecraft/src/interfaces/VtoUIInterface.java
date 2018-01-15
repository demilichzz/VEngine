/**
 * 文件名称：VtoUIInterface.java
 * 类路径：interfaces
 * 描述：TODO UI转化接口,实现该接口的类可以按自身属性将其转化为一个VUI类
 * 作者：Demilichzz
 * 时间：2012-3-17上午08:46:45
 * 版本：Ver 1.0
 */
package interfaces;
import ui.VUI;

/**
 * @author Demilichzz
 *
 */
public interface VtoUIInterface {
	public VUI toUI(String id,int index);	//转化UI函数
	//应用此接口将类转化为UI形式通常只用于某些提示文字或UI需要小的附加元素显示时使用
	//而非用于生成功能复杂的大型UI面板
}
