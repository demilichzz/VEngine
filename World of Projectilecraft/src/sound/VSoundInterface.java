/**
 * 文件名称：VSoundInterface.java
 * 类路径：sound
 * 描述：TODO 用于控制声音播放的接口
 * 作者：Demilichzz
 * 时间：2012-3-3上午03:38:03
 * 版本：Ver 1.0
 */
package sound;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;

/**
 * @author Demilichzz
 *
 */
public interface VSoundInterface {
	public void soundPlay();		//开始或继续播放声音
	public void soundStop();		//停止播放声音
	public void soundPause();		//暂停播放声音
	//public void soundReset();		//重新开始播放声音
	//public void setType(int i);		//设置类型为音乐或音效
	//public int getType();
	//public void setBaseVolume(double i);
	//public void setVolume(int i);
	//public void setMode(int i);			//设置模式
	//public void process();
}
