/**
 * @author Demilichzz
 *	仇恨值计量条UI，与当前玩家对象的仇恨值关联
 * 2013-2-6
 */
package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import jgui.GamePanel;
import config.ProjectConfig;
import interfaces.VValueInterface;
import entities.*;
import global.*;
import view.*;

/**
 * @author Demilichzz
 *
 * 2013-2-6
 */
public class VThreatBarUI extends VUI{
	protected VImageInterface[] coverimage=new VImageInterface[4];	//动态条覆盖图像,原image为背景图像
	protected VImageInterface staticcover;	//静态覆盖图像,在动态条上一层
	protected int dw,dh;		//动态图宽高
	protected boolean displayvalue=true;	//是否显示数值
	protected int threatmax = 12000;
	protected int vxoff=-10,vyoff=100;		//仇恨数值坐标偏移
	
	protected VPlayer player;		//绑定的玩家对象
	
	public VThreatBarUI(String str,String ID){
		// TODO 使用默认覆盖图像的仇恨值UI
		super(str,ID);
		coverimage[0]=Imageconst.GetImageByName("UI_20x200_Threatbar_state0.png");
		coverimage[1]=Imageconst.GetImageByName("UI_20x200_Threatbar_state1.png");
		coverimage[2]=Imageconst.GetImageByName("UI_20x200_Threatbar_state2.png");
		coverimage[3]=Imageconst.GetImageByName("UI_20x200_Threatbar_state3.png");
		staticcover=Imageconst.GetImageByName("UI_20x200_Threatbar_stcover.png");
		if (coverimage[0] != null) {
			dw = coverimage[0].getWidth();
			dh = coverimage[0].getHeight();
		} else {
			dw = 0;
			dh = 0;
		}
	}
	public VThreatBarUI(String str,String coverstr,String scover, String ID){
		super(str, ID);
		coverimage[0]=Imageconst.GetImageByName("UI_20x200_Threatbar_state0.png");
		staticcover=Imageconst.GetImageByName("UI_20x200_Threatbar_stcover.png");
	}
	public void bindPlayer(VPlayer p){
		// TODO 绑定玩家
		this.player = p;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {		//底图
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(coverimage!=null&&player!=null){		//绘制仇恨值动态部分
				int imageindex = player.getThreatState();		//获取玩家的仇恨状态
				double d = dh-(dh*(player.getValue(3))/threatmax);	//计算当前动态条削减的长度
				int di = (int)d;
				g.drawImage(coverimage[imageindex].getImage(),(int)getRealX(),(int)getRealY()+di,(int)getRealX()+dw,(int)getRealY()+dh,0,0,dw,dh-di,p);
				//g.drawImage(coverimage.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+dw-di, (int)getRealY()+dh,di,0,dw,dh,p);
			}
			if(staticcover!=null){		//静态覆盖图
				g.drawImage(staticcover.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(displayvalue&&player!=null){
				String dispvaluestr = ""+(int)(player.getValue(3)/100);
				g2d.setFont(Global.f_threat);
				FontMetrics fm = g2d.getFontMetrics(Global.f_threat);
				int strW = fm.stringWidth(dispvaluestr);
				int x = (int)(getRealX()+vxoff) - strW;
				g2d.setColor(Color.black);
				g2d.drawString(dispvaluestr,x,(int)(getRealY()+vyoff));
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {	//UI边界
				area.drawMe(g2d, p);
			}
			if(text!=null){
				text.drawMe(this,g,p);
			}
			/*if(ifdisplayvalue&&v!=null){		//是否显示关联数值
				g.setFont(f);
				g.setColor(c);
				g.drawString(""+v.getValue(index),(int)(getRealX()+vxoff), (int)(getRealY()+vyoff));
			}*/
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);
			}
		}
	}
}
