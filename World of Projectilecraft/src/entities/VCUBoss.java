/**
 * 
 */
package entities;

import event.GlobalEvent;
import global.Debug;
import global.Imageconst;

/**
 * @author Demilichzz
 *
 * 2012-11-6
 */
public class VCUBoss extends VCombatUnit{
	protected int phase = 0;
	protected String bgm = null;
	protected int bgmindex = 0;
	/**
	 * @param istr
	 * @param life
	 */
	public VCUBoss(String istr, int life) {	
		//	Boss初始化时不会加入列表而是直接设置为GameData当前Boss进行行为更新和绘制
		image = Imageconst.GetImageByName(istr);
		this.life = life;
		CType = 0;	//碰撞类型为圆心碰撞
		collrad = 75;
	}
	public void setPhase(int i){
		// TODO 设置阶段
		phase = i;
	}
	public void die(int index) {
		// TODO 战斗单位死亡时行为
		alive=false;
		switch(index){
		case DEATH_BYPC:{	//被玩家杀死
			GlobalEvent.activateNextBoss();
			break;
		}
		case DEATH_LIFETIME:{	//存活时间结束而死
			//Debug.DebugSimpleMessage("Boss死亡时发生错误：错误的调用参数");
			break;
		}
		case DEATH_ACTION:{	//关联附带行为的死亡
			//Debug.DebugSimpleMessage("Boss死亡时发生错误：错误的调用参数");
			break;
		}
		case DEATH_PHASE:{
			break;
		}
		}
	}
	public int getBGM(){
		// TODO 获取Boss音乐
		return bgmindex;
	}
	public void addBossSkill(String index){
		
	}
	public int getValue(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case 0:{
			return (int) life;
		}
		case 1:{
			return 1;		//Boss则返回1，用于在阶段转换时清除小怪而不清除Boss
		}
		}
		return 0;
	}
}
