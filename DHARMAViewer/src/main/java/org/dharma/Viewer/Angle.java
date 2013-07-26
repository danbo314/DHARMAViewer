package org.dharma.Viewer;

/**
 * @author James Sweet
 */
public class Angle {
    /* Static Data */
    public enum Type {
        DEGREE,
        RADIAN
    }
    private final static float mDegToRad = (float) Math.PI / 180.0f;
    private final static float mRadToDeg = 180.0f / (float) Math.PI;

    /* Class Data */
    private float mDegrees;
    private float mRadians;

    /**
     * Constructor to create an angle data type.
     */
    public Angle( float value, Type inType ) {
        switch ( inType ) {
            case DEGREE:
                Degrees( value );
                break;
            case RADIAN:
                Radians( value );
                break;
        }
    }

    @Override
    public String toString() {
        return "Angle( Degrees: " + mDegrees + ", Radians: " + mRadians + ")";
    }

    /**
     * Returns the angle's value in degrees.
     *
     * @return Degree value
     */
    public float Degrees() {
        return mDegrees;
    }

    /**
     * Returns the angle's value in radians.
     *
     * @return Radian value
     */
    public float Radians() {
        return mRadians;
    }

    /**
     * Sets the angle's values based upon the given input.
     *
     * @param   value   Value in degrees
     */
    private void Degrees( float value ) {
        mDegrees = value;
        mRadians = value * mDegToRad;
    }

    /**
     * Sets the angle's values based upon the given input.
     *
     * @param   value   Value in radians
     */
    private void Radians( float value ) {
        mDegrees = value * mRadToDeg;
        mRadians = value;
    }

    /**
     * Adds the second angle to the first.
     *
     * @param   angA    First angle
     * @param   angB    Second angle
     */
    public static Angle Add( Angle angA, Angle angB ) {
        return new Angle( angA.mDegrees + angB.mDegrees, Type.DEGREE );
    }

    /**
     * Subtracts the second angle from the first.
     *
     * @param   angA    First angle
     * @param   angB    Second angle
     */
    public static Angle Subtract( Angle angA, Angle angB ) {
        return new Angle( angA.mDegrees - angB.mDegrees, Type.DEGREE );
    }

    /**
     * Multiplies the first angle by the second.
     *
     * @param   angA    First angle
     * @param   angB    Second angle
     */
    public static Angle Multiply( Angle angA, Angle angB ) {
        return new Angle( angA.mDegrees * angB.mDegrees, Type.DEGREE );
    }

    /**
     * Divides the first angle by the second.
     *
     * @param   angA    First angle
     * @param   angB    Second angle
     */
    public static Angle Divide( Angle angA, Angle angB ) {
        return new Angle( angA.mDegrees / angB.mDegrees, Type.DEGREE );
    }
}
