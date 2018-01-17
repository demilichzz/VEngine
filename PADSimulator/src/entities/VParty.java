/**
 * 	队伍类，包含玩家队伍中角色信息等
 */
package entities;

import java.util.ArrayList;

import data.GameData;

import event.GlobalEvent;
import global.CharacterConst;

import system.VEngine;
import timer.VLuaAction;
import timer.VTimer;
import ui.*;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VParty implements VUIBindingInterface,VValueInterface{
	public static final int PARTY_MAXHP = 0;
	public static final int PARTY_CURRENTHP = 1;
	public static final int PARTY_CURRENTRECOVERY = 2;
	
	protected int basemovetime = 400;	//基础移动时间
	
	protected int temprecoverypool = 0;
	protected VPartyUI partyUI = null;
	protected VCharacter[] charlist = new VCharacter[6];
	protected int dealindex = 0;
	protected int maxhp = 0;
	protected int currenthp = 0;
	protected int currentrecovery = 0;
	protected int maxcombo = 0;
	protected int currentcombo = 0;
	protected int turncount=0;
	//-----------伤害计算参数---------------------------------
	protected int awoken_row[]=new int[6];
	protected int awoken_list[] = new int[31];	//记录索引对应的觉醒数量
	
	protected ArrayList<VMatchOrb> m_orblist = null;	//定义当前消除中消除的宝石列表
	
	
	public VParty(){
		resetPartyState();
	}
	public static VUI createUI(){
		VPartyUI ui = new VPartyUI("partybackground.png","ui_party");
		return ui;
	}
	public void resetPartyState(){
		// TODO 伤害计算完成后调用，重置伤害计算相关的队伍状态
		maxcombo = 0;
		currentcombo=0;
		dealindex=0;
		resetMOrblist();
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				charlist[i].resetDamagePool();	//清除本轮战斗伤害池
			}
		}
		resetPartyAwokenState();
		this.setValue(VParty.PARTY_CURRENTRECOVERY, 0);
		currentrecovery=0;
	}
	public void addRowAwoken(int index,int num){
		// TODO 添加指定数量的横排觉醒触发
		awoken_row[index] = awoken_row[index]+num;
	}
	public void loadMaxCombo(int combo){
		maxcombo = combo;
	}
	public void resetMOrblist(){
		// TODO 重置消除宝石的列表
		m_orblist = new ArrayList<VMatchOrb>();
	}
	public void addMOrb(VMatchOrb orb){
		// TODO 添加一个消除对象
		m_orblist.add(orb);
	}
	public void initParty(int[] party){
		// TODO 初始化队伍
		for(int i=0;i<6;i++){
			
		}
	}
	public void loadPartyData(){
		// TODO 加载队伍数据
		charlist[0]=CharacterConst.getCharacter(2253).createCopy();
		charlist[1]=CharacterConst.getCharacter(2657).createCopy();
		charlist[2]=CharacterConst.getCharacter(2498).createCopy();
		charlist[3]=CharacterConst.getCharacter(2235).createCopy();
		charlist[4]=CharacterConst.getCharacter(1728).createCopy();
		charlist[5]=CharacterConst.getCharacter(2253).createCopy();	
		setPartyHP();
		uiBindUpdate();
	}
	public void setPartyHP() {
		// TODO 在队伍状态改动时调用，更新队伍的HP
		int hp = 0;
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				//hp = hp + charlist[i].getIntValue(VCharacter.PARAM_MAXHP);
				hp=hp+charlist[i].getTotalHP();
			}
		}
		maxhp = hp;
		currenthp = maxhp;
		
	}
	@Override
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		if(ui instanceof VPartyUI){
			partyUI = (VPartyUI)ui;
			partyUI.bindEntity(this);
			uiBindUpdate();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#uiBindUpdate()
	 */
	@Override
	public void uiBindUpdate() {
		// TODO 更新UI显示
		if(partyUI!=null){
			partyUI.uiAction("UIupdate");
		}
	}
	public VCharacter[] getCharList(){
		return charlist;
	}
	public boolean getAlive(){
		return getValue(currenthp)>0;
	}
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case PARTY_MAXHP:
			return maxhp;
		case PARTY_CURRENTHP:
			return currenthp;
		case PARTY_CURRENTRECOVERY:
			return currentrecovery;
		}
		return 0;
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#setValue(int, int)
	 */
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		switch(index){
		case PARTY_MAXHP:
			maxhp = value;
		case PARTY_CURRENTHP:
			currenthp = value;
		case PARTY_CURRENTRECOVERY:
			currentrecovery = value;
		}
	}
	public void getMatchOrbs(ArrayList<VMatchOrb> matchorbs) {
		// TODO 获取回合结束时进行消除的宝石串数据，以进行计算
		m_orblist = matchorbs;
		for(VMatchOrb m:m_orblist){
			m.printOrb();
		}
	}
	public void getMatchOrb(VMatchOrb matchorb){
		m_orblist.add(matchorb);
		//matchorb.printOrb();
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				charlist[i].addMatch(matchorb);
			}
		}
		GlobalEvent.getGameAreaUI().addHint(matchorb); 
		double rec = 0;
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				rec = rec+charlist[i].recoverypool;
			}
		}
		if(rec<0){
			rec=0;
		}
		this.setValue(VParty.PARTY_CURRENTRECOVERY, (int)rec);
	}
	public int getMoveTime(){
		// TODO 获取转珠时间
		return basemovetime+getAvaliableAwoken(VCharacter.AWOKEN_TIME)*50;
	}
	public void calculateDamage(){
		// TODO 计算当前消除造成的伤害与回复
		for(VMatchOrb m:m_orblist){
			for(int i=0;i<6;i++){
				if(charlist[i]!=null){
					charlist[i].temppool1 = charlist[i].damagepool1;
					charlist[i].temppool2 = charlist[i].damagepool2;
					//charlist[i].temprecoverypool = charlist[i].recoverypool;
				}
			}
			this.temprecoverypool = this.getValue(VParty.PARTY_CURRENTRECOVERY);
		}
		for(int c=0;c<maxcombo;c++){	//添加消除结束后计算combo倍率的Timer
			VTimer tm = new VTimer(c*500+1,c*500+1,false,new VLuaAction(){
				@Override
				public void action() {
					// TODO 计算combo倍率
					for(VMatchOrb m:m_orblist){
						for(int i=0;i<6;i++){
							if(charlist[i]!=null){
								charlist[i].damagepool1 = charlist[i].temppool1*(1+currentcombo*0.25);
								charlist[i].damagepool2 = charlist[i].temppool2*(1+currentcombo*0.25);
							}
						}
					}
					GlobalEvent.getGameAreaUI().getText(currentcombo*3).setVisible(false);
					GlobalEvent.getGameAreaUI().getText(currentcombo*3+1).setVisible(false);
					GlobalEvent.getGameAreaUI().getText(currentcombo*3+2).setVisible(false);
					setValue(VParty.PARTY_CURRENTRECOVERY,(int) (temprecoverypool*(1+currentcombo*0.25)));
					currentcombo = currentcombo+1;
				}			
			});
			tm.timerStart(VTimer.TIMER_STAGE);
		}
		VTimer tm_multi = new VTimer(maxcombo*500+1,maxcombo*500+1,false,new VLuaAction(){
			@Override
			public void action() {
				// TODO combo倍率计算完成后，计算总体伤害倍率(队长技，横排，主动技等)
				for(int i=0;i<6;i++){
					if(charlist[i]!=null){
						int color1 = charlist[i].getIntValue(VCharacter.PARAM_COLOR1);
						int color2 = charlist[i].getIntValue(VCharacter.PARAM_COLOR2);
						charlist[i].damagepool1=charlist[i].damagepool1*(1+awoken_row[color1]*0.1);
						charlist[i].damagepool2=charlist[i].damagepool2*(1+awoken_row[color2]*0.1);
						charlist[i].damagepool1*=16;
						charlist[i].damagepool2*=16;
					}
				}
			}
		});
		tm_multi.timerStart(VTimer.TIMER_STAGE);
		for(int i=0;i<6;i++){		//主属性伤害
			if(charlist[i]!=null){
				VTimer tm_deal = new VTimer(maxcombo*500+50+50*i,maxcombo*500+50+50*i,false,new VLuaAction(){
					@Override
					public void action() {
						// TODO 造成伤害过程
						charlist[dealindex].setTarget(0);
						charlist[dealindex].dealDamage(0);
						dealindex++;
					}
				});
				tm_deal.timerStart(VTimer.TIMER_STAGE);
			}
		}
		for(int i=0;i<6;i++){		//副属性伤害
			if(charlist[i]!=null){
				VTimer tm_deal = new VTimer(maxcombo*500+350+50*i,maxcombo*500+350+50*i,false,new VLuaAction(){
					@Override
					public void action() {
						// TODO 造成伤害过程
						dealindex--;
						charlist[dealindex].setTarget(1);
						charlist[dealindex].dealDamage(1);
					}
				});
				tm_deal.timerStart(VTimer.TIMER_STAGE);
			}
		}
		VTimer tm_heal = new VTimer(maxcombo*500+350,maxcombo*500+350,false,new VLuaAction(){
			@Override
			public void action() {
				// TODO 回复进行治疗
				getHeal(getValue(VParty.PARTY_CURRENTRECOVERY));
			}
		});
		tm_heal.timerStart(VTimer.TIMER_STAGE);
		VTimer tm_enemybuffcheck = new VTimer(maxcombo*500+710,maxcombo*500+710,false,new VLuaAction(){
			@Override
			public void action() {
				// TODO 检查根性Buff
				GameData.instance.checkGutsBuff();
			}
		});
		tm_enemybuffcheck.timerStart(VTimer.TIMER_STAGE);
		VTimer tm_done = new VTimer(maxcombo*500+1000,maxcombo*500+1000,false,new VLuaAction(){
			@Override
			public void action() {
				// TODO 造成伤害步骤结束，重置伤害池等状态
				resetPartyState();
				GameData.ga.orbsExecuteFinish_Step2();
			}
		});
		tm_done.timerStart(VTimer.TIMER_STAGE);
	}

	public void turnStart() {
		// TODO 回合开始时的处理
		turncount++;
		processBuff();
	}
	public void processBuff(){
		// TODO 处理附加状态效果的时间计算
		
	}
	public void processBinding(){
		// TODO 处理角色的封锁时间
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				if(charlist[i].battlebinding>0){
					charlist[i].battlebinding--;
				}
			}
		}
	}
	public void processCoolDown() {
		// TODO 处理角色的冷却时间
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				charlist[i].processCoolDown(1);
			}
		}
	}
	/**
	 * @param damage
	 * @param color
	 */
	public void getDamage(double damage, int color) {
		// TODO 受到敌人的伤害
		double awoken_dr = getAvaliableAwoken(VCharacter.AWOKEN_DR_FIRE-1+color);
		damage = damage*(1-awoken_dr*0.05);
		if(damage<0){
			damage=0;
		}
		currenthp = (int) (currenthp - damage);
	}
	public void getDamagePercent(double percent,int color){
		// TODO 受到百分比伤害
		double awoken_dr = getAvaliableAwoken(VCharacter.AWOKEN_DR_FIRE-1+color);
		double hp = currenthp;
		currenthp = (int) (hp*(100-percent)/100*(1-awoken_dr*0.05));
	}
	public void getHeal(double heal){
		currenthp = (int) (currenthp + heal);
		if(currenthp>maxhp){
			currenthp=maxhp;
		}
	}
	/**
	 * @param awokenSkillboost
	 * @return
	 */
	public int getAvaliableAwoken(int a) {
		// TODO 获取全部角色的指定类型觉醒数量总和
		int awoken = 0;
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				awoken = awoken + charlist[i].getAvaliableAwoken(a);
			}
		}
		return awoken;
	}
	public void getPartyAwokenState(){
		// TODO 当转珠释放时调用，获取当前队伍的全局状态型觉醒参数
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				for(int j=0;j<31;j++){
					awoken_list[j]=awoken_list[j]+charlist[i].getAvaliableAwoken(j);
				}
			}
		}
	}
	public int getPartyAwokenState(int index){
		// TODO 获取队伍中指定觉醒的数量
		return awoken_list[index];
	}
	public void resetPartyAwokenState(){
		// TODO 重置全局状态型觉醒参数
		awoken_row=new int[6];		//重置横排触发
		awoken_list=new int[31];
		
	}
	public void dealMatchAction(boolean matched){
		// TODO 当次有消除时，在回合结束触发的行为
		if(matched){
			int autorec = getAvaliableAwoken(VCharacter.AWOKEN_AUTORCV);
			getHeal(autorec*500);
			processCoolDown();	//主动技能冷却时间的处理
		}
		processBinding();	//封锁状态的处理
	}

	public void bindRecovery() {
		// TODO 横排心觉醒触发的封锁回复
		int awoken_rcvbind = this.getAvaliableAwoken(VCharacter.AWOKEN_RCVBIND);
		bindRecovery(awoken_rcvbind*3);
	}
	public void bindRecovery(int turn){
		// TODO 封锁回复指定的回合数
		for(int i=0;i<6;i++){
			if(charlist[i]!=null){
				charlist[i].bindRecovery(turn);
			}
		}
	}
}
