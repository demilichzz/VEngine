/**
 * @author Demilichzz
 *	DBM计时条UI，会根据生成时间和结束时间自动变换长度及更改位置
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
	protected VImageInterface coverimage;	//动态条覆盖图像,原image为背景图像
	protected VImageInterface staticcover;	//静态覆盖图像,在动态条上一层	
	protected int dw,dh;					//动态图宽高
	protected String name;		//技能名称
	protected String timestring="";	//剩余时间字符串
	protected double duration;		//持续时间
	protected int startframe;	//起始关卡更新帧
	protected int endframe = 1;
	protected int currentframe;
	protected int runcount = 0;
	protected double percent = 0.0;		//显示百分比
	protected boolean activate = false;
	protected boolean prompted=false;	//是否已提升
	protected boolean flash = false;	//是否闪烁计时条
	
	public VDBMBarUI(String str,String ID){
		super(str,ID);
		autoupdate=true;
		setDefaultImage();
	}
	public void setDefaultImage(){
		// TODO 默认图像参数
		//image = Imageconst.GetImageByName("UI_200x20_DBMbottom.png");
		coverimage=Imageconst.GetImageByName("UI_200x20_DBMcover.png");
		dw = coverimage.getWidth();
		dh = coverimage.getHeight();
		staticcover=Imageconst.GetImageByName("UI_200x20_DBM_stcover.png");
	}
	public void updateUI(){
		if(autoupdate){
			//-----------计算剩余时间并转换字符串格式为时间显示格式---------------
			int remainf = endframe - runcount;		//剩余帧数
			int remaintime = remainf*VEngine.gs.getMSecond()/10;	//剩余时间，精确到0.01s
			int rt_decimal = remaintime%100;	//小数部分
			int rt_second = (remaintime/100)%60;	//秒部分
			int rt_minute = remaintime/6000;	//分部分
			String str_m = Integer.toString(rt_minute);
			String str_s = Integer.toString(rt_second);
			String str_d = Integer.toString(rt_decimal);
			if(rt_minute<10){
				str_m = "0"+str_m;	//补0
			}
			if(rt_second<10){
				str_s = "0"+str_s;
			}
			if(rt_decimal<10){
				str_d = "0"+str_d;
			}
			timestring = str_m+":"+str_s+"."+str_d;	//连接
			//--------------------------------------------------------------------
			if(endframe-runcount<500){	//剩余时间不足5s
				parentpanel.promoteTimerBar(this);	//单次调用，提升该计时条为激活状态
			}
			if(endframe-runcount<300){	//剩余时间不足3s
				flash = true;
			}
		}
		if(runcount>=endframe){	//计时条结束
			this.visible=false;
			this.removeUIRelation();	//移除关联，下次不会再更新
			parentpanel.removeActivatedTimerBar(this);
			parentpanel.stacksize--;
		}
		else{
			percent = (double)runcount/(double)endframe;
		}
		super.updateUI();		//递归更新
		runcount++;
	}
	public void setSkill(String name,double duration){
		// TODO 设置技能信息
		this.name = name;
		this.duration=duration;
		endframe = (int) (duration/VEngine.gs.getSecond());
	}
	public void setParentPanel(VDBMPanelUI pui) {
		// TODO 设置装载计时条UI的DBM面板，以在销毁时响应减少面板的栈计数
		parentpanel=pui;
	}
	public void setActivate(boolean b){
		activate=b;
	}
	public boolean getActivate(){
		return activate;
	}
	public void moveTo(double tarx,double tary,double speed){
		// TODO 在每次更新时调用，使DBM计时条向指定方向位移，速度px/s
		if(x==tarx&&y==tary){
			
		}
		else{
			int direction=0;
			if(tary>y){	//方向
				direction=1;
			}
			else{
				direction=-1;
			}
			double distance = speed*VEngine.gs.getSecond();	//单次更新移动量
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
		// TODO 绘制UI
		if (visible) {
			Graphics2D g2d = (Graphics2D) g;
			if (image != null) {
				g.drawImage(image.getImage(), (int)getRealX(), (int)getRealY(), p);
			}
			if(flash){
				float alpha = Math.abs(20-runcount%40);
				alpha=alpha/20;
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);	//透明度 
				g2d.setComposite(ac);
			}
			if(coverimage!=null){
				double d = dw-(dw*percent);	//计算当前动态条削减的长度
				int di = (int)d;
				g2d.drawImage(coverimage.getImage(), (int)getRealX(), (int)getRealY(),(int)getRealX()+dw-di, (int)getRealY()+dh,di,0,dw,dh,p);
			}
			if(flash){
				g2d.setPaintMode();		//重置半透明Graphic
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
