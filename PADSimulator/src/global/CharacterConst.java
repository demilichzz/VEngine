/**
 * 文件名称：CharacterConst.java
 * 类路径：global
 * 描述：TODO	在游戏加载时初始化，从xml文件中获取宠物属性信息并生成对象
 * 作者：Demilichzz
 * 时间：2015-9-10下午10:38:10
 * 版本：Ver 1.0
 */
package global;

import java.util.HashMap;
import java.util.List;

import org.jdom.Element;

import view.VTexture;
import xml.VStringNode;
import xml.XMLIO;
import xml.XMLPack;
import entities.VCharacter;

/**
 * @author Demilichzz
 *
 */
public class CharacterConst {
	public static HashMap<Integer,VCharacter>characterlist = new HashMap<Integer,VCharacter>();;
	
	public static void Init(){
		Debug.DebugTestTimeStart();
		for (int i = 0; i < 28; i++) {
			loadCharFromXMLFile("res/Xml/MonsterData" + i + ".xml");
			System.out.println("加载角色数据_" + i);
		}
		Debug.DebugTestTimeEnd("加载角色数据总计时间", true);
	}
	
	public static void loadCharFromXMLFile(String url){
		Element root = XMLIO.LoadRootFromXML(url);
		List<Element> l = root.getChildren();		//获取属性列表元素
		VStringNode lnode = initLabelNode();	//获取基础属性对象的XML包的标签节点
		VStringNode vnode;		//基础属性对象的XML包的值节点
		for(int i=0;i<l.size();i++){	//遍历列表中元素
			Element e = l.get(i);	//获取元素
			vnode=XMLIO.getNodeFromXML(e, lnode);	//将元素转化为字符串列表
			XMLPack p = new XMLPack();		//创建XML包对象
			p.labelnode=lnode;		//设置XML包的标签节点为在GameData中预设的Player节点
			p.valuenode=vnode;		//设置XML包的值节点为从存档XML文件中获取的节点
			//System.out.println(p.valuenode.getChildren().get(0).getName());
			VCharacter c = new VCharacter();
			c.setValueFromPack(p);	//调用基础属性对象的XML转换接口的函数以通过XML包的值构建基础属性对象
			//System.out.println(c.getStringValue(0));
			c.setAdditionalStat();
			characterlist.put(c.getIntValue(VCharacter.PARAM_ID), c);
			//System.out.println(st.getDescribe());
		}
	}
	private static VStringNode initLabelNode() {
		// TODO 初始化怪物对象的属性标签节点
		VStringNode lnode = new VStringNode("モンスター");
		VStringNode id = new VStringNode("No.");
		String[]namelist = {"名前","主属性","副属性","タイプ1","タイプ2","レア度","レベル","最大レベル","コスト"
				,"HP","最大HP","経験値","攻撃","最大攻撃","回復","最大回復","モンスターポイント"
				,"スキル","ターン","スキル説明","リーダースキル","リーダースキル説明"};
		
		VStringNode type = new VStringNode("タイプ");
		type.addChild("タイプ");
		VStringNode awoken = new VStringNode("覚醒スキル");
		awoken.addChild("覚醒スキル");
		VStringNode sameskill = new VStringNode("同スキルモンスター");
		sameskill.addChild("No.");
		
		id.setAttribute(true);
		lnode.addChild(id);
		lnode.addChildren(namelist);	//快捷添加多个简单节点
		lnode.addChild(type);
		lnode.addChild(awoken);
		lnode.addChild(sameskill);
		return lnode;
	}

	public static VCharacter getCharacter(int id){
		return characterlist.get(id);
	}
}
