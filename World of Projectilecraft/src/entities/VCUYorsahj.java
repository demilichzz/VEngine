/**
 * 	TODO Boss���ߵ�Լ��ϣ
 */
package entities;

import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-10-24
 */
public class VCUYorsahj extends VCUBoss{
	/**
	 * @param istr
	 * @param life
	 */
	public VCUYorsahj(String istr, int life) {
		super(istr, life);
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 50;
		bgmindex=2;
		Soundconst.getSpeech().soundPlay("VO_DS_YORSAHJ_AGGRO_01.mp3");
	}
	protected int lt_a = 0;
	public void update(){
		// TODO ����Boss״̬
		super.update();
	}
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_YORSAHJ_DEATH_01.mp3");
	}
}
