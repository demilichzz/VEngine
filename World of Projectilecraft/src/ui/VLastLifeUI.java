/**
 * 	TODO 挑战模式下的残机显示UI
 */
package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import jgui.GamePanel;
import config.ProjectConfig;
import interfaces.VValueInterface;
import entities.VArea;
import global.Imageconst;

/**
 * @author Demilichzz
 *
 * 2012-11-8
 */
public class VLastLifeUI extends VUI{
	protected VValueInterface v;		//数值接口形式的玩家类，用于获取玩家剩余残机
	protected int index;
	
	public VLastLifeUI(String str, String ID) {
		// TODO 通过图像文件名建立UI的构造函数
		image = Imageconst.GetImageByName(str);
		x = 0;
		y = 0;
		visible = true;
		enable = true;
		uiID = ID;
		area = new VArea(0, 0, image.getWidth(), image.getHeight());
		area.boldborder = false;
	}
	
	public void bindValue(VValueInterface v,int index){
		this.v = v;
		this.index = index;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null&&v!=null) {
				int life = v.getValue(index);
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+life*30, (int)getRealY()+30,0,0,life*30,30,p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);
			}
			if(text!=null){
				text.drawMe(this,g,p);
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);
			}
		}
	}
}
