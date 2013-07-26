package org.dharma.Viewer;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Cloud {
    public float Scale;
    public int Points;

    public FloatBuffer Transformation;
    private ByteBuffer DataBuffer = null;

    public Cloud( float[] transform, float scale, int points, InputStream in){
        Scale = scale;
        Points = points;

        try{
            // Setup Vertex Data
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            copy(in, data);

            DataBuffer = ByteBuffer.allocateDirect(data.size());
            DataBuffer.order(ByteOrder.nativeOrder());
            DataBuffer.put(data.toByteArray());
            DataBuffer.rewind();

            // Setup Transformation
            ByteBuffer TempBuffer = ByteBuffer.allocateDirect(16 * 4);
            TempBuffer.order(ByteOrder.nativeOrder());

            Transformation = TempBuffer.asFloatBuffer();
            Transformation.put(transform);
            Transformation.rewind();

            data.close();
        }catch( Exception e ){
            Log.e("Cloud", e.toString());
        }
    }

    public void Draw( GL10 gl ){
        gl.glPointSize(Scale);

        gl.glPushMatrix();
        {
            gl.glMultMatrixf(Transformation);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            ByteBuffer vertex = DataBuffer.duplicate();
            gl.glVertexPointer(3, GL10.GL_FLOAT, 28, vertex);

            ByteBuffer color = DataBuffer.duplicate();
            color.position(12);
            gl.glColorPointer(4, GL10.GL_FLOAT, 28, color);

            gl.glDrawArrays(GL10.GL_POINTS, 0, Points);

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
        gl.glPopMatrix();
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
