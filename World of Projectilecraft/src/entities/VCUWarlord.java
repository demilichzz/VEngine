/**
 * �ļ����ƣ�VCUWarlord.java
 * ��·����entities
 * ������TODO Boss������ŵ��
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-11����03:44:26
 * �汾��Ver 1.0
 */
package entities;
import global.Soundconst;
import interfaces.*;
/**
 * @author Demilichzz
 *
 */
public class VCUWarlord extends VCUBoss{
	public VCUWarlord(String istr, int life) {
		super(istr, life);
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 75;
		bgmindex=2;
		Soundconst.getSpeech().soundPlay("VO_DS_ZONOZZ_AGGRO_01.mp3");
	}
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_ZONOZZ_DEATH_01.mp3");
	}
}