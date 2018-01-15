/**
 * 
 */
package ui;

import entities.GameArea;
import entities.VEnemy;
import entities.VInstance;

/**
 * @author Demilichzz
 *
 */
public class VInstanceUI extends VUI{
	protected VInstance inst = null;
	protected VEnemyUI subui[];
	public VSkillHintUI skillhint_ui;
	
	public VInstanceUI(){
		
	}
	public VInstanceUI(int width, int height, String ID) {
		// TODO 通过宽高定义制定大小的图像资源为空的UI
		super(width,height,ID);
	}
	public VInstanceUI(String str, String ID) {
		super(str,ID);
		initSubUI();
		skillhint_ui=new VSkillHintUI("skillhintui.png","ui_skillhint");
		skillhint_ui.setLoc(56, 10);
		skillhint_ui.setParent(this);
		skillhint_ui.setVisible(true);
		skillhint_ui.setTransparency(0.0);
	}
	public void initSubUI(){
		// TODO 初始化敌人子UI
		subui = new VEnemyUI[7];
		for(int i=0;i<7;i++){	//横行
			subui[i] = VEnemyUI.createUI(i);
			subui[i].setParent(this);
			subui[i].setScale(1.0);
			subui[i].setLoc(i*73, 0);
			subui[i].setVisible(false);
		}
	}
	public void drawUI() {
		// TODO 绘制UI
		if (visible) {
			drawUIImage();		
			drawUIBorder();
			drawSubUI();
			drawUIText();	//令副本提示文字前端显示
		}
	}
	public void bindEntity(VInstance inst) {
		// TODO Auto-generated method stub
		this.inst = inst;
	}
	public void setEnemyUITCount(int c){
		// TODO 设置敌人子UI的渐隐计数
		for(int i=0;i<subui.length;i++){
			subui[i].setTransparencyCount(c);
		}
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
				for(int i=0;i<7;i++){
					subui[i].setTransparency(1.0);
				}
			}
		}
		if(trigtype.equals("UIupdate")){	//副本UI更新，将子UI绑定对应的敌人对象
			VEnemy[] enemylist = inst.getEnemyList();
			if(enemylist!=null){
				int enemysum = 0;
				for(int i=0;i<7;i++){
					if(enemylist[i]!=null){
						enemysum++;
					}
				}
				for(int i=0;i<7;i++){
					subui[i].bindEntity(enemylist[i]);
				}
				for(int i=0;i<enemysum;i++){
					//根据当前敌人数量计算坐标偏移值
					subui[i].setVisible(true);
					subui[i].textlist.get(0).setLoc(0, 20);	//重设文本位置以适应缩放
					subui[i].textlist.get(1).setLoc(80,20);
					subui[i].setScale(0.8);
					int x_offset = (int) (512*0.5/(double)enemysum-0.5*(subui[i].area.w-subui[i].area.x)+i*(512.0/(double)enemysum));
					int y_offset = (int) (160-0.5*(subui[i].area.h-subui[i].area.y));
					subui[i].setLoc(x_offset,y_offset);
				}
				for(int i=enemysum;i<7;i++){
					// 隐藏未使用敌人UI
					subui[i].setVisible(false);
				}
			}
		}
	}
}
