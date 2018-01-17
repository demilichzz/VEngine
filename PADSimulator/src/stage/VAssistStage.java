/**
 * 	辅助模式关卡，辅助模式下可进行编辑宝石面板及转珠练习等功能
 */
package stage;

import system.GameState;
import system.VEngine;

/**
 * @author Demilichzz
 *
 */
public class VAssistStage extends VStage{
	public VAssistStage(){
		super();
	}
	public VAssistStage(GameState gs,String sid){
		super(gs,sid);
	}
	
	public void updateStage(){
		// TODO 随着GS的状态更新当前关卡
		if(!inited){
			Init();
		}
		tp.process();
	}
	public void processKeyAction(int[] keystate) {
		super.processKeyAction(keystate);
	}
	public void processInput(){
		super.processInput();
	}
}
