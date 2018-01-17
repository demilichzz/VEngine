/**
 * �ļ����ƣ�Textconst.java
 * ��·����global
 * ������TODO ���ַ���������ʽ�洢��Ϸ��Ӧ�õ��ַ�����
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-11����08:59:22
 * �汾��Ver 1.0
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
	public static HashMap<String, VUIText> text_hashmap = new HashMap<String, VUIText>();  //���ļ����洢�ı���Hashmap
	
	public static void Init(){
		Debug.DebugSimpleMessage("��ʼ���ı���Դ");
		//LoadText("xml/Text/UItext.xml");
	}

	public static VUIText GetTextByName(String filename){
		// TODO ���ļ�����ȡVImageInterface��ʽ��ͼ��
		VUIText t = text_hashmap.get(filename);
		if(t!=null){
			return t;
		}
		else{
			Debug.DebugSimpleMessage("δ�ҵ�ָ���ı���Դ"+filename);
			return null;
		}
	}
	public static void LoadText(String ad) {
		// TODO ��ָ��·��װ��xml�ļ���ת��ΪVUIText
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
		// TODO ����ȡ��xmlԪ��ת��Ϊ�ı���
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
