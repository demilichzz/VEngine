/**
 * 文件名称：VLuaAction.java
 * 类路径：timer
 * 描述：TODO Lua行为接口，通过createProxy在Lua脚本中实现接口
 * 作者：Demilichzz
 * 时间：2012-3-3上午05:51:09
 * 版本：Ver 1.0
 */
package timer;

/**
 * @author Demilichzz
 *
 */
public interface VLuaAction {
	public void action();	//脚本行为函数,在Lua中创建代理以实现需要执行的行为
}
