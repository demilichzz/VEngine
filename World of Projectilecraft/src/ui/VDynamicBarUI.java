/**
 * �ļ����ƣ�VDynamicBarUI.java
 * ��·����ui
 * ������TODO ��̬��UI,����ָ����VValueInterface���ݽӿڻ��Ƴ��ȱ仯��UI
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-13����02:55:40
 * �汾��Ver 1.0
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
	protected int xmax=1;						//��ֵ���ֵ,������ֵΪ��ֵʱ��ʾȫ������ͼ��
	protected VValueInterface v;			//��̬��ֵ�ӿ�
	protected int index;					//��̬��ֵ����
	protected VImageInterface coverimage;	//��̬������ͼ��,ԭimageΪ����ͼ��
	protected VImageInterface staticcover;	//��̬����ͼ��,�ڶ�̬����һ��
	protected int dw,dh;					//��̬ͼ���
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
		// TODO ����UI��Ӧ��ֵ����ʾ���ֵ
		this.xmax = xmax;
	}
	public void setValueDisplay(boolean d,int x,int y){
		// TODO �����Ƿ���ʾ��ֵ����ʾ����
		ifdisplayvalue=d;
		vxoff = x;
		vyoff = y;
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(coverimage!=null&&v!=null){
				double d = dw-(dw*(v.getValue(index))/xmax);	//���㵱ǰ��̬�������ĳ���
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
			if(ifdisplayvalue&&v!=null){		//�Ƿ���ʾ������ֵ
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
