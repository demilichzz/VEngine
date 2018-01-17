/**
 * 文件名称：XMLPack.java
 * 类路径：xml
 * 描述：TODO XML包类,可将各种类的实例通过实现toXML接口转换为包类,再将XML包类以统一方式与XML文件交互
 * 作者：Demilichzz
 * 时间：2012-4-19下午02:11:46
 * 版本：Ver 1.0
 */
package xml;


import org.jdom.Element;

/**
 * @author Demilichzz
 *
 */
public class XMLPack {
	public VStringNode labelnode;	//XML标签字符串节点
	public VStringNode valuenode;	//XML值字符串节点	
	
	public XMLPack(){
		
	}
	public Element convertToElement(){
		// TODO 转化为XML元素
		return convertToElementByNode(labelnode,valuenode);
		/*Element ele = new Element(elementname);
		if(namelist!=null&&valuestring!=null){
			if(namelist.length==valuestring.length){
				for(int i=0;i<namelist.length;i++){
					if(i==0&&firstisAttribute){
						ele.setAttribute(namelist[0],valuestring[0]);
					}
					else{
						ele.addContent(new Element(namelist[i]).setText(valuestring[i]));
					}
				}
			}
		}
		return ele;	*/	
	}
	public Element convertToElementByNode(VStringNode l,VStringNode v){
		// TODO 使用节点将自身转化为XML元素
		Element ele = new Element(l.nodename);
		if(l!=null&&v!=null){
			for(int i=0;i<l.children.size();i++){
				if(!l.children.get(i).haveChildren()){	//如果当前处理的节点没有子节点
					if(l.children.get(i).isAttribute()){	//是属性
						ele.setAttribute(l.children.get(i).nodename,v.children.get(i).nodename);
					}
					else{
						ele.addContent(new Element(l.children.get(i).nodename).setText(v.children.get(i).nodename));
					}
				}
				else{	//如果有子节点
					Element e = convertToElementByNode(l.children.get(i),v.children.get(i));//进行递归操作将子节点数据转化为元素
					ele.addContent(e);
				}
			}
		}
		return ele;	
	}
}
