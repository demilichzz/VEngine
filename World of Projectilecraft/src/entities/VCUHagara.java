/**
 * 	TODO BOSS�����߹�����
 */
package entities;

import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-11-2
 */
public class VCUHagara extends VCUBoss{
	/**
	 * @param istr
	 * @param life
	 */
	public VCUHagara(String istr, int life) {
		super(istr, life);
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 50;
		bgmindex=3;
		Soundconst.getSpeech().soundPlay("VO_DS_HAGARA_AGGRO_01.mp3");
	}
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_HAGARA_DEATH_01.mp3");
	}
}
