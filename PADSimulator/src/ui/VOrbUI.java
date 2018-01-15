/**
 * @author Demilichzz
 *	转珠UI
 * 2014-1-6
 */
package ui;

import interfaces.VValueInterface;

import org.newdawn.slick.Color;

import global.Imageconst;
import global.VMath;
import data.GameData;
import data.StaticData;
import entities.VOrb;
import event.GlobalEvent;
import system.VEngine;
import view.VTexture;

/**
 * @author Demilichzz
 *
 * 2014-1-6
 */
public class VOrbUI extends VMouseActionUI implements VValueInterface{
	public static double orbwidth = 85;
	
	protected VTexture plusimage = null;
	protected VTexture lockimage = null;
	protected VTexture translateimage=null;		//宝石转换时使用的动画效果
	protected VDynamicBarUI timerdbui = null;		//转珠计时条ui
	protected int movetimer = 0;		//转珠计时
	protected boolean startmoving = false;
	protected VOrb bindorb = null;
	protected int orbImageIndex;
	protected int update_count = 0;	//UI更新计数，以模拟宝石斜拉处理
	protected int grid_x=-1;	//宝珠UI网格坐标
	protected int grid_y=-1;
	public int choseX = -1;		//鼠标浮动指向宝珠UI对应的原始宝珠UI网格坐标
	public int choseY = -1;
	protected int gemindex=0;	//宝石类型索引
	protected int transp_timer=-1;	//宝珠UI透明度计数，在updateUI中控制以实现宝珠消失动画
	protected int drop_timer = 0;	//宝珠下落时间计数，在updateUI中控制以实现宝珠下落动画
	protected int translate_timer=0;	//宝石转换时间计数，用于控制宝石转换动画
	
	public VOrbUI(int width, int height, String ID) {
		super(width, height, ID);
		allowdrag = true;
		plusimage = Imageconst.GetImageByName("gem_plus.png");
		lockimage = Imageconst.GetImageByName("gem_lock.png");
	}
	public VOrbUI(String str,String ID){
		super(str,ID);
		allowdrag = true;
		plusimage = Imageconst.GetImageByName("gem_plus.png");
		lockimage = Imageconst.GetImageByName("gem_lock.png");
	}
	public void setGridLoc(int x,int y){
		// TODO 设置网格坐标
		grid_x = x;
		grid_y = y;
	}
	public void setTranspCount(int c){
		// TODO 设置Combo时间计数
		transp_timer = c;
	}
	public void setDropTimer(int t){
		drop_timer = t;
	}
	public void bindOrb(VOrb orb){
		// TODO 绑定宝石
		bindOrb(orb,false);
	}
	public void bindOrb(VOrb orb,boolean b){
		// TODO 绑定宝石，参数为是否显示转换过程
		this.bindorb = orb;
		if(b){
			if(orbImageIndex!=orb.getValue(VOrb.ORB_TYPE)){
				GlobalEvent.getGameAreaUI().uiLock(800);	//转换动画过程锁定游戏区域及队伍UI
				GlobalEvent.getPartyUI().uiLock(800);
				translateimage=image;
				translate_timer=80;
			}
		}
		this.setImage(orb.getValue(VOrb.ORB_TYPE));
		orbImageIndex=orb.getValue(VOrb.ORB_TYPE);
	}
	public void setImage(int index){
		// TODO 根据索引重设UI图像
		if(index<1){
			this.setImage("zblank.png");
		}
		else{
			this.setImage(StaticData.gempiclist[index-1]);
		}
	}
	public void addTimerDBUI(VDynamicBarUI dbui){
		this.timerdbui = dbui;
		dbui.setParent(this);
		dbui.bindValue(this, 0);
		this.setScale(this.scale);
	}
	public void setScale(double scale){
		super.setScale(scale);
		if(timerdbui!=null){
			timerdbui.setScale(scale);
			timerdbui.setLoc(0,80*scale);
		}
	}
	public void setIndex(int i){
		// TODO 设置UI的宝石样式索引，并相应设置图片
		gemindex = i;
		if(i<1){
			this.setImage("zblank.png");
		}
		else{
			this.setImage(StaticData.gempiclist[i-1]);
		}
	}
	public int getIndex(){
		if(bindorb!=null){
			return bindorb.getValue(VOrb.ORB_TYPE);
		}
		else{
			return -1;
		}
		
	}
	public void uiDragPress(int x, int y) {
		// TODO 点击UI时调用，切换UI是否被拖拽的状态并根据点击位置设置偏移
		if(enable){
			if(!pressstate&&allowdrag){
				GameData.pathAI.resetAIState();	//点击时重置AI状态
				GameData.pathAI.loadOrbs(GameData.ga.orbs);	//加载移动前的宝石矩阵数据
				GameData.pathAI.addMove(5);	//添加初始不移动位置的移动步数
				setTransparency(0.5);		//点击位置的UI设置透明度为0.5
				//VGameAreaUI gaui = GlobalEvent.getGameAreaUI();
				//GameData.ga.resetOrbState();
				VOrbUI mhui = GlobalEvent.getMouseHoldUI();		//获取跟随鼠标移动的宝石UI
				mhui.bindOrb(this.bindorb);
				mhui.setIndex(this.getIndex());		//设置鼠标拖拽UI的宝珠索引为点击的UI
				mhui.setVisible(true);				//并显示鼠标拖拽UI
				mhui.setLoc(x-orbwidth/2, y-orbwidth/2);	//将鼠标拖拽UI的位置设置为当前鼠标位置
				mhui.pressstate = true;
				mhui.choseX = this.grid_x;	//设置拖拽UI的起始位置网格坐标
				mhui.choseY = this.grid_y;
				GameData.pathAI.loadChosenOrb(grid_x,grid_y);	//将当次选取的宝石网格坐标加载入路径AI对象
				VEngine.newgame.mcontroller.setActiveUI(mhui);
				//activeoffx = (int)this.GetX()-x;
				//activeoffy = (int)this.GetY()-y;
			}
		}
	}
	public void uiDragMoveTo(int x,int y){
		// TODO 将UI拖拽至指定位置，参数x,y为拖拽时的鼠标坐标
		if(this.update_count==0){
			if(pressstate&&allowdrag){
				VGameAreaUI gaui = GlobalEvent.getGameAreaUI();
				double x_area = x-gaui.getRealX();
				double y_area = y-gaui.getRealY();	//获取鼠标指向点在GameAreaUI中的位置
				x_area = VMath.restrictDouble(x_area, 0, GameData.gameareaWidth-1);
				y_area = VMath.restrictDouble(y_area, 0, GameData.gameareaHeight-1);	//限制鼠标位置于GameArea内
				this.setLoc(x_area+gaui.getRealX()-orbwidth/2, y_area+gaui.getRealY()-orbwidth/2);
				//System.out.println(""+x_area+","+y_area);
				int cu_grid_x = (int) (x_area/orbwidth);
				int cu_grid_y = (int) (y_area/orbwidth);	//计算当前鼠标处于的网格坐标
				if(cu_grid_x!=choseX||cu_grid_y!=choseY	){	//当前网格非起始格，则宝珠互换
					int x_differ = cu_grid_x - choseX;
					int y_differ = cu_grid_y - choseY;
					if(x_differ*y_differ==0||Math.abs(x_differ)==Math.abs(y_differ)){
						// 仅当目标宝石与原宝石在同一斜线或直线上时，进行交换
						gaui.subui[choseY][choseX].setTransparency(1.0);
						GameData.ga.orbSwap(this,cu_grid_x,cu_grid_y);
						gaui.subui[choseY][choseX].setTransparency(0.5);
						this.startMoving(true);
					}			
					//GameData.ga.gemSwap(choseX,choseY,cu_grid_x,cu_grid_y);
					//this.choseX = cu_grid_x;
					//this.choseY = cu_grid_y;
				}
			}
		}
	}

	private void startMoving(boolean b) {
		// TODO 开始移动，进行转珠计时
		if(!startmoving&&b){	//如果此次调用为开始移动
			this.setValue(0, GameData.party.getMoveTime());
		}
		if(startmoving&&!b){
			
		}
		startmoving = b;
	}
	public void uiDragRelease(int x,int y){
		// TODO 释放UI拖拽
		if(enable){
			if(pressstate&&allowdrag){
				this.startMoving(false);
				pressstate = false;
				activeoffx = 0;
				activeoffy = 0;
				VGameAreaUI gaui = GlobalEvent.getGameAreaUI();
				double x_area = x-gaui.getRealX();
				double y_area = y-gaui.getRealY();	//获取鼠标指向点在GameAreaUI中的位置
				x_area = VMath.restrictDouble(x_area, 0, GameData.gameareaWidth-1);
				y_area = VMath.restrictDouble(y_area, 0, GameData.gameareaHeight-1);	//限制鼠标位置于GameArea内
				int cu_grid_x = (int) (x_area/orbwidth);
				int cu_grid_y = (int) (y_area/orbwidth);	//计算当前鼠标处于的网格坐标
				for(int i=0;i<GameData.ga.orb_maxy;i++){
					for(int j=0;j<GameData.ga.orb_maxx;j++){
						gaui.subui[i][j].setTransparency(1.0);
					}
				}
				//gaui.subui[cu_grid_y][cu_grid_x].setTransparency(1.0);
				GlobalEvent.getMouseHoldUI().setVisible(false);
				//GlobalEvent.getGameAreaUI().uiAction("UIrestore");
				//GameData.pathAI.print();
				if(GameData.ga.moved){
					GameData.party.getPartyAwokenState();
					boolean matched = GameData.ga.orbsExecute();	 	//释放时进行宝珠消除计算
					if(matched){
						//GameData.party.dealMatchAction();
					}
				} 
			}
		}
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			drawUIText();
			drawUIBorder();
			drawSubUI();
		}
	}
	protected void drawUIImage(){
		if (image != null) {	//绘制UI图像
			image.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
		}
		else{
			Imageconst.nullpic.directDrawTexture(getRealX(), getRealY(), -1,scale);
		}
		if(translateimage!=null){	//宝石转换动画过程
			Color t_c=new Color(255,255,255,1.0f*translate_timer/80+0.2f);
			double t_scale = 1.0-1.0*Math.abs((translate_timer-40))/40;
			translateimage.directDrawTextureFromCenter(getRealX()+getWidth()/2, getRealY()+getHeight()/2, -1,t_scale,t_c);
		}
		if(bindorb.getValue(VOrb.ORB_PLUS)==1){
			plusimage.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
		}
		if(bindorb.getValue(VOrb.ORB_LOCK)==1){
			lockimage.directDrawTexture(getRealX(), getRealY(), -1,scale,uicolor);
		}
	}
	public void updateUI(){
		// TODO 更新UI显示内容,在每次GS更新时调用保证显示与实际数据同步,在继承VUI的需要的子类中重载
		//		并用于实现子UI中的UI效果
		update_count = update_count+1;
		if(startmoving&&movetimer>0){
			movetimer--;
			if(movetimer==0){
				VEngine.newgame.mcontroller.dealRelease();
				//this.uiDragRelease((int)this.getRealX(), (int)this.getRealY());
			}
		}
		if(update_count==3){
			update_count = 0;
		}
		if(transp_timer!=-1){
			transp_timer=transp_timer-1;
			if(transp_timer<50){
				double d = transp_timer;
				this.setTransparency(d/50);		//设置透明度
			}
			if(transp_timer==0){
				transp_timer=-1;
			}
		}
		if(drop_timer!=0){
			this.setTransparency(1.0);
			drop_timer=drop_timer-1;
			this.moveLoc(0, 5);		//如果宝珠为下落状态，则每次更新位置下移5像素
			
			if(drop_timer==0){
				drop_timer=0;
			}
		}
		if(translate_timer>0){
			translate_timer--;
			if(translate_timer==0){
				translateimage=null;
			}
		}
		// 递归更新子UI
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if(child.autoupdate){
				child.updateUI();
			}
		}
	}
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		if(index==0){
			return movetimer;
		}
		else{
			return -1;
		}
	}
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		if(index==0){				//设置转珠最大时间
			movetimer = value;
			if(timerdbui!=null){
				timerdbui.setMaxValue(value);		//为计时条绑定最大值
			}
		}
	}
}
