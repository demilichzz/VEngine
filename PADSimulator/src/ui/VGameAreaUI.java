/**
 * @author Demilichzz
 *	转珠区域UI
 * 2014-1-5
 */
package ui;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import view.VText;

import data.GameData;
import data.StaticData;
import entities.GameArea;
import entities.VCharacter;
import entities.VMatchOrb;
import entities.VOrb;
import event.GlobalEvent;
import global.Fontconst;
import global.VMath;

/**
 * @author Demilichzz
 *
 * 2014-1-5
 */
public class VGameAreaUI extends VUI{	
	protected GameArea ga;
	protected VOrbUI subui[][];
	protected int orbdrop_count = -1;	//记录宝石消除需要的时间，并控制宝石下落过程的变量
	protected int droplock_timer = -1;	//在宝石掉落时锁定UI的控制变量
	
	protected double suborb_scale = 1.0;	//根据版面状态确定宝石UI缩放及长宽像素值
	protected int suborb_width = 100;
	protected int sizex=0;
	protected int sizey=0;
	protected int droptime=20;	//掉落时间(更新数)，每次更新默认掉落速度为5像素
	protected boolean showDisableCover = false;	//设置是否显示不可用状态的蒙版
	protected ArrayList<String[]> hintlist;		//提示信息列表
	protected ArrayList<double[]>hintcoords;		//提示信息坐标
	protected int hintindex;
	
	
	public VGameAreaUI(int width, int height, String ID,int sizeindex) {
		super(width,height,ID);
		resetHintList();
		initTextList();
		//this.setPanelSize(sizeindex);		//设置面板分布		
	}
	public VGameAreaUI(String str, String ID){
		super(str,ID);
		resetHintList();
		initTextList();
	}
	public void initTextList(){
		for(int i=0;i<60;i++){
			VText text = new VText("");
			text.setLayout(VText.Layout_CENTER);
			text.setFont("font_default");
			text.setStroke(false);
			text.setParentUI(this);
			text.setColor(150,255,255);
			text.setVisible(false);
			this.addText(text);
		}
	}
	public void initSubUI(){
		// TODO 初始化宝石子UI
		for(int i=0;i<sizey;i++){	//横行
			for(int j=0;j<sizex;j++){	//横行中从左到右宝石
				int index = i*sizex+j;
				subui[i][j] = new VOrbUI("fire.png","ui_gem_"+index);
				subui[i][j].setParent(this);
				subui[i][j].setScale(suborb_scale);
				subui[i][j].setLoc(j*suborb_width, i*suborb_width);
				subui[i][j].setGridLoc(j,i);
			}
		}
	}
	public void setShowDisableCover(boolean b){
		showDisableCover = b;
	}
	public void uiAction(String trigtype) {
		// TODO 参数为调用者类别字符串
		if (enable) {
			if (!trigtype.equals("MouseEvent")) {
				if (action != null) {
					action.action();
				}
			}
			if(trigtype.equals("UIrestore")){	//恢复UI未被选定的状态
				for(int i=0;i<sizey;i++){
					for(int j=0;j<sizex;j++){
						subui[i][j].setTransparency(1.0);
					}
				}
			}
			if(trigtype.equals("UIeliminate")){	//UI开始进行消除宝珠过程
				uiEliminate();
			}
			if(trigtype.equals("UIorbdrop")){	//UI进行宝石掉落过程
				
			}
		}
		if(trigtype.equals("UIupdate")){	//根据当前宝石矩阵生成UI，在宝石矩阵变化时更新UI显示
			for(int i=0;i<sizey;i++){
				for(int j=0;j<sizex;j++){
					subui[i][j].bindOrb(ga.orbs[i][j]);
				}
			}
		}
		if(trigtype.equals("UItranslate")){	//根据当前宝石矩阵生成UI，在宝石矩阵变化时更新UI显示
			for(int i=0;i<sizey;i++){
				for(int j=0;j<sizex;j++){
					subui[i][j].bindOrb(ga.orbs[i][j],true);
				}
			}
		}
	}
	public int uiOrbDrop(){
		// TODO UI进行宝石掉落的过程，在消除宝石结束后自动调用
		/*ga.orbsDrop();
		int[][] newgems = ga.generateNewGems();	//生成新的掉落宝石
		
		uiSetDropTimer(newgems);	//使用掉落前矩阵计算每个宝石需要掉落的时间
		newgems = VMath.matrixFloat(newgems);	//将新生成的掉落宝石数据上浮

		ga.orbsDropMatrix();	//计算掉落后矩阵
		ga.orbsDropMatrixCover(newgems);
		//VMath.printMatrix(ga.orbs);
		 * 
		 */
		ga.orbsDrop();
		VOrb[][] beforedrop = ga.orbs;
		VOrb[][] newgems = ga.generateNewGems();
		newgems = (VOrb[][]) VMath.matrixDrop(newgems,VOrb.ORB_TYPE);
		VOrb[][] refermatrix = new VOrb[beforedrop.length*2][beforedrop[0].length];	//定义掉落参考矩阵
		for(int i=0;i<beforedrop.length;i++){
			for(int j=0;j<beforedrop[0].length;j++){
				refermatrix[i+beforedrop.length][j]=beforedrop[i][j];
			}
		}
		for(int i=0;i<newgems.length;i++){
			for(int j=0;j<newgems[0].length;j++){
				refermatrix[i][j]=newgems[i][j];
			}
		}		//计算掉落参考矩阵，上半部分为下落处理过的新生成宝石，下半部分为消除后宝石矩阵
		//VMath.printMatrix(refermatrix,VOrb.ORB_TYPE);
		//System.out.println();
		setDropTime(refermatrix);	//设置下落时间计数
		
		
		//System.out.println(droplock_timer-1);
		if(droplock_timer==1){
			droplock_timer=0;
		}
		return droplock_timer-1;	//返回在uiSetDropTimer中设置的掉落时间-1，不掉落则返回-1	
	}
	public void setDropTime(VOrb[][] refer){
		// TODO 重置宝石对象的矩阵位置，并设置每个宝石UI下落的时间计数，进行下落过程
		int maxdroplevel = 0;
		for(int j=0;j<sizex;j++){
			int droplevel = 0;
			for(int i=0;i<sizey;i++){
				//if(refer[sizey*2-1-i][j].getValue(VOrb.ORB_TYPE)>0){	//参考矩阵的该项不为0
				//}
				//else{	//参考矩阵该项为0，则
				for(int k=sizey-1-i-droplevel;k>=0;k--){	//从参考矩阵指定值向上搜索
					if(refer[sizey+k][j].getValue(VOrb.ORB_TYPE)<=0){	//向上搜索空位，并增加掉落计数
						droplevel = droplevel+1;
					}
					else{	//遇到非空位则中断
						break;
					}
				}			
				//}
				ga.orbs[sizey-1-i][j]=refer[sizey*2-1-i-droplevel][j];	//将该位置的宝石索引修正
				subui[sizey-1-i][j].bindOrb(ga.orbs[sizey-1-i][j]);		//重新绑定UI
				subui[sizey-1-i][j].setDropTimer(droplevel*droptime);
				subui[sizey-1-i][j].setLoc(subui[i][j].GetX(),(sizey-1-i-droplevel)*VOrbUI.orbwidth);
			}
			if(droplevel>maxdroplevel){
				maxdroplevel = droplevel;
			}
		}
		//VMath.printMatrix(ga.orbs,VOrb.ORB_TYPE);
		//System.out.println();
		droplock_timer = maxdroplevel*droptime+1;	//计算宝石下落总计时间
	}
	public void uiSetDropTimer(int[][] newgems){
		// TODO 设置每个宝石UI下落的时间计数，进行下落过程
		//this.setEnable(false);
		int maxdroplevel = 0;
		for(int i=0;i<sizey;i++){
			for(int j=0;j<sizex;j++){
				int droplevel = 0;		//该单元宝石下落的网格数				
				if(ga.orbs[i][j].getValue(VOrb.ORB_TYPE)!=-1){	//如果不是已消除宝石
					for (int k = i + 1; k < sizey; k++) {
						if (ga.orbs[k][j].getValue(VOrb.ORB_TYPE) == -1) {
							droplevel = droplevel + 1; // 计算该单元向下的网格有几个空位，即下落的网格数
						}
					}
					if(droplevel>maxdroplevel){
						maxdroplevel = droplevel;	//获取最大下落格数
					}
					subui[i][j].setDropTimer(droplevel*droptime);
				}
				else{
					int d_dropseq = 0;	//新生成宝石的掉落顺序
					int d_droplevel = 0;	//新生成宝石的最大下落格数
					for(int d_i=0;d_i<sizey;d_i++){
						if(ga.orbs[d_i][j].getValue(VOrb.ORB_TYPE)==-1){		//遍历直列计算已消除宝石数量
							d_droplevel = d_droplevel+1;
							if(d_droplevel>maxdroplevel){
								maxdroplevel = d_droplevel;	//获取最大下落格数
							}
						}
					}
					for(int d_i=0;d_i<sizey;d_i++){
						if(ga.orbs[d_i][j].getValue(VOrb.ORB_TYPE)==-1){		//遍历直列计算该宝石掉落顺序
							if(d_i!=i){
								d_dropseq = d_dropseq + 1;
							}
							else{
								break;
							}
						}
					}
					subui[i][j].setDropTimer(d_droplevel*droptime);
					subui[i][j].setLoc(subui[i][j].GetX(),0-(d_droplevel-d_dropseq)*VOrbUI.orbwidth);
					subui[i][j].setIndex(newgems[i][j]);
				}
			}
		}
		droplock_timer = maxdroplevel*droptime+1;	//计算宝石下落总计时间
	}
	public int uiEliminate(){
		// TODO UI进行消除宝珠的过程，返回总掉落所需时间毫秒数以确定是否重复进行掉落
		int maxcombo = 0;
		for(int i=0;i<sizey;i++){
			for(int j=0;j<sizex;j++){
				subui[i][j].setTranspCount(50*ga.orbs[i][j].getValue(VOrb.ORB_STATE)-1);	//在宝石子UI中设置根据combo时间计算的渐隐时间计数，combo值越高者越晚消失
				if(ga.orbs[i][j].getValue(VOrb.ORB_STATE)>maxcombo){
					maxcombo = ga.orbs[i][j].getValue(VOrb.ORB_STATE);
				}
			}
		}
		orbdrop_count = 50*maxcombo;	//在游戏区域对象中设置最大渐隐时间计数，所有消去的宝石渐隐完毕后开始下落过程
		return orbdrop_count;	//返回掉落所需时间毫秒数
	}
	public void resetSubUI(){
		// TODO 重置宝石子UI的位置并生成图像
		for(int i=0;i<subui.length;i++){
			for(int j=0;j<subui[0].length;j++){
				subui[i][j].setLoc(j*suborb_width, i*suborb_width);
				subui[i][j].setGridLoc(j,i);
				subui[i][j].bindOrb(ga.orbs[i][j]);
				subui[i][j].setTransparency(1.0);
			}
		}
	}
	public void resetTransparency(){
		// TODO 重置宝石UI透明度
		for(int i=0;i<sizey;i++){
			for(int j=0;j<sizex;j++){
				subui[i][j].setTransparency(1.0);
			}
		}
	}
	public void bindEntity(GameArea ga) {
		// TODO Auto-generated method stub
		this.ga = ga;
	}
	public VOrbUI getOrbUI(int i,int j){
		// TODO 通过坐标值获取指定UI
		return subui[i][j];
	}
	public void updateUI(){
		// TODO 更新UI显示内容,在每次GS更新时调用保证显示与实际数据同步,在继承VUI的需要的子类中重载
		//		并用于实现子UI中的UI效果
		if(orbdrop_count!=-1){
			orbdrop_count=orbdrop_count-1;
			if(orbdrop_count==0){	//宝石消除渐隐动画结束时，开始宝石掉落计算
				uiOrbDrop();
				orbdrop_count = -1;
			}
		}
		if(droplock_timer!=-1){
			droplock_timer=droplock_timer-1;
			if(droplock_timer==0){	//宝石掉落动画结束时
				resetSubUI();	//掉落过程结束，重置宝石UI位置并设置图像
				ga.orbsExecute();	//再次判断是否有宝石进行消除
				droplock_timer = -1;
			}
		}
		// 递归更新子UI
		for (VUI child = firstChild; child != null; child = child.nextUI) {
			if(child.autoupdate){
				child.updateUI();
			}
		}
	}
	/**
	 * @param sizeindex
	 */
	public void setPanelSize(int sizeindex) {
		// TODO 设置版面布局
		switch (sizeindex) {
		case GameArea.PSIZE_6x5: 
			this.setImage("panelbg_6x5.png");
			suborb_scale = 0.85;
			GlobalEvent.getMouseHoldUI().setScale(0.85);
			suborb_width = 85;
			sizex = 6;
			sizey = 5;
			droptime = 17;
			this.setLoc(105,375);
			break;
		case GameArea.PSIZE_5x4:
			this.setImage("panelbg_5x4.png");
			suborb_scale = 1.0;
			GlobalEvent.getMouseHoldUI().setScale(1.0);
			suborb_width = 100;
			sizex = 5;
			sizey = 4;
			droptime = 20;
			this.setLoc(100+(520-100*5)/2,800-100*4);
			break;
		case GameArea.PSIZE_7x6:
			this.setImage("panelbg_7x6.png");
			suborb_scale = 0.7;
			GlobalEvent.getMouseHoldUI().setScale(0.7);
			suborb_width = 70;
			sizex = 7;
			sizey = 6;	
			droptime = 14;
			this.setLoc(100+(520-70*7)/2,800-70*6);
			break;
		}
		//this.setAreaSize(GameData.gameareaWidth, GameData.gameareaHeight);			//设置游戏区域UI范围
		GlobalEvent.getUIByID("ui_arrowpanel").setAreaSize(GameData.gameareaWidth, GameData.gameareaHeight);
		subui = new VOrbUI[sizey][sizex];	//子UI矩阵
		initSubUI();
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			//drawUIText();
			drawUIBorder();
			
			//--------对子UI进行模板测试，使在UI范围以外的子UI不显示-----------
			double px = getRealX();
			double py = getRealY();
			double pwidth = area.w-area.x;
			GL11.glEnable(GL11.GL_STENCIL_TEST);  	//开启模板测试
			GL11.glStencilFunc(GL11.GL_NEVER, 0x0, 0x0);  
			GL11.glStencilOp(GL11.GL_INCR, GL11.GL_INCR, GL11.GL_INCR);  
			//GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0f,1.0f,1.0f,1.0f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(px,py-200);	//左上
			GL11.glVertex2d(px+pwidth,py-200);	//右上
			GL11.glVertex2d(px+pwidth,py);	//右下	
			GL11.glVertex2d(px,py);	//左下
			GL11.glEnd();
			GL11.glStencilFunc(GL11.GL_NOTEQUAL, 0x1, 0x1);  
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);  
			GL11.glColor4f(0.0f,0.0f,0.0f,1.0f);		
			drawSubUI();
			GL11.glDisable(GL11.GL_STENCIL_TEST);  	//关闭模板测试
			if (showDisableCover) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0); // 重置绑定的纹理
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
				GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.3f);
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2d(px, py); // 左上
				GL11.glVertex2d(px + pwidth, py); // 右上
				GL11.glVertex2d(px + pwidth, area.h); // 右下
				GL11.glVertex2d(px, area.h); // 左下
				GL11.glEnd();
			}
			//drawHintTextList();			//绘制消除时提示的combo计数及加珠倍率触发倍率等
			drawUIText();
		}
	}
	public void resetHintList(){
		hintlist = new ArrayList<String[]>();
		hintcoords = new ArrayList<double[]>();
		hintindex=0;
	}
	public void addHint(VMatchOrb m){
		// TODO 添加一个消除提示
		int orbtype = m.getValue(VMatchOrb.ORB_TYPE);
		int orbplus = m.getValue(VMatchOrb.ORB_PLUSSUM);
		String[]hint = new String[3];
		hint[0] = "Combo "+(hintindex+1);
		if(orbplus>0){
			double dmg_multi_plus = (1 + orbplus * 0.06)
					* (1 + GameData.party
							.getAvaliableAwoken(VCharacter.AWOKEN_ENH_FIRE - 1
									+ orbtype) * 0.04);
			DecimalFormat df = new DecimalFormat("######0.00");   
			hint[1] = "x"+df.format(dmg_multi_plus);
		}
		else{
			hint[1]="";
		}
		hint[2] = "";
		hintlist.add(hint);
		hintcoords.add(m.getCoords());
		if(hintindex<20){
			this.getText(hintindex*3).setText(hint[0]);
			this.getText(hintindex*3+1).setText(hint[1]);
			this.getText(hintindex*3+2).setText(hint[2]);
		}
		else{
			VText[] text = new VText[3];
			text[0] = new VText(hint[0]);
			text[0].setLayout(VText.Layout_CENTER);
			text[0].setFont("font_default");
			text[0].setParentUI(this);
			text[0].setColor(150,255,255);
			this.addText(text[0]);
			text[1] = new VText(hint[1]);
			text[1].setLayout(VText.Layout_CENTER);
			text[1].setFont("font_default");
			text[1].setParentUI(this);
			text[1].setColor(150,255,255);
			this.addText(text[1]);
			text[2] = new VText(hint[2]);
			text[2].setLayout(VText.Layout_CENTER);
			text[2].setFont("font_default");
			text[2].setParentUI(this);
			text[2].setColor(150,255,255);
			text[0].setVisible(false);
			text[1].setVisible(false);
			text[2].setVisible(false);
			this.addText(text[2]);
		}
		int i=0;
		for(i=0;i<3;i++){
			if(hint[i].equals("")){
				break;
			}
		}
		for(int j=0;j<i;j++){		//控制仅当有倍率加成时显示倍率信息
			this.getText(hintindex*3+j).setVisible(true);
			this.getText(hintindex*3+j).setLoc(m.getCoords()[0], m.getCoords()[1]-10*i+20*j);
		}
		hintindex++;
	}
	public void drawHintTextList(){
		// TODO 绘制消除时提示的combo计数及加珠倍率触发倍率等
		int index=0;
		for(String[] hint:hintlist){
			Fontconst.getFont("font_default").drawString((float)hintcoords.get(index)[0], (float)hintcoords.get(index)[1], hint[0],Color.white);
			index++;
		}
	}
}