/**
 * �ļ����ƣ�VSoundInterface.java
 * ��·����sound
 * ������TODO ���ڿ����������ŵĽӿ�
 * ���ߣ�Demilichzz
 * ʱ�䣺2012-3-3����03:38:03
 * �汾��Ver 1.0
 */
package sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

/**
 * @author Demilichzz
 *
 */
public interface VSoundInterface {
	public void soundPlay();		//��ʼ�������������
	public void soundStop();		//ֹͣ��������
	public void soundPause();		//��ͣ��������
	//public void soundReset();		//���¿�ʼ��������
	//public void setType(int i);		//��������Ϊ���ֻ���Ч
	//public int getType();
	//public void setBaseVolume(double i);
	//public void setVolume(int i);
	//public void setMode(int i);			//����ģʽ
	//public void process();
}
