/**
 * 文件名称：XMLIO.java
 * 类路径：xml
 * 描述：TODO 静态XML加载类,包含常用的加载XML文件相关静态函数
 * 作者：Demilichzz
 * 时间：2012-3-15上午07:55:01
 * 版本：Ver 1.0
 */
package xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * @author Demilichzz
 *
 */
public class XMLIO {
	public static Element LoadRootFromXML(String url){
		// TODO 从指定xml文件中获取根节点
		try{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(url));
			Element root = doc.getRootElement();
			return root;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static Element LoadElementFromXML(String url,String childname){
		// TODO 从指定xml文件中获取一个指定Element
		try{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(url));
			Element root = doc.getRootElement();
			Element ep = root.getChild(childname);
			return ep;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	public static VStringNode getNodeFromXML(Element root,VStringNode labelNode){
		// TODO 通过指定的严格规范的标签名称父节点,从XML文件中获取指定元素,并返回元素转换为的字符串节点类实体
		//      调用时需严格保证root元素的层次结构与labelNode相同
		VStringNode node = null;
		/*if(labelNode.getName().equals("links")){
			Debug.DebugSimpleMessage("ss");
		}*/
		if(labelNode.haveChildren()){	//如果不是最底层节点
			node = new VStringNode(labelNode.getName());
			for(VStringNode vsn:labelNode.getChildren()){	//遍历子节点获取属性值
				if(vsn.haveChildren()){
					String name1 = vsn.getName();
					//for(VStringNode nc:labelNode.children){
					List<Element> childrenlist = root.getChildren(name1);
					for(int i=0;i<childrenlist.size();i++){
						Element child = childrenlist.get(i);
						VStringNode childnode = getNodeFromXML(child,vsn);
						node.addChild(childnode);
					}
					/*VStringNode childnode = getNodeFromXML(root.getChild(name1),vsn);
					node.addChild(childnode);*/
					//}
				}
				else{
					String name1 = vsn.getName();
					String value;
					List<Element> childrenlist = root.getChildren(name1); //获取全部指定名称的子元素
					if(vsn.isAttribute()){
						value = root.getAttributeValue(name1);
						node.addChild(new VStringNode(value));
					}
					else{
						for(int i=0;i<childrenlist.size();i++){
							Element child = childrenlist.get(i);
							value = child.getText();
							node.addChild(new VStringNode(value));
						}
					}
					/*if(vsn.isAttribute()){
						value = root.getAttributeValue(name1);
					}
					else{
						value = root.getChildText(name1);
					}
					node.addChild(new VStringNode(value));*/
				}
			}
			
		}
		else{		//如果是最底层节点,即没有子节点
			String name2 = labelNode.getName();	//获取标签
			String value;
			if(labelNode.isAttribute()){
				value = root.getAttributeValue(name2);
				node = new VStringNode(value);
			}
			else{
				value = root.getChildText(name2);
				node = new VStringNode(value);
			}
		}
		return node;
	}
	public static String[] ConvertElementChildrenToString(Element e,String[]namelist){
		// TODO 转换一个xml Element的Attribute和Child为字符串数组
		String[] valuelist = new String[namelist.length];
		for(int i=0;i<e.getChildren().size()+1&&i<namelist.length;i++){
			valuelist[i] = e.getChildText(namelist[i]);
			if(i==0){
				if(e.getAttribute(namelist[i])!=null){	//如果第一个字节点是Attribute
					valuelist[0] = e.getAttributeValue(namelist[0]);//则获取该属性的值
				}
			}
		}
		return valuelist;
	}
	public static int[] ConvertStringlistToIntlist(String[]strlist){
		// TODO 将指定确定是数字的字符串数组转换为int值数组
		int[] valuelist = new int[strlist.length];
		for(int i=0;i<strlist.length;i++){
			if(strlist[i]!=""){
				valuelist[i]=Integer.parseInt(strlist[i]);
			}
			else{
				valuelist[i]=0;
			}
		}
		return valuelist;
	}
	public static double[] ConvertStringlistToDoublelist(String[]strlist){
		// TODO 将指定确定是数字的字符串数组转换为double值数组
		double[] valuelist = new double[strlist.length];
		for(int i=0;i<strlist.length;i++){
			if(strlist[i]!=""){
				valuelist[i]=Double.parseDouble(strlist[i]);
			}
			else{
				valuelist[i]=0;
			}
		}
		return valuelist;
	}
	
	
	
	public static void SaveElement(Element e,String url){
		// TODO 将目标元素保存到指定url的文件中,通常会将已构建好的Root元素传入此函数
		Document Doc = new Document(e);
		// 设置格式
		Format format=Format.getPrettyFormat(); 
		format.setEncoding("UTF-8");
		format.setLineSeparator("\r\n"); 
		format.setIndent("    ");
		XMLOutputter XMLOut = new XMLOutputter(format);
	    // 输出 user.xml 文件；
	    try {
			XMLOut.output(Doc, new FileOutputStream(url));
			//Debug.DebugSimpleMessage("保存至"+url+"成功");
		} 
	    catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
