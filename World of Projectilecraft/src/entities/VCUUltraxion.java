/**
 * @author Demilichzz
 *	Boss奥卓克西昂
 * 2012-12-5
 */
package entities;

import event.GlobalEvent;
import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-12-5
 */
public class VCUUltraxion extends VCUBoss{

	/**
	 * @param istr
	 * @param life
	 */
	public VCUUltraxion(String istr, int life) {
		super(istr, life);
		CType = 0;	//碰撞类型为圆心碰撞
		collrad = 75;
		bgmindex=4;
		Soundconst.getSpeech().soundPlay("VO_DS_ULTRAXION_AGGRO_01.mp3");
	}
	
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_ULTRAXION_DEATH_01.mp3");
	}
	public void addBossSkill(String index){
		// TODO 将指定参数定义的Boss技能添加到DBM计时器并生成计时条
		if(index.equals("Judgement")){
			GlobalEvent.DBMAddSkill("暮光审判", 30);
		}
		if(index.equals("ShimmerLight")){
			GlobalEvent.DBMAddSkill("暗淡之光", 6);
		}
	}
}
