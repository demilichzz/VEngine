/**
 * 文件名称：VActiveSkill.java
 * 类路径：entities
 * 描述：TODO  主动技能类
 * 作者：dell
 * 时间：20152015-9-15下午12:12:45
 * 版本：Ver 1.0
 */
package entities;

import data.GameData;
import system.VEngine;
import timer.VLuaAction;
import timer.VLuaObjectAction;

/**
 * @author Demilichzz
 *
 */
public class VActiveSkill {
	protected VCharacter vchar;
	protected VLuaObjectAction action;
	public VActiveSkill(VCharacter vchar){
		this.vchar = vchar;
	}
	public void setCharacter(VCharacter vchar){
		this.vchar = vchar;
	}
	public void action(){
		// TODO 执行技能定义的行为，用含对象的Lua行为接口传入发动技能的角色对象
		if(action!=null){
			action.action(vchar);
		}
	}
	public void initSkill() {
		// TODO 调用Lua脚本中的初始化技能函数
		VEngine.newgame.gs.lua_core.runScriptFunction("initSkill",this);
	}
	public void addAction(VLuaObjectAction a){
		this.action = a;
	}
	public VCharacter getCharacter(){
		return vchar;
	}
}
