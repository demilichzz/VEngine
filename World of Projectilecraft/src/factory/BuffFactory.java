/**
 * 	TODO Buff工厂，用于使用指定参数创建Buff效果
 */
package factory;

import java.util.*;

import combat.*;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class BuffFactory {
	protected static HashMap<String,int[]> valuemap=new HashMap<String,int[]>();	//以BuffID为键的Buff参数值Hashmap
	
	public static VBuff creator(String ID){
		// TODO 通过ID创建指定Buff
		int[]value=valuemap.get(ID);
		if(value!=null){
			VBuff b=new VBuff(ID,value[0]);		//参数0为持续时间
			b.setMaxStack(value[1]);			//参数1为最大堆叠次数
			b.dr=value[2];						//参数2为伤害增加百分比
			return b;
		}
		else{
			return null;
		}
	}

	/**
	 * 
	 */
	public static void initFactory() {
		// TODO 初始化Buff工厂
		valuemap=new HashMap<String,int[]>();
		valuemap.put("arcaneblast",new int[]{1,4,15});	//奥术
		valuemap.put("wrath", new int[]{-1,99,15});		//汇聚之怒
		valuemap.put("weakness", new int[]{6,1,100});	//哈格拉虚弱
		valuemap.put("invincible", new int[]{-1,1,-100});	//无敌
	}
}
