package org.dharma.Viewer;

/**
 * @author James Sweet
 */
public class Vector3f {
    public final static Vector3f Zero  = new Vector3f( 0.0f, 0.0f, 0.0f );
    public final static Vector3f One   = new Vector3f( 1.0f, 1.0f, 1.0f );
    public final static Vector3f UnitX = new Vector3f( 1.0f, 0.0f, 0.0f );
    public final static Vector3f UnitY = new Vector3f( 0.0f, 1.0f, 0.0f );
    public final static Vector3f UnitZ = new Vector3f( 0.0f, 0.0f, 1.0f );
    
    private float[] mData = new float[3];

    /**
     * Constructor to create a 3d vector initialised to 0.0.
     */
    public Vector3f() {
        mData[0] = 0.0f;
        mData[1] = 0.0f;
        mData[2] = 0.0f;
    }

    /**
     * Constructor to create a 3d vector initialised to values in an array.
     *
     * @param   inArray     Array to initialise from
     */
    public Vector3f( float[] inArray ) {
        mData[0] = inArray[0];
        mData[1] = inArray[1];
        mData[2] = inArray[2];
    }

    /**
     * Constructor to create a 3d vector initialised to values in another vector.
     *
     * @param   inVector     Vector to initialise from
     */
    public Vector3f( Vector3f inVector ) {
        mData[0] = inVector.mData[0];
        mData[1] = inVector.mData[1];
        mData[2] = inVector.mData[2];
    }

    /**
     * Constructor to create a 3d vector with initialised values.
     *
     * @param   X     X component value
     * @param   Y     Y component value
     * @param   Z     Z component value
     */
    public Vector3f( float X, float Y, float Z ) {
        mData[0] = X;
        mData[1] = Y;
        mData[2] = Z;
    }

    /**
     * Sets x component.
     *
     * @param   in    New component value
     */
    public void x( float in ) {
        mData[0] = in;
    }

    /**
     * Sets y component.
     *
     * @param   in    New component value
     */
    public void y( float in ) {
        mData[1] = in;
    }

    /**
     * Sets z component.
     *
     * @param   in    New component value
     */
    public void z( float in ) {
        mData[2] = in;
    }

    /**
     * sets component at selected position
     *
     * @param pos position to set
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
     * Returns component at selected position
     *
     * @param pos position to retrive
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
    public boolean equals( Vector3f inVector ) {
        final boolean bX = Math.abs( x() - inVector.x() ) < 0.000001f;
        final boolean bY = Math.abs( y() - inVector.y() ) < 0.000001f;
        final boolean bZ = Math.abs( z() - inVector.z() ) < 0.000001f;

        return bX && bY && bZ;
    }

    @Override
    public String toString() {
        return "Vector3(" + mData[0] + ", " + mData[1] + ", " + mData[2] + ")";
    }

    /**
     * Adds a value to each component of this vector.
     *
     * @param scalar Value to add
     */
    public Vector3f Add( float scalar ) {
        mData[0] += scalar;
        mData[1] += scalar;
        mData[2] += scalar;

        return this;
    }

    /**
     * Subtracts a value from each component of this vector.
     *
     * @param scalar Value to subtract
     */
    public Vector3f Subtract( float scalar ) {
        mData[0] -= scalar;
        mData[1] -= scalar;
        mData[2] -= scalar;

        return this;
    }

    /**
     * Multiplys a value by each component of this vector
     *
     * @param scalar Value to multiply by
     */
    public Vector3f Multiply( float scalar ) {
        mData[0] *= scalar;
        mData[1] *= scalar;
        mData[2] *= scalar;

        return this;
    }

    /**
     * Divides a value by each component of this vector.
     * Utilises multiplication by the inverse of the scalar for faster calculation.
     *
     * @param scalar Value to divide by
     */
    public Vector3f Divide( float scalar ) {
        final float invScalar = 1.0f / scalar;

        Multiply( invScalar );

        return this;
    }

    /**
     * Adds another vector to this.
     *
     * @param inVector Vector to add
     */
    public Vector3f Add( Vector3f inVector ) {
        mData[0] += inVector.mData[0];
        mData[1] += inVector.mData[1];
        mData[2] += inVector.mData[2];

        return this;
    }

    /**
     * Subtracts another vector to this.
     *
     * @param inVector Vector to subtract
     */
    public Vector3f Subtract( Vector3f inVector ) {
        mData[0] -= inVector.mData[0];
        mData[1] -= inVector.mData[1];
        mData[2] -= inVector.mData[2];

        return this;
    }

    /**
     * Multiplys by another vector to this.
     *
     * @param inVector Vector to multiply by
     */
    public Vector3f Multiply( Vector3f inVector ) {
        mData[0] *= inVector.mData[0];
        mData[1] *= inVector.mData[1];
        mData[2] *= inVector.mData[2];

        return this;
    }

    /**
     * Divides by another vector to this.
     *
     * @param inVector Vector to divide by
     */
    public Vector3f Divide( Vector3f inVector ) {
        mData[0] *= (1.0f / inVector.mData[0]);
        mData[1] *= (1.0f / inVector.mData[1]);
        mData[2] *= (1.0f / inVector.mData[2]);

        return this;
    }

    /**
     * Calculate the magnitude of the vector.
     * Function: sqrt( x * x + y * y + z * z )
     *
     * @return Vectors magnitude
     */
    public float Magnitude() {
        float retVal = 1.0f;

        float sum = Vector3f.Dot( this, this );


        //if ( sum > 0f ) {
        retVal = (float) Math.sqrt( sum );
        //}

        return retVal;
    }

    /**
     * Normalises vector to be of unit length.
     * Function: Divide all components by the vectors magnitude
     */
    public Vector3f Normalise() {
        float length = 1.0f / Magnitude();

        Multiply( length );

        return this;
    }

    /**
     * Calculates the dot product between this and another vector.
     * Function: Vector * Vector then returns x + y + z of resulting vector.
     */
    public float Dot( Vector3f inVector ) {
        Vector3f mDot = Vector3f.Multiply( this, inVector );

        return mDot.mData[0] + mDot.mData[1] + mDot.mData[2];
    }

    /**
     * Calculates the cross product between this and another vector.
     */
    public Vector3f Cross( Vector3f inVector ) {
        return new Vector3f(
                mData[1] * inVector.mData[2] - mData[2] * inVector.mData[1],
                mData[2] * inVector.mData[0] - mData[0] * inVector.mData[2],
                mData[0] * inVector.mData[1] - mData[1] * inVector.mData[0] );
    }

    /**
     * Calculates the absolute value of this vector
     *
     * @return Absolute vector
     */
    public Vector3f Absolute() {
        return new Vector3f(
                Math.abs( mData[0] ),
                Math.abs( mData[1] ),
                Math.abs( mData[2] ) );
    }

    /**
     * Checks if the vector is smaller than the privided one
     *
     * @param inVec Vector to compare against
     *
     * @return true if this is smaller than the provided
     */
    public boolean isLessThan( Vector3f inVec ){
        boolean xLess = mData[0] < inVec.x();
        boolean yLess = mData[1] < inVec.y();
        boolean zLess = mData[2] < inVec.z();

        return xLess && yLess && zLess;
    }

    /**
     * Checks if the vector is greater than the privided one
     *
     * @param inVec Vector to compare against
     *
     * @return true if this is greater than the provided
     */
    public boolean isGreaterThan( Vector3f inVec ){
        boolean xLess = mData[0] > inVec.x();
        boolean yLess = mData[1] > inVec.y();
        boolean zLess = mData[2] > inVec.z();

        return xLess && yLess && zLess;
    }

    /**
     * Adds scalar to input vectors components.
     *
     * @param   vecA    Input Vector
     * @param   scalar  Value to add
     * @return  Resulting vector
     */
    public static Vector3f Add( Vector3f vecA, float scalar ) {
        return new Vector3f(
                vecA.mData[0] + scalar,
                vecA.mData[1] + scalar,
                vecA.mData[2] + scalar );
    }

    /**
     * Subtracts scalar from input vectors components.
     *
     * @param   vecA    Input Vector
     * @param   scalar  Value to subtract
     * @return  Resulting vector
     */
    public static Vector3f Subtract( Vector3f vecA, float scalar ) {
        return new Vector3f(
                vecA.mData[0] - scalar,
                vecA.mData[1] - scalar,
                vecA.mData[2] - scalar );
    }

    /**
     * Multiplies components of input vector by the scalar.
     *
     * @param   vecA    Input Vector
     * @param   scalar  Value to Multiply by
     * @return  Resulting vector
     */
    public static Vector3f Multiply( Vector3f vecA, float scalar ) {
        return new Vector3f(
                vecA.mData[0] * scalar,
                vecA.mData[1] * scalar,
                vecA.mData[2] * scalar );
    }

    /**
     * Divides components of input vector by the scalar.
     *
     * @param   vecA    Input Vector
     * @param   scalar  Value to divide by
     * @return  Resulting vector
     */
    public static Vector3f Divide( Vector3f vecA, float scalar ) {
        final float invScalar = 1.0f / scalar;

        return new Vector3f(
                vecA.mData[0] * invScalar,
                vecA.mData[1] * invScalar,
                vecA.mData[2] * invScalar );
    }

    /**
     * Adds second vector to the first vector.
     *
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return  Resulting vector
     */
    public static Vector3f Add( Vector3f vecA, Vector3f vecB ) {
        return new Vector3f(
                vecA.mData[0] + vecB.mData[0],
                vecA.mData[1] + vecB.mData[1],
                vecA.mData[2] + vecB.mData[2] );
    }

    /**
     * Subtracts second vector from the first vector.
     *
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return  Resulting vector
     */
    public static Vector3f Subtract( Vector3f vecA, Vector3f vecB ) {
        return new Vector3f(
                vecA.mData[0] - vecB.mData[0],
                vecA.mData[1] - vecB.mData[1],
                vecA.mData[2] - vecB.mData[2] );
    }

    /**
     * Multiplies first vector by the second vector.
     *
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return  Resulting vector
     */
    public static Vector3f Multiply( Vector3f vecA, Vector3f vecB ) {
        return new Vector3f(
                vecA.mData[0] * vecB.mData[0],
                vecA.mData[1] * vecB.mData[1],
                vecA.mData[2] * vecB.mData[2] );
    }

    /**
     * Divides first vector by the second vector.
     *
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return  Resulting vector
     */
    public static Vector3f Divide( Vector3f vecA, Vector3f vecB ) {
        return new Vector3f(
                vecA.mData[0] * (1.0f / vecB.mData[0]),
                vecA.mData[1] * (1.0f / vecB.mData[1]),
                vecA.mData[2] * (1.0f / vecB.mData[2]) );
    }

    /**
     * Calculates the dot product between two vectors.
     *
     * @param   inVec       Vector to normalise
     * @return  Normalised version of vector
     */
    public static Vector3f Normalised( Vector3f inVec ) {
        float length = 1.0f / inVec.Magnitude();

        return Vector3f.Multiply( inVec, length );
    }

    /**
     * Calculates normal vector of 3 points
     *
     * @param   a   First Point
     * @param   b   Second Point
     * @param   c   Third Point
     * @return  Normal vector
     */
    public static Vector3f NormalVector( Vector3f a, Vector3f b, Vector3f c ) {
        Vector3f result = Vector3f.Cross(
                Vector3f.Subtract( b, a ),
                Vector3f.Subtract( c, a ) );

        return Vector3f.Normalised( result );
    }

    /**
     * Calculates the dot product between two vectors.
     *
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return  Value of dot product
     */
    public static float Dot( Vector3f vecA, Vector3f vecB ) {
        Vector3f mDot = Vector3f.Multiply( vecA, vecB );

        return mDot.mData[0] + mDot.mData[1] + mDot.mData[2];
    }

    /**
     * Calculates the cross product between two vectors.
     * 
     * @param   vecA    First vector
     * @param   vecB    Second vector
     * @return Vector containing cross product result
     */
    public static Vector3f Cross( Vector3f vecA, Vector3f vecB ) {
        return new Vector3f(
                vecA.mData[1] * vecB.mData[2] - vecA.mData[2] * vecB.mData[1],
                vecA.mData[2] * vecB.mData[0] - vecA.mData[0] * vecB.mData[2],
                vecA.mData[0] * vecB.mData[1] - vecA.mData[1] * vecB.mData[0] );
    }

    /**
     * Calculates the absolute value of this vector
     *
     * @return Absolute vector
     */
    public static Vector3f Absolute( Vector3f vecA ) {
        return new Vector3f(
                Math.abs( vecA.mData[0] ),
                Math.abs( vecA.mData[1] ),
                Math.abs( vecA.mData[2] ) );
    }
}
