/**
 * 文件名称：VStringNode.java
 * 类路径：xml
 * 描述：TODO 字符串节点类,用于实现从同一类型实体获取单个String或一个String数组的数据结构
 * 			  自顶向下单向树状结构
 * 作者：Demilichzz
 * 时间：2012-4-21上午08:58:09
 * 版本：Ver 1.0
 */
package xml;

import java.util.ArrayList;

/**
 * @author Demilichzz
 *
 */
public class VStringNode {
	protected String nodename;
	protected boolean isAttribute;

	protected ArrayList<VStringNode> children;	// 子节点
		
	public VStringNode(String name){
		nodename = name;
		isAttribute = false;
		children=new ArrayList<VStringNode>();
	}
	public void addChildren(ArrayList<VStringNode> children){
		// TODO 添加子节点列表
		this.children.addAll(children);
	}
	public void addChildren(String[] children){
		// TODO 通过字符串数组添加子节点
		for(int i=0;i<children.length;i++){
			VStringNode n = new VStringNode(children[i]);
			addChild(n);
		}
	}
	public void addChild(VStringNode n){
		// TODO 添加单个子节点
		if(n!=null){
			this.children.add(n);
		}
	}
	public void addChild(String str){
		// TODO 通过字符串添加单个节点
		VStringNode n = new VStringNode(str);
		addChild(n);
	}
	
	public ArrayList<VStringNode> getChildren(){
		// TODO 获取子节点数组
		return children;
	}
	public String getName(){
		// TODO 获取该节点名称（值）
		return nodename;
	}
	public void setName(String name){
		this.nodename = name;
	}
	public boolean haveChildren(){
		// TODO 获取节点是否尚有子节点
		if(children!=null){
			if(children.size()>0){
				return true;
			}
		}
		return false;
	}
	public boolean isAttribute() {
		// TODO 获取节点是否是属性
		return isAttribute;
	}
	public void setAttribute(boolean b) {
		// TODO 设置节点是否是属性
		this.isAttribute = b;
	}
}
