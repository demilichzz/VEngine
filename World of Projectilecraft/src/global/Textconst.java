/**
 * 文件名称：Textconst.java
 * 类路径：global
 * 描述：TODO 以字符串索引形式存储游戏中应用的字符串类
 * 作者：Demilichzz
 * 时间：2012-3-11上午08:59:22
 * 版本：Ver 1.0
 */
package global;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.*;

/*import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;*/

import ui.VUI;
import ui.VUIText;
import view.VImageInterface;

/**
 * @author Demilichzz
 *
 */
public class Textconst {
	public static HashMap<String, VUIText> text_hashmap = new HashMap<String, VUIText>();  //按文件名存储文本的Hashmap
	
	public static void Init(){
		Debug.DebugSimpleMessage("初始化文本资源");
		//LoadText("xml/Text/UItext.xml");
	}

	public static VUIText GetTextByName(String filename){
		// TODO 按文件名获取VImageInterface形式的图像
		VUIText t = text_hashmap.get(filename);
		if(t!=null){
			return t;
		}
		else{
			Debug.DebugSimpleMessage("未找到指定文本资源"+filename);
			return null;
		}
	}
	public static void LoadText(String ad) {
		// TODO 从指定路径装载xml文件并转化为VUIText
		/*try{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(ad));
			Element root = doc.getRootElement();
			List tlist = root.getChildren("VUIText");
			for(int i=0;i<tlist.size();i++){
				VUIText t = ConvertElementToText((Element)tlist.get(i));
				text_hashmap.put(t.getID(), t);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}*/
	}

	/*private static VUIText ConvertElementToText(Element e) {
		// TODO 将获取的xml元素转换为文本类
		VUIText temp;
		String text = e.getChild("text").getValue();
		String id = e.getAttribute("textID").getValue();
		String x = e.getChild("x").getValue();
		String y = e.getChild("y").getValue();
		String fname = e.getChild("fname").getValue();
		String fstyle = e.getChild("fstyle").getValue();
		String fsize = e.getChild("fsize").getValue();
		String cR = e.getChild("cR").getValue();
		String cG = e.getChild("cG").getValue();
		String cB = e.getChild("cB").getValue();
		String stroke = e.getChild("stroke").getValue();
		String sR = e.getChild("sR").getValue();
		String sG = e.getChild("sG").getValue();
		String sB = e.getChild("sB").getValue();
		String layout = e.getChild("layout").getValue();
		temp = ConvertStringlistToText(text,id,x,y,fname,fstyle,fsize,cR,cG,cB,stroke,sR,sG,sB,layout);
		return temp;
	}*/

	private static VUIText ConvertStringlistToText(String text,String id,String x, String y,
			String fname, String fstyle, String fsize, String cr, String cg,
			String cb, String stroke, String sr, String sg, String sb,String layout) {
		// TODO Auto-generated method stub
		int xi = Integer.parseInt(x);
		int yi = Integer.parseInt(y);
		int fstylei = Integer.parseInt(fstyle);
		int fsizei = Integer.parseInt(fsize);
		int cri = Integer.parseInt(cr);
		int cgi = Integer.parseInt(cg);
		int cbi = Integer.parseInt(cb);
		int sri = Integer.parseInt(sr);
		int sgi = Integer.parseInt(sg);
		int sbi = Integer.parseInt(sb);
		boolean strokeb = (stroke.equals("1"));
		int layouti = Integer.parseInt(layout);
		if(fname.equals("null")){
			fname=null;
		}
		VUIText temp = new VUIText(text);
		temp.setID(id);
		temp.setOffset(xi, yi);
		temp.setStyle(new Font(fname,fstylei,fsizei), new Color(cri,cgi,cbi));
		temp.setStroke(strokeb, new Color(sri,sgi,sbi));
		temp.setLayout(layouti);
		return temp;
	}
}
