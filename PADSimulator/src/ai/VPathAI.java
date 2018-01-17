/**
 * 
 */
package ai;

import java.util.ArrayList;

import data.*;
import entities.VOrb;
import event.*;
import global.*;
import system.*;
import timer.*;
import ui.*;

/**
 * @author Demilichzz
 *	路径AI，用于记录转珠路径并控制回放
 */
public class VPathAI {
	protected int orbs[][] = new int[6][7];		//转珠前宝石分布
	protected int orbx = -1;	//当前选取宝石的网格坐标
	protected int orby = -1;
	protected int orbx_max = 5;		//当前选区宝石的网格坐标范围限制
	protected int orbx_min = 0;
	protected int orby_max = 4;
	protected int orby_min = 0;
	protected ArrayList<Integer> movelist;	//移动列表
	protected int maxcombo = 0;
	protected boolean block = false;	//是否禁止获取转珠数据，在回放时禁止
	protected VArrowPanelUI arrowUI = null;
	
	public VPathAI(){
		resetAIState();
	}
	public void resetAIState(){
		// TODO 重置记录的转珠路径参数
		orbx = -1;
		orby = -1;
		orbs = new int[6][7];
		currentorbx = -1;
		currentorby = -1;
		movestep = 0;
		movelist = new ArrayList<Integer>();
		maxcombo = 0;
		if(arrowUI!=null){
			arrowUI.resetArrowList();
		}
	}
	/**
	 * @param grid_x
	 * @param grid_y
	 */
	public void loadChosenOrb(int x, int y) {
		// TODO 加载选取的宝石坐标
		if(!block){
			orbx = x;
			orby = y;
		}
	}
	public void addMove(int movement){
		// TODO 向移动列表添加移动值，2,4,6,8表示下，左，右，上
		if(!block){
			movelist.add(movement);
		}
	}
	public void print(){
		// TODO 调试用，输出路径信息
		Debug.DebugSimpleMessage("StartOrb:X:"+orbx+",Y:"+orby);
		if(false){
			Debug.DebugSimpleMessage("Movelist:");
			for(int i:movelist){
				System.out.print(i+",");
			}
		}
		System.out.println();
		Debug.DebugSimpleMessage("Step:"+(movelist.size()-1));
		Debug.DebugSimpleMessage("Combo:"+maxcombo);
	}
	/**
	 * @param totalcombo
	 */
	public void loadMaxCombo(int totalcombo) {
		// TODO 加载转珠结束后的总combo值
		if(!block){
			maxcombo = totalcombo;
		}
	}
	/**
	 * 
	 */
	protected int moverate = 200;	//AI回放控制宝石移动时的间隔时间
	protected int currentorbx = -1;	//当前位移的宝石网格坐标
	protected int currentorby = -1;
	public void playback() {
		// TODO 进行转珠回放
		if(orbx!=-1&&orby!=-1){
			block = true;	//开始进行转珠回放，禁止路径AI重复获取转珠数据
			movestep = 0;	//将AI步数索引归0
			loadOrbs();	//加载上次转珠开始时的宝石分布
			VEngine.newgame.gs.uiparent.setEnable(false);	//锁定UI
			currentorbx = orbx;
			currentorby = orby;
			int arraylength = movelist.size();
			VTimer playbackTimer = new VTimer(moverate,arraylength*moverate,true,new VLuaAction(){
				//建立Timer按固定时间间隔执行AI移动宝石
				@Override
				public void action() {
					// TODO Auto-generated method stub
					aiOrbmove();
				}	
			});
			playbackTimer.timerStart(VTimer.TIMER_STAGE);
			VTimer orbsExecuteTimer = new VTimer(arraylength*moverate+500,arraylength*moverate+500,false,new VLuaAction(){
				@Override
				public void action() {
					// TODO Auto-generated method stub
					GameData.ga.orbsExecute();
					block = false;
				}
			});
			orbsExecuteTimer.timerStart(VTimer.TIMER_STAGE);
		}
	}
	protected int movestep = 0;	//AI控制移动时的步数索引
	public boolean checkMoveAvalible(int lastmove,int movement){
		// TODO 获取对currentorb坐标，某个移动数据是否可行
		int movex = (movement*2+1)%3-1;	//通过计算将一维移动数据还原为二维坐标
		int movey = -(movement*2+1)/3+1;
		if(currentorbx+movex<0){
			return false;
		}
		if(currentorbx+movex>5){
			return false;
		}
		if(currentorby+movey<0){
			return false;
		}
		if(currentorby+movey>4){
			return false;
		}
		if(lastmove!=-1){
			if(lastmove+movement==3){
				return false;
			}
		}
		return true;
	}
	public void aiOrbmove(){
		// TODO 由AI控制的宝石移动，在回放时建立的Timer中循环获取移动并实现
		/*int movement = movelist.get(movestep);	//获取当前位移方向
		int movex = (movement-1)%3-1;	//通过计算将一维移动数据还原为二维坐标
		int movey = -(movement-1)/3+1;
		arrowUI.addArrow(currentorbx,currentorby,movex,movey);
		GameData.ga.orbSwap(currentorbx,currentorby,currentorbx+movex,currentorby+movey);	//按当前步数移动宝石，并更新UI显示
		currentorbx = currentorbx+movex;
		currentorby = currentorby+movey;
		//System.out.println(movement);
		//System.out.println(movex+","+movey);
		movestep++;*/
		
		int movement = movelist.get(movestep);	//获取移动方向
		int movex = (movement-1)%3-1;	//通过计算将一维移动数据还原为二维坐标
		int movey = -(movement-1)/3+1;
		arrowUI.addArrow(currentorbx,currentorby,movex,movey);	//在箭头UI上增加箭头
		aiOrbmove(movement);		//实施移动
		movestep++;
	}
	public void aiOrbmove(int movement){
		// TODO 由AI控制的宝石移动，在回放时建立的Timer中循环获取移动并实现
		int movex = (movement-1)%3-1;	//通过计算将一维移动数据还原为二维坐标
		int movey = -(movement-1)/3+1;
		GameData.ga.orbSwap(currentorbx,currentorby,currentorbx+movex,currentorby+movey);	//按当前步数移动宝石，并更新UI显示
		currentorbx = currentorbx+movex;
		currentorby = currentorby+movey;
		//System.out.println(movement);
		//System.out.println(movex+","+movey);
	}
	/**
	 * @param uiByID
	 */
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		if(ui instanceof VArrowPanelUI){
			arrowUI = (VArrowPanelUI)ui;
			arrowUI.bindEntity(this);
			GameData.currentAI = this;
		}
	}
	public void loadOrbs(int[][] orbs) {
		// TODO 转珠开始时加载转珠前宝石分布数组
		//int[][]loadorbs = VMath.getMartixValueCopy(orbs, VOrb.ORB_TYPE); 
		for(int i=0;i<orbs.length;i++){
			for(int j=0;j<orbs[0].length;j++){
				this.orbs[i][j]=orbs[i][j];
			}
		}
	}
	public void loadOrbs(VOrb[][] orbs) {
		// TODO 转珠开始时加载转珠前宝石分布数组
		int[][]loadorbs = VMath.getMartixValueCopy(orbs, VOrb.ORB_TYPE); 
		for(int i=0;i<loadorbs.length;i++){
			for(int j=0;j<loadorbs[0].length;j++){
				this.orbs[i][j]=loadorbs[i][j];
			}
		}
	}
	public void loadOrbs() {
		// TODO 将宝石面板恢复为AI中记载的分布
		for(int i=0;i<orbs.length;i++){
			for(int j=0;j<orbs[0].length;j++){
				GameData.ga.orbs[i][j].setValue(orbs[i][j],VOrb.ORB_TYPE);
			}
		}
		//GlobalEvent.getGameAreaUI().uiAction("UIupdate");
	}
}
