/**
 * @author Demilichzz
 *	���ֵ������UI���뵱ǰ��Ҷ���ĳ��ֵ����
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
	protected VImageInterface[] coverimage=new VImageInterface[4];	//��̬������ͼ��,ԭimageΪ����ͼ��
	protected VImageInterface staticcover;	//��̬����ͼ��,�ڶ�̬����һ��
	protected int dw,dh;		//��̬ͼ���
	protected boolean displayvalue=true;	//�Ƿ���ʾ��ֵ
	protected int threatmax = 12000;
	protected int vxoff=-10,vyoff=100;		//�����ֵ����ƫ��
	
	protected VPlayer player;		//�󶨵���Ҷ���
	
	public VThreatBarUI(String str,String ID){
		// TODO ʹ��Ĭ�ϸ���ͼ��ĳ��ֵUI
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
		// TODO �����
		this.player = p;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {		//��ͼ
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(coverimage!=null&&player!=null){		//���Ƴ��ֵ��̬����
				int imageindex = player.getThreatState();		//��ȡ��ҵĳ��״̬
				double d = dh-(dh*(player.getValue(3))/threatmax);	//���㵱ǰ��̬�������ĳ���
				int di = (int)d;
				g.drawImage(coverimage[imageindex].getImage(),(int)getRealX(),(int)getRealY()+di,(int)getRealX()+dw,(int)getRealY()+dh,0,0,dw,dh-di,p);
				//g.drawImage(coverimage.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+dw-di, (int)getRealY()+dh,di,0,dw,dh,p);
			}
			if(staticcover!=null){		//��̬����ͼ
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
			if (ProjectConfig.getConfigValue(ProjectConfig.SHOWUIBORDER) == 1) {	//UI�߽�
				area.drawMe(g2d, p);
			}
			if(text!=null){
				text.drawMe(this,g,p);
			}
			/*if(ifdisplayvalue&&v!=null){		//�Ƿ���ʾ������ֵ
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
