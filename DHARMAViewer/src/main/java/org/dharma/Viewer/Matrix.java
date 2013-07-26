package org.dharma.Viewer;

/**
 *
 * @author James Sweet
 */
public class Matrix {
    private double[] mData = {
        1.0, 0.0, 0.0, 0.0,
        0.0, 1.0, 0.0, 0.0,
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    };

    public Matrix() {
    }

    public Matrix( double[] data ) {
        for ( int i = 0; i < 16; i++ ) {
            mData[i] = data[i];
        }
    }

    public Matrix( double a, double e, double i, double m,
                   double b, double f, double j, double n,
                   double c, double g, double k, double o,
                   double d, double h, double l, double p ) {
        mData[0] = a;
        mData[1] = e;
        mData[2] = i;
        mData[3] = m;
        mData[4] = b;
        mData[5] = f;
        mData[6] = j;
        mData[7] = n;
        mData[8] = c;
        mData[9] = g;
        mData[10] = k;
        mData[11] = o;
        mData[12] = d;
        mData[13] = h;
        mData[14] = l;
        mData[15] = p;
    }

    public float[] getFloats(){
        float[] temp = new float[16];
        for (int i = 0; i < 16; i++)
            temp[i] = (float)mData[i];
        return temp;
    }

    public double at( int pos ) {
        return mData[pos];
    }

    public double at( int x, int y ) {
        return mData[( y * 4 ) + x];
    }

//    public Matrix Multiply( Matrix other ){
//        mData[0] = a;
//        mData[1] = e;
//        mData[2] = i;
//        mData[3] = m;
//        mData[4] = b;
//        mData[5] = f;
//        mData[6] = j;
//        mData[7] = n;
//        mData[8] = c;
//        mData[9] = g;
//        mData[10] = k;
//        mData[11] = o;
//        mData[12] = d;
//        mData[13] = h;
//        mData[14] = l;
//        mData[15] = p;
//
//        for ( int i = 0; i < 4; i++ ){
//            const int iPos = i * 4;
//
//            for ( int j = 0; j < 4; ++j ) {
//                double sum = 0;
//
//                for ( int k = 0; k < 4; ++k ) {
//                    sum += at( i, k ) * other.at( k, j );
//                }
//
//                mData[ iPos + j ] = sum;
//            }
//        }
//
//        return this;
//    }
    public static Matrix generateScale( double x, double y, double z ) {
        return new Matrix(
                x, 0.0, 0.0, 0.0,
                0.0, y, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, z, 1.0 );
    }

    public static Matrix generateTranslation( double x, double y, double z ) {
        return new Matrix(
                1.0, 0.0, 0.0, x,
                0.0, 1.0, 0.0, y,
                0.0, 0.0, 1.0, z,
                0.0, 0.0, 0.0, 1.0 );
    }

    public static Matrix generateRotation( float radAngle, Vector3f axis ) {
        Vector3f point = Vector3f.Normalised( axis );

        //Precompute cos and sin
        double aCos = Math.cos( radAngle );
        double aSin = Math.sin( radAngle );

        double mCos = 1 - aCos;

        //Compute rotation
        double mMatrix[] = new double[16];

        mMatrix[0] = ( point.x() * point.x() * mCos ) + ( aCos );
        mMatrix[4] = ( point.x() * point.y() * mCos ) - ( point.z() * aSin );
        mMatrix[8] = ( point.x() * point.z() * mCos ) + ( point.y() * aSin );
        mMatrix[12] = 0;

        mMatrix[1] = ( point.y() * point.x() * mCos ) + ( point.z() * aSin );
        mMatrix[5] = ( point.y() * point.y() * mCos ) + ( aCos );
        mMatrix[9] = ( point.y() * point.z() * mCos ) - ( point.x() * aSin );
        mMatrix[13] = 0;

        mMatrix[2] = ( point.z() * point.x() * mCos ) - ( point.y() * aSin );
        mMatrix[6] = ( point.z() * point.y() * mCos ) + ( point.x() * aSin );
        mMatrix[10] = ( point.z() * point.z() * mCos ) + ( aCos );
        mMatrix[14] = 0;

        mMatrix[3] = 0;
        mMatrix[7] = 0;
        mMatrix[11] = 0;
        mMatrix[15] = 1;

        return new Matrix( mMatrix );
    }
}
