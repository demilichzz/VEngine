/**
 * 	TODO ��VSound����ʹ�õ����ڲ�����Ƶ�ļ����߳�
 */
package sound;

import fsm.*;
import global.*;

import interfaces.VSoundController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.*;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @author Demilichzz
 *
 * 2012-11-12
 */
public class VSoundThread implements Runnable{
	protected File sound;

	protected int mode = 0;		//����ģʽ��0Ϊ��ѭ����1Ϊѭ����2Ϊ���
	protected double basevolume=0;
	protected int volume=50;
	protected FSMclass fsm=null;
	protected VSoundController parent=null;
	private AudioFormat format = null; 
	private SourceDataLine sourceData = null; 
	private DataLine.Info info = null; 
	private AudioInputStream ais = null; 
	private FloatControl volctrl = null;		//��������
	
	public VSoundThread(File sound){
		//--------����ʱ�俪ʼ-------------
		initFSM();
		this.sound = sound;
		try {
			ais = AudioSystem.getAudioInputStream(sound);//�õ���������  
			format = ais.getFormat();//�õ��˸�ʽ  
			if (format.getEncoding()!=AudioFormat.Encoding.PCM_SIGNED&&format.getEncoding()!=AudioFormat.Encoding.PCM_UNSIGNED) { //mp3ת��
				format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
						format.getSampleRate(), 16, format.getChannels(),
						format.getChannels() * 2, format.getSampleRate(), false);
				ais = AudioSystem.getAudioInputStream(format, ais);
			}	
			info = new DataLine.Info(SourceDataLine.class, format,
					AudioSystem.NOT_SPECIFIED);
			sourceData = (SourceDataLine) AudioSystem.getLine(info);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		//-------����ʱ�����-----------  
	}
	public void initFSM(){
		// TODO ��ʼ��״̬��
		fsm = new FSMclass(FSMconst.SOUND_STATE_STOP);
		FSMstate fsms;
		fsms = new FSMstate(FSMconst.SOUND_STATE_STOP,1);
		fsms.AddTransition(FSMconst.SOUND_INPUT_PLAY, FSMconst.SOUND_STATE_PLAY);
		fsm.AddState(fsms);
		fsms = new FSMstate(FSMconst.SOUND_STATE_PLAY,2);
		fsms.AddTransition(FSMconst.SOUND_INPUT_STOP, FSMconst.SOUND_STATE_STOP);
		fsms.AddTransition(FSMconst.SOUND_INPUT_PAUSE, FSMconst.SOUND_STATE_PAUSE);
		fsm.AddState(fsms);
		fsms = new FSMstate(FSMconst.SOUND_STATE_STOP,2);
		fsms.AddTransition(FSMconst.SOUND_INPUT_PLAY, FSMconst.SOUND_STATE_PLAY);
		fsms.AddTransition(FSMconst.SOUND_INPUT_PAUSE, FSMconst.SOUND_STATE_PAUSE);
		fsm.AddState(fsms);
	}
	public void stateTransition(int input){
		fsm.SetCurrentState(fsm.StateTransition(input));
	}
	public int getCurrentState(){
		return fsm.GetCurrentState();
	}
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void setMode(int m){
		mode = m;
	}
	public void setBaseVolume(double v){
		basevolume=v;
		updateVolume();
	}
	public void setVolume(int v){
		volume = v;
		updateVolume();
	}
	public void updateVolume(){
		// TODO �����û������������ʱ���ã����µ�ǰ������ֵ
		if(volctrl!=null){
			volctrl.setValue((VMath.convertVolumeValue(basevolume, volume)));
		}
	}
	public void setSound(File s){
		sound=s;
		
	}
	public void run() {
		// TODO Auto-generated method stub
		/*if(mode==1){	//mode=1ʱѭ������ֱ��ֹͣ
			while(!threadexit){
				playMusic();
			}
		}
		else{
			playMusic();
		}*/
		playMusic();
		if(mode==1&&getCurrentState()!=FSMconst.SOUND_INPUT_STOP){
			while(getCurrentState()!=FSMconst.SOUND_INPUT_STOP){
				playMusic();
			}
		}
	}
	private void playMusic() {
		// TODO Auto-generated method stub
		try {
			if(mode==1||mode==2){	//modeΪ1��2ʱ��������
				ais=AudioSystem.getAudioInputStream(sound);	//�ɿ�ģʽѭ��������Ҫ����������Ƶ������
				format=ais.getFormat();//�õ��˸�ʽ  
				if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {	//mp3ת��
					format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
							format.getSampleRate(), 16, format.getChannels(),
							format.getChannels() * 2, format.getSampleRate(), false);
					ais = AudioSystem.getAudioInputStream(format, ais);
				}
	            info=new DataLine.Info(SourceDataLine.class, format,
	            		AudioSystem.NOT_SPECIFIED);  
	            sourceData=(SourceDataLine)AudioSystem.getLine(info);  
			}
			sourceData.open(format);
			volctrl = (FloatControl) sourceData.getControl(FloatControl.Type.MASTER_GAIN);	//��ȡ����������
			volctrl.setValue(VMath.convertVolumeValue(basevolume,volume));
			sourceData.start();
			byte b[] = new byte[1024];// 1k
			int readCount = 0;
			while ((readCount = ais.read(b)) != -1 &&getCurrentState()!=FSMconst.SOUND_INPUT_STOP){// !threadexit) {
				if(getCurrentState()==FSMconst.SOUND_INPUT_STOP){
					sourceData.stop();		//��ǰֹͣ
				}
				else if(getCurrentState()==FSMconst.SOUND_INPUT_PAUSE){
					// do nothing
				}
				else{
					sourceData.write(b, 0, readCount);
				}
			}
			if(mode!=1){
				Thread.sleep(500);	//�ǿɿ�ģʽ������0.5����߳̽������ɿ�ģʽ�����߽���ѭ������
			}
			/*if(mode==1&&parent!=null){
				parent.setEnd(true);
			}*/
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				if(mode==1){
					if(parent!=null){
						parent.musicend(fsm.GetCurrentState());	//�ص��������ĺ������ظ���Ƶ�Ѳ�����ϵ���Ϣ
					}
				}
				if (sourceData != null) {
					sourceData.drain();
					sourceData.close();
				}
				if (ais != null) {
					ais.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
	}

	public void setController(VSoundController p) {
		// TODO Auto-generated method stub
		parent = p;
	}
}
