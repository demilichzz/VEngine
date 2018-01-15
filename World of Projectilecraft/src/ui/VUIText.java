/**
 * 文件名称：VUIText.java
 * 类路径：ui
 * 描述：TODO 用于在UI上显示的字体格式,包括常规的字体和UI坐标偏移等等
 * 作者：Demilichzz
 * 时间：2012-3-4上午04:21:59
 * 版本：Ver 1.0
 */
package ui;

import global.VString;

import interfaces.VtoUIInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import jgui.GamePanel;

/**
 * @author Demilichzz
 *
 */
public class VUIText implements VtoUIInterface{
	//---------------Layout-----------------------
	public static final int Layout_Free = 0;		//自由布局,按文本相对UI的x,y偏移决定位置
	public static final int Layout_YCenter = 1;	    //Y轴居中布局
	//------------------------------------------------
	protected String text;	//文本
	protected String textID;	//文本ID
	protected String[] convertedText;	//经过转换的临时分段文本
	protected double x_offset=0,y_offset=0;	//相对文本载体UI坐标的偏移
	protected Font f;				//字体
	protected Color c;			//颜色
	protected boolean stroke = true;	//是否描边
	protected Color scolor = Color.BLACK;
	protected int layout = Layout_Free;
	protected VUI parentUI;
	protected int xmax = 0;							//以Char形式绘制时的x轴最大坐标
	protected char[] textchar;						//将text转换为的char[]
	
	public VUIText(){
		
	}
	public VUIText(String text){
		this.text=text;
	}
	public String getID(){
		return textID;
	}
	public void setID(String id){
		this.textID=id;
	}
	public void setOffset(int x,int y){
		// TODO 设置相对于UI的坐标偏移
		x_offset=x;
		y_offset=y;
	}
	public void setStyle(Font f,Color c){
		// TODO 设置字体和颜色
		this.f = f;
		this.c = c;		
	}
	public void setStroke(boolean s,Color sc){
		// TODO 设置是否描边和颜色
		stroke = s;
		scolor = sc;
	}
	public void setLayout(int l){
		// TODO 设置布局
		this.layout = l;
	}
	public void setParent(VUI p){
		this.parentUI = p;
	}
	public void drawMe(VUI ui,Graphics g, GamePanel p){
		int x=0;	
		int y=0;
		switch(layout){
		case 0:{
			x =(int)(ui.getRealX()+x_offset);	//获取文字实际坐标
			y =(int)(ui.getRealY()+y_offset);
			break;
		}
		case 1:{
			if(parentUI!=null){
				x = (int)(ui.getRealX()+x_offset);
				y = (ui.area.h+ui.area.y)/2+(f.getSize()/2);
			}
			else{

			}
			break;
		}
		default:{

		}
		}
		if(text!=null){
			if(xmax!=0){
				drawStringByChar(text,x,y,g,p);
			}
			else{
				drawString(text,x,y,g,p);
			}
		}
	}
	private void drawString(String text,int x,int y,Graphics g,GamePanel p){
		// TODO 基于现有文本类中已设置参数绘制单行指定文字
		if(stroke){								//描边效果,在周围以描边颜色绘制8次
			g.setColor(scolor);
			g.setFont(f);
			for(int i=-1;i<2;i++){
				for(int j=-1;j<2;j++){
					if(i!=0||j!=0){
						g.drawString(text,x+i,y+j);
					}
				}
			}
		}
		g.setColor(c);
		g.setFont(f);
		g.drawString(text,x,y);
	}
	private void drawStringByChar(String text,int x,int y,Graphics g,GamePanel p){
		// TODO 基于现有文本类中已设置参数绘制单行指定文字
		int spacing = f.getSize();	//获取中文字符间距
		int linespacing = f.getSize()+2;
		int xc=x;					//字符坐标
		int yc=y;
		for(int i=0;i<textchar.length;i++){
			drawChars(textchar,i,xc,yc,g,p);
			if(VString.isLetter((textchar[i]))){	//是字母
				spacing = f.getSize()-5;
			}
			else{
				spacing = f.getSize();
			}
			xc = xc+spacing;
			if(xc>=xmax){
				xc = x;
				yc = yc + linespacing;
			}
		}
	}
	
	public VUI toUI(String id,int index){
		// TODO 根据文本自动创建一个合适大小的鼠标提示UI
		/*int w = 300;
		int h = (calTextRows(w)+1)*(f.getSize()+2);
		VASizeUI temp = new VASizeUI(w,h,id);
		temp.addText(this.textID);
		temp.text.setTypesetting(temp);
		return temp;*/
		return null;
	}
	
	public int calTextRows(int width){
		// TODO 根据文本和预定宽度计算分割文字的行数
		int spacing;
		int xc=0,yc=1;
		textchar = text.toCharArray();
		for(int i=0;i<textchar.length-1;i++){
			if(VString.isLetter((textchar[i]))){	//是字母
				spacing = f.getSize()-5;
			}
			else{
				spacing = f.getSize();
			}
			xc = xc+spacing;
			if(xc>=width){
				xc = 0;
				yc = yc + 1;
			}
		}
		return yc;
	}
	/**
	 * @param textchar2
	 * @param i
	 * @param j
	 * @param xc
	 * @param yc
	 * @param g
	 * @param p
	 */
	private void drawChars(char[] textchar, int ic, int xc, int yc,
			Graphics g, GamePanel p) {
		// TODO Auto-generated method stub
		if(stroke){								//描边效果,在周围以描边颜色绘制8次
			g.setColor(scolor);
			g.setFont(f);
			for(int i=-1;i<2;i++){
				for(int j=-1;j<2;j++){
					if(i!=0||j!=0){
						g.drawChars(textchar,ic,1,xc+i,yc+j);
					}
				}
			}
		}
		g.setColor(c);
		g.setFont(f);
		g.drawChars(textchar,ic,1,xc,yc);
	}
	public void setTypesetting(VUI vui) {
		// TODO 基于指定UI设置排版
		int w = vui.area.w - f.getSize();	//x坐标最大值
		xmax = w;
		textchar = text.toCharArray();
		//convertedText = new String[VString.length(text)/w+1];
	}
}
