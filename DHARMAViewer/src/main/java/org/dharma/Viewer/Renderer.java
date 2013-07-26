package org.dharma.Viewer;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Renderer implements GLSurfaceView.Renderer {

    public float rotX, rotY, scaleZ, xPos, yPos;
    public Model currentModel;
    public int currentView;
    public Quaternion newRot;
    public Quaternion oldRot;
    public boolean reset;

    private final float[] transformation = {1f,0f,0f,0f,
                                            0f,1f,0f,0f,
                                            0f,0f,1f,0f,
                                            0f,0f,0f,1f};

    private org.dharma.Viewer.Matrix finalRot;
    private int myView;
    private boolean hasRotated;

    // Initialize all fields in constructor
    public Renderer(){
        oldRot = new Quaternion();
        oldRot.w(1.0f);
        newRot = new Quaternion(oldRot);
        hasRotated = false;
        finalRot = new org.dharma.Viewer.Matrix();
        rotX = 0.0f;
        rotY = 0.0f;
        scaleZ = 0.0f;
        xPos = 0.0f;
        yPos = 0.0f;
        reset = false;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {

        // If double tap is detected and reset set to true, reset view and model
        if(reset)
        {
            Matrix.setIdentityM(transformation, 0);
            oldRot = new Quaternion();
            oldRot.w(1.0f);
            newRot = new Quaternion(oldRot);
            hasRotated = false;
            rotX = 0.0f;
            rotY = 0.0f;
            scaleZ = 0.0f;
            xPos = 0.0f;
            yPos = 0.0f;
            reset = false;
        }

        // If rotation gesture is detected, notify Renderer that we are rotating
        if(rotX != 0.0f || rotY != 0.0f)
            hasRotated = true;

        gl.glClearColor( 0.0f, 0.0f, 0.0f, 1.0f );
        gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();


        gl.glPushMatrix();
        {
            if( currentModel != null ){

                // pan model
                gl.glTranslatef(xPos,yPos, -(4*currentModel.Radius));

                // rotate model using Quaternions
                if(hasRotated){
                    finalRot = org.dharma.Viewer.Matrix.generateRotation(newRot.Angle().Radians(),newRot.Vector());
                    gl.glMultMatrixf(finalRot.getFloats(),0);
                }

                // scale model
                if(scaleZ > 0)
                    gl.glScalef(scaleZ,scaleZ,scaleZ);

                // change view
                switch(currentView){
                    case R.id.radioPointsView:
                        gl.glDisable(GL10.GL_TEXTURE_2D);
                        myView = 0;
                        break;
                    case R.id.radioSurfaceView:
                        gl.glDisable(GL10.GL_TEXTURE_2D);
                        myView = 1;
                        break;
                    case R.id.radioTextureView:
                        // Enable texture mapping
                        gl.glEnable(GL10.GL_TEXTURE_2D);
                        myView = 2;
                        if(!currentModel.isLoaded)
                            currentModel.LoadClouds(gl);
                        break;
                }
                // Draw model
                currentModel.DrawClouds(gl,myView);
            }
        }
        gl.glPopMatrix();

	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();

		GLU.gluPerspective( gl, 45.0f, (float)width / (float)height, 0.1f, 1000.0f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(transformation, 0);

		gl.glClearDepthf( 1.0f );
		gl.glEnable( GL10.GL_DEPTH_TEST );
		gl.glDepthFunc( GL10.GL_LEQUAL );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST );

        // Lighting
        float[] ambient0 = { 1.0f, 1.0f, 1.0f, 1.0f };
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, ambient0, 0 );
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        //gl.glEnable(GL10.GL_NORMALIZE);


        float[] diffuse0 = { 0.5f, 0.5f, 0.5f, 1.0f };
        float[] specular0 = { 0.8f, 0.8f, 0.8f, 1.0f };
        float[] position0 = { 10f, 10f, 10f, 1.0f };



        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE,diffuse0,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_AMBIENT,ambient0,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_SPECULAR,specular0,0);
        gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_POSITION,position0,0);

        gl.glEnable(GL10.GL_LIGHT1);

        float[] ambient1 = { 1.0f, 1.0f, 1.0f, 1.0f };
        float[] diffuse1 = { 0.8f, 0.8f, 0.8f, 1.0f };
        float[] specular1 = { 0.7f, 0.7f, 0.7f, 1.0f };
        float[] position1 = { -10f, -10f, -10f, 1.0f };

        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_DIFFUSE,diffuse1,0);
        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_AMBIENT,ambient1,0);
        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_SPECULAR,specular1,0);
        gl.glLightfv(GL10.GL_LIGHT1,GL10.GL_POSITION,position1,0);


        gl.glShadeModel( GL10.GL_SMOOTH );

        gl.glClearColor( 0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearDepthf(1.0f);

        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}
}

