/**
 * @author Demilichzz
 *
 * 2014-1-5
 */
package entities;

import java.util.ArrayList;

import data.GameData;
import event.GlobalEvent;
import struct.VIntGroup;
import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;
import ui.*;
import global.Debug;
import global.FSMconst;
import global.VMath;
import interfaces.*;

/**
 * @author Demilichzz
 *
 * 2014-1-5
 */
public class GameArea implements VUIBindingInterface{
	public static final int PSIZE_6x5 = 0;		//面板模式
	public static final int PSIZE_7x6 = 1;
	public static final int PSIZE_5x4 = 2;
	public static int currentPSIZE = 0;
	
	protected VGameAreaUI gameareaUI;
	public int orb_maxx = 6;
	public int orb_maxy = 5;
	protected int panelsize = PSIZE_6x5;
	public VOrb orbs[][]= new VOrb[orb_maxy][orb_maxx];		//表示宝石类型的矩阵
	//1:R,2:G,3:B,4:L,5:D,6:H,7:P,8:N,9:PP
	//-1表示已消除
	//public int orbstate[][] = new int[orb_maxy][orb_maxx];	//表示宝石combo状态的矩阵、
	//0为未形成combo，1以上的值为combo数
	//public int orbcheck[][] = new int[orb_maxy][orb_maxx];	//表示宝石是否已在递归中检查过的矩阵，防止循环递归
	protected int comboindex = 0;
	protected int totalcombo = 0;
	protected ArrayList<VMatchOrb> matchorbs;
	public boolean moved = false;	//表示宝石是否被移动过
	protected boolean matched = false;
	
	public GameArea(){
		//setPanelSize(PSIZE_5x4);
		//resetOrb();
	}
	public static VUI createUI(String str){
		VGameAreaUI gui = new VGameAreaUI(510,425,str,PSIZE_6x5);
		//gui.setPanelSize(PSIZE_6x5);	//默认为6x5版面
		return gui;
	}
	public void setPanelSize(int sizeindex){
		// TODO 设置版面布局，默认为6x5
		GameArea.currentPSIZE = sizeindex;
		panelsize = sizeindex;
		switch(sizeindex){
		case GameArea.PSIZE_6x5: 
			VOrbUI.orbwidth = 85.0;		//根据面板布局定义宝石UI长度的一半
			orb_maxx = 6;
			orb_maxy = 5;
			GameData.gameareaHeight = 425;
			GameData.gameareaWidth = 510;
			break;
		case GameArea.PSIZE_5x4:
			VOrbUI.orbwidth = 100.0;
			orb_maxx = 5;
			orb_maxy = 4;
			GameData.gameareaHeight = 400;
			GameData.gameareaWidth = 500;
			break;
		case GameArea.PSIZE_7x6:
			VOrbUI.orbwidth = 70.0;
			orb_maxx = 7;
			orb_maxy = 6;	
			GameData.gameareaHeight = 420;
			GameData.gameareaWidth = 490;
			break;
		}
		orbs = new VOrb[orb_maxy][orb_maxx];		//初始化宝石面板
		initOrbs();	//初始化宝石对象
		if(gameareaUI!=null){
			gameareaUI.setPanelSize(sizeindex);
		}
		GameData.party.getPartyAwokenState();	//在生成宝石之前获取加珠觉醒等状态
		//resetOrb();
	}
	public void initOrbs(){
		// TODO 初始化宝石面板中的宝石对象
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				orbs[i][j] = new VOrb();
			}
		}
		uiBindUpdate();
	}
	public void orbSwap(int xa,int ya,int xb,int yb){
		// TODO 指定两坐标的值互换
		VOrb va = orbs[ya][xa].createCopy();
		VOrb vb = orbs[yb][xb].createCopy();
		orbs[ya][xa] = vb;
		orbs[yb][xb] = va;
		//System.out.println(""+xa+","+ya+"与"+xb+","+yb+"互换");
		uiBindUpdate();
	}
	public VOrb getOrb(int i,int j){
		// TODO 获取指定坐标的宝石对象
		if(i<orb_maxy&&i>=0&&j<orb_maxx&&j>=0){
			return orbs[i][j];
		}
		else{
			return null;
		}
	}
	/**
	 * @param vGemUI 浮动宝珠UI
	 * @param xArea	区域内x坐标
	 * @param yArea	区域内y坐标
	 */
	public void orbSwap(VOrbUI ui, int x, int y) {
		// TODO 将宝珠向指定网格坐标移动
		int cx = ui.choseX;	//原始网格坐标
		int cy = ui.choseY;
		int x_differ = x - cx;
		int y_differ = y - cy;
		if(!(x_differ*y_differ==0||Math.abs(x_differ)==Math.abs(y_differ))){
			System.out.println("目标点与原始位置不在斜线或直线上，无法进行移动");
		}
		else{
			orbMove(ui,x_differ,y_differ);
			GameData.ga.moved=true;
		}
		/*
		if(x>cx*VOrbUI.orbwidth+VOrbUI.orbwidth){
			orbMove(ui,1,0);
			return;
		}
		else if(x<cx*VOrbUI.orbwidth){
			orbMove(ui,-1,0);
			return;
		}
		if(y>cy*VOrbUI.orbwidth+VOrbUI.orbwidth){
			orbMove(ui,0,1);
			return;
		}
		else if(y<cy*VOrbUI.orbwidth){
			orbMove(ui,0,-1);
			return;
		}*/
	}
	public void orbMove(VOrbUI ui,int xmove,int ymove){
		// TODO 将宝珠向指定方向移动,Y向+1为向下,X向+1为向右
		int x_direction = 0;
		int y_direction = 0;
		if(xmove==0){		//纵向
			for(int i=0;i<Math.abs(ymove);i++){
				x_direction = 0;
				y_direction = ymove/Math.abs(ymove);
				orbSwap(ui.choseX,ui.choseY,ui.choseX,ui.choseY+y_direction);
				ui.choseX = ui.choseX;
				ui.choseY = ui.choseY+y_direction;
				
			}
		}
		if(ymove==0){		//横向
			for(int i=0;i<Math.abs(xmove);i++){
				x_direction = xmove/Math.abs(xmove);;
				y_direction = 0;
				orbSwap(ui.choseX,ui.choseY,ui.choseX+x_direction,ui.choseY);
				ui.choseX = ui.choseX+x_direction;
				ui.choseY = ui.choseY;
			}
		}	
		if(Math.abs(xmove)==Math.abs(ymove)){	//斜向
			for(int i=0;i<Math.abs(xmove);i++){
				x_direction = xmove/Math.abs(xmove);;
				y_direction = ymove/Math.abs(ymove);
				orbSwap(ui.choseX,ui.choseY,ui.choseX+x_direction,ui.choseY+y_direction);
				ui.choseX = ui.choseX+x_direction;
				ui.choseY = ui.choseY+y_direction;
			}
		}
		
		GameData.pathAI.addMove(5+x_direction-y_direction*3);	//向移动列表添加移动值
	}
	public void resetOrb(){
		// TODO 复位宝珠分布
		/*orbs = new int[][]{{4,4,1,4,4,4},
						   {4,1,4,1,4,4},
						   {1,4,4,1,1,4},
						   {4,4,1,4,4,4},
						   {4,4,4,1,4,4}};*/
		/*orbs=new int[][]{{1,1,2,3,2,2},
						 {2,2,1,4,3,3},
						 {3,3,2,5,4,4},
						 {4,4,3,6,5,5},
						 {5,5,4,5,6,6}};*/
		resetMatchOrbs();
		randomizeStartOrb();
		resetOrbState();
		resetOrbCheck();
		resetCombo();	
		//resetPlus();
		//resetLock();
		uiBindUpdate();
	}
	public void resetOrb(int[][] orbs){
		// TODO 将宝珠分布重置为指定分布
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				this.orbs[i][j]=new VOrb(orbs[i][j]);
			}
		}
		resetOrbState();
		resetOrbCheck();
		resetCombo();
		//resetPlus();
		//resetLock();
		uiBindUpdate();
	}
	public VOrb[][] randomizeBlankOrb(VOrb[][] tarorb){
		// TODO 对目标宝石矩阵中的空位随机生成宝石
		VOrb[][] neworb = new VOrb[orb_maxy][orb_maxx];
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				neworb[i][j] = new VOrb();
			}
		}
		int orblist[] = {1,2,3,4,5,6};	//可能生成的宝珠列表
		VIntGroup orbgroup = new VIntGroup(orblist);	//建立随机组
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				if(tarorb[i][j].getValue(VOrb.ORB_TYPE)==0||tarorb[i][j].getValue(VOrb.ORB_TYPE)==-1){	//该矩阵值为未赋值或宝石已消除
					neworb[i][j].setValue(VOrb.ORB_TYPE,VMath.GetRandomIntFromGroup(orbgroup.getGroup()));
					//测试用，所有火珠生成为加珠
					int orb_type = neworb[i][j].getValue(VOrb.ORB_TYPE);
					if(orb_type>0&&orb_type<6){
						int awoken_plus = GameData.party
								.getAvaliableAwoken(VCharacter.AWOKEN_ENH_FIRE
										- 1 + orb_type);
						if (VMath.GetRandomBoolean(awoken_plus / 5.0)) {
							neworb[i][j].setValue(VOrb.ORB_PLUS, 1);
						}
					}
				}
			}
		}
		return neworb;
	}
	public void randomizeStartOrb(){
		// TODO 在战斗初始使用，随机生成不会消除的宝石
		this.orbs = randomizeBlankOrb(this.orbs);
		int combo = orbsElimination();	//对宝石矩阵进行计算并获取combo最大值
		while(combo!=0){			//如果有combo
			//totalcombo = totalcombo+combo;
			orbsDrop();		//计算消除后掉落前矩阵
			orbsDropMatrix();	//计算一轮掉落后矩阵
			VOrb[][] neworbs = randomizeBlankOrb(this.orbs);	//对已消除部分随机填充宝石
			VMath.matrixCover(this.orbs, neworbs,VOrb.ORB_TYPE,false);
			resetOrbState();
			resetOrbCheck();
			resetCombo();
			//resetPlus();
			//resetLock();
			combo = orbsElimination();	//对掉落后矩阵进行计算并获取combo最大值，为0则消除掉落过程结束
		}
		resetMatchOrbs();	//在初始随机生成时可能有消除，清除这些消除的宝石串信息	
	}
	public void resetCombo(){
		// TODO 复位Combo状态
		comboindex = 0;
		totalcombo = 0;
	}
	public void resetOrbState(){
		// TODO 复位宝石的消除状态
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				this.orbs[i][j].setValue(VOrb.ORB_STATE, 0);
			}
		}
	}
	public void resetOrbCheck(){
		// TODO 复位宝石检查状态
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				this.orbs[i][j].setValue(VOrb.ORB_CHECK, 0);
			}
		}
	}
	public void resetMatchOrbs(){
		// TODO 重置消除宝石串，在回合结束时调用消除宝石串之后执行以重置消除宝石串状态
		matchorbs = new ArrayList<VMatchOrb>();
	}
	public void clearEliminatedPlus(){
		// TODO 清除已消除的宝石强化状态
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				if(this.orbs[i][j].getValue(VOrb.ORB_STATE)!=0){
					this.orbs[i][j].setValue(VOrb.ORB_PLUS, 0);
				}
			}
		}
	}
	public void clearEliminatedLock(){
		// TODO 清除已消除的宝石锁定状态
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				if(this.orbs[i][j].getValue(VOrb.ORB_STATE)!=0){
					this.orbs[i][j].setValue(VOrb.ORB_LOCK, 0);
				}
			}
		}
	}
	public void orbsDrop(){
		// TODO 计算消去后宝珠掉落前的矩阵
		//Debug.DebugSimpleMessage("=======combo矩阵：=======");
		//VMath.printMatrix(orbstate);
		//Debug.DebugSimpleMessage("=======宝珠矩阵：=======");
		//VMath.printMatrix(orbs);
		for(int y=0;y<orb_maxy;y++){
			for(int x=0;x<orb_maxx;x++){
				if(orbs[y][x].getValue(VOrb.ORB_STATE)!=0){	//如果宝珠的combo值不为0，则已消去
					orbs[y][x].setValue(VOrb.ORB_TYPE, -1);
					orbs[y][x].setValue(VOrb.ORB_PLUS, 0);
					orbs[y][x].setValue(VOrb.ORB_LOCK, 0);
				}
			}
		}		//获取宝珠下落前矩阵
	}
	public VOrb[][] generateNewGems(){
		// TODO 掉落过程中生成新的掉落宝石
		VOrb[][]neworbs = randomizeBlankOrb(this.orbs);
		return neworbs;
	}
	public void orbsDropMatrix(){
		// TODO 计算宝珠掉落后的矩阵
		orbs = (VOrb[][]) VMath.matrixDrop(orbs,VOrb.ORB_TYPE);
		//Debug.DebugSimpleMessage("=======宝珠下落后矩阵：=======");
		//VMath.printMatrix(orbs);
	}
	public void orbsDropMatrixCover(int[][] neworbs){
		// TODO 将新生成的宝石添加到掉落后的矩阵
		VMath.matrixCover(orbs, neworbs,VOrb.ORB_TYPE,false);
	}
	public boolean orbsExecute(){
		// TODO 进行移动后，对宝石进行消除及掉落的递归处理
		//this.comboindex = 0;	//重置总体combo计数
		matched = false;
		GlobalEvent.movingLockUI(true);
		//VEngine.newgame.gs.uiparent.setEnable(false);	//在宝石消除掉落过程锁定UI以防止操作冲突
		//gameareaUI.setEnable(false);
		resetOrbState();
		resetOrbCheck();
		int combo = orbsElimination();	//对宝石矩阵进行计算并获取combo最大值
		if(combo!=0){
			totalcombo = totalcombo+combo;
			int orbdropcount = gameareaUI.uiEliminate();	//处理消除掉的宝石UI渐隐
			addMatchOrbs(combo,orbdropcount);	//以Timer形式添加宝石串对象，
			matched = true;
		}
		else{	//combo值为0，则跳过消除宝石渐隐阶段，直接进入宝石掉落阶段。
			//
			int droptime = 0;
			droptime = gameareaUI.uiOrbDrop();	//进行宝石掉落计算并获取掉落时间
			if(droptime<=0){	//当掉落时间为0，则本次宝石消除掉落处理结束
				//VEngine.newgame.gs.uiparent.setEnable(true);
				orbsExecuteFinish();	//当前宝石处理完成，记录本次操作数据
				//gameareaUI.setEnable(true);
			}
		}
		return matched;
	}
	public void orbsResultExecute(){
		// TODO 进行移动后，直接计算宝石消除后的结果，忽略为显示宝石掉落和消除过程而重复进行等待的过程
		GlobalEvent.movingLockUI(true);	//在宝石消除掉落过程锁定UI以防止操作冲突
		//gameareaUI.setEnable(false);
		resetOrbState();
		resetOrbCheck();
		resetCombo();
		//resetPlus();
		//resetLock();
		int combo = orbsElimination();	//对宝石矩阵进行计算并获取combo最大值
		while(combo!=0){
			totalcombo = totalcombo+combo;
			//gameareaUI.uiEliminate();	//处理消除掉的宝石UI渐隐
			orbsDrop();		//计算消除后掉落前矩阵
			orbsDropMatrix();	//计算一轮掉落后矩阵
			resetOrbState();
			resetOrbCheck();
			combo = orbsElimination();	//对掉落后矩阵进行计算并获取combo最大值，为0则消除掉落过程结束
		}
		orbsDrop();		//计算消除后掉落前矩阵
		orbsDropMatrix();	//计算一轮掉落后矩阵
		VEngine.newgame.gs.uiparent.setEnable(true);
		orbsExecuteFinish();	//当前宝石处理完成，记录本次操作数据
		GlobalEvent.movingLockUI(false);
	}
	public void orbsExecuteFinish() {
		// TODO 在一次操作引起的宝石消除掉落过程完成时调用，用于记录单次宝石处理过程数据
		GameData.currentAI.loadMaxCombo(totalcombo);
		GameData.party.loadMaxCombo(totalcombo);
		GlobalEvent.showDisableCover(true);
		//gameareaUI.setShowDisableCover(true);
		//GameData.party.getMatchOrbs(matchorbs);		//向队伍传入消除宝石串的数据
		if(totalcombo>0){
			GameData.party.calculateDamage();	//计算伤害与回复，在此函数中会使用timer调用orbsExecuteFinish_Step2函数
			matched = true;
		}
		else{
			orbsExecuteFinish_Step2();
			matched = false;
		}
		resetMatchOrbs();
		resetCombo();
		moved = false;	//重置移动状态
		//GameData.currentAI.print();
	}
	public void orbsExecuteFinish_Step2(){
		// TODO 消除造成的伤害和回复计算完毕后调用，回合结束，处理敌人状态
		GlobalEvent.getGameAreaUI().resetHintList();
		
		GameData.party.dealMatchAction(matched);	//处理转珠触发，包括自动回复及天照/月鸡等转珠触发技能
		
		VTimer tm_turnend = new VTimer(500,500,false,new VLuaAction(){	//转珠触发之后转到敌人行动过程添加延迟
			@Override
			public void action() {
				// TODO Auto-generated method stub
				GameData.instance.processState();	//处理敌人状态，判断是否死亡
				GameData.instance.processBuffDecay();	//处理Buff衰减
				if(GameData.instance.checkBattleEnd()){		//检查本层敌人是否全部死亡
					GameData.instance.instanceStateTransition(FSMconst.INPUT_ENEMYDIE);	//FSM转入MoveToNextStage
				}
				else{
					GameData.instance.instanceStateTransition(FSMconst.INPUT_ENEMYALIVE);	//FSM转入EnemyAction
				}
			}	
		});
		tm_turnend.timerStart(VTimer.TIMER_STAGE);
		
		//GameData.instance.enemyAction();
		
		//GameData.party.turnStart();		//进入下一回合开始
		/*gameareaUI.setShowDisableCover(false);
		VEngine.newgame.gs.uiparent.setEnable(true);*/
	}
	
	
	public int orbsElimination(){
		// TODO 移动后调用，对宝石矩阵进行计算判断宝石消除状况，返回combo矩阵最大值
		comboindex=1;
		for (int y = 0; y < orb_maxy; y++) {
			for (int x = 0; x < orb_maxx; x++) { //遍历宝珠
				//System.out.print(orbs[y][x]+",");
				if (orbs[y][x].getValue(VOrb.ORB_TYPE)!= -1 && orbs[y][x].getValue(VOrb.ORB_STATE) == 0) { //如果宝珠不为空，且不在已确定的combo内，则进行横竖排判断
					boolean b = eliminationCalculate(x, y, -1); //递归判断是否有消除进行
					if (b) {
						comboindex = comboindex + 1; //combo计数增加
						resetOrbCheck(); //复位宝珠检查状态以应对下一combo的检查
					}
				}
			}
			//System.out.println("");
		}
		VMath.restrictMatrixValue(orbs,VOrb.ORB_STATE);	//标准化combo矩阵值
		//VMath.printMatrix(orbstate);
		int maxcombo = VMath.getMatrixMax(orbs,VOrb.ORB_STATE);
		//addMatchOrbs(maxcombo);		//添加消除宝石串对象，用于计算伤害
		//System.out.println(maxcombo);
		return maxcombo;
	}
	public void addMatchOrbs(int maxcombo,int timedelay) {
		// TODO 添加该次消除中，消除的每组宝石
		for(int c=1;c<=maxcombo;c++){
			int orb_sum = 0;	//宝石总数
			int plus_sum = 0;	//加珠总数
			int orb_type = -1;
			int orb_end_i = 0;	//该串宝石最末位的网格坐标
			int orb_end_j = 0;
			double[] coords = new double[2];
			for (int i = 0; i < orb_maxy; i++) {
				for (int j = 0; j < orb_maxx; j++) {
					if (orbs[i][j].getValue(VOrb.ORB_STATE) == c) { // 如果宝石在指定的combo中
						coords[0]=coords[0]+j;
						coords[1]=coords[1]+i;
						orb_end_i = i;
						orb_end_j = j;
						orb_sum++;	//计算宝石数量总和
						orb_type = orbs[i][j].getValue(VOrb.ORB_TYPE);
						if(orbs[i][j].getValue(VOrb.ORB_PLUS)==1){
							plus_sum++;
						}
					}
				}
			}
			coords[0]=coords[0]/orb_sum;
			coords[1]=coords[1]/orb_sum;
			VMatchOrb match_orb = new VMatchOrb(orb_type);
			match_orb.setCoords(coords);	//传入平均网格坐标
			match_orb.setValue(VMatchOrb.ORB_ORBSUM, orb_sum);
			match_orb.setValue(VMatchOrb.ORB_PLUSSUM, plus_sum);
			switch (orb_sum){
			case 3:
				break;
			case 4:
				match_orb.setValue(VMatchOrb.ORB_DOUBLE, 1);
				break;
			case 5:
				match_orb.setValue(VMatchOrb.ORB_FIVE, 1);
				if(orb_end_i>1&&orb_end_j>0&&orb_end_j<orb_maxx-1){	//末位宝石在第3行及第2列或以后，才可能是十字串的末位宝石
					int orb_mi = orb_end_i-1;
					int orb_mj = orb_end_j;
					if((orbs[orb_mi][orb_mj].getValue(VOrb.ORB_STATE)==c)
						&&(orbs[orb_mi][orb_mj-1].getValue(VOrb.ORB_STATE)==c)
						&&(orbs[orb_mi][orb_mj+1].getValue(VOrb.ORB_STATE)==c)
						&&(orbs[orb_mi-1][orb_mj].getValue(VOrb.ORB_STATE)==c)){
						match_orb.setValue(VMatchOrb.ORB_CROSS, 1);
					}
				}
				break;
			default:				//因为消除数量不可能<3，default为消除总数>=6的状况
				for(int i=0;i<orb_maxy;i++){
					boolean iflinestate = true;	//判断是否横排
					for(int j=0;j<orb_maxx;j++){
						if(orbs[i][j].getValue(VOrb.ORB_STATE)!=c){	//如果一行内有不是当前combo或非combo宝石，则不是横排
							iflinestate = false;
							break;
						}
					}
					if(iflinestate){	//如果某一行判断是横行
						match_orb.setValue(VMatchOrb.ORB_LINE,1);
						break;
					}
				}
				break;
			}
			matchorbs.add(match_orb);	//添加消除宝石串
			VTimer tm = new VTimer(500*(c-1)+1,500*(c-1)+1,false,new VLuaAction(){
				@Override
				public void action() {
					// TODO 向队伍中添加消除串的计时器函数
					GameData.party.getMatchOrb(matchorbs.get(0));	
					matchorbs.remove(matchorbs.get(0));
				}	
			});
			tm.timerStart(VTimer.TIMER_STAGE);
		}
	}
	public boolean eliminationCalculate(int x, int y, int currentorb) {
		// TODO 对指定宝珠坐标进行消除检查
		boolean b = false; // 当前宝珠是否满足三消条件，即该宝珠是横向或竖向三个同颜色宝珠中的一个
		if (x >= 0 && x < orb_maxx && y >= 0 && y < orb_maxy) {
			if (orbs[y][x].getValue(VOrb.ORB_CHECK) == 0) { // 仅检查当前递归中未被标记的宝珠
				/*if(x==2&&y==2){
					System.out.println(x+","+y);
				}*/
				orbs[y][x].setValue(VOrb.ORB_CHECK, 1);	//设置该宝珠坐标已被检查的标记
				if (currentorb == -1 || currentorb == orbs[y][x].getValue(VOrb.ORB_TYPE)) { // currentorb=-1时为初始递归,仅当该宝珠是同样宝珠时进行十字检查
					currentorb = orbs[y][x].getValue(VOrb.ORB_TYPE);
					if(orbs[y][x].getValue(VOrb.ORB_STATE)==comboindex){		//如果当前宝珠状态已被标记为当前combo
						b = true;			//直接确认满足条件，跳转至对四周宝珠进行递归
					}
					else{	//未被标记，则需检查是否满足是三连的一部分
						if (x + 1 < orb_maxx && x > 0) {	//检查当前宝珠是否为横行三连中心
							if (orbs[y][x - 1].getValue(VOrb.ORB_TYPE) == currentorb
									&& orbs[y][x + 1].getValue(VOrb.ORB_TYPE) == currentorb) { // 检查横行
								for (int i = -1; i < 2; i++) { // 如横行满足条件
									orbs[y][x + i].setValue(VOrb.ORB_STATE,comboindex); // 标记横行宝珠combo为当前combo
								}
								b = true;
							}
						}
						if (y + 1 < orb_maxy && y > 0) {	//检查当前宝珠是否为竖行三连中心
							if (orbs[y - 1][x].getValue(VOrb.ORB_TYPE) == currentorb
									&& orbs[y + 1][x].getValue(VOrb.ORB_TYPE) == currentorb) { // 检查竖行
								for (int i = -1; i < 2; i++) { // 如竖行满足条件
									orbs[y + i][x].setValue(VOrb.ORB_STATE, comboindex); // 标记竖行宝珠combo为当前combo
								}
								b = true;
							}
						}
						if(x<orb_maxx&&x>1){	//检查当前宝珠是否为横行三连右侧
							if(orbs[y][x-1].getValue(VOrb.ORB_TYPE)==currentorb&&orbs[y][x-2].getValue(VOrb.ORB_TYPE)==currentorb){
								for(int i=-2;i<1;i++){
									orbs[y][x+i].setValue(VOrb.ORB_STATE, comboindex);	//标记combo
								}
								b = true;
							}
						}
						if(x+2<orb_maxx&&x>=0){	//检查当前宝珠是否为横行三连左侧
							if(orbs[y][x+1].getValue(VOrb.ORB_TYPE)==currentorb&&orbs[y][x+2].getValue(VOrb.ORB_TYPE)==currentorb){
								for(int i=0;i<3;i++){
									orbs[y][x+i].setValue(VOrb.ORB_STATE, comboindex);	//标记combo
								}
								b = true;
							}
						}
						if(y<orb_maxy&&y>1){	//检查当前宝珠是否为竖行三连下侧
							if(orbs[y-1][x].getValue(VOrb.ORB_TYPE)==currentorb&&orbs[y-2][x].getValue(VOrb.ORB_TYPE)==currentorb){
								for(int i=-2;i<1;i++){
									orbs[y+i][x].setValue(VOrb.ORB_STATE, comboindex);	//标记combo
								}
								b = true;
							}
						}
						if(y+2<orb_maxy&&y>=0){	//检查当前宝珠是否为竖行三连上侧
							if(orbs[y+1][x].getValue(VOrb.ORB_TYPE)==currentorb&&orbs[y+2][x].getValue(VOrb.ORB_TYPE)==currentorb){
								for(int i=0;i<3;i++){
									orbs[y+i][x].setValue(VOrb.ORB_STATE, comboindex);	//标记combo
								}
								b = true;
							}
						}
					}
					if(b){
						int adajectvalue=0;	//相邻四个宝珠中与该宝珠相同的数量值
						boolean r1 = false;	//标记上下左右相邻的四个宝珠是否满足当前combo
						boolean r2 = false;
						boolean r3 = false;
						boolean r4 = false;
						if (x > 0) {
							if (orbs[y][x - 1].getValue(VOrb.ORB_TYPE) == currentorb) {	//如果相邻左方宝珠相同
								adajectvalue = adajectvalue + 1;
								r1 = eliminationCalculate(x - 1, y, orbs[y][x].getValue(VOrb.ORB_TYPE));	//递归检查左方宝珠是否形成combo
							}
						}
						if (x+1<orb_maxx) {
							if (orbs[y][x + 1].getValue(VOrb.ORB_TYPE) == currentorb) {
								adajectvalue = adajectvalue + 1;
								r2 = eliminationCalculate(x + 1, y, orbs[y][x].getValue(VOrb.ORB_TYPE));
							}
						}
						if (y > 0) {
							if (orbs[y - 1][x].getValue(VOrb.ORB_TYPE) == currentorb) {
								adajectvalue = adajectvalue + 1;
								r3 = eliminationCalculate(x, y - 1, orbs[y][x].getValue(VOrb.ORB_TYPE));
							}
						}
						if (y+1<orb_maxy) {
							if (orbs[y + 1][x].getValue(VOrb.ORB_TYPE) == currentorb) {
								adajectvalue = adajectvalue + 1;
								r4 = eliminationCalculate(x, y + 1, orbs[y][x].getValue(VOrb.ORB_TYPE));
							}
						}
						if(r1||r2||r3||r4){
							b=true;
						}
					}		
				}
			}
			else{	//如该宝珠在当前递归已经检查过，则跳过
				
			}
		}
		return b;
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#bindUI(ui.VUI)
	 */
	@Override
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		if(ui instanceof VGameAreaUI){
			gameareaUI = (VGameAreaUI)ui;
			gameareaUI.bindEntity(this);
			uiBindUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#uiBindUpdate()
	 */
	@Override
	public void uiBindUpdate() {
		// TODO 更新UI显示
		if(gameareaUI!=null){
			gameareaUI.uiAction("UIupdate");
		}
	}
	public void uiTranslateUpdate() {
		// TODO 在宝石转换时更新UI显示
		if(gameareaUI!=null){
			gameareaUI.uiAction("UItranslate");
		}
	}
	public void synchronousGems(int[][] orbs) {
		// TODO 同步宝石矩阵数据
		for(int i=0;i<orb_maxy;i++){
			for(int j=0;j<orb_maxx;j++){
				this.orbs[i][j].setValue(orbs[i][j],VOrb.ORB_TYPE);
			}
		}
		uiBindUpdate();
	}
}
