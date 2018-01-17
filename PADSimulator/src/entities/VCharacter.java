/**
 * 	角色类，包含角色属性参数等
 */
package entities;

import interfaces.VUIBindingInterface;

import java.util.ArrayList;

import data.GameData;

import ui.VCharacterUI;
import ui.VUI;
import view.VTexture;
import xml.VStringNode;
import xml.VtoXMLInterface;
import xml.XMLPack;
import global.Debug;
import global.Imageconst;

/**
 * @author Demilichzz
 *
 */
public class VCharacter implements VtoXMLInterface,VUIBindingInterface{	
	//----------参数解码表--------------------------------
	public static final String[]colorDecode = {"―","火","水","木","光","闇"};
	public static final String[]awokenDecode = {"~","~","~","HP強化","攻撃強化","回復強化"
			,"火ダメージ軽減","水ダメージ軽減","木ダメージ軽減","光ダメージ軽減","闇ダメージ軽減"
			,"自動回復","バインド耐性","暗闇耐性","お邪魔耐性","毒耐性"
			,"火ドロップ強化","水ドロップ強化","木ドロップ強化","光ドロップ強化","闇ドロップ強化"
			,"操作時間延長","バインド回復","スキルブースト"
			,"火属性強化","水属性強化","木属性強化","光属性強化","闇属性強化"
			,"2体攻撃","封印耐性","回復ドロップ強化","マルチブースト","ドラゴンキラー"
			,"神キラー","悪魔キラー","マシンキラー"};
	public static final String[]typeDecode = {"―","神タイプ","ドラゴンタイプ","悪魔タイプ","バランスタイプ"
									,"攻撃タイプ","体力タイプ","回復タイプ","マシンタイプ","進化用モンスター"
									,"強化合成用モンスター","覚醒用モンスター","特別保護タイプ","売却用モンスター"};
	//----------常量参数表---------------------------------
	public static final int PARAM_ID = 0;
	public static final int PARAM_NAME = 1;
	public static final int PARAM_COLOR1 = 2;
	public static final int PARAM_COLOR2 = 3;
	public static final int PARAM_TYPE1 = 4;
	public static final int PARAM_TYPE2 = 5;
	public static final int PARAM_RAREITY = 6;
	public static final int PARAM_LV = 7;
	public static final int PARAM_MAXLV = 8;
	public static final int PARAM_COST = 9;
	public static final int PARAM_HP = 10;
	public static final int PARAM_MAXHP = 11;
	public static final int PARAM_EXP = 12;
	public static final int PARAM_ATK = 13;
	public static final int PARAM_MAXATK = 14;
	public static final int PARAM_RCV = 15;
	public static final int PARAM_MAXRCV = 16;
	public static final int PARAM_POINT = 17;
	public static final int PARAM_SKILL = 18;
	public static final int PARAM_COOLDOWN = 19;
	public static final int PARAM_SKILLDESC = 20;
	public static final int PARAM_LSKILL = 21;
	public static final int PARAM_LSKILLDESC = 22;
	public static final int PARAM_AWOKEN = 23;
	public static final int PARAM_SAMESKILLCHAR = 24;
	//----------------------------------------------------
	//-----------觉醒参数表-------------------------------
	public static final int AWOKEN_HP = 3;
	public static final int AWOKEN_ATK = 4;
	public static final int AWOKEN_RCV = 5;
	public static final int AWOKEN_DR_FIRE = 6;		//减伤觉醒
	public static final int AWOKEN_DR_WATER = 7;
	public static final int AWOKEN_DR_WOOD = 8;
	public static final int AWOKEN_DR_LIGHT = 9;
	public static final int AWOKEN_DR_DARK = 10;
	public static final int AWOKEN_AUTORCV = 11;
	public static final int AWOKEN_RESBIND = 12;
	public static final int AWOKEN_RESDARK = 13;
	public static final int AWOKEN_RESJAMMER = 14;
	public static final int AWOKEN_RESPOISON = 15;
	public static final int AWOKEN_ENH_FIRE = 16;		//加珠觉醒
	public static final int AWOKEN_ENH_WATER = 17;
	public static final int AWOKEN_ENH_WOOD = 18;
	public static final int AWOKEN_ENH_LIGHT = 19;
	public static final int AWOKEN_ENH_DARK = 20;
	public static final int AWOKEN_TIME = 21;
	public static final int AWOKEN_RCVBIND = 22;
	public static final int AWOKEN_SKILLBOOST = 23;
	public static final int AWOKEN_ENR_FIRE = 24;		//横行觉醒
	public static final int AWOKEN_ENR_WATER = 25;
	public static final int AWOKEN_ENR_WOOD = 26;
	public static final int AWOKEN_ENR_LIGHT = 27;
	public static final int AWOKEN_ENR_DARK = 28;
	public static final int AWOKEN_DOUBLE = 29;
	public static final int AWOKEN_SKILLRESBIND = 30;
	public static final int AWOKEN_ENH_RCV = 31;
	public static final int AWOKEN_MULTIBOOST = 32;
	public static final int AWOKEN_DRAGONKILLER = 33;
	public static final int AWOKEN_GODKILLER = 34;
	public static final int AWOKEN_DEVILKILLER = 35;
	public static final int AWOKEN_MACHINEKILLER = 36;
	//-----------目标选择模式------------------------
	public static final int TARGET_SINGLE=0;
	public static final int TARGET_DOUBLE=1;
	public static final int TARGET_ALL=3;
	//----------------------------------------------
	protected VTexture image;
	protected String name;	
	//----------读取XML数据生成角色属性相关变量------------------------
	protected String[] stringParamList;		//字符串属性列表
	protected int[] intParamList;		//索引或存在int形式的属性列表
	protected int[] awokenList;		//觉醒ID索引列表
	protected int[] typeList;
	protected int activeawoken;		//已激活的觉醒技能数量
	protected int plus_hp,plus_atk,plus_rcv;
	protected int[] sameSkillCharList;		//同技能角色ID索引列表
	protected int basecooldown;		//未练技能时冷却回合数
	protected int maxcooldown;		//满技冷却回合数
	protected int currentcooldown;	//当前技能状况下冷却回合数
	protected int battlecooldown;	//在战斗中使用的冷却回合数参数
	protected int battlebinding;	//封锁回合数
	//----------------------------------------------------------------
	//----------战斗伤害计算相关变量----------------------------------
	protected VActiveSkill askill;
	protected double damagepool1,damagepool2;	//主副属性的伤害池
	protected double temppool1,temppool2;
	protected double recoverypool;		//回复
	protected double temprecoverypool;
	protected int targetmode1,targetmode2; //主副属性的目标模式，0为单体，1为2体，2为全体	
	protected VEnemy targetlist1[],targetlist2[];
	
	protected VCharacterUI char_ui;
	
	public VCharacter(){
		stringParamList = new String[23];
		intParamList = new int[23];
		awokenList = new int[20];
		typeList = new int[5];
		sameSkillCharList = new int[20];
		resetDamagePool();
	}
	public VCharacter(String str){
		image = Imageconst.GetImageByName(str);
		stringParamList = new String[23];
		resetDamagePool();
	}
	
	public void setAdditionalStat(){
		// TODO 设置角色额外属性，包括技能等级，觉醒数，加蛋等
		this.currentcooldown = this.basecooldown;
		this.battlecooldown = this.currentcooldown;
		this.activeawoken = 99;
		this.plus_hp = 99;
		this.plus_atk = 99;
		this.plus_rcv = 99;
	}
	public VCharacter createCopy(){
		// TODO 创建一个当前角色对象的复制
		VCharacter newchar = new VCharacter();
		newchar.typeList = this.typeList.clone();
		newchar.stringParamList = this.stringParamList.clone();
		newchar.awokenList = this.awokenList.clone();
		newchar.image = Imageconst.GetImageByName(intParamList[0]+".png");
		newchar.activeawoken = this.activeawoken;
		newchar.plus_hp = this.plus_hp;
		newchar.plus_atk = this.plus_atk;
		newchar.plus_rcv = this.plus_rcv;
		newchar.intParamList = this.intParamList.clone();
		newchar.sameSkillCharList = this.sameSkillCharList.clone();
		newchar.basecooldown = this.basecooldown;		
		newchar.maxcooldown = this.maxcooldown;;		
		newchar.currentcooldown = this.currentcooldown;
		newchar.battlecooldown = this.basecooldown;
		newchar.battlebinding = this.battlebinding;
		VActiveSkill askill = new VActiveSkill(newchar);
		newchar.addASkill(askill);
		askill.initSkill();
		return newchar;
	}
	public VTexture getImage(){
		return image;
	}
	public void resetDamagePool(){
		damagepool1 = 0;
		damagepool2 = 0;
		recoverypool = 0;
		targetmode1 = 0;
		targetmode2 = 0;
		temppool1 = 0;
		temppool2 = 0;
		temprecoverypool = 0;
		targetlist1 = new VEnemy[7];
		targetlist2 = new VEnemy[7];
	}
	public int getDamageValue(int index){
		// TODO 按指定索引获取伤害数值
		switch(index){
		case 0:
			return (int) damagepool1;
		case 1:
			return (int) damagepool2;
		default:
			return -1;
		}
	}
	public void setTarget(int index){
		// TODO 设置主副属性的攻击目标
		VEnemy chosen = GameData.instance.getChosenEnemy();
		if (index == 0) {	//主属性
			switch (targetmode1) {
			case 0: // 单体
				targetlist1[0] = chosen;
				break;
			case 1: // 二体
				targetlist1[0] = chosen;
				targetlist1[1] = GameData.instance.getNextEnemy(chosen);
				break;
			case 2: // 全体
				for (int i = 0; i < 7; i++) {
					targetlist1[i] = GameData.instance.getEnemyList()[i];
				}
				break;
			default:
				break;
			}
		}
		else{
			switch (targetmode2) {
			case 0: // 单体
				targetlist2[0] = chosen;
				break;
			case 1: // 二体
				targetlist2[0] = chosen;
				targetlist2[1] = GameData.instance.getNextEnemy(chosen);
				break;
			case 2: // 全体
				for (int i = 0; i < 7; i++) {
					targetlist2[i] = GameData.instance.getEnemyList()[i];
				}
				break;
			default:
				break;
			}
		}
	}
	public void dealDamage(int index){
		// TODO 造成伤害
		if(index==0){		//主属性
			for(int i=0;i<7;i++){
				if(targetlist1[i]!=null){
					targetlist1[i].getDamage(damagepool1,this.getIntValue(VCharacter.PARAM_COLOR1));
				}
			}
		}
		else{
			for(int i=0;i<7;i++){
				if(targetlist2[i]!=null){
					targetlist2[i].getDamage(damagepool2,this.getIntValue(VCharacter.PARAM_COLOR2));
				}
			}
		}
	}
	public void addASkill(VActiveSkill askill){
		this.askill = askill;
	}
	/**
	 * @param m
	 */
	public void addMatch(VMatchOrb m) {
		// TODO 为角色添加一个消除串，判断该消除串造成的伤害并添加到伤害池中
		int orbtype = m.getValue(VMatchOrb.ORB_TYPE);
		int orbsum = m.getValue(VMatchOrb.ORB_ORBSUM);
		int orbplus = m.getValue(VMatchOrb.ORB_PLUSSUM);
		int orbdouble = m.getValue(VMatchOrb.ORB_DOUBLE);
		int orbrow = m.getValue(VMatchOrb.ORB_LINE);
		
		int atk = getTotalAtk();
		int rcv = getTotalRcv();
		
		double basedamage = 0;
		if(orbdouble==1){	//是U，则伤害乘二体觉醒倍率
			basedamage = atk*1.25*Math.pow(1.5, getAvaliableAwoken(VCharacter.AWOKEN_DOUBLE));
		}
		else{
			basedamage = atk*(1+(orbsum-3)*0.25);
		}
		if(orbrow==1){	//是横排，则像VParty中添加横排觉醒触发数量
			if(orbtype>0&&orbtype<6){
				GameData.party.addRowAwoken(
						m.getValue(VMatchOrb.ORB_TYPE),
						getAvaliableAwoken(VCharacter.AWOKEN_ENR_FIRE - 1
								+ m.getValue(VMatchOrb.ORB_TYPE)));
			}
			else if(orbtype==6){
				GameData.party.bindRecovery();
			}
		}
		if (orbplus > 0) {
			basedamage = basedamage * (1 + orbplus * 0.06); // 计算加珠伤害加成
			if (orbtype > 0 && orbtype < 6) {
				basedamage = basedamage
						* (1 + GameData.party
								.getAvaliableAwoken(VCharacter.AWOKEN_ENH_FIRE
										- 1 + orbtype) * 0.04);
			}
		}
		if(getIntValue(PARAM_COLOR1)==orbtype){	//符合主属性
			damagepool1 = damagepool1 + basedamage;
			if(orbsum>=5&&targetmode1<2){
				targetmode1 = 2;
			}
			else if(orbdouble==1&&targetmode1<1){
				targetmode1 = 1;
			}
		}
		if(getIntValue(PARAM_COLOR2)==orbtype){
			if(getIntValue(PARAM_COLOR1)==getIntValue(PARAM_COLOR2)){
				damagepool2 = damagepool2 + basedamage/10;
			}
			else{
				damagepool2 = damagepool2 + basedamage/3;
			}
			if(orbsum>=5&&targetmode2<2){
				targetmode2 = 2;
			}
			else if(orbdouble==1&&targetmode2<1){
				targetmode2 = 1;
			}
		}
		if(orbtype==6){	//回复
			double temprcv = rcv*(1+(orbsum-3)*0.25);
			recoverypool = recoverypool+temprcv;
		}
	}

	/* (non-Javadoc)
	 * @see xml.VtoXMLInterface#toXMLPack()
	 */
	@Override
	public XMLPack toXMLPack() {
		// TODO 转换为XML包
		return null;
	}

	/* (non-Javadoc)
	 * @see xml.VtoXMLInterface#setValueFromPack(xml.XMLPack)
	 */
	@Override
	public void setValueFromPack(XMLPack p) {
		// TODO 通过XML包获取参数
		ArrayList<VStringNode> childrenlist = p.valuenode.getChildren();
		for(int i=0;i<23;i++){		//获取字符串形式的属性列表
			stringParamList[i]=childrenlist.get(i).getName();
		}
		if(stringParamList[1].equals("-")){
			//System.out.println(stringParamList[0]);
		}
		else {
			// --------将字符串属性列表转换为int属性列表，或其他形式的变量--------------
			intParamList[0] = Integer.parseInt(stringParamList[0]);
			image = Imageconst.GetImageByName(intParamList[0] + ".png");
			intParamList[1] = -1; // 保持字符串形式的参数，将对应的属性列表值设置为-1
			intParamList[2] = VCharacter.getDecodeValue(stringParamList[2],
					VCharacter.colorDecode);
			intParamList[3] = VCharacter.getDecodeValue(stringParamList[3],
					VCharacter.colorDecode);
			intParamList[4] = VCharacter.getDecodeValue(stringParamList[4],
					VCharacter.typeDecode);
			intParamList[5] = VCharacter.getDecodeValue(stringParamList[5],
					VCharacter.typeDecode);
			intParamList[6] = Integer.parseInt(stringParamList[6]);
			intParamList[7] = 1;
			for (int i = 8; i < 12; i++) {
				intParamList[i] = Integer.parseInt(stringParamList[i]);
			}
			if (stringParamList[12].equals("―")||stringParamList[12].equals("未確認")) {
				intParamList[12] = 0;
			} else {
				intParamList[12] = Integer.parseInt(stringParamList[12]);
			}
			for (int i = 13; i < 17; i++) {
				intParamList[i] = Integer.parseInt(stringParamList[i]);
			}
			intParamList[18] = 0; // mp appbank网站上没有数据
			for (int i = 18; i < 23; i++) {
				intParamList[i] = -1;
			}
			// ---------技能冷却数值处理------------------------
			String cd_str = stringParamList[19]; // 获取技能冷却字符串
			if (cd_str.equals("")) {
				this.basecooldown = 0;
				this.maxcooldown = 0;
			} else {
				String basecd_str = cd_str.substring(cd_str.indexOf("：") + 1,
						cd_str.indexOf("（"));
				this.basecooldown = Integer.parseInt(basecd_str);
				String maxcd_str = cd_str.substring(cd_str.indexOf("（") + 1,
						cd_str.indexOf("）"));
				this.maxcooldown = Integer.parseInt(maxcd_str);
			}
			// -------------------------------------------------
			ArrayList<VStringNode> typeList = childrenlist.get(23)
					.getChildren();
			if (typeList != null) {
				this.typeList = new int[typeList.size()];
				for (int i = 0; i < typeList.size(); i++) { // 获取类别索引
					this.typeList[i] = VCharacter.getDecodeValue(typeList
							.get(i).getName(), VCharacter.typeDecode);
				}
			}
			ArrayList<VStringNode> awokenList = childrenlist.get(24)
					.getChildren();
			if (awokenList != null) {
				this.awokenList = new int[awokenList.size()];
				for (int i = 0; i < awokenList.size(); i++) { // 获取觉醒技能索引
					this.awokenList[i] = VCharacter.getDecodeValue(awokenList
							.get(i).getName(), VCharacter.awokenDecode);
				}
			}
			ArrayList<VStringNode> sameSkillCharList = childrenlist.get(25)
					.getChildren();
			if (sameSkillCharList != null) {
				this.sameSkillCharList = new int[sameSkillCharList.size()];
				for (int i = 0; i < sameSkillCharList.size(); i++) {
					String strid = sameSkillCharList.get(i).getName();
					strid = strid.substring(3, strid.indexOf(" "));
					this.sameSkillCharList[i] = Integer.parseInt(strid);
				}
			}
		}
	}

	private static int getDecodeValue(String str,String[] decodeStr) {
		// TODO 通过匹配解码字符串数组的形式，获取字符串对应的int索引
		int decode = -1;
		for(int i=0;i<decodeStr.length;i++){
			if(decodeStr[i].equals(str)){
				decode = i;
			}
		}
		return decode;
	}
	public String getStringValue(int index){
		return stringParamList[index];
	}
	public int getIntValue(int index){
		return intParamList[index];
	}

	@Override
	public void uiBindUpdate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		this.char_ui = (VCharacterUI) ui;
	}
	
	public int getTotalHP(){
		// TODO 获取经过觉醒技能及加蛋加成的总HP
		int hp = getIntValue(VCharacter.PARAM_MAXHP);
		int hpawoken = getAvaliableAwoken(VCharacter.AWOKEN_HP);
		hp=hp+hpawoken*200+plus_hp*10;
		return hp;
	}
	public int getTotalAtk(){
		// TODO 获取总计攻击力
		int atk = getIntValue(VCharacter.PARAM_MAXATK);
		int atkawoken = getAvaliableAwoken(VCharacter.AWOKEN_ATK);
		atk=atk+atkawoken*100+plus_atk*5;
		return atk;
	}
	public int getTotalRcv(){
		// TODO 获取总计回复力
		int rcv = getIntValue(VCharacter.PARAM_MAXRCV);
		int rcvawoken = getAvaliableAwoken(VCharacter.AWOKEN_RCV);
		rcv=rcv+rcvawoken+plus_rcv*3;
		return rcv;
	}
	public int getCoolDown(){
		// TODO 获取目前战斗中的技能冷却时间
		return battlecooldown;
	}
	public void setCoolDown(int cd){
		battlecooldown = cd;
	}
	public void processCoolDown(int cd) {
		// TODO 在进行消除后处理所有角色冷却时间-1
		battlecooldown = battlecooldown-cd;
		if(battlecooldown<0){
			battlecooldown=0;
		}
		else if(battlecooldown>currentcooldown){
			battlecooldown=currentcooldown;
		}
	}
	public void bindRecovery(int turn){
		// TODO 解除封印
		battlebinding = battlebinding - turn;
		if(battlebinding<0){
			battlebinding=0;
		}
	}
	public int getAvaliableAwoken(int index){
		// TODO 获取角色可用指定觉醒的数量
		int num=0;
		if(battlebinding==0){		//如果未被封印
			for(int i=0;i<awokenList.length;i++){
				if(i<activeawoken){		//寻找激活的觉醒技能是否有HP增加
					if(awokenList[i]==index){
						num++;
					}
				}
				else{
					break;
				}
			}
		}
		return num;
	}
	/**
	 * 
	 */
	public void activeSkillAction() {
		// TODO 执行角色定义的技能行为
		if(askill!=null){
			if(battlecooldown==0&&askill!=null){
				askill.action();
				battlecooldown = currentcooldown;
			}
		}
	}
}
