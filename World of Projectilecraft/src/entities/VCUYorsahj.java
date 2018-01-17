/**
 * 	TODO Boss不眠的约萨希
 */
package entities;

import global.Soundconst;

/**
 * @author Demilichzz
 *
 * 2012-10-24
 */
public class VCUYorsahj extends VCUBoss{
	/**
	 * @param istr
	 * @param life
	 */
	public VCUYorsahj(String istr, int life) {
		super(istr, life);
		CType = 0;	//碰撞类型为圆心碰撞
		collrad = 50;
		bgmindex=2;
		Soundconst.getSpeech().soundPlay("VO_DS_YORSAHJ_AGGRO_01.mp3");
	}
	protected int lt_a = 0;
	public void update(){
		// TODO 更新Boss状态
		super.update();
	}
	public void die(int index){
		super.die(index);
		Soundconst.getSpeech().soundPlay("VO_DS_YORSAHJ_DEATH_01.mp3");
	}
}
