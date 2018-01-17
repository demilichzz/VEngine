/**
 * @author Demilichzz
 *	DBM��ʱ��UI�����������ʱ��ͽ���ʱ���Զ��任���ȼ�����λ��
 * 2012-12-12	
 */
package ui;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import jgui.GamePanel;
import config.ProjectConfig;
import global.Global;
import global.Imageconst;
import system.VEngine;
import view.VImageInterface;

/**
 * @author Demilichzz
 *
 * 2012-12-12
 */
public class VDBMBarUI extends VUI{
	protected VDBMPanelUI parentpanel;
	protected VImageInterface coverimage;	//��̬������ͼ��,ԭimageΪ����ͼ��
	protected VImageInterface staticcover;	//��̬����ͼ��,�ڶ�̬����һ��	
	protected int dw,dh;					//��̬ͼ���
	protected String name;		//��������
	protected String timestring="";	//ʣ��ʱ���ַ���
	protected double duration;		//����ʱ��
	protected int startframe;	//��ʼ�ؿ�����֡
	protected int endframe = 1;
	protected int currentframe;
	protected int runcount = 0;
	protected double percent = 0.0;		//��ʾ�ٷֱ�
	protected boolean activate = false;
	protected boolean prompted=false;	//�Ƿ�������
	protected boolean flash = false;	//�Ƿ���˸��ʱ��
	
	public VDBMBarUI(String str,String ID){
		super(str,ID);
		autoupdate=true;
		setDefaultImage();
	}
	public void setDefaultImage(){
		// TODO Ĭ��ͼ�����
		//image = Imageconst.GetImageByName("UI_200x20_DBMbottom.png");
		coverimage=Imageconst.GetImageByName("UI_200x20_DBMcover.png");
		dw = coverimage.getWidth();
		dh = coverimage.getHeight();
		staticcover=Imageconst.GetImageByName("UI_200x20_DBM_stcover.png");
	}
	public void updateUI(){
		if(autoupdate){
			//-----------����ʣ��ʱ�䲢ת���ַ�����ʽΪʱ����ʾ��ʽ---------------
			int remainf = endframe - runcount;		//ʣ��֡��
			int remaintime = remainf*VEngine.gs.getMSecond()/10;	//ʣ��ʱ�䣬��ȷ��0.01s
			int rt_decimal = remaintime%100;	//С������
			int rt_second = (remaintime/100)%60;	//�벿��
			int rt_minute = remaintime/6000;	//�ֲ���
			String str_m = Integer.toString(rt_minute);
			String str_s = Integer.toString(rt_second);
			String str_d = Integer.toString(rt_decimal);
			if(rt_minute<10){
				str_m = "0"+str_m;	//��0
			}
			if(rt_second<10){
				str_s = "0"+str_s;
			}
			if(rt_decimal<10){
				str_d = "0"+str_d;
			}
			timestring = str_m+":"+str_s+"."+str_d;	//����
			//--------------------------------------------------------------------
			if(endframe-runcount<500){	//ʣ��ʱ�䲻��5s
				parentpanel.promoteTimerBar(this);	//���ε��ã������ü�ʱ��Ϊ����״̬
			}
			if(endframe-runcount<300){	//ʣ��ʱ�䲻��3s
				flash = true;
			}
		}
		if(runcount>=endframe){	//��ʱ������
			this.visible=false;
			this.removeUIRelation();	//�Ƴ��������´β����ٸ���
			parentpanel.removeActivatedTimerBar(this);
			parentpanel.stacksize--;
		}
		else{
			percent = (double)runcount/(double)endframe;
		}
		super.updateUI();		//�ݹ����
		runcount++;
	}
	public void setSkill(String name,double duration){
		// TODO ���ü�����Ϣ
		this.name = name;
		this.duration=duration;
		endframe = (int) (duration/VEngine.gs.getSecond());
	}
	public void setParentPanel(VDBMPanelUI pui) {
		// TODO ����װ�ؼ�ʱ��UI��DBM��壬��������ʱ��Ӧ��������ջ����
		parentpanel=pui;
	}
	public void setActivate(boolean b){
		activate=b;
	}
	public boolean getActivate(){
		return activate;
	}
	public void moveTo(double tarx,double tary,double speed){
		// TODO ��ÿ�θ���ʱ���ã�ʹDBM��ʱ����ָ������λ�ƣ��ٶ�px/s
		if(x==tarx&&y==tary){
			
		}
		else{
			int direction=0;
			if(tary>y){	//����
				direction=1;
			}
			else{
				direction=-1;
			}
			double distance = speed*VEngine.gs.getSecond();	//���θ����ƶ���
			double movey = y+distance*direction;
			if((movey>tary&&direction==1)||(movey<tary&&direction==-1)){
				y = tary;
			}
			else{
				y = movey;
			}
		}
	}
	public void drawUI(Graphics g, GamePanel p) {
		// TODO ����UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(flash){
				float alpha = Math.abs(20-runcount%40);
				alpha=alpha/20;
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);	//͸���� 
				g2d.setComposite(ac);
			}
			if(coverimage!=null){
				double d = dw-(dw*percent);	//���㵱ǰ��̬�������ĳ���
				int di = (int)d;
				g2d.drawImage(coverimage.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+dw-di, (int)getRealY()+dh,di,0,dw,dh,p);
			}
			if(flash){
				g2d.setPaintMode();		//���ð�͸��Graphic
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
			g.setFont(Global.Font_DBMTimer);
			g.setColor(Global.Color_DBMTimer);
			g.drawString(name, (int)getRealX()+4, (int)getRealY()+15);
			g.drawString(timestring, (int)getRealX()+80, (int)getRealY()+15);
			for (VUI child = firstChild; child != null; child = child.nextUI) {
				child.drawUI(g2d, p);
			}
		}
	}
}
