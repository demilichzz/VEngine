/**
 * 文件名称：VCUMorchok.java
 * 类路径：entities
 * 描述：TODO Boss莫卓克
 * 作者：Demilichzz
 * 时间：2012-8-8上午02:13:52
 * 版本：Ver 1.0
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
		CType = 0;	//碰撞类型为圆心碰撞
		collrad = 75;
		bgmindex=1;
		Soundconst.getSpeech().soundPlay("VO_DS_MORCHOK_AGGRO_01.mp3");
	}
	protected int lt_a = 0;

	public void update(){
		// TODO 更新Boss状态
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
