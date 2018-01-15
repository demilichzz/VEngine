/**
 * 	使用遗传算法计算转珠路径的AI
 */
package ai;

import java.util.ArrayList;

import global.VMath;
import struct.*;
import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;
import data.GameData;
import entities.VOrb;

/**
 * @author Demilichzz
 *
 */
public class VGeneticPathAI extends VPathAI{
	protected int g_orbs[][] = null;
	protected int preforbX = -1;	//转珠起始宝石位置
	protected int preforbY = -1;
	protected int maxstep = 100;	//最大步数
	protected VMovementGene tempgene = null;
	
	public VGeneticPathAI(){
		super();
	}
	public void getStartState(){
		// TODO 获取转珠面板起始状态
		if(true/*g_orbs==null*/){		//如果起始状态为空则获取当前的宝石面板，否则使用AI中记载的宝石面板进行计算
			g_orbs = new int[5][6];
			loadGeneticOrbs(VMath.getMartixValueCopy(GameData.ga.orbs,VOrb.ORB_TYPE));
		}
		preforbX = -1;
		preforbY = -1;
	}
	public void loadGeneticOrbs(int[][]orbs){
		// TODO 记录当前的宝石分布
		for(int i=0;i<5;i++){
			for(int j=0;j<6;j++){
				this.g_orbs[i][j]=orbs[i][j];
			}
		}
	}
	/**
	 * 
	 */
	public void resetGPathAIState(){
		// TODO 重置遗传算法中单次生成路径前的转珠参数
		loadOrbs(g_orbs);	//将AI中记录的遗传子宝石分布加载至本次生成
		GameData.ga.resetCombo();
		movestep = 0;
		movelist = new ArrayList<Integer>();	//初始化转珠路径
		maxcombo = 0;
		if(arrowUI!=null){
			arrowUI.resetArrowList();
		}
	}
	public void outputMovementGene(){
		// TODO 使用AI中当前转珠路径数据生成一条基因组
		VMovementGene mgene = new VMovementGene(movelist);
		mgene.maxlength = maxstep*2;
		mgene.fitlength = maxstep*2;
		mgene.startorbx = preforbX;
		mgene.startorby = preforbY;
		tempgene = mgene;
	}
	public void generatePathGroup(){
		// TODO 应用遗传算法生成转珠路径组，并保存相应的基因组至初始基因池中
		//已在自动计算路径UI按钮点击时获取宝石面板
		generateSinglePath(); //生成一条路径
		moveResultProcess();	//计算该路径的转珠结果
		/*outputMovementGene();	//生成一条基因组数据
		
		int mcombo = 0;
		int fitlength = tempgene.maxlength;
		for(int i=tempgene.maxlength;i>0;i=i-2){
			moveResultProcess(tempgene,i);
			if(this.maxcombo>=mcombo){		//较短路径的combo数较高
				mcombo = this.maxcombo;
				fitlength = i;			//记录该路径的长度
			}
		}
		System.out.println("Combo:"+mcombo);
		System.out.println("Length:"+fitlength);
		*/
		
		
	}
	public void generateSinglePath() {
		// TODO 应用遗传算法生成转珠路径并显示结果
		resetGPathAIState();	//重置生成参数
		if(preforbX!=-1&&preforbY!=-1){
			orbx = preforbX;
			orby = preforbY;
		}
		else{
			orbx = 0;
			orby = 0;
		}
		addMove(5);		//添加初始位移方向5
		int lastmove = -1;	//定义上次移动的方向，以控制每次移动方向不会与上一次相反形成无意义移动
		int[] movearray = new int[]{2,4,6,8};	//定义总体有效移动方向
		VIntGroup movegroup = new VIntGroup();
		for(int i = 0;i<maxstep;i++){
			movegroup.setGroup(movearray);	//定义位移方向集合
			checkOrbPos(movegroup,lastmove);		//检查宝石位置并去除不可用移动方向
			int movement = VMath.GetRandomIntFromGroup(movegroup.getGroup());	//从修正的集合中随机获取方向值
			addMove(movement);
			lastmove = movement;	//设置上次移动方向
			int mx = (movement-1)%3-1;	//通过计算将一维移动数据还原为二维坐标
			int my = -(movement-1)/3+1;
			orbx = orbx+mx;		//计算位移后的宝石坐标
			orby = orby+my;
		}
		if(preforbX!=-1&&preforbY!=-1){
			orbx = preforbX;
			orby = preforbY;
		}
		else{
			orbx = 0;
			orby = 0;
		}
		//System.out.println();
	}
	public void playback() {
		moverate = 10;	//AI回放控制宝石移动时的间隔时间
		currentorbx = -1;
		currentorby = -1;
		super.playback();
	}
	public void moveResultProcess(VMovementGene gene,int length){
		// TODO 固定起始面板，移动宝珠坐标，根据基因组形式的移动数据计算转珠结果的函数，返回转珠combo值
		if(gene.startorbx!=-1&&gene.startorby!=-1){
			block = true;	//禁止路径AI重复获取转珠数据
			//movestep = 0;	//将AI步数索引归0
			loadOrbs();	//加载上次转珠开始时的宝石分布
			VEngine.newgame.gs.uiparent.setEnable(false);	//锁定UI
			currentorbx = gene.startorbx;
			currentorby = gene.startorby;	 //获取起始转珠位置
			int lastmove = -1;
			for(int i=0;i<length;i=i+2){
				int movement = 0;
				if(gene.movementgene[i]){
					movement = movement+2;
				}
				if(gene.movementgene[i+1]){
					movement = movement+1;
				}
				if(checkMoveAvalible(lastmove,movement)){
					// 如果检测移动合法，则进行移动
					movement = (movement+1)*2;		//通过二进制基因组获取的0-3转化为对应的移动数据2468
					aiOrbmove(movement);
					lastmove = movement;
				}
				//否则抛弃此次移动	
			}
			block = false;
			//GameData.ga.orbsExecute();
			GameData.ga.orbsResultExecute();	//直接计算消除后结果
			
			GameData.ga.uiBindUpdate();		//更新宝石面板
		}
	}
	public void moveResultProcess(){
		// TODO 在获得转珠路径之后，直接获取转珠结果，忽略为显示转珠过程而重复进行等待的过程
		/*if(orbx!=-1&&orby!=-1){
			block = true;	//禁止路径AI重复获取转珠数据
			movestep = 0;	//将AI步数索引归0
			loadOrbs();	//加载上次转珠开始时的宝石分布
			VEngine.newgame.gs.uiparent.setEnable(false);	//锁定UI
			currentorbx = orbx;
			currentorby = orby;	 //获取起始转珠位置
			int arraylength = movelist.size();
			//------此处直接重复进行调用aiOrbMove，省略宝石消除掉落过程--------
			for(int i=0;i<arraylength;i++){
				aiOrbmove();
			}
			//---------------------------------------------------------------
			block = false;
			GameData.ga.orbsResultExecute();
			GameData.ga.uiBindUpdate();		//更新宝石面板
		}
		*/
		this.playback();
	}
	public void checkOrbPos(VIntGroup movegroup,int lastmove) {
		// TODO 检查宝石位置并去除不可用移动方向
		if(orbx==orbx_min){
			movegroup.removeContent(4);
		}
		if(orby==orby_min){
			movegroup.removeContent(8);
		}
		if(orbx==orbx_max){
			movegroup.removeContent(6);
		}
		if(orby==orby_max){
			movegroup.removeContent(2);
		}
		if(lastmove!=-1){
			movegroup.removeContent(10-lastmove);
		}
	}
	
}
