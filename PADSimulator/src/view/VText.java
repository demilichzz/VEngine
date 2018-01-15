/**
 * @author Demilichzz
 *	文本对象类，用于实现UI文本的参数定义与绘制
 * 2013-8-30
 */
package view;

import global.*;

import org.lwjgl.opengl.*;
import org.newdawn.slick.*;

import ui.*;

/**
 * @author Demilichzz
 *
 * 2013-8-30
 */
public class VText {
	protected boolean visible =true;
	protected String text;			//内容文本
	protected String font;			//字体对象索引
	protected UnicodeFont ufont;
	protected VUI parentui;			//字体位于的UI
	protected double xoffset,yoffset;	//定位锚点坐标相对于UI原点的偏移值
	protected double transparency = 1.0;	//透明度
	protected Color color=new Color(0,0,0);
	protected boolean stroke = true;
	protected int layout = 0;		//文字布局类型
	protected int maxwidth = -1;	//文字横行距离上限,-1表示文字不分行
	protected String[] textlist;	//文本间隔点索引
	protected int textrow = 1;			//文本行数
	public static final int Layout_REGULAR = 0;		//常规布局，锚点位于文本左上角
	public static final int Layout_CENTER = 1;		//中心布局，锚点位于文本中心
	public static final int Layout_RIGHT = 2;		//右侧布局，锚点位于文本右侧中心
	protected int expiretime = -1;
	
	public VText(String text){
		this.setText(text,0,0,Color.white,"font_default",true);
	}
	public VText(String text,double xoff,double yoff,Color col,String font,boolean stroke){
		this.setText(text,xoff,yoff,col,font,stroke);
	}
	public void setText(String text,double xoff,double yoff,Color col,String font,boolean stroke){
		// TODO 设置文本绘制参数
		this.text = text;
		xoffset = xoff;
		yoffset = yoff;
		this.font = font;
		color = col;
		this.stroke = stroke;
		textlist = new String[32];
		ufont = Fontconst.getFont(font);
		ufont.addGlyphs(text);				//对字体添加文本的字形
	}
	public void setText(String text){
		this.text = text;
	}
	public void setLoc(double x,double y){
		xoffset = x;
		yoffset = y;
	}
	public void setFont(String font){
		ufont = Fontconst.getFont(font);
		ufont.addGlyphs(text);
	}
	public double getX(){
		return xoffset;
	}
	public double getY(){
		return yoffset;
	}
	public void setColor(int r,int g,int b){
		color = new Color(r, g, b);
	}
	public void setColor(Color c){
		color = c;
	}
	public void setTransparency(double d){
		// TODO 设置文字的透明度
		transparency = d;
		color = new Color(color.r,color.g,color.b,(float)d);
	}
	public void setStroke(boolean b){
		stroke = b;
	}
	public void setLayout(int layout){
		// TODO 设置布局类型
		this.layout=layout;
	}
	public void setRestrict(int maxwidth){
		// TODO 设置横行文字允许的最大像素值
		this.maxwidth = maxwidth;
		if(maxwidth>0){
			char[] tempchar = text.toCharArray();
			int spacing = 0;
			int xc=0,yc=1;
			int lastsub = 0;
			textrow = 0;
			for(int i=0;i<tempchar.length;i++){
				if(VString.isLetter((tempchar[i]))){	//是字母
					spacing = ufont.getWidth(""+tempchar[i]);			
				}
				else{
					spacing = ufont.getWidth(""+tempchar[i]);
				}
				xc = xc+spacing;
				if(xc>=maxwidth){
					xc = 0;
					yc = yc + 1;
					textlist[textrow]=text.substring(lastsub,i);
					lastsub = i;
					textrow++;
				}
			}
			textlist[textrow]=text.substring(lastsub,text.length());		//计算分段点字符的索引列表
		}
	}
	/**
	 * 
	 */
	public void restrictScale(double scale) {
		// TODO 将文本宽度限制进行缩放
		if(maxwidth!=-1){
			setRestrict((int) (maxwidth*scale));
		}
	}
	public void drawText(){
		if (expiretime == -1 || expiretime > 0) {
			// GL11.glColor4f(1.0f,1.0f,1.0f,0.5f); // 全亮度， 50% Alpha 混合
			// GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE);
			double uix = 0;
			double uiy = 0;
			if (parentui != null) {
				uix = parentui.getRealX();
				uiy = parentui.getRealY();
			}
			if (stroke) { // 描边
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (j != 0 || i != 0) {
							float startx = 0;
							float starty = 0;
							if (maxwidth > 0) {
								switch (layout) {
								case Layout_REGULAR: {
									startx = (float) (uix + xoffset + i);
									starty = (float) (uiy + yoffset + j);
									break;
								}
								case Layout_CENTER: {
									if (textrow == 0) {
										startx = (float) (uix + xoffset + i)
												- ufont.getWidth(text) / 2;
									} else {
										startx = (float) (uix + xoffset + i)
												- maxwidth / 2;
									}
									starty = (float) (uiy + yoffset + j)
											- ufont.getHeight(text) * textrow
											/ 2;
									break;
								}
								case Layout_RIGHT: {
									startx = (float) (uix + xoffset)
											- ufont.getWidth(text);
									starty = (float) (uiy + yoffset + j)
											- ufont.getHeight(text) * textrow
											/ 2;
									break;
								}
								}
								for (int si = 0; si < textrow + 1; si++) {
									ufont.drawString(startx, starty + si
											* ufont.getHeight(text),
											textlist[si], new Color(0,0,0,(float)transparency));
								}
							} else {
								switch (layout) {
								case Layout_REGULAR: {
									startx = (float) (uix + xoffset + i);
									starty = (float) (uiy + yoffset + j);
									break;
								}
								case Layout_CENTER: {
									startx = (float) (uix + xoffset + i)
											- ufont.getWidth(text) / 2;
									starty = (float) (uiy + yoffset + j)
											- ufont.getHeight(text) / 2;
									break;
								}
								case Layout_RIGHT: {
									startx = (float) (uix + xoffset + i)
											- ufont.getWidth(text);
									starty = (float) (uiy + yoffset + j)
											- ufont.getHeight(text) * textrow
											/ 2;
									break;
								}
								}
								ufont.drawString(startx, starty, text,
										new Color(0,0,0,(float)transparency));
							}
						}
					}
				}
			}
			float startx = 0;
			float starty = 0;
			if (maxwidth > 0) { // 绘制文字部分
				switch (layout) {
				case Layout_REGULAR: {
					startx = (float) (uix + xoffset);
					starty = (float) (uiy + yoffset);
					break;
				}
				case Layout_CENTER: {
					if (textrow == 0) {
						startx = (float) (uix + xoffset) - ufont.getWidth(text)
								/ 2;
					} else {
						startx = (float) (uix + xoffset) - maxwidth / 2;
					}
					starty = (float) (uiy + yoffset) - ufont.getHeight(text)
							* textrow / 2;
					break;
				}
				case Layout_RIGHT: {
					startx = (float) (uix + xoffset) - ufont.getWidth(text);
					starty = (float) (uiy + yoffset) - ufont.getHeight(text)
							* textrow / 2;
					break;
				}
				}
				for (int si = 0; si < textrow + 1; si++) {
					ufont.drawString(startx,
							starty + si * ufont.getHeight(text), textlist[si],
							color);
				}
			} else {
				switch (layout) {
				case Layout_REGULAR: {
					startx = (float) (uix + xoffset);
					starty = (float) (uiy + yoffset);
					break;
				}
				case Layout_CENTER: {
					startx = (float) (uix + xoffset) - ufont.getWidth(text) / 2;
					starty = (float) (uiy + yoffset) - ufont.getHeight(text)
							/ 2;
					break;
				}
				case Layout_RIGHT: {
					startx = (float) (uix + xoffset) - ufont.getWidth(text);
					starty = (float) (uiy + yoffset) - ufont.getHeight(text)
							* textrow / 2;
					break;
				}
				}
				ufont.drawString(startx, starty, text, color);
			}
		}
	}
	/**
	 * @param vui
	 */
	public void setParentUI(VUI ui) {
		// TODO Auto-generated method stub
		parentui = ui;
	}
	public void setVisible(boolean b){
		visible = b;
	}
	public boolean getVisible(){
		return visible;
	}
	/**
	 * 
	 */
	public void setExpire(int time){
		// TODO 设置文字可显示的时间计数
		expiretime = time;
	}
	public void expire() {
		// TODO UI更新时，如果设置了消除时间，则消除时间-1
		if(expiretime>0){
			expiretime--;
		}
	}
}
