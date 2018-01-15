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

import jgui.GamePanel;
import config.ProjectConfig;
import view.VImageInterface;
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
	protected VImageInterface coverimage;	//动态条覆盖图像,原image为背景图像
	protected VImageInterface staticcover;	//静态覆盖图像,在动态条上一层
	protected int dw,dh;					//动态图宽高
	protected boolean ifdisplayvalue=false;
	protected int vxoff,vyoff;
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
		if(coverimage!=null){
			dw=coverimage.getWidth();
			dh=coverimage.getHeight();
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
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(coverimage!=null&&v!=null){
				double d = dw-(dw*(v.getValue(index))/xmax);	//计算当前动态条削减的长度
				int di = (int)d;
				g.drawImage(coverimage.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+dw-di, (int)getRealY()+dh,di,0,dw,dh,p);
			}
			if(staticcover!=null){
				g.drawImage(staticcover.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);
			}
			if(text!=null){
				text.drawMe(this,g,p);
			}
			if(ifdisplayvalue&&v!=null){		//是否显示关联数值
				g.setFont(f);
				g.setColor(c);
				g.drawString(""+v.getValue(index),(int)(getRealX()+vxoff), (int)(getRealY()+vyoff));
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);
			}
		}
	}
}
