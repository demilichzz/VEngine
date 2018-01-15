/**
 * @author Demilichzz
 *	显示类，使用lwjgl提供的api实现绘制游戏场景
 * 2013-6-8
 */
package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.*;
import org.newdawn.slick.opengl.GLUtils;

import global.Fontconst;
import global.Global;
import system.VEngine;
import ui.VUI;

/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
public class VDisplay {
	protected VEngine ve;
	public float x = 0;
	public float y = 0;
	public float z = 0;
	public float rotationx = 0;
	public float rotationy = 0;
	public float rotationz = 0;
	public VUI testui;
	public VCamera newCamera;				//摄像机对象
	public VLightControl lightcontroller;	//光照控制对象
	
	public UnicodeFont font;
	
	public VDisplay(VEngine e){
		ve = e;
		newCamera = new VCamera();
		lightcontroller = new VLightControl();
		//initGL();
	}
	
	public void initGL(){
		try {
			Display.setDisplayMode(new DisplayMode(Global.windowX, Global.windowY));
			//Display.setTitle("FPS:60");
			PixelFormat pf = new PixelFormat(0,8,8);
			Display.create(pf);
			Display.setLocation(150, 10);
			//Display.setVSyncEnabled(true);
		} 
		catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		//GL11.glEnable(GL11.GL_CULL_FACE);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);	
		//GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		//GL11.glDisable(GL11.GL_LIGHTING);		
		//GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glEnable(GL11.GL_BLEND);							//启用混合
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);      
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);		//关闭深度测试
		GL11.glDisable(GL11.GL_STENCIL_TEST);  	//关闭模板测试
		//GL11.glDisable(GL11.GL_LIGHTING);        
		//GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//GL11.glClearDepth(1);
    	//lightcontroller.initLighting();
		GL11.glViewport(0,0,Global.windowX, Global.windowY);				//设置视口
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Global.windowX, Global.windowY, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glClearStencil(0);
		//GLU.gluPerspective(45.0f,(float)800/(float)600,0.1f,100.0f);
		/*GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);						// 启用深度测试
		GL11.glDepthFunc(GL11.GL_LEQUAL);							// 所作深度测试的类型
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);*/
		
	}
	public void renderPrepare(){
		// TODO 渲染预处理，以保证渲染机制正确运行
		GL11.glClearStencil(0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		Fontconst.loadGlyphs();
		font = Fontconst.getFont("font_default");
		font.drawString(1280, 800, "test",Color.black);
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		Display.update();
	}

	public void render(){
		// TODO 在每帧游戏窗体更新时渲染当前游戏场景
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		//GL11.glClearColor(0.0f,0.0f,0.0f,0.0f);
		Fontconst.loadGlyphs();
		VEngine.newgame.gs.drawScene();
		lightcontroller.processLighting();		
	}
}
	