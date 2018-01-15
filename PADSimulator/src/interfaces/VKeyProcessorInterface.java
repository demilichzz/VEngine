/**
 * 文件名称：VKeyProcessorInterface.java
 * 类路径：interfaces
 * 描述：TODO 按键行为处理器接口，实现接口中的按键响应函数以为对象赋予在GS更新时进行按键响应功能
 * 作者：Demilichzz
 * 时间：2012-8-7上午02:04:48
 * 版本：Ver 1.0
 */
package interfaces;

/**
 * @author Demilichzz
 *
 */
public interface VKeyProcessorInterface {
	public void processKeyAction(int[] keystate);
}
