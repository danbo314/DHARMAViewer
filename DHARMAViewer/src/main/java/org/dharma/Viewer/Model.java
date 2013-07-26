package org.dharma.Viewer;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Model{

    private AssetManager mManager;

    public String Title;
    public float Radius;
    public float[] View;
    public float[] Center;
    public String Path;
    public List<Cloud> Data = new ArrayList<Cloud>();
    public List<Mesh> mMesh = new ArrayList<Mesh>();
    public boolean isLoaded;

    public Model( AssetManager assets ){
        mManager = assets;
        isLoaded = false;
    }

    public void addCloud( float[] transform, float scale, int points, String path ) throws IOException {
        Data.add( new Cloud( transform, scale, points, mManager.open(Path + "/" + path) ));
    }

    public void addMesh(float[] transformation, String pointsPath, int points, String indexPath, int indicies, String texturePath) throws IOException {
        Log.i("Mesh", "Adding Mesh: " + pointsPath + " " + indexPath + " " + texturePath);
        try{
            mManager.open(Path + "/" + pointsPath);
        }
        catch (Exception e){
            Log.i("Mesh", "Opening Stream points");
        }
        try{
            mManager.open(Path + "/" + indexPath);
        }
        catch (Exception e){
            Log.i("Mesh", "Opening Stream index");
        }
        try{
            mManager.open(Path + "/" + texturePath);
        }
        catch (Exception e){
            Log.i("Mesh", "Opening Stream texture");
        }
        mMesh.add( new Mesh( transformation, mManager.open(Path + "/" + pointsPath), points, mManager.open(Path + "/" + indexPath), indicies, mManager.open(Path + "/" + texturePath) ) );
    }

    public void DrawClouds(GL10 gl, int v) {
        gl.glPushMatrix();
        {
            gl.glRotatef( 270.0f, 1.0f, 0.0f, 0.0f );
            gl.glTranslatef(-Center[0], -Center[1], -Center[2]);

            for( Mesh m : mMesh ){
                m.Draw(gl, v);
            }

            for( Cloud c : Data ){
                c.Draw(gl);
            }
        }
        gl.glPopMatrix();
    }

    // Function that loads textures from all meshes in model when Texture view is selected
    public void LoadClouds(GL10 gl)
    {
        isLoaded = true;
        for( Mesh m : mMesh )
            m.LoadTexture(gl);
    }

    // returns the number of points in this model
    public int numPoints()
    {
        int sum = 0;
        for (Mesh m : mMesh)
            sum += m.Verticies;
        for (Cloud c : Data)
            sum += c.Points;

        return sum;
    }
}
