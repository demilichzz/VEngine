/**
 * 	TODO ������Ƶ����ʹ�ò�������ָ����Ƶ���ر���һ�����ŵ���Ƶ
 */
package sound;

import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-11-21
 */
public class VQueueSound {
	protected String lastsound="";
	protected String currentsount="";
	
	public VQueueSound(){
		
	}
	public void soundPlay(String sound){
		// TODO ����ָ����Ƶ
		if(sound.equals(lastsound)){
			
		}
		else{
			VSoundInterface last = Soundconst.GetSoundByName(lastsound);
			if(last!=null){
				last.soundStop();
			}
		}
		VSoundInterface current = Soundconst.GetSoundByName(sound);
		if(current!=null){
			current.soundPlay();
			lastsound=sound;
		}
	}
	public void soundPlay(VSoundPack p,String prefix){
		// TODO ѭ������ָ������ָ��ǰ׺����Ƶ
		VSoundInterface last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			last.soundStop();
		}
		if(p!=null){
			
		}
	}
	public void soundStop(){
		// TODO ֹͣ��������Ƶ
		VSoundInterface last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			last.soundStop();
		}
	}
}
