package org.dharma.Viewer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Date: 5/21/13
 * Author: James Sweet
 */
@TargetApi(Build.VERSION_CODES.FROYO)
public class Mesh {
    public int Verticies;
    public int Indicies;
    public int mTextureDataHandler;
    public FloatBuffer Transformation;

    private ByteBuffer DataBuffer = null;
    private ByteBuffer IndexBuffer = null;
    private Bitmap mText;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Mesh( float[] transform, InputStream vertex, int points, InputStream index, int indicies, InputStream texture){
        Verticies = points;
        Indicies = indicies;

        try{
            // Setup Vertex Data
            ByteArrayOutputStream vData = new ByteArrayOutputStream();
            copy(vertex, vData);
            DataBuffer = ByteBuffer.allocateDirect(vData.size());
            DataBuffer.order(ByteOrder.nativeOrder());
            DataBuffer.put(vData.toByteArray());
            DataBuffer.rewind();
            Log.i("Mesh: ","Size of mesh is " + DataBuffer.array().length);
            vData.close();

            // Setup Index Data
            ByteArrayOutputStream vIndex = new ByteArrayOutputStream();
            copy(index, vIndex);

            ByteBuffer Temp = ByteBuffer.allocateDirect(vIndex.size());
            Temp.order(ByteOrder.nativeOrder());
            Temp.put(vIndex.toByteArray());
            Temp.rewind();
            vIndex.close();

            IndexBuffer = ByteBuffer.allocateDirect(vIndex.size() / 2);
            IndexBuffer.order(ByteOrder.nativeOrder());

            IntBuffer iTemp = Temp.asIntBuffer();
            while( iTemp.remaining() > 0 ){
                IndexBuffer.putShort( (short)iTemp.get() );
            }
            IndexBuffer.rewind();

            // Setup Texture Data
            final BitmapFactory.Options ops = new BitmapFactory.Options();
            //ops.inScaled = false;
            ops.inSampleSize = 2;

            // Read in the resource
            mText = BitmapFactory.decodeStream(texture, null, ops);

            // Setup Transformation
            ByteBuffer TempBuffer = ByteBuffer.allocateDirect(16 * 4);
            TempBuffer.order(ByteOrder.nativeOrder());

            Transformation = TempBuffer.asFloatBuffer();
            Transformation.put(transform);
            Transformation.rewind();
        }catch( Exception e ){
            Log.e("Mesh", e.toString());
        }
    }

    public void Draw( GL10 gl, int v){

        gl.glPushMatrix();
        {
            gl.glMultMatrixf(Transformation);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

            ByteBuffer vertex = DataBuffer.duplicate();
            gl.glVertexPointer(3, GL10.GL_FLOAT, 32, vertex);

            ByteBuffer normal = DataBuffer.duplicate();
            normal.position(12);
            gl.glNormalPointer(GL10.GL_FLOAT, 32, normal);

            // If Texture view is selected, bind mesh texture to gl object
            if( v == 2){
                gl.glActiveTexture(GL10.GL_TEXTURE0);
                gl.glBindTexture(GL10.GL_TEXTURE_2D,mTextureDataHandler);
            }
            // If not in points view, draw meshes as triangles
            if(v != 0)
                gl.glDrawElements(GL10.GL_TRIANGLES, Indicies, GL10.GL_UNSIGNED_SHORT, IndexBuffer);
            // Otherwise, draw them as simple points
            else
                gl.glDrawElements(GL10.GL_POINTS, Indicies, GL10.GL_UNSIGNED_SHORT,IndexBuffer);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
        }
        gl.glPopMatrix();

    }

    // Function to load texture from bitmap when model is loaded
    public void LoadTexture(GL10 gl)
    {
        // Setup Texture Data
        final int[] textureHandle = new int[1];
        gl.glGenTextures(1,textureHandle,0);

        if(textureHandle[0] != 0){
            mTextureDataHandler = textureHandle[0];

            // Bind to the texture in OpenGL
            gl.glActiveTexture(GL10.GL_TEXTURE0);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureDataHandler);

            // Set filtering
            gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

            // Load the bitmap into the bound texture.
            if(!mText.isRecycled())
                GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mText, 0);

            // Recycle bitmap once it is bound to opengl
            mText.recycle();
        }
        else if (textureHandle[0] == 0)
            throw new RuntimeException("Error loading texture.");
    }

    public static int copy(InputStream in, OutputStream out) throws IOException {
        int count = 0;
        int read = 0;
        byte[] buffer = new byte[1024];
        while (read != -1) {
            read = in.read(buffer);
            if (read != -1) {
                count += read;
                out.write(buffer,0,read);
            }
        }
        return count;
    }
}
