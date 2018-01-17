/**
 * 	TODO 队列音频对象，使用参数播放指定音频并关闭上一个播放的音频
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
		// TODO 播放指定音频
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
		// TODO 循环播放指定包中指定前缀的音频
		VSoundInterface last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			last.soundStop();
		}
		if(p!=null){
			
		}
	}
	public void soundStop(){
		// TODO 停止队列中音频
		VSoundInterface last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			last.soundStop();
		}
	}
}
