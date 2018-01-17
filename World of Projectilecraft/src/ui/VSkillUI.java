/**
 * 	技能UI，显示技能图标和技能冷却，并在冷却结束时显示动画边框
 */
package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import view.VImageInterface;

import config.ProjectConfig;

import jgui.GamePanel;
import global.Animeconst;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2012-11-30
 */
public class VSkillUI extends VUI{
	protected VValueInterface value;
	protected int vindex=0;
	protected int x_offset=0,y_offset=0;
	protected VImageInterface cdanime=null;
	protected int text_x = 12,text_y = 36;
	protected Color c = Color.YELLOW;
	protected Font f = new Font(null,Font.BOLD,32);
	
	public VSkillUI(String str,String id){
		super(str,id);
	}
	
	public void bindValue(VValueInterface v,int index){
		value = v;
		vindex = index;
	}
	public void setCDAnime(String str){
		// TODO 设置冷却动画
		cdanime = Animeconst.GetAnimeByName(str);
	}
	/**
	 * @param i
	 * @param j
	 */
	public void setOffset(int x, int y) {
		// TODO 设置冷却动画偏移
		x_offset = x;
		y_offset = y;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {	//绘制UI图像
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);	//绘制UI边界线
			}
			if(text!=null){			//绘制UI文字
				text.drawMe(this,g,p);
			}
			if(value!=null){
				int cd = value.getValue(vindex);
				if(cd>0){		//冷却>0
					cd=cd/100+1;		//换算成秒
					g.setFont(f);
					g.setColor(c);
					g.drawString(cd+"",(int)getRealX()+text_x, (int)getRealY()+text_y);
				}
				else if(cdanime!=null){			//冷却完毕
					g.drawImage(cdanime.getImage(),(int)getRealX()+x_offset, (int)getRealY()+y_offset, p );
				}
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);	//遍历子UI绘制
			}
		}
	}
}
