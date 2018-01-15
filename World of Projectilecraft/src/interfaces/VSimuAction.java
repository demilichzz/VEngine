/**
 * 文件名称：VSimuAction.java
 * 类路径：interfaces
 * 描述：TODO 通过仿真器控制GS更新时执行的行为接口，当实体有需要随时间进行的行为时实现此接口，并在相应Stage更新时调用接口的行为函数
 * 作者：Demilichzz
 * 时间：2012-4-16下午12:23:22
 * 版本：Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public interface VSimuAction {
	public void simuAction();	//GS(Stage)更新时执行的行为
}
