package org.dharma.Viewer;

/**
 *
 * @author James Sweet
 */
public class Quaternion {
    private Vector4f mVector;

    public Quaternion() {
        mVector = new Vector4f();
    }

    public Quaternion( float[] inArray ) {
        mVector = new Vector4f( inArray );
    }

    public Quaternion( Vector4f inVector ) {
        mVector = inVector;
    }

    public Quaternion( Quaternion inQuat ) {
        mVector = inQuat.mVector;
    }

    public Quaternion( Angle inAngle, Vector3f inVector ) {
        float halfAngle = inAngle.Radians() * 0.5f;

        final float angleSin = (float) Math.sin( halfAngle );
        final float angleCos = (float) Math.cos( halfAngle );

        Vector3f vNorm = Vector3f.Multiply( Vector3f.Normalised( inVector ), angleSin );

        mVector = new Vector4f( vNorm.x(), vNorm.y(), vNorm.z(), angleCos );
    }

    public float x() {
        return mVector.x();
    }

    public float y() {
        return mVector.y();
    }

    public float z() {
        return mVector.z();
    }

    public float w() {
        return mVector.w();
    }

    public void x( float val ) {
        mVector.x( val );
    }

    public void y( float val ) {
        mVector.y( val );
    }

    public void z( float val ) {
        mVector.z( val );
    }

    public void w( float val ) {
        mVector.w( val );
    }

    public boolean equals( Quaternion inQuat ) {
        return mVector.equals( inQuat.mVector );
    }

    @Override
    public String toString() {
        return "Quaternion(" + mVector.x() + ", " + mVector.y() + ", " + mVector.z() + ", " + mVector.w() + ")";
    }

    //Scalar Functions
    public void Add( float scalar ) {
        mVector.Add( scalar );
    }

    public void Subtract( float scalar ) {
        mVector.Subtract( scalar );
    }

    public void Multiply( float scalar ) {
        mVector.Multiply( scalar );
    }

    public void Divide( float scalar ) {
        mVector.Divide( scalar );
    }

    //Vector Functions
    public void Add( Quaternion inQuart ) {
        mVector.Add( inQuart.mVector );
    }

    public void Subtract( Quaternion inQuart ) {
        mVector.Subtract( inQuart.mVector );
    }

    public void Multiply( Quaternion inQuart ) {
        mVector.x( (mVector.w() * inQuart.x()) + (mVector.x() * inQuart.w()) + (mVector.y() * inQuart.z()) - (mVector.z() * inQuart.y()) );
        mVector.y( (mVector.w() * inQuart.y()) + (mVector.y() * inQuart.w()) + (mVector.z() * inQuart.x()) - (mVector.x() * inQuart.z()) );
        mVector.z( (mVector.w() * inQuart.z()) + (mVector.z() * inQuart.w()) + (mVector.x() * inQuart.y()) - (mVector.y() * inQuart.x()) );
        mVector.w( (mVector.w() * inQuart.w()) - (mVector.x() * inQuart.x()) - (mVector.y() * inQuart.y()) - (mVector.z() * inQuart.z()) );
    }

    public Vector3f Multiply( Vector3f inVect ) {
        Quaternion qVector = new Quaternion( 
                new Vector4f( inVect.x(), inVect.y(), inVect.z(), 0.0f ) );

        Quaternion result = Quaternion.Multiply(
                Quaternion.Multiply( this, qVector), this.Conjugate() );

        return new Vector3f( result.x(), result.y(), result.z() );
    }

    public void Divide( Quaternion inQuart ) {
        this.Multiply( this.Inverse() );
    }

    // Helper functions
    public Angle Angle() {
        return new Angle( (float) Math.acos( mVector.w() ) * 2.0f, Angle.Type.RADIAN );
    }

    public Vector3f Vector() {
        return Vector3f.Normalised( new Vector3f( mVector.x(), mVector.y(), mVector.z() ) );
    }

    public float Magnitude() {
        return mVector.Magnitude();
    }

    public void Normalise() {
        mVector.Normalise();
    }

    public Quaternion Conjugate() {
        return new Quaternion( new Vector4f( -mVector.x(), -mVector.y(), -mVector.z(), mVector.w() ) );
    }

    public Quaternion Inverse() {
        return new Quaternion( Vector4f.Divide( mVector, mVector.Magnitude() ) );
    }

    //Static Functions
    public static Quaternion Add( Quaternion inQuat, float scalar ) {
        return new Quaternion( Vector4f.Add( inQuat.mVector, scalar ) );
    }

    public static Quaternion Subtract( Quaternion inQuat, float scalar ) {
        return new Quaternion( Vector4f.Subtract( inQuat.mVector, scalar ) );
    }

    public static Quaternion Multiply( Quaternion inQuat, float scalar ) {
        return new Quaternion( Vector4f.Multiply( inQuat.mVector, scalar ) );
    }

    public static Quaternion Divide( Quaternion inQuat, float scalar ) {
        return new Quaternion( Vector4f.Divide( inQuat.mVector, scalar ) );
    }

    //Vector Functions
    public static Quaternion Add( Quaternion inQuatA, Quaternion inQuatB ) {
        return new Quaternion( Vector4f.Add( inQuatA.mVector, inQuatB.mVector ) );
    }

    public static Quaternion Subtract( Quaternion inQuatA, Quaternion inQuatB ) {
        return new Quaternion( Vector4f.Subtract( inQuatA.mVector, inQuatB.mVector ) );
    }

    public static Quaternion Multiply( Quaternion inQuatA, Quaternion inQuatB ) {
        Quaternion retVal = new Quaternion();

        retVal.x( (inQuatA.w() * inQuatB.x()) + (inQuatA.x() * inQuatB.w()) + (inQuatA.y() * inQuatB.z()) - (inQuatA.z() * inQuatB.y()) );
        retVal.y( (inQuatA.w() * inQuatB.y()) + (inQuatA.y() * inQuatB.w()) + (inQuatA.z() * inQuatB.x()) - (inQuatA.x() * inQuatB.z()) );
        retVal.z( (inQuatA.w() * inQuatB.z()) + (inQuatA.z() * inQuatB.w()) + (inQuatA.x() * inQuatB.y()) - (inQuatA.y() * inQuatB.x()) );
        retVal.w( (inQuatA.w() * inQuatB.w()) - (inQuatA.x() * inQuatB.x()) - (inQuatA.y() * inQuatB.y()) - (inQuatA.z() * inQuatB.z()) );

        return retVal;
    }

    public static Quaternion Divide( Quaternion inQuatA, Quaternion inQuatB ) {
        return new Quaternion( Quaternion.Multiply( inQuatA, inQuatB.Conjugate() ) );
    }
}
