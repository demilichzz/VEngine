/**
 * @author Demilichzz
 *	光照控制类，在显示类中应用相应生成的光照控制对象的初始化及调节光照参数的函数
 * 2013-6-18
 */
package display;

import java.nio.*;

import org.lwjgl.opengl.GL11;

/**
 * @author Demilichzz
 *
 * 2013-6-18
 */
public class VLightControl {
	protected float[] LightAmbient = { 0.5f, 0.5f, 0.5f, 1.0f }; 	//环境光
	protected float[] LightDiffuse= { 1.0f, 1.0f, 1.0f, 1.0f };	//漫射光
	protected float[] LightPosition= { 10.0f, 5.0f, 10.0f, 1.0f };	//光源位置
	protected float[] LightPosition2= { -10.0f, 5.0f, 10.0f, 1.0f };	//光源位置
	protected float[] LightPosition3= { 10.0f, 5.0f, -10.0f, 1.0f };	//光源位置
	protected float[] LightPosition4= { -10.0f, 5.0f, -10.0f, 1.0f };	//光源位置
	
	
	public VLightControl(){
		
	}


	public void initLighting() {
		// TODO 在gl初始化时开启光照相关选项
		//GL11.glEnable(GL11.GL_LIGHTING);
    	//GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE , GL11.GL_FALSE);
	}
	public void processLighting(){
		// TODO 处理光照
		/*ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(LightAmbient).flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(LightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(LightPosition).flip());
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(LightAmbient).flip());
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(LightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT2, GL11.GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(LightPosition2).flip());
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(LightAmbient).flip());
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(LightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT3, GL11.GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(LightPosition3).flip());
		GL11.glLight(GL11.GL_LIGHT4, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(LightAmbient).flip());
		GL11.glLight(GL11.GL_LIGHT4, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(LightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT4, GL11.GL_POSITION, (FloatBuffer)temp.asFloatBuffer().put(LightPosition4).flip());
		GL11.glEnable(GL11.GL_LIGHT1);*/
		//GL11.glEnable(GL11.GL_LIGHT2);
		//GL11.glEnable(GL11.GL_LIGHT3);
		//GL11.glEnable(GL11.GL_LIGHT4);
	}
	
}
