/**
 * 
 */
package ui;

import entities.VCharacter;
import entities.VEnemy;
import entities.VInstance;
import entities.VParty;

/**
 * @author Demilichzz
 *
 */
public class VPartyUI extends VUI{
	protected VParty party = null;
	protected VCharacterUI subui[];
	
	public VPartyUI(){
		
	}
	public VPartyUI(int width, int height, String ID) {
		// TODO 通过宽高定义制定大小的图像资源为空的UI
		super(width,height,ID);
		initSubUI();
	}
	public VPartyUI(String str, String ID) {
		super(str,ID);
		initSubUI();
		//this.visible = false;
	}
	public void initSubUI(){
		// TODO 初始化角色子UI
		subui = new VCharacterUI[6];
		for(int i=0;i<6;i++){	//横行
			subui[i] = new VCharacterUI(98,98,"ui_char_"+i);
			subui[i].setParent(this);
			subui[i].setScale(0.85);
			subui[i].setLoc(i*85, 0);
		}
	}
	public void bindEntity(VParty party) {
		// TODO Auto-generated method stub
		this.party = party;
	}
	public void uiAction(String trigtype) {
		// TODO 参数为调用者类别字符串
		if (enable) {
			if (!trigtype.equals("MouseEvent")) {
				if (action != null) {
					action.action();
				}
			}
			if(trigtype.equals("UIrestore")){	//恢复UI未被选定的状态
				for(int i=0;i<6;i++){
					subui[i].setTransparency(1.0);
				}
			}
		}
		if(trigtype.equals("UIupdate")){	//队伍UI更新，将子UI绑定对应的角色对象
			VCharacter[] charlist = party.getCharList();
			if(charlist!=null){
				for(int i=0;i<6;i++){
					subui[i].bindEntity(charlist[i]);
				}
			}
		}
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();
			drawUIText();
			drawUIBorder();
			drawSubUI();
			
		}
	}
}
