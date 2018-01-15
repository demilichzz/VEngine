/**
 * �ļ����ƣ�VUIText.java
 * ��·����ui
 * ������TODO ������UI����ʾ�������ʽ,��������������UI����ƫ�Ƶȵ�
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-4����04:21:59
 * �汾��Ver 1.0
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
	public static final int Layout_Free = 0;		//���ɲ���,���ı����UI��x,yƫ�ƾ���λ��
	public static final int Layout_YCenter = 1;	    //Y����в���
	//------------------------------------------------
	protected String text;	//�ı�
	protected String textID;	//�ı�ID
	protected String[] convertedText;	//����ת������ʱ�ֶ��ı�
	protected double x_offset=0,y_offset=0;	//����ı�����UI�����ƫ��
	protected Font f;				//����
	protected Color c;			//��ɫ
	protected boolean stroke = true;	//�Ƿ����
	protected Color scolor = Color.BLACK;
	protected int layout = Layout_Free;
	protected VUI parentUI;
	protected int xmax = 0;							//��Char��ʽ����ʱ��x���������
	protected char[] textchar;						//��textת��Ϊ��char[]
	
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
		// TODO ���������UI������ƫ��
		x_offset=x;
		y_offset=y;
	}
	public void setStyle(Font f,Color c){
		// TODO �����������ɫ
		this.f = f;
		this.c = c;		
	}
	public void setStroke(boolean s,Color sc){
		// TODO �����Ƿ���ߺ���ɫ
		stroke = s;
		scolor = sc;
	}
	public void setLayout(int l){
		// TODO ���ò���
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
			x =(int)(ui.getRealX()+x_offset);	//��ȡ����ʵ������
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
		// TODO ���������ı����������ò������Ƶ���ָ������
		if(stroke){								//���Ч��,����Χ�������ɫ����8��
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
		// TODO ���������ı����������ò������Ƶ���ָ������
		int spacing = f.getSize();	//��ȡ�����ַ����
		int linespacing = f.getSize()+2;
		int xc=x;					//�ַ�����
		int yc=y;
		for(int i=0;i<textchar.length;i++){
			drawChars(textchar,i,xc,yc,g,p);
			if(VString.isLetter((textchar[i]))){	//����ĸ
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
		// TODO �����ı��Զ�����һ�����ʴ�С�������ʾUI
		/*int w = 300;
		int h = (calTextRows(w)+1)*(f.getSize()+2);
		VASizeUI temp = new VASizeUI(w,h,id);
		temp.addText(this.textID);
		temp.text.setTypesetting(temp);
		return temp;*/
		return null;
	}
	
	public int calTextRows(int width){
		// TODO �����ı���Ԥ����ȼ���ָ����ֵ�����
		int spacing;
		int xc=0,yc=1;
		textchar = text.toCharArray();
		for(int i=0;i<textchar.length-1;i++){
			if(VString.isLetter((textchar[i]))){	//����ĸ
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
		if(stroke){								//���Ч��,����Χ�������ɫ����8��
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
		// TODO ����ָ��UI�����Ű�
		int w = vui.area.w - f.getSize();	//x�������ֵ
		xmax = w;
		textchar = text.toCharArray();
		//convertedText = new String[VString.length(text)/w+1];
	}
}
