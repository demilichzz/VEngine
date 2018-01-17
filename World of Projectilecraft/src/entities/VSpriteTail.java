/**
 * 	TODO 曳尾弹
 */
package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import jgui.GamePanel;
import ui.VCombatAreaUI;
import view.VImageInterface;
import global.VMath;
import interfaces.VCombatObject;
import interfaces.VPointProxy;

/**
 * @author Demilichzz
 *
 * 2012-11-15
 */
public class VSpriteTail extends VSprite{
	protected double[] xlist=new double[5];	//尾坐标列表
	protected double[] ylist=new double[5];
	//protected int updatecount=0;		//更新计数，控制尾坐标更新
	protected int updaterate=5;		//更新次数
	
	public VSpriteTail(){
		super();
		CType = 2;
	}
	public VSpriteTail(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		// TODO
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
		CType = 2;
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态
		if (alive && lifetime_f > 0) {
			if(spaction!=null){
				spaction.action(this);
			}
			if(updatecount%updaterate==0){
				for(int i=0;i<xlist.length-1;i++){			//更新尾坐标
					xlist[xlist.length-i-1]=xlist[xlist.length-i-2];
					ylist[xlist.length-i-1]=ylist[xlist.length-i-2];
				}
				xlist[0]=x;
				ylist[0]=y;
			}
			move();
			LifeMinus();
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
				if(!getBound()){
					die();
				}
			}
		}
		else {
			die();
		}
		updatecount++;
	}
	public void drawImage(Graphics g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO Auto-generated method stub
		VImageInterface image = null;		//绘制的图像，根据参数i决定是上层还是下层图像
		int index=0;
		if(i==0){
			image = image_top;
			index = t_index;
		}
		else if(i==1){
			image = image_bottom;
			index = b_index;
		}
		// 获取真实粒子中心坐标
		int x = (int) (this.GetX()+plat.getRealX());
		int y = (int) (this.GetY()+plat.getRealY());
		int iw = this.getImageByLayer(i).getWidth()/2;
		int ih = this.getImageByLayer(i).getHeight()/2;
		int x_i = x-iw;
		int y_i = y-ih;
		Image ib = image.getImage(index);	//获取image
		if(ifrotate){	//如果需求旋转
			AffineTransform trans = new AffineTransform ();
			trans.translate(x_i,y_i);	//位移矩阵
		    trans.rotate(getRotate(),x-x_i,y-y_i);	//旋转矩阵
		    Graphics2D g2d = (Graphics2D)g;
		    g2d.drawImage(ib,trans,p);
		}
		else{			//不旋转则直接绘制原image
			if(image!=null){
				for(int ti=0;ti<xlist.length;ti++){
					g.drawImage(ib,(int)(xlist[xlist.length-ti-1]+plat.getRealX()-iw),
							(int)(ylist[ylist.length-ti-1]+plat.getRealY()-ih),p);
				}
				g.drawImage(ib, x_i, y_i, p);
			}
		}
		if(anime_state==1){		//绘制生成动画
			if(anime_born!=null){
				int x_anime=(int) (ab_x+plat.getRealX()-anime_born.getWidth()/2);
				int y_anime=(int) (ab_y+plat.getRealY()-anime_born.getWidth()/2);
				g.drawImage(anime_born.getImage(), x_anime, y_anime, p);
			}
		}
	}
	public void setTailLength(int length){
		// TODO 设置尾节点数量
		if(length>1){
			xlist = new double[length];
			ylist = new double[length];
		}
		setOverallCor(x,y);
	}
	public void setOverallCor(double x,double y){
		// TODO 设置包括所有尾节点的位置
		setCor(x,y);
		for(int i=0;i<xlist.length;i++){
			xlist[i]=x;
			ylist[i]=y;
		}
	}
	public double[] getTail(int index){
		// TODO 获取尾坐标列表
		if(index==0){
			return xlist;
		}
		else if(index==1){
			return ylist;
		}
		return null;
	}
}
