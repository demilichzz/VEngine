/**
 * �ļ����ƣ�VCUMorchok.java
 * ��·����entities
 * ������TODO BossĪ׿��
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-8-8����02:13:52
 * �汾��Ver 1.0
 */
package entities;

import global.Soundconst;
import interfaces.*;

/**
 * @author Demilichzz
 *
 */
public class VCUMorchok extends VCUBoss{
	
	public VCUMorchok(String istr, int life) {
		super(istr, life);
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 75;
		bgmindex=1;
		Soundconst.getSpeech().soundPlay("VO_DS_MORCHOK_AGGRO_01.mp3");
	}
	protected int lt_a = 0;

	public void update(){
		// TODO ����Boss״̬
		super.update();
	}
	public void die(int index){
		// TODO 
		if(phase==1){
			this.life=1;
		}
		else{
			super.die(index);
			Soundconst.getSpeech().soundPlay("VO_DS_MORCHOK_DEATH_01.mp3");
		}
	}
}
