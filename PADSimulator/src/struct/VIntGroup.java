/**
 * 	整型数据集合类，用于选择宝石移动的方向时判断方向可靠性。
 */
package struct;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VIntGroup {
	protected ArrayList<Integer> intgroup;
	
	public VIntGroup(){
		intgroup = new ArrayList<Integer>();
	}
	public VIntGroup(int[]group){
		intgroup = new ArrayList<Integer>();
		setGroup(group);
	}
	public void setGroup(int[]group){
		// TODO 将集合设置为给定的数组参数
		for(int i=0;i<group.length;i++){
			addContent(group[i]);		//以去除重复的形式向集合添加数据
		}
	}
	public ArrayList<Integer> getGroup(){
		return intgroup;
	}
	public void addContent(int newint){
		// TODO 向集合添加非重复的整型数据
		boolean exist = false;
		for(int i:intgroup){
			if(i==newint){
				exist = true;
				break;
			}
		}
		if(!exist){
			intgroup.add(newint);
		}
	}
	public void removeContent(int newint){
		// TODO 从集合移除指定数据
		intgroup.remove((Object)newint);
	}
}
