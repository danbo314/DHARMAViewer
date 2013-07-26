package org.dharma.Viewer;

/**
 * @author James Sweet
 */
public class Vector4f {
    private float[] mData = new float[4];

    /**
     * Constructor to create a 4d vector initialised to 0.0.
     */
    public Vector4f() {
        mData[0] = 0.0f;
        mData[1] = 0.0f;
        mData[2] = 0.0f;
        mData[3] = 0.0f;
    }

    /**
     * Constructor to create a 4d vector initialised to values in an array.
     *
     * @param   inArray     Array to initialise from
     */
    public Vector4f( float[] inArray ) {
        mData[0] = inArray[0];
        mData[1] = inArray[1];
        mData[2] = inArray[2];
        mData[3] = inArray[3];
    }

    /**
     * Constructor to create a 4d vector initialised to values in another vector.
     *
     * @param   inVector     Vector to initialise from
     */
    public Vector4f( Vector4f inVector ) {
        mData[0] = inVector.mData[0];
        mData[1] = inVector.mData[1];
        mData[2] = inVector.mData[2];
        mData[3] = inVector.mData[3];
    }

    /**
     * Constructor to create a 4d vector with initialised values.
     *
     * @param   X     X component value
     * @param   Y     Y component value
     * @param   Z     Z component value
     * @param   W     W component value
     */
    public Vector4f( float X, float Y, float Z, float W ) {
        mData[0] = X;
        mData[1] = Y;
        mData[2] = Z;
        mData[3] = W;
    }

    /**
     * Sets x component.
     * 
     * @param   in    Value to set component to
     */
    public void x( float in ) {
        mData[0] = in;
    }

    /**
     * Sets y component.
     * 
     * @param   in  New component value
     */
    public void y( float in ) {
        mData[1] = in;
    }

    /**
     * Sets z component.
     *
    @param   in    New component value
     */
    public void z( float in ) {
        mData[2] = in;
    }

    /**
     * Sets w component.
     *
    @param   in    New component value
     */
    public void w( float in ) {
        mData[3] = in;
    }

    /**
     * Sets component at selected position
     *
     * @param pos position to return
     */
    public void at( int pos, float in ) {
        mData[pos] = in;
    }

    /**
     * Returns x component.
     *
     * @return  Component value
     */
    public float x() {
        return mData[0];
    }

    /**
     * Returns y component
     *
     * @return  Component value
     */
    public float y() {
        return mData[1];
    }

    /**
     * Returns z component
     *
     * @return  Component value
     */
    public float z() {
        return mData[2];
    }

    /**
     * Returns w component
     *
     * @return  Component value
     */
    public float w() {
        return mData[3];
    }

    /**
     * Returns component at selected position
     *
     * @param pos position to return
     * @return  Component value
     */
    public float at( int pos ) {
        return mData[pos];
    }

    /**
     * Returns a copy of the internal state of the vector.
     * 
     * @return  Internal array
     */
    public float[] array() {
        return (float[]) mData.clone();
    }

    /**
     * Compares a this vector against another.
     *
     * @return  Boolean indicating if the arrays are the same
     */
    public boolean equals( Vector4f inVector ) {
        final boolean bX = Math.abs( x() - inVector.x() ) < 0.000001f;
        final boolean bY = Math.abs( y() - inVector.y() ) < 0.000001f;
        final boolean bZ = Math.abs( z() - inVector.z() ) < 0.000001f;
        final boolean bW = Math.abs( w() - inVector.w() ) < 0.000001f;

        return bX && bY && bZ && bW;
    }

    @Override
    public String toString() {
        return "Vector4(" + mData[0] + ", " + mData[1] + ", " + mData[2] + ", " + mData[3] + ")";
    }

    /**
     * Adds a value to each component of this vector.
     *
     * @param scalar Value to add
     */
    public void Add( float scalar ) {
        mData[0] += scalar;
        mData[1] += scalar;
        mData[2] += scalar;
        mData[3] += scalar;
    }

    /**
     * Subtracts a value from each component of this vector.
     *
     * @param scalar Value to subtract
     */
    public void Subtract( float scalar ) {
        mData[0] -= scalar;
        mData[1] -= scalar;
        mData[2] -= scalar;
        mData[3] -= scalar;
    }

    /**
     * Multiplys a value by each component of this vector
     *
     * @param scalar Value to multiply by
     */
    public void Multiply( float scalar ) {
        mData[0] *= scalar;
        mData[1] *= scalar;
        mData[2] *= scalar;
        mData[3] *= scalar;
    }

    /**
     * Divides a value by each component of this vector.
     * Utilises multiplication by the inverse of the scalar for faster calculation.
     *
     * @param scalar Value to divide by
     */
    public void Divide( float scalar ) {
        final float invScalar = 1.0f / scalar;

        Multiply( invScalar );
    }

    /**
     * Adds another vector to this.
     *
     * @param inVector Vector to add
     */
    public void Add( Vector4f inVector ) {
        mData[0] += inVector.mData[0];
        mData[1] += inVector.mData[1];
        mData[2] += inVector.mData[2];
        mData[3] += inVector.mData[3];
    }

    /**
     * Subtracts another vector to this.
     *
     * @param inVector Vector to subtract
     */
    public void Subtract( Vector4f inVector ) {
        mData[0] -= inVector.mData[0];
        mData[1] -= inVector.mData[1];
        mData[2] -= inVector.mData[2];
        mData[3] -= inVector.mData[3];
    }

    /**
     * Multiplys by another vector to this.
     *
     * @param inVector Vector to multiply by
     */
    public void Multiply( Vector4f inVector ) {
        mData[0] *= inVector.mData[0];
        mData[1] *= inVector.mData[1];
        mData[2] *= inVector.mData[2];
        mData[3] *= inVector.mData[3];
    }

    /**
     * Divides by another vector to this.
     *
     * @param inVector Vector to divide by
     */
    public void Divide( Vector4f inVector ) {
        mData[0] *= (1.0f / inVector.mData[0]);
        mData[1] *= (1.0f / inVector.mData[1]);
        mData[2] *= (1.0f / inVector.mData[2]);
        mData[3] *= (1.0f / inVector.mData[3]);

    }

    /**
     * Calculate the magnitude of the vector.
     * Function: sqrt( x * x + y * y + z * z + w * w )
     *
     * @return Vectors magnitude
     */
    public float Magnitude() {
        float retVal = 1.0f;

        float sum = Vector4f.Dot( this, this );

        if ( Math.abs( sum - 1.0f ) > 0.00001f ) {
            retVal = (float) Math.sqrt( sum );
        }

        return retVal;
    }

    /**
     * Normalises vector to be of unit length.
     * Function: Divide all components by the vectors magnitude
     */
    public void Normalise() {
        float length = 1.0f / Magnitude();

        Multiply( length );
    }

    /**
     * Calculates the dot product between this and another vector.
     * Function: Vector * Vector then returns x + y + z + w of resulting vector.
     */
    public float Dot( Vector4f inVector ) {
        Vector4f mDot = Vector4f.Multiply( this, inVector );

        return mDot.mData[0] + mDot.mData[1] + mDot.mData[2] + mDot.mData[3];
    }

    /**
     * Adds scalar to input vectors components.
     *
     * @param   VecA    Input Vector
     * @param   scalar  Value to add
     * @return  Resulting vector
     */
    public static Vector4f Add( Vector4f vecA, float scalar ) {
        return new Vector4f(
                vecA.mData[0] + scalar,
                vecA.mData[1] + scalar,
                vecA.mData[2] + scalar,
                vecA.mData[3] + scalar );
    }

    /**
     * Subtracts scalar from input vectors components.
     *
     * @param   VecA    Input Vector
     * @param   scalar  Value to subtract
     * @return  Resulting vector
     */
    public static Vector4f Subtract( Vector4f vecA, float scalar ) {
        return new Vector4f(
                vecA.mData[0] - scalar,
                vecA.mData[1] - scalar,
                vecA.mData[2] - scalar,
                vecA.mData[3] - scalar );
    }

    /**
     * Multiplies components of input vector by the scalar.
     *
     * @param   VecA    Input Vector
     * @param   scalar  Value to Multiply by
     * @return  Resulting vector
     */
    public static Vector4f Multiply( Vector4f vecA, float scalar ) {
        return new Vector4f(
                vecA.mData[0] * scalar,
                vecA.mData[1] * scalar,
                vecA.mData[2] * scalar,
                vecA.mData[3] * scalar );
    }

    /**
     * Divides components of input vector by the scalar.
     *
     * @param   VecA    Input Vector
     * @param   scalar  Value to divide by
     * @return  Resulting vector
     */
    public static Vector4f Divide( Vector4f vecA, float scalar ) {
        final float invScalar = 1.0f / scalar;

        return new Vector4f(
                vecA.mData[0] * invScalar,
                vecA.mData[1] * invScalar,
                vecA.mData[2] * invScalar,
                vecA.mData[3] * invScalar );
    }

    /**
     * Adds second vector to the first vector.
     *
     * @param   VecA    First vector
     * @param   VecB    Second vector
     * @return  Resulting vector
     */
    public static Vector4f Add( Vector4f vecA, Vector4f vecB ) {
        return new Vector4f(
                vecA.mData[0] + vecB.mData[0],
                vecA.mData[1] + vecB.mData[1],
                vecA.mData[2] + vecB.mData[2],
                vecA.mData[3] + vecB.mData[3] );
    }

    /**
     * Subtracts second vector from the first vector.
     *
     * @param   VecA    First vector
     * @param   VecB    Second vector
     * @return  Resulting vector
     */
    public static Vector4f Subtract( Vector4f vecA, Vector4f vecB ) {
        return new Vector4f(
                vecA.mData[0] - vecB.mData[0],
                vecA.mData[1] - vecB.mData[1],
                vecA.mData[2] - vecB.mData[2],
                vecA.mData[3] - vecB.mData[3] );
    }

    /**
     * Multiplies first vector by the second vector.
     *
     * @param   VecA    First vector
     * @param   VecB    Second vector
     * @return  Resulting vector
     */
    public static Vector4f Multiply( Vector4f vecA, Vector4f vecB ) {
        return new Vector4f(
                vecA.mData[0] * vecB.mData[0],
                vecA.mData[1] * vecB.mData[1],
                vecA.mData[2] * vecB.mData[2],
                vecA.mData[3] * vecB.mData[3] );
    }

    /**
     * Divides first vector by the second vector.
     *
     * @param   VecA    First vector
     * @param   VecB    Second vector
     * @return  Resulting vector
     */
    public static Vector4f Divide( Vector4f vecA, Vector4f vecB ) {
        return new Vector4f(
                vecA.mData[0] * (1.0f / vecB.mData[0]),
                vecA.mData[1] * (1.0f / vecB.mData[1]),
                vecA.mData[2] * (1.0f / vecB.mData[2]),
                vecA.mData[3] * (1.0f / vecB.mData[3]) );
    }

    /**
     * Calculates the dot product between two vectors.
     *
     * @param   inVec       Vector to normalise
     * @return  Normalised version of vector
     */
    public static Vector4f Normalised( Vector4f inVec ) {
        float length = 1.0f / inVec.Magnitude();

        return Vector4f.Multiply( inVec, length );
    }

    /**
     * Calculates the dot product between two vectors.
     *
     * @param   VecA    First vector
     * @param   VecB    Second vector
     * @return  Value of dot product
     */
    public static float Dot( Vector4f vecA, Vector4f vecB ) {
        Vector4f mDot = Vector4f.Multiply( vecA, vecB );

        return mDot.mData[0] + mDot.mData[1] + mDot.mData[2] + mDot.mData[3];
    }
}
