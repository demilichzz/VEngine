/**
 * 	副本类，包含副本的面板大小，宝石类型，层数及怪物分布等信息
 */
package entities;

import data.GameData;
import fsm.FSMclass;
import fsm.FSMstate;
import global.FSMconst;
import global.Fontconst;
import interfaces.VUIBindingInterface;
import event.GlobalEvent;
import timer.VLuaAction;
import timer.VTimer;
import ui.*;
import view.VText;

/**
 * @author Demilichzz
 *
 */
public class VInstance implements VUIBindingInterface{
	protected VInstanceUI instanceUI = null;
	protected int maxlevel = 2;
	protected int currentlevel = 1;
	protected VEnemy[][] enemylist = null;
	protected String name;
	public FSMclass fsm_instance;
	
	protected VEnemy[] currentEnemyList = null;
	
	public VInstance(){
		
	}
	public static VUI createUI(){
		VInstanceUI ui = new VInstanceUI("starbackground.png","ui_instance");
		VText text = new VText("Battle 1/50");
		text.setLayout(VText.Layout_CENTER);
		text.setFont("font_big");
		text.setLoc(ui.getWidth()/2, ui.getHeight()/2);
		ui.addText(text);
		return ui;
	}
	public void initInstance(){
		// TODO 初始化副本数据
		initFSM();
		/*enemylist = new VEnemy[maxlevel][7];
		for(int i=0;i<1;i++){
			enemylist[0][i] = new VEnemy(2013);
			enemylist[1][i] = new VEnemy(1001);
		}*/
		//GlobalEvent.getLuaCore().runScriptFunction("initInstances", null);
	}
	public void initFSM(){
		fsm_instance = new FSMclass(FSMconst.BATTLE_ENEMYACTION);
		FSMstate fsms;
		fsms = new FSMstate(FSMconst.BATTLE_ENEMYACTION, 3);
		fsms.AddTransition(FSMconst.INPUT_PLAYERALIVE, FSMconst.BATTLE_PLAYERACTION);
		fsms.AddTransition(FSMconst.INPUT_PLAYERDIE, FSMconst.BATTLE_GAMEOVER);
		fsms.AddTransition(FSMconst.INPUT_ENEMYDIE, FSMconst.BATTLE_MOVETONEXTSTAGE);
		fsm_instance.AddState(fsms);
		fsms = new FSMstate(FSMconst.BATTLE_PLAYERACTION, 3);
		fsms.AddTransition(FSMconst.INPUT_PLAYERDIE, FSMconst.BATTLE_GAMEOVER);
		fsms.AddTransition(FSMconst.INPUT_ENEMYALIVE, FSMconst.BATTLE_ENEMYACTION);
		fsms.AddTransition(FSMconst.INPUT_ENEMYDIE, FSMconst.BATTLE_MOVETONEXTSTAGE);
		fsm_instance.AddState(fsms);
		fsms = new FSMstate(FSMconst.BATTLE_MOVETONEXTSTAGE, 2);
		fsms.AddTransition(FSMconst.INPUT_HAVENEXTSTAGE, FSMconst.BATTLE_ENEMYACTION);
		fsms.AddTransition(FSMconst.INPUT_NOTHAVENEXTSTAGE, FSMconst.BATTLE_GAMEWIN);
		fsm_instance.AddState(fsms);
	}
	@Override
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		if(ui instanceof VInstanceUI){
			instanceUI = (VInstanceUI)ui;
			instanceUI.bindEntity(this);
			uiBindUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#uiBindUpdate()
	 */
	@Override
	public void uiBindUpdate() {
		// TODO 更新UI显示
		if(instanceUI!=null){
			instanceUI.uiAction("UIupdate");
		}
	}
	public void loadEnemyData(){
		if(currentlevel!=0){
			currentEnemyList = new VEnemy[7];
			for(int i=0;i<7;i++){
				if(enemylist[currentlevel-1][i]!=null){	//从定义的当前层敌人对象中获取复制
					currentEnemyList[i]=enemylist[currentlevel-1][i].createCopy();
				}
			}
		}
		uiBindUpdate();		//更新UI
	}
	/**
	 * @return
	 */
	public void initEnemyList(int level){
		maxlevel = level;
		enemylist = new VEnemy[level][7];
	}
	public void setEnemy(int i,int j,VEnemy enemy){
		this.enemylist[i][j]=enemy;
	}
	public void setName(String name){
		this.name = name;
	}
	public VEnemy[] getEnemyList() {
		// TODO Auto-generated method stub
		return currentEnemyList;
	}
	public VEnemy getChosenEnemy(){
		// TODO 获取选定的敌人
		if(currentEnemyList!=null){
			return currentEnemyList[0];
		}
		else{
			return null;
		}
	}
	public VEnemy getEnemy(int index){
		// TODO 获取指定索引的敌人
		if(index<currentEnemyList.length){
			if(currentEnemyList[index]!=null){
				return currentEnemyList[index];
			}
		}
		return null;
	}
	public VEnemy getNextEnemy(VEnemy e){
		// TODO 获取指定敌人的下一个敌人
		VEnemy next = null;
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]==e){
				int j = i;
				while(j+1<currentEnemyList.length){		//如果指定的不是最后一个
					if(currentEnemyList[j+1]!=null){	//如果下一个敌人非空
						if(currentEnemyList[j+1].getAlive())
						{
							next = currentEnemyList[i+1];
							break;
						}
						else{
							j++;
						}
					}
					else{
						j++;
					}
				}
				if(next==null){		//如果指定敌人之后没有敌人，返回初始搜索
					int k=0;
					while(k<i){
						if(currentEnemyList[k]!=null){
							if(currentEnemyList[k].alive){
								next = currentEnemyList[k];
								break;
							}
							else{
								k++;
							}
						}
						else{
							k++;
						}
					}
				}
			}
		}
		if(next==e){
			return null;
		}
		else{
			return e;		//返回与指定敌人不同的下一个敌人
		}
	}
	/**
	 * 
	 */
	public void checkGutsBuff(){
		VEnemy[] enemylist = getEnemyList();
		if(enemylist!=null){
			for(int i=0;i<7;i++){
				if(enemylist[i]!=null){
					enemylist[i].checkGutsBuff();
				}
			}
		}
	}
	public void processState() {
		// TODO 处理敌人状态，判断是否死亡
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]!=null){
				if(currentEnemyList[i].getValue(VEnemy.ENEMY_CURRENTHP)==0){
					currentEnemyList[i].die();
				}
			}
		}
	}
	public void processBuffDecay(){
		// TODO 处理Buff衰减
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]!=null){
				currentEnemyList[i].buffDecay();
			}
		}
	}
	public boolean checkBattleEnd(){
		// TODO 判断当前战斗所有敌人是否全部死亡
		boolean enemydie = true;
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]!=null){
				if(currentEnemyList[i].getAlive()){
					enemydie=false;
				}
			}
		}
		if(enemydie){		//
			//toNextBattle();
		}
		return enemydie;
	}
	public void toNextBattle(){
		// TODO 进入下一战斗
		if(name==null){
			
		}
		else{
			if(currentlevel<maxlevel){
				currentlevel++;
				instanceUI.getText(0).setText("Battle"+currentlevel+"/"+maxlevel);
				instanceUI.getText(0).setExpire(100);
				VTimer tm = new VTimer(1000,1000,false,new VLuaAction(){
					@Override
					public void action() {
						// TODO 控制敌人出现效果的占用时间
						loadEnemyData();
						instanceUI.setEnemyUITCount(30);
					}
				});
				tm.timerStart(VTimer.TIMER_STAGE);	
				VTimer tmstate = new VTimer(1300,1300,false,new VLuaAction(){
					@Override
					public void action() {
						// TODO 控制敌人行动的占用时间
						instanceStateTransition(FSMconst.INPUT_HAVENEXTSTAGE);
					}
				});
				tmstate.timerStart(VTimer.TIMER_STAGE);
			}
			else{
				instanceUI.getText(0).setText("CLEAR!");
				instanceUI.getText(0).setExpire(-1);
			}
		}
	}
	/**
	 * 
	 */
	public void enemyAction() {
		// TODO 当前敌人进行行动
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]!=null){
				if(currentEnemyList[i].getAlive()){
					currentEnemyList[i].enemyAction();
				}
			}
		}
	}
	public void enemyActionEnd(){
		// TODO 敌人行动结束，回合数恢复为行动回合数
		for(int i=0;i<currentEnemyList.length;i++){
			if(currentEnemyList[i]!=null){
				if(currentEnemyList[i].getAlive()){
					currentEnemyList[i].enemyActionEnd();
				}
			}
		}
	}
	public void addHint(String hint){
		// TODO 使提示UI显示并添加一条提示文本
		instanceUI.skillhint_ui.addText(hint);
		instanceUI.skillhint_ui.setTransparencyCount(100);
	}
	/**
	 * 
	 */
	public void instanceStateTransition(int input){
		// TODO 对副本对象的fsm输入一个状态控制
		int state=fsm_instance.StateTransition(input);
		switch(state){		//获取fsm状态转换后的当前状态
		case FSMconst.BATTLE_ENEMYACTION:
			enemyAction();
			VTimer tm = new VTimer(1000,1000,false,new VLuaAction(){
				@Override
				public void action() {
					// TODO Auto-generated method stub
					enemyActionEnd();
					if(GameData.party.getAlive()){
						instanceStateTransition(FSMconst.INPUT_PLAYERALIVE);
					}
					else{
						instanceStateTransition(FSMconst.INPUT_PLAYERALIVE);
					}
				}
			});
			tm.timerStart(VTimer.TIMER_STAGE);
			break;
		case FSMconst.BATTLE_PLAYERACTION:
			//GameData.party.dealMatchAction(matched);	//处理转珠触发，包括自动回复及月鸡技能等
			GameData.party.turnStart();		//进入下一回合开始
			GlobalEvent.movingLockUI(false);
			GlobalEvent.showDisableCover(false);
			break;
		case FSMconst.BATTLE_MOVETONEXTSTAGE:
			toNextBattle();
			break;
		case FSMconst.BATTLE_GAMEOVER:
			break;
		case FSMconst.BATTLE_GAMEWIN:
			break;
		}
	}
	public void gameStart() {
		// TODO 战斗开始
		
		GlobalEvent.movingLockUI(true);
		GlobalEvent.showDisableCover(true);
		currentlevel=0;
		fsm_instance.resetState();		//重置FSM状态机
		instanceStateTransition(FSMconst.INPUT_ENEMYDIE);
	}
}
