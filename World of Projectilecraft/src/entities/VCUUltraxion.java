/**
 * @author Demilichzz
 *	Boss��׿������
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
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 75;
		bgmindex=4;
		Soundconst.getSpeech().soundPlay("VO_DS_ULTRAXION_AGGRO_01.mp3");
	}
	
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_ULTRAXION_DEATH_01.mp3");
	}
	public void addBossSkill(String index){
		// TODO ��ָ�����������Boss������ӵ�DBM��ʱ�������ɼ�ʱ��
		if(index.equals("Judgement")){
			GlobalEvent.DBMAddSkill("ĺ������", 30);
		}
		if(index.equals("ShimmerLight")){
			GlobalEvent.DBMAddSkill("����֮��", 6);
		}
	}
}
