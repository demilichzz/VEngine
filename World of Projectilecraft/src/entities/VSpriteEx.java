/**
 * @author Demilichzz
 *	扩展粒子类，继承基础粒子类的同时具有原Gravity,Corpse,Reflect粒子和时间线行为等特性
 * 2012-12-5
 */
package entities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import jgui.GamePanel;

import timer.*;
import ui.VCombatAreaUI;
import view.VImageInterface;
import global.Debug;
import global.VMath;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2012-12-5
 */
public class VSpriteEx extends VSprite{
	protected VLuaSPAction deathaction = null;
	protected VLuaSPAction reflectaction = null;
	protected VLuaSPAction updateaction = null;
	protected int deathactionstate=1;		//执行死亡行为的条件，值为各种条件对应的boolean值复合，
											//默认状态为仅当时间结束时执行
	protected double forceangle=0;	//作用力角度
	protected double forceaccel=0;	//作用力加速度
	protected int forceendframe=0;	//作用力停止帧，默认为0不会计算作用力
	protected int refmode = 0;	//二进制法高到低位表示上下左右壁是否反弹,取值0-15，默认不反弹
								//取值11时下不反弹
	protected int reftime = 0;	//反弹次数，-1为不限，默认为0不反弹
	protected int refx,refy,refw,refh;	//反弹边缘
	protected boolean invisiblemode=false;	//隐形模式，为隐形模式时半透明显示且无判定
	protected double sprad;		//记录粒子半径
	protected ArrayList<int[]> invisibletimeline = new ArrayList<int[]>();
	protected ArrayList<int[]> actiontimeline = new ArrayList<int[]>();	//时间线，每个时间节点记录行为执行帧的除数，余，上下限，用于控制间隔
	
	public VSpriteEx(){
		super();
	}
	public VSpriteEx(String image_t,String image_b,int t_index,int b_index,
			   double lifetime,double c_rad,double angle,VCombatObject owner){
		super(image_t, image_b, t_index, b_index, lifetime, c_rad, angle, owner);
		sprad=c_rad;
	}	
	public void addDeathAction(VLuaSPAction action){
		deathaction = action;
	}
	public void addReflectAction(VLuaSPAction action){
		reflectaction = action;
	}
	public void addUpdateAction(VLuaSPAction action){
		updateaction = action;
	}
	public void addTimeNode(int divide,int mod,int min,int max){
		// TODO 向时间线添加执行更新行为的时间节点
		int[] node = new int[]{divide,mod,min,max};
		actiontimeline.add(node);
	}
	public void addInvTimeNode(int divide,int mod,int min,int max){
		// TODO 添加隐形时间线
		int[] node = new int[]{divide,mod,min,max};
		invisibletimeline.add(node);
	}
	public void setRad(double rad){
		// TODO 覆盖父类设置半径，设置的同时在另一个不会更改的变量中记录，以便切换透明模式使用
		this.collrad=rad;
		this.sprad=rad;
	}
	public void clearTimeLine(){
		// TODO 清空时间线
	}
	public void setDeathActionState(int i){
		// TODO 设置死亡行为条件
		deathactionstate = i;
	}
	public void setReflectArea(int x,int y,int w,int h){
		refx = x;
		refy = y;
		refw = w;
		refh = h;
	}
	public void setReflectMode(int mode){
		refmode = mode;
	}
	public void setReflectTime(int rt) {
		// TODO Auto-generated method stub
		this.reftime = rt;
	}
	public void setForce(double angle,double accel,int f){
		// TODO 设置作用力参数
		forceangle=angle;
		forceaccel=accel;
		forceendframe=f;
	}
	//---------------------------------------------------------------------------------------------------
	//------------以下是粒子的更新机制与渲染等功能函数-----------------------------------------------------
	
	public void die() {
		// TODO 粒子死亡
		born=true;
		alive=false;
	}
	public void die(int index){
		if(VMath.getBinaryValue(deathactionstate, index)==1){	//如果行为条件满足
			if(deathaction!=null){
				deathaction.action(this);		//执行死亡行为
			}
		}
		die();
	}
	public void drawImage(Graphics2D g, GamePanel p, int i,VCombatAreaUI plat) {
		// TODO 绘制粒子图像
		if(invisiblemode){
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.2);	//透明度 
			g.setComposite(ac);
		}
		super.drawImage(g, p, i, plat);
		g.setPaintMode();		//重置Graphic模式
	}
	public void spriteUpdate() {
		// TODO 更新粒子状态
		if (alive && (lifetime_f > 0||lifetime_f==-1000)) {	//存活且剩余时间>0
			forcemove();	//在运行前施加作用力
			reflect();		//在运行前计算反弹
			timelineprocess();	//处理时间线事件
			invisibleprocess();	//处理隐形时间线
			move();			//移动		
			LifeMinus();	//剩余时间-每帧时间
			if(restricted){	//如果粒子范围受限，则检查范围，不在指定范围则死亡
				if(!getBound()){
					die(DEATHSTATE_OUTBOUND);
				}
			}
			if(debugmark){	//如果该粒子是调试状态
				Debug.DebugSimpleMessage("X:"+this.GetX()+",Y:"+this.GetY());	//每帧输出其坐标
			}
		}
		else {
			die(DEATHSTATE_TIMEEND);
		}
		if(rotatemode==2||rotatemode==3){
			double rotate = VMath.StandardizationAngle(updatecount*rotatespeed/100+Math.PI/2);	//在更新过程中计算旋转值
			this.setRotate(rotate);
		}
		updatecount++;
	}

	private void invisibleprocess() {
		// TODO 处理隐形时间线
		boolean mark=false;
		if(invisibletimeline.size()>0){
			for(int[] node:invisibletimeline){	//遍历时间线节点获取当前时间点的隐形状态
				if(checknode(node)){
					mark=true;
				}
			}
		}
		if(mark){
			invisiblemode=true;
			collrad=0;			//碰撞为0不进行碰撞
		}
		else{
			invisiblemode=false;
			collrad=sprad;		//恢复碰撞
		}
	}
	private void timelineprocess() {
		// TODO 处理时间线行为
		if(actiontimeline.size()>0&&updateaction!=null){		//时间线非空且行为非空
			for(int[] node:actiontimeline){
				if(checknode(node)){	//如果节点合法且当前更新帧数满足节点
					updateaction.action(this);
				}
			}
		}
	}
	private boolean checknode(int[] node) {
		// TODO 检查节点是否合法
		if(node.length==4){
			if(node[0]>0&&node[1]<node[0]&&node[2]<node[3]){	//间隔>0且余数<间隔
				if(updatecount>=node[2]&&updatecount<=node[3]){	//更新次数满足范围
					if(updatecount%node[0]==node[1]){	//更新次数对间隔求余满足余数
						return true;		//在以上时间节点参数定义的时间点执行行为
					}
				}
			}
		}
		return false;
	}
	private void forcemove() {
		// TODO 根据作用力调整粒子运行角度和速度
		//setAngle(getAngle());		//标准化角度
		if(updatecount<forceendframe||forceendframe==-1){
			if (forceaccel != 0) {
				double vx = Math.cos(angle) * speed + Math.cos(forceangle)
						* forceaccel;
				double vy = Math.sin(angle) * speed + Math.sin(forceangle)
						* forceaccel; // 计算 xy向量
				double newangle = 0;
				if (vx != 0) {
					newangle = Math.atan2(vy,vx);	//atan2返回弧度值
				}
				else {
					if (vy > 0) {
						newangle = Math.PI * 0.5;
					} else {
						newangle = Math.PI * 1.5;
					}
				}
				double newspeed = Math.sqrt(vx * vx + vy * vy);
				this.setAngle(newangle);
				this.setSpeed(newspeed);
			}
		}
	}
	protected void reflect(){
		// TODO 计算反弹角度
		if(reftime!=0&&(x<5||y<5||x>395||y>595)){
			VPointInterface vtar=VMath.PolarMove(this,angle, speed/100);	//计算下次移动的目标位置
			double xt=vtar.GetX();
			double yt=vtar.GetY();
			int ref_up = VMath.getBinaryValue(refmode, 3);
			int ref_down = VMath.getBinaryValue(refmode, 2);
			int ref_left = VMath.getBinaryValue(refmode, 1);
			int ref_right = VMath.getBinaryValue(refmode, 0);
			if(xt<1&&ref_left!=0){
				this.reflect(Math.PI-angle);
			}
			else if(xt>399&&ref_right!=0){
				this.reflect(Math.PI-angle);
			}
			if(yt<1&&ref_up!=0){
				this.reflect(-angle);
			}
			else if(yt>599&&ref_down!=0){
				this.reflect(-angle);
			}
		}
	}
	protected void reflect(double angle){
		// TODO 反弹设置角度，同时计数
		if(reftime!=0){
			if(reflectaction!=null){
				reflectaction.action(this);
			}
			this.setAngle(angle);
		}
		if(reftime>0){
			reftime--;
		}
	}
}
