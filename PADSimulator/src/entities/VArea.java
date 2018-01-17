/**
 * 文件名称：VArea.java
 * 类路径：entities
 * 描述：TODO 使用左上坐标和右下坐标定义的矩形区域，用于在UI编辑中显示外框及进行鼠标事件处理等
 * 作者：Demilichzz
 * 时间：2011-10-26上午07:55:59
 * 版本：Ver 1.0
 */
package entities;

import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;


/**
 * @author Demilichzz
 *
 */
public class VArea {
	public static final boolean drawArea = false; 
	
	public int x, y, w, h;		//存储区域左上坐标和右下坐标的变量
	public boolean boldborder = true;	//控制显示时是否为粗外框

	public VArea(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public VArea() {
		// TODO Auto-generated constructor stub
		x = 0;
		y = 0;
		w = 0;
		h = 0;
	}
	public void setLoc(double x,double y){
		this.w = (int) (this.w + x - this.x);
		this.h = (int) (this.h + y - this.y);
		this.x = (int) (x);
		this.y = (int) (y);
	}

	public boolean ifvalid(double x, double y) {
		// TODO 获取目标点x,y是否在此区域范围内
		if (this.x == 0 && this.y == 0 && this.w == 0 && this.h == 0) {
			return true;
		}
		if (x > this.x && x < this.w && y > this.y && y < this.h) {
			return true;
		} else
			return false;
	}
	public int getWidth(){
		return w-x;
	}
	public int getHeight(){
		return h-y;
	}
	public void drawMe() {
		// TODO 绘制区域边框
		//System.out.println(x+";"+y+";"+w+";"+h);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK ,GL11.GL_LINE);
		GL11.glColor3f(0.0f,0.0f,1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x,y);	//左上
		GL11.glVertex2d(w,y);	//右上
		GL11.glVertex2d(w,h);	//右下
		GL11.glVertex2d(x,h);	//左下
		GL11.glEnd();
		if(boldborder){
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x+1,y+1);
			GL11.glVertex2d(w-1,y+1);
			GL11.glVertex2d(w-1,h-1);
			GL11.glVertex2d(x+1,h-1);
			GL11.glEnd();
		}
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK ,GL11.GL_FILL);
	}
}
