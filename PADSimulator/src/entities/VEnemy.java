/**
 * 	定义副本中进行战斗的敌人
 */
package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import data.GameData;
import data.StaticData;
import interfaces.VUIBindingInterface;
import interfaces.VValueInterface;
import global.FSMconst;
import global.Imageconst;
import global.VMath;
import timer.VLuaObjectAction;
import ui.VEnemyUI;
import ui.VUI;
import view.VText;
import view.VTexture;

/**
 * @author Demilichzz
 *
 */
public class VEnemy implements VValueInterface,VUIBindingInterface{
	public static final int ENEMY_MAINTYPE = 0;		//主属性，血条左半
	public static final int ENEMY_SECONDTYPE = 1;	//副属性，血条右半
	public static final int ENEMY_MAXHP = 2;
	public static final int ENEMY_CURRENTHP = 3;
	public static final int ENEMY_MOVETURN = 4;
	public static final int ENEMY_CURRENTTURN = 5;
	public static final int ENEMY_BASEDAMAGE = 6;
	public static final int ENEMY_DEFEND = 7;

	protected int[]paramlist = new int[8];
	
	protected VTexture image;
	protected String name;
	protected VEnemyUI ui;
	protected boolean alive = true;
	protected int id = 0;
	protected VLuaObjectAction action;
	protected int runtime = 0;
	protected int currentturn = 1;
	
	public ArrayList<VEnemyBuff> bufflist;
	
	public VEnemy(){
		
	}
	public VEnemy(int id){
		this.id = id;
		bufflist = new ArrayList<VEnemyBuff>();
		image = Imageconst.GetImageByName("MONS_"+id+".png");
	}
	public VEnemy createCopy(){
		// TODO 创建一个当前敌人对象的复制
		VEnemy newenemy = new VEnemy(id);
		newenemy.setName(this.name);
		newenemy.paramlist = this.paramlist.clone();
		newenemy.currentturn = this.getValue(ENEMY_CURRENTTURN);
		newenemy.addAction(this.action);
		newenemy.bufflist = new ArrayList<VEnemyBuff>();
		return newenemy;
	}
	public VTexture getImage(){
		return image;
	}

	public void setName(String name){
		this.name = name;
	}
	public void setValues(int id,int mtype,int stype,int maxhp,int chp,int moveturn,int currentturn,int basedamage,int defend){
		// TODO 在Lua中调用以设置参数
		this.id = id;
		image = Imageconst.GetImageByName("MONS_"+id+".png");
		this.setValue(ENEMY_MAINTYPE, mtype);
		this.setValue(ENEMY_SECONDTYPE, stype);
		this.setValue(ENEMY_MAXHP,maxhp);
		this.setValue(ENEMY_CURRENTHP,chp);
		this.setValue(ENEMY_MOVETURN,moveturn);
		this.setValue(ENEMY_CURRENTTURN,currentturn);
		this.setValue(ENEMY_BASEDAMAGE,basedamage);
		this.setValue(ENEMY_DEFEND,defend);
	}
	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#getValue(int)
	 */
	@Override
	public int getValue(int index) {
		// TODO Auto-generated method stub
		if(index == ENEMY_CURRENTTURN){
			return currentturn;
		}
		return paramlist[index];
	}

	/* (non-Javadoc)
	 * @see interfaces.VValueInterface#setValue(int, int)
	 */
	@Override
	public void setValue(int index, int value) {
		// TODO Auto-generated method stub
		paramlist[index] = value;
	}
	public void addBuff(int type,int turn,int subtype,String image){
		// TODO 添加一个新创建的Buff
		VEnemyBuff b = new VEnemyBuff(type,turn,subtype,image);
		bufflist.add(b);
		sortBuffList();
	}
	public void sortBuffList(){
		// TODO 为Buff列表排序以确定显示位置
		ArrayList<Integer> typelist = new ArrayList<Integer>();
		for(VEnemyBuff b:bufflist){		//根据Buff类型划分Buff位置
			if (b.enable) {
				int type = b.getValue(VEnemyBuff.E_BUFF_BUFFTYPE);
				if (!typelist.contains(type)) {
					typelist.add(type);
				}
			}
		}
		Collections.sort(typelist);		
		for(VEnemyBuff b:bufflist){		//
			if (b.enable) {
				int type = b.getValue(VEnemyBuff.E_BUFF_BUFFTYPE);
				int priorty = typelist.indexOf(type); // 计算优先级
				b.setLoc(this.ui.getRealX(), this.ui.getRealY() + priorty * 30
						+ 30);
			}
		}
		//int[]typelistsort = new int[typelist.size()];
		//for(int i=0;i<typelist.size();i++){
		//	typelistsort[i] = typelist.get(i);
		//}
	}
	public void buffDecay(){
		ArrayList trashList = new ArrayList<VEnemyBuff>();
		for(VEnemyBuff b:bufflist){	//根据Buff类型划分Buff位置
			b.buffDecay();
			if(b.getValue(VEnemyBuff.E_BUFF_BUFFTURN)==0){
				trashList.add(b);
			}
		}
		bufflist.removeAll(trashList);
	}
	public double checkBuff(int color){
		// TODO 根据指定颜色检查Buff，并返回相应的伤害乘数
		double basemultip = 1;		//设定伤害基础乘数
		for(VEnemyBuff b:bufflist){	//根据Buff类型划分Buff位置
			if(b.getValue(VEnemyBuff.E_BUFF_BUFFSUBTYPE)==color){	//如果受到伤害类型与Buff影响的伤害类型相同
				switch(b.getValue(VEnemyBuff.E_BUFF_BUFFTYPE)){
				case VEnemyBuff.E_BUFF_TYPE_COLORSHIELD:
					basemultip = basemultip*0.5;
					break;
				case VEnemyBuff.E_BUFF_TYPE_ABSORB:
					basemultip = basemultip*-1;
					break;
				case VEnemyBuff.E_BUFF_TYPE_REDUCE:
					basemultip = basemultip*1;
					break;
				}
			}
		}
		return basemultip;
	}
	public void checkGutsBuff() {
		// TODO 检测根性Buff是否触发
		double gutspercent = -1;
		VEnemyBuff gutsbuff = null;
		for(VEnemyBuff b:bufflist){	//检测Buff列表
			if(b.getValue(VEnemyBuff.E_BUFF_BUFFTYPE)==VEnemyBuff.E_BUFF_TYPE_GUTS){	//如果存在根性类型Buff
				gutspercent = (double)(b.getValue(VEnemyBuff.E_BUFF_BUFFSUBTYPE))/100;
				gutsbuff = b;
				break;
			}
		}
		if(gutspercent!=-1){
			if(this.getValue(ENEMY_CURRENTHP)==0&&gutsbuff.enable){
				this.setValue(ENEMY_CURRENTHP, 1);
			}
			if(this.getHPPercent()>gutspercent){
				gutsbuff.enable = true;
			}
			else{
				gutsbuff.enable = false;
			}
		}
		else{
			return;
		}
	}
	public void dealDamage(double damage){
		// TODO 造成伤害
		int color = this.getValue(ENEMY_SECONDTYPE);
		GameData.party.getDamage(damage,color);
	}
	public void getDamageEvent(double damage,int color){
		// TODO 使敌人受到伤害的事件，事件产生后处理相应的触发器
		getDamage(damage,color);
		GameData.instance.checkGutsBuff();
		GameData.instance.processState();
		if(GameData.instance.checkBattleEnd()){
			GameData.instance.instanceStateTransition(FSMconst.INPUT_ENEMYDIE);
		}
	}
	/**
	 * @param damagepool1
	 * @param intValue
	 */
	public void getDamage(double damage, int color) {
		// TODO 受到伤害
		if (damage > 0) {
			switch (this.getValue(ENEMY_SECONDTYPE)) { // 判断当前属性，计算属性克制
			case 1: // 火
				if (color == 2) {
					damage = damage * 2;
				} else if (color == 3) {
					damage = damage * 0.5;
				}
				break;
			case 2: // 水
				if (color == 3) {
					damage = damage * 2;
				} else if (color == 1) {
					damage = damage * 0.5;
				}
				break;
			case 3: // 木
				if (color == 1) {
					damage = damage * 2;
				} else if (color == 2) {
					damage = damage * 0.5;
				}
				break;
			case 4:	//光
				if (color == 5){
					damage = damage *2;
				}
				break;
			case 5:	//暗
				if (color == 4){
					damage = damage *2;
				}
				break;
			case 6:	//无属性，无视防御
				break;
			default:
				break;
			}
			double buff_multip = checkBuff(color);		//检查敌人Buff获取伤害倍率
			damage = damage*buff_multip;		//计算受Buff影响的伤害倍率
			double hp = (this.getValue(ENEMY_CURRENTHP));	//
			hp = hp - damage;	
			VText damageText = new VText("" + (int) damage);
			ui.addText(damageText);
			damageText.setColor(StaticData.colorlist[color - 1]);
			damageText.setExpire(100); // 设置文字消失时间
			damageText.setLayout(VText.Layout_CENTER);
			damageText.setLoc(ui.getWidth() / 2 + VMath.GetRandomInt(-50, 50),
					ui.getHeight() / 2 + VMath.GetRandomInt(-50, 50));

			if (hp <= this.getValue(ENEMY_MAXHP) / 2) {
				this.setValue(ENEMY_SECONDTYPE,
						this.getValue(ENEMY_MAINTYPE)); // 半血转颜色 一次性
			}
			if(hp<=0){
				hp=0;
			}
			if(hp>=this.getValue(ENEMY_MAXHP)){
				hp = this.getValue(ENEMY_MAXHP);
			}
			this.setValue(ENEMY_CURRENTHP, (int) hp);
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#uiBindUpdate()
	 */
	@Override
	public void uiBindUpdate() {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see interfaces.VUIBindingInterface#bindUI(ui.VUI)
	 */
	@Override
	public void bindUI(VUI ui) {
		// TODO Auto-generated method stub
		this.ui = (VEnemyUI) ui;
	}

	public void die() {
		// TODO Auto-generated method stub
		alive = false;
		ui.setTransparencyCount(30);
	}
	public boolean getAlive(){
		return alive;
	}
	public double getHPPercent(){
		// TODO 获取hp百分比
		double percent = (double)getValue(VEnemy.ENEMY_CURRENTHP)/(double)getValue(VEnemy.ENEMY_MAXHP);
		return percent;
	}
	public int getRunTime(){
		// TODO 获取行为进行次数
		return runtime;
	}
	public void runTimeInc(){
		// TODO 行为次数增加
		runtime++;
	}
	/**
	 * 
	 */
	public void addAction(VLuaObjectAction a){
		this.action = a;
	}
	public void enemyAction() {
		// TODO 敌人进行的行为
		currentturn--;
		if(currentturn==0){
			if(action!=null){
				action.action(this);
				runTimeInc();		//进行行为之后计数增加
			}
			//currentturn=getValue(VEnemy.ENEMY_MOVETURN);
		}
	}
	public void enemyActionEnd(){
		// TODO 敌人行为结束，回合恢复到行动回合数
		if(currentturn==0){
			currentturn=getValue(VEnemy.ENEMY_MOVETURN);
		}
	}
}
