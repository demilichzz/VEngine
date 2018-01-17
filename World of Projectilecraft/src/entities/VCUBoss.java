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
		//	Boss��ʼ��ʱ��������б����ֱ������ΪGameData��ǰBoss������Ϊ���ºͻ���
		image = Imageconst.GetImageByName(istr);
		this.life = life;
		CType = 0;	//��ײ����ΪԲ����ײ
		collrad = 75;
	}
	public void setPhase(int i){
		// TODO ���ý׶�
		phase = i;
	}
	public void die(int index) {
		// TODO ս����λ����ʱ��Ϊ
		alive=false;
		switch(index){
		case DEATH_BYPC:{	//�����ɱ��
			GlobalEvent.activateNextBoss();
			break;
		}
		case DEATH_LIFETIME:{	//���ʱ���������
			//Debug.DebugSimpleMessage("Boss����ʱ�������󣺴���ĵ��ò���");
			break;
		}
		case DEATH_ACTION:{	//����������Ϊ������
			//Debug.DebugSimpleMessage("Boss����ʱ�������󣺴���ĵ��ò���");
			break;
		}
		case DEATH_PHASE:{
			break;
		}
		}
	}
	public int getBGM(){
		// TODO ��ȡBoss����
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
			return 1;		//Boss�򷵻�1�������ڽ׶�ת��ʱ���С�ֶ������Boss
		}
		}
		return 0;
	}
}
