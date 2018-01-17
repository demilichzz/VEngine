/**
 * 文件名称：VCUWarlord.java
 * 类路径：entities
 * 描述：TODO Boss督军佐诺兹
 * 作者：Demilichzz
 * 时间：2012-8-11上午03:44:26
 * 版本：Ver 1.0
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
		CType = 0;	//碰撞类型为圆心碰撞
		collrad = 75;
		bgmindex=2;
		Soundconst.getSpeech().soundPlay("VO_DS_ZONOZZ_AGGRO_01.mp3");
	}
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_ZONOZZ_DEATH_01.mp3");
	}
}