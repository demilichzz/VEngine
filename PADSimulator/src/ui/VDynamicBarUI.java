/**
 * 文件名称：VDynamicBarUI.java
 * 类路径：ui
 * 描述：TODO 动态条UI,基于指定的VValueInterface数据接口绘制长度变化的UI
 * 作者：Demilichzz
 * 时间：2012-3-13上午02:55:40
 * 版本：Ver 1.0
 */
package ui;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import entities.VParty;

import view.VImageInterface;
import view.VText;
import view.VTexture;
import global.Imageconst;
import interfaces.VValueInterface;

/**
 * @author Demilichzz
 *
 */
public class VDynamicBarUI extends VUI{
	protected int xmax=1;						//数值最大值,即当数值为此值时显示全部覆盖图像
	protected VValueInterface v;			//动态数值接口
	protected int index;					//动态数值索引
	protected VTexture coverimage;	//动态条覆盖图像,原image为背景图像
	protected VTexture staticcover;	//静态覆盖图像,在动态条上一层
	protected int dw,dh;					//动态图宽高
	protected boolean ifdisplayvalue=false;
	protected int vxoff,vyoff;		//数值显示
	protected Color c = Color.BLACK;
	protected Font f = new Font(null,Font.PLAIN,16);
	
	public VDynamicBarUI() {
		super();
	}
	public VDynamicBarUI(String str,String ID){
		super(str,ID);
	}
	public VDynamicBarUI(String str,String coverstr,String scover, String ID) {
		super(str, ID);
		coverimage=Imageconst.GetImageByName(coverstr);
		staticcover=Imageconst.GetImageByName(scover);
		if(image!=null){
			dw=image.getWidth();
			dh=image.getHeight();
		}
		else{
			dw=0;
			dh=0;
		}
	}

	public VDynamicBarUI(VUI ui) {
		super(ui.image.toString(), ui.uiID);
	}
	
	public void bindValue(VValueInterface v,int index){
		this.v = v;
		this.index = index;
	}
	
	public void setMaxValue(int xmax){
		// TODO 设置UI对应的值的显示最大值
		this.xmax = xmax;
	}
	public void setValueDisplay(boolean d,int x,int y){
		// TODO 设置是否显示数值和显示坐标
		ifdisplayvalue=d;
		vxoff = x;
		vyoff = y;
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			drawUIText();
			drawUIBorder();
			drawSubUI();
		}
	}
	protected void drawUIImage(){
		if (image != null) {	//绘制UI图像
			if (image != null) {
				image.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(coverimage!=null&&v!=null){
				double percent = (double)v.getValue(index)/(double)xmax;
				double d = dw*percent;	//计算当前动态条长度
				coverimage.drawTexturePart(getRealX(), getRealY(), getRealX()+d*scale, getRealY()+(area.h-area.y)*scale, (float)(1-percent),(float)0.0,(float)1.0, (float)1.0, scale,uicolor);
			}
			if(staticcover!=null){
				staticcover.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
			}
			if(ifdisplayvalue&&v!=null){		//是否显示关联数值
				this.textlist.get(0).setText(v.getValue(VParty.PARTY_CURRENTHP)+"/"+v.getValue(VParty.PARTY_MAXHP));
			}
		}
		else{
			//Imageconst.nullpic.directDrawTexture(getRealX(), getRealY(), -1,scale);
		}
	}
	protected void drawUIText(){
		if (textlist.size()>0){
			for(VText tt :textlist.values()){
				tt.drawText();
			}
		}
	}
}
