/**
 * 文件名称：LuaDataProcess.java
 * 类路径：global
 * 描述：TODO	Lua数据交换通用类，用于向Lua脚本传递数组等数据结构的参数
 * 作者：dell
 * 时间：20152015-9-23上午2:15:28
 * 版本：Ver 1.0
 */
package global;

/**
 * @author Demilichzz
 *
 */
public class LuaDataProcess {
	public static Object getObjectFromArray(Object[]target,int index){
		if(index<target.length){
			return target[index];
		}
		else{
			return null;
		}
	}
}
