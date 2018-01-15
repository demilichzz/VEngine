/**
 * @author Demilichzz
 *	摄像机类，用于定义视图中的摄像机位置及角度等参数
 * 2013-6-13
 */
package display;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * @author Demilichzz
 *
 * 2013-6-13
 */
public class VCamera {
	public float dist = -6.0f;
	
	
	public VCamera(){
		
	}
	public void setCamera(){
		// TODO 设置摄像机位置，在渲染时调用以通过计算透视投影矩阵进行摄像机位置改变
		//GL11.glTranslatef(-0.5f,-0.5f,dist);
		GL11.glTranslatef(0.0f,0.0f,-100f);
		//GLU.gluLookAt(0.5f, 0.5f, 6f, 0.5f, 0.5f, 0f, 0f, 1f, 0f);
	}
	
}
