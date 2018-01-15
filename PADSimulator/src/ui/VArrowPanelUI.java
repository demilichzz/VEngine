/**
 * 	用于在转珠回放时，绘制方向箭头的面板UI
 */
package ui;

import org.newdawn.slick.Color;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import ai.*;
import entities.*;
import global.Imageconst;

/**
 * @author Demilichzz
 *
 */
public class VArrowPanelUI extends VUI{
	protected VPathAI ai = null;
	protected ArrayList<int[]> arrowlist = new ArrayList<int[]>();
	
	
	public VArrowPanelUI(){
		
	}
	public VArrowPanelUI(int width, int height, String ID) {
		// TODO 通过宽高定义制定大小的图像资源为空的UI
		super(width,height,ID);
	}
	public VArrowPanelUI(String str, String ID) {
		super(str,ID);
	}
	public void addArrow(int cx, int cy, int movex, int movey){
		// TODO 添加箭头的位置参数，在UI的draw函数中进行绘制
		int index = 0;		//判断箭头位置是否重复
		for(int[] arrow:arrowlist){
			if(cx==arrow[0]&&cy==arrow[1]&&movex==arrow[2]&&movey==arrow[3]){	//位置重复
				index = index + 1;
			}
		}
		int[] newarrow = new int[]{cx,cy,movex,movey,index};
		arrowlist.add(newarrow);
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			drawUIText();
			drawUIBorder();
			double px=0,py=0,tx=0,ty=0;
			int index = 0;
			if(arrowlist.size()>0){		//如果箭头列表不为空，表示已开始绘制转珠，绘制起始点
				px = getRealX()+50+100*arrowlist.get(0)[0];
				py = getRealY()+50+100*arrowlist.get(0)[1];
				Imageconst.GetImageByName("startpoint.png").directDrawTexture(px,py, -1,1.0,Color.white);
			}
			for(int[]arrow:arrowlist){		//绘制箭头
				px = getRealX()+50+100*arrow[0];
				py = getRealY()+50+100*arrow[1];
				tx = arrow[2];
				ty = arrow[3];	//获取箭头起始坐标和方向
				index = arrow[4];
				drawArrow(px,py,tx,ty,index);
			}
			px = px+tx*100;
			py = py+ty*100;
			if(arrowlist.size()>0){		//绘制当前宝石转动时外框	
				GL11.glEnable(GL11.GL_STENCIL_TEST);  	//开启模板测试
				GL11.glStencilFunc(GL11.GL_NEVER, 0x0, 0x0);  
				GL11.glStencilOp(GL11.GL_INCR, GL11.GL_INCR, GL11.GL_INCR);  
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2d(px-40,py-40);	//左上
				GL11.glVertex2d(px+40,py-40);	//右上
				GL11.glVertex2d(px+40,py+40);	//右下
				GL11.glVertex2d(px-40,py+40);	//左下
				GL11.glEnd();
				GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0x1, 0x1);  
				GL11.glStencilOp(GL11.GL_INCR, GL11.GL_INCR, GL11.GL_INCR);  
				GL11.glColor4f(0.0f,0.0f,1.0f,1.0f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2d(px-50,py-50);	//左上
				GL11.glVertex2d(px+50,py-50);	//右上
				GL11.glVertex2d(px+50,py+50);	//右下
				GL11.glVertex2d(px-50,py+50);	//左下
				GL11.glEnd();
				GL11.glDisable(GL11.GL_STENCIL_TEST);  	//关闭模板测试
			}
			drawSubUI();
		}
	}
	public void drawArrow(double px,double py,double tx,double ty,int index){
		//根据给定起始结束坐标及重复索引绘制转动过程箭头
		double asx = px + ty*(4+index*8);
		double asy = py - tx*(4+index*8);	//arrowstartx,y定义不同方向箭头相对宝石中心的起始偏移
		double tgemx = asx+tx*100;
		double tgemy = asy+ty*100;	//目标宝石中心点坐标，用于绘制箭头三角
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK ,GL11.GL_FILL);

		GL11.glColor4f(0.0f,0.0f,0.0f,1.0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(asx-ty*2,asy+tx*2);	//左上
		GL11.glVertex2d(asx+ty*2,asy-tx*2);	//右上
		GL11.glVertex2d(asx+ty*2+tx*100,asy-tx*2+ty*100);	//右下
		GL11.glVertex2d(asx-ty*2+tx*100,asy+tx*2+ty*100);	//左下
		/*GL11.glVertex2d(px-4,py-4);	//左上
		GL11.glVertex2d(tx+2,ty-2);	//右上
		GL11.glVertex2d(tx+2,ty+2);	//右下
		GL11.glVertex2d(px-4,py+4);	//左下
*/		GL11.glEnd();
		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glVertex2d(tgemx-ty*5,tgemy+tx*5);	
		GL11.glVertex2d(tgemx+ty*5,tgemy-tx*5);	
		GL11.glVertex2d(tgemx+tx*20, tgemy+ty*20);
		GL11.glEnd();
	}
	public void bindEntity(VPathAI vPathAI) {
		// TODO Auto-generated method stub
		ai = vPathAI;
	}
	public void resetArrowList() {
		// TODO 清空箭头数据列表
		arrowlist = new ArrayList<int[]>();
	}
}
