/**
 * 文件名称：LuaScript.java
 * 类路径：script
 * 描述：TODO 用于加载及运行Lua脚本的类
 * 作者：Demilichzz
 * 时间：2012-3-3上午05:25:31
 * 版本：Ver 1.0
 */
package script;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * @author Demilichzz
 *
 */
public class LuaScript {
	LuaState L;
	public int callcount = 1;

	public LuaScript(final String filename) {
		this.L = LuaStateFactory.newLuaState();
		this.L.openLibs();
		this.L.openMath();
		this.L.LdoFile(filename);
	}	
	public void closeScript() {
		// TODO 关闭Lua脚本
		this.callcount--;
		if(this.callcount<=0){
			this.L.close();
			callcount = 0;
		}
	}
	public void destroyScript(){
		// TODO 强制关闭Lua脚本
		this.callcount=0;
		this.L.close();
	}
	public void finalize(){
		// TODO 释放资源时强制关闭此Lua脚本
		destroyScript();
	}
	public void runScriptFunction(String functionName, Object obj) {
		// TODO 运行无返回值得Lua函数
		this.L.getGlobal(functionName);		//按名字获取Lua函数
		this.L.pushJavaObject(obj);			//将JavaObject进行压栈
		this.L.call(1, 0); // 1个参数,0个返回值,以之前压栈的Object作为参数进行调用获取的Lua函数
	}
	public void runScriptFunction(String functionName,int i){
		this.L.getGlobal(functionName);		//按名字获取Lua函数
		this.L.pushInteger(i);	//将JavaObject进行压栈
		this.L.call(1, 0); // 1个参数,0个返回值,以之前压栈的Object作为参数进行调用获取的Lua函数
	}
	public Object runScriptFunctionB(String functionName,Object obj){
		// TODO 运行返回Java对象的指定Lua函数
		L.getField(LuaState.LUA_GLOBALSINDEX, functionName);		//按名字获取Lua函数
		this.L.pushJavaObject(obj);			//将JavaObject进行压栈
		this.L.call(1, 1);	//1个参数,1个返回值
		L.setField(LuaState.LUA_GLOBALSINDEX, "b");
        LuaObject l = L.getLuaObject("b");
        try {
			return l.getObject();
		} catch (LuaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
