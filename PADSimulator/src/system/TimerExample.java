/**
 * @author Demilichzz
 *
 * 2013-6-8
 */
package system;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class TimerExample {

	/** position of quad */
	float x = 400, y = 300;
	/** angle of quad rotation */
	float rotation = 30;

	/** time at last frame */
	long lastFrame;

	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL(); // init OpenGL
		while (!Display.isCloseRequested()) {
			update(10);
			renderGL();
			Display.update();
			Display.sync(60); // cap fps to 60fps
		}
		Display.destroy();
	}

	public void update(int delta) {
		// rotate quad
		rotation += 0.0f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
			x -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
			x += 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			y -= 0.35f * delta;
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			y += 0.35f * delta;
		// keep quad on the screen
		if (x < 0)
			x = 0;
		if (x > 800)
			x = 800;
		if (y < 0)
			y = 0;
		if (y > 600)
			y = 600;
	}
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public void initGL() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	public void renderGL() {
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// R,G,B,A Set The Color To Blue One Time Only
		GL11.glColor3f(0.5f, 0.5f, 1.0f);

		// draw quad
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-x, -y, 0);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x - 50, y - 50);
		GL11.glVertex2f(x + 50, y - 50);
		GL11.glVertex2f(x + 50, y + 50);
		GL11.glVertex2f(x - 50, y + 50);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(400 - 50, 300 - 50);
		GL11.glVertex2f(400 + 50, 300 - 50);
		GL11.glVertex2f(400 + 50, 300 + 50);
		GL11.glVertex2f(400 - 50, 300 + 50);
		GL11.glEnd();
	}
	public static void main(String[] argv) {
		TimerExample timerExample = new TimerExample();
		timerExample.start();
	}
}
