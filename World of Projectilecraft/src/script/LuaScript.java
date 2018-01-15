/**
 * �ļ����ƣ�LuaScript.java
 * ��·����script
 * ������TODO ���ڼ��ؼ�����Lua�ű�����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-3����05:25:31
 * �汾��Ver 1.0
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
		// TODO �ر�Lua�ű�
		this.callcount--;
		if(this.callcount<=0){
			this.L.close();
			callcount = 0;
		}
	}
	public void destroyScript(){
		// TODO ǿ�ƹر�Lua�ű�
		this.callcount=0;
		this.L.close();
	}
	public void finalize(){
		// TODO �ͷ���Դʱǿ�ƹرմ�Lua�ű�
		destroyScript();
	}
	public void runScriptFunction(String functionName, Object obj) {
		// TODO �����޷���ֵ��Lua����
		this.L.getGlobal(functionName);		//�����ֻ�ȡLua����
		this.L.pushJavaObject(obj);			//��JavaObject����ѹջ
		this.L.call(1, 0); // 1������,0������ֵ,��֮ǰѹջ��Object��Ϊ�������е��û�ȡ��Lua����
	}
	public void runScriptFunction(String functionName,int i){
		this.L.getGlobal(functionName);		//�����ֻ�ȡLua����
		this.L.pushInteger(i);	//��JavaObject����ѹջ
		this.L.call(1, 0); // 1������,0������ֵ,��֮ǰѹջ��Object��Ϊ�������е��û�ȡ��Lua����
	}
	public Object runScriptFunctionB(String functionName,Object obj){
		// TODO ���з���Java�����ָ��Lua����
		L.getField(LuaState.LUA_GLOBALSINDEX, functionName);		//�����ֻ�ȡLua����
		this.L.pushJavaObject(obj);			//��JavaObject����ѹջ
		this.L.call(1, 1);	//1������,1������ֵ
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
