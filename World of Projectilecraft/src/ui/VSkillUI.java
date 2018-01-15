/**
 * 	����UI����ʾ����ͼ��ͼ�����ȴ��������ȴ����ʱ��ʾ�����߿�
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
		// TODO ������ȴ����
		cdanime = Animeconst.GetAnimeByName(str);
	}
	/**
	 * @param i
	 * @param j
	 */
	public void setOffset(int x, int y) {
		// TODO ������ȴ����ƫ��
		x_offset = x;
		y_offset = y;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {	//����UIͼ��
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {
				area.drawMe(g2d, p);	//����UI�߽���
			}
			if(text!=null){			//����UI����
				text.drawMe(this,g,p);
			}
			if(value!=null){
				int cd = value.getValue(vindex);
				if(cd>0){		//��ȴ>0
					cd=cd/100+1;		//�������
					g.setFont(f);
					g.setColor(c);
					g.drawString(cd+"",(int)getRealX()+text_x, (int)getRealY()+text_y);
				}
				else if(cdanime!=null){			//��ȴ���
					g.drawImage(cdanime.getImage(),(int)getRealX()+x_offset, (int)getRealY()+y_offset, p );
				}
			}
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);	//������UI����
			}
		}
	}
}
