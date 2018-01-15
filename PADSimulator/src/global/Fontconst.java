/**
 * @author Demilichzz
 *	字体管理类，存储游戏中使用的字体对象s
 * 2013-8-30
 */
package global;

import java.awt.Font;
import java.util.HashMap;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.OutlineEffect;

import view.VTexture;

/**
 * @author Demilichzz
 *
 * 2013-8-30
 */
public class Fontconst {
	public static HashMap<String, UnicodeFont> font_hashmap = new HashMap<String, UnicodeFont>();  //按索引存储字体类
	public static void Init(){
		Debug.DebugTestTimeStart();
		//Debug.DebugSimpleMessage("初始化字体");
		UnicodeFont f = createFont("微软雅黑", Font.BOLD, 16);
		font_hashmap.put("font_default", f);
		f = createFont("微软雅黑", Font.PLAIN, 12);
		font_hashmap.put("font_small", f);
		f = createFont("微软雅黑", Font.BOLD, 32);
		font_hashmap.put("font_big", f);
		f = createFont("微软雅黑", Font.PLAIN, 32);
		font_hashmap.put("font_hint", f);
		f = createFont("微软雅黑", Font.BOLD, 20);
		font_hashmap.put("font_dm1", f);
		f = createFont("微软雅黑", Font.BOLD, 16);
		font_hashmap.put("font_dm2", f);
		Debug.DebugTestTimeEnd("初始化字体", true);
	}
	public static UnicodeFont createFont(String fontname,int style,int size){
		Font awtFont = new Font(fontname, style, size);
		UnicodeFont font = new UnicodeFont(awtFont);
		//font.getEffects().add(new OutlineEffect(1,java.awt.Color.black));
		font.getEffects().add(new ColorEffect());		//虽然该代码并无实际效果但是因为Slick库的问题必须有
		font.addGlyphs("0123456789/.,-");
		font.addGlyphs("Combox");
		font.addGlyphs("恵みの土塊");
		font.addGlyphs("グラウンド・リベンジ");
		return font;
	}
	public static void initGlyph(String text){
		// TODO 对所有的字体对象初始化指定的字形
		for(UnicodeFont ff:font_hashmap.values()){
			ff.addGlyphs(text);
		}
	}
	public static UnicodeFont getFont(String index){
		// TODO 根据名称索引获取字体
		UnicodeFont f = font_hashmap.get(index);
		if(f==null){
			f=font_hashmap.get("font_default");
		}
		return f;
	}
	public static void loadGlyphs(){
		// TODO 对所有字体进行载入字形
		for(UnicodeFont ff : font_hashmap.values()){
			try {
				//ff.addAsciiGlyphs();
				ff.loadGlyphs();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void clearGlyphs() {
		// TODO Auto-generated method stub
		for(UnicodeFont ff : font_hashmap.values()){
			ff.clearGlyphs();
		}
	}
}
