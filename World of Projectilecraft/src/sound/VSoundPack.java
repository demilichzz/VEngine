/**
 * 	TODO 音频包类，可通过定义的字符串参数控制选取指定音频播放，并包含设置是否并发播放等功能
 */	
package sound;

import interfaces.VSoundController;
import global.Debug;
import global.FSMconst;
import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-11-28
 */
public class VSoundPack implements VSoundController{
	protected String[]	soundprefix;	//音频前缀
	protected int[]	indexrange;			//索引范围
	protected int[] indexlist;			//播放索引列表
	protected int currentindex=-1;			//当前索引
	protected String lastsound="";
	protected String currentsound="";
	protected int maxtype;			//一个包内记录的音频前缀类别上限
	protected String suffix = ".mp3";	//后缀
	protected boolean mixmode = false;	//是否为混音模式，如不是则在音频播放时停止前一音频
	
	public VSoundPack(int i){
		soundprefix = new String[i];
		indexrange = new int[i];
		indexlist = new int[i];
		maxtype = i;
	}
	/**
	 * @param prefix 音频前缀字符串，为文件名最后一位数字之前的所有字符
	 * @param range
	 */
	public void addPrefix(String prefix,int range){
		// TODO 向音频包内添加音频前缀
		for(int i=0;i<maxtype;i++){
			if(indexrange[i]==0){		//如果还有剩余
				soundprefix[i]=prefix;
				indexrange[i]=range;
				indexlist[i]=1;
				return;
			}
		}
		Debug.DebugSimpleMessage("该音频包已满");
	}
	public void setMixMode(boolean b){
		mixmode = b;
	}
	public void soundPlay(int p){
		if(p<maxtype&&p!=-1){
			currentindex=p;
			String str = soundprefix[p];
			soundPlay(str);
		}
	}
	public void soundPlay(String prefix){
		int sindex=-1;		//获取对应索引
		for(int i=0;i<maxtype;i++){
			if(soundprefix[i]!=null){
				if(soundprefix[i].equals(prefix)){
					sindex = i;
				}
			}
		}
		if(sindex!=-1){
			String filename = soundprefix[sindex]+indexlist[sindex]+suffix;		//连接字符串获取音频文件名
			indexlist[sindex]++;
			if(indexlist[sindex]>indexrange[sindex]){	//超范围
				indexlist[sindex]=1;
			}
			if(!lastsound.equals("")){
				VSound last = Soundconst.GetSoundByName(lastsound);
				if(last!=null){
					last.soundStop();
				}
			}
			VSound current = Soundconst.GetSoundByName(filename);
			if(current!=null){
				lastsound = filename;
				current.soundPlay(this);
			}
		}
		else{
			Debug.DebugSimpleMessage("无法在此包内找到前缀为"+prefix+"的音频文件");
		}
	}
	public void soundStop(){
		// TODO 停止队列中音频
		VSound last = Soundconst.GetSoundByName(lastsound);
		if(last!=null){
			currentindex=-1;
			last.soundStop();
		}
	}
	/* (non-Javadoc)
	 * @see interfaces.VSoundController#musicend()
	 */
	@Override
	public void musicend(int i) {
		// TODO Auto-generated method stub
		switch(i){
		case FSMconst.SOUND_STATE_PLAY:{		//在PLAY状态回调，说明该音乐已播放完毕
			this.soundPlay(currentindex);	//播放正在播放的列表中下一个音频
			break;
		}
		case FSMconst.SOUND_STATE_STOP:{		//STOP状态回调，说明音乐中途停止
			break;
		}
		}
		
	}
}
