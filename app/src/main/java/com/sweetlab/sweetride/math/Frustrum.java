package com.sweetlab.sweetride.math;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;

/**
 * Supports perspective and orthogonal projection.
 * <p/>
 * Old class that I have had around for a while. Should be cleaned up though.
 */
public class Frustrum extends NoHandleNotifier {
    /**
     * Field of view can be either horizontal, vertical or auto fit.
     */
    public enum FovType {
        HORIZONTAL_LOCK, VERTICAL_LOCK, AUTO_FIT
    }

    /**
     * The frustrum mode.
     */
    private enum Mode {
        PERSPECTIVE, ORTHOGRAPHIC, UNINITIALIZED
    }

    /**
     * Camera has been updated.
     */
    private Action mFrustrumUpdated = new Action(this, ActionId.FRUSTRUM_UPDATED, HandleThread.MAIN);

    /**
     * The projection or orthographic matrix
     */
    private final Matrix44 mFrustrumMat = new Matrix44();

    /**
     * The projection or orthographic matrix
     */
    private final Matrix44 mInvFrustrumMat = new Matrix44();

    /**
     * View port values.
     */
    private final LowerLeftBox mViewPort = new LowerLeftBox();

    /**
     * The near plane values.
     */
    public float mLeft, mRight, mTop, mBottom, mNear, mFar;

    /**
     * Projection or orthographic setup
     */
    private Mode mMode;

    /**
     * Field of view in degrees, in projection case.
     */
    private float mFovDeg;

    /**
     * Input field of view type, in projection case.
     */
    private FovType mInputFovType;

    /**
     * Resulted field of view type in case of projection and auto fit
     */
    private FovType mFovType;

    /**
     * Constructor creating an uninitialized frustrum.
     */
    public Frustrum() {
        mMode = Mode.UNINITIALIZED;
    }

    @Override
    protected void onActionAdded(Action action) {
        super.onActionAdded(action);
        switch (action.getType()) {
            case FRUSTRUM_UPDATED:
                updateMatrices();
                break;
            default:
                break;
        }
    }

    /**
     * Get the view port values.
     *
     * @return The view port values.
     */
    public LowerLeftBox getViewPort() {
        return mViewPort;
    }

    /**
     * <pre>
     * We want to create a projection matrix taking us from view (=eye) coordinates (4D)
     * to clip coordinates (4D). Based on http://www.songho.ca/opengl/gl_projectionmatrix.html
     *
     * The step after clip coordinates is calculation of ndc (done in gpu) coordinates by
     * doing a perspective division. So, we need to make sure that we supply clip coordinates
     * with correct divider (wc) together with xc, yc, zc. This means that for example when the
     * matrix is used to calculate xc, we have to take into account the perspective division that
     * will be performed. The trick is to figure out what xn, yn, zn (ndc) should be and only
     * use the part of it without the w (perspective division).
     *
     * After ndc, the viewport transform is performed (in gpu) based on viewport settings
     *
     * Eucledian (X, Y, Z) = (x/w, y/w, z/w) <-> Homogeneous (x, y, z, w) = (X, Y, Z, 1)
     * w == 0 means infinity (representable in homogeneous space by 0)
     *
     * clip = perspective matrix * euclidean vertex
     * |xc|   |m0  m4  m8   m12|   |xe|
     * |yc| = |m1  m5  m9   m13| * |ye|
     * |zc|   |m2  m6  m10  m14|   |ze|
     * |wc|   |m3  m7  m11  m15|   |we|
     *
     *
     * '*' is a point.
     * 'n' is near plane
     *
     * __________________Looking down Y______________________
     *
     * 	                 |
     *                   |          *(xe, ye, ze)
     *                   *(xp, yp, zp)
     * z <----*----------|--------------------------
     *        |          |
     *        |          |
     *        x          n
     *
     * This gives -> xp = n * (xe/-ze).
     *
     *
     * __________________Looking along X______________________
     *
     * 	      y          |
     *        |          |          *(xe, ye, ze)
     *        |          *(xp, yp, zp)
     * z <----*----------|--------------------------
     *                   |
     *                   |
     *                   n
     *
     * This gives -> yp = n * (ye/-ze)
     *
     * Now that we have xp and yp (projection of xe and ye onto near field plane), we go
     * ahead and convert them to ndc coordinates. NDC coordinates are in the range -1 to +1.
     * So, for the xp coordinate which lies somewhere between the left and right plane we
     * need a formula to convert any xp point between left and right to -1 and +1. Note, that
     * if we would restrict the frustrum to left = -0.5 and right = +0.5 the formula would just
     * be xn = 2*xp. However, we want (?) to be able to have a non-symmetric near field plane.
     *
     * The slope factor of the linear conversion is 2 / (r -l). Possibility of non-symmetric left
     * and right, we add a beta factor. This gives us
     *
     * xn = (2 / (r-l)) * xp + beta
     *
     * Solving the above equation by inserting xp = r -> xn = 1 gives us that
     *
     * beta = -(r+l)/(r-l)
     *
     * And finally we have
     *
     * xn = (2 / (r-l)) * xp - (r+l)/(r-l)
     *
     * Similarly we have
     *
     * yn = (2 / (t-b)) * yp - (t+b)/(t-b)
     *
     * We also already have
     *
     * xp = n * (xe/-ze
     * yp = n * (ye/-ze)
     *
     * Combining xn and xp gives the below formula
     *
     * xn = ((2*n*xe)/(r-l) + (r+l)/(r-l)*ze) / (-ze)
     *
     * The above formula is the ndc x coordinate. Remember that we don't want this, we want clip
     * space coordinate and a perspective divider ->
     *
     * Perspective divider = -ze
     * xc = x clip space coordinate = (2*n*xe)/(r-l) + (r+l)/(r-l)*ze
     *
     * Fitting this into the matrix gives us
     *
     * |xc| = |m0  m4  m8   m12| * |xe|
     *                             |ye|
     *                             |ze|
     *                             |we|
     *
     * Thus,
     * m0  = (2*n)/(r-l)
     * m4  = 0
     * m8  = (r+l)/(r-l)
     * m12 = 0
     *
     * yc is calculated in the same way as above (xc) which gives us
     *
     *                             |xe|
     * |yc| = |m1  m5  m9   m13| * |ye|
     *                             |ze|
     *                             |we|
     *
     * m1  = 0
     * m5  = (2*n)/(t-b)
     * m9  = (t+b)/(t-b)
     * m13 = 0
     *
     * And the perspective division part is simply the following which should result in
     * -ze.
     *
     *                              |xe|
     *                              |ye|
     *                              |ze|
     * |wc| = |m3  m7  m11   m15| * |we|
     *
     * m3  =  0
     * m7  =  0
     * m11 = -1
     * m15 =  0
     *
     * Now we only have the third row to calculate which is used to get the zc value. This is a
     * bit strange since ze is always projected to -n on near plane. BUT we need a unique zc to be
     * able to invert, do clipping and depth testing. There is no relationship between zc and xe,yc
     * so we can setMatrix44 the first two columns to zero. The third column (z) and fourth column
     * (w) we
     * don't know about, so we setMatrix44 them to A and B which we need to solve.
     *
     *                              |xe|
     *                              |ye|
     * |zc| = |0   0    A     B | * |ze|
     *                              |we|
     *
     * Since we know that in eye (view) space we is 1, this gives us
     *
     * zc = (A*ze + B) and thus zn = (zc / wc) = (A * ze + B) / wc
     *
     * We know that the relationship between ndc and clip coordinates are that near (-n) is -1 and
     * far (-f) is +1.
     *
     * This gives us two equations with two unknows which can be solved.
     *
     * -1 = -A*n + B
     * +1 = -A*f + B
     *
     * Solving the above gives us that
     *
     * A = -(f+n)/(f-n)
     * B = -2fn/(f-n)
     *
     *                              |xe|
     *                              |ye|
     * |zc| = |m2  m6  m10   m14| * |ze|
     *                              |we|
     *
     * m2 = 0
     * m6 = 0
     * m10 = A
     * m14 = B
     * </pre>
     */
    public void setProjectionPerspective(float left, float right, float bottom, float top,
                                         float near, float far) {
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;
        mNear = near;
        mFar = far;

        /** xc */
        mFrustrumMat.m[0] = (2 * near) / (right - left);
        mFrustrumMat.m[4] = 0;
        mFrustrumMat.m[8] = (right + left) / (right - left);
        mFrustrumMat.m[12] = 0;

        /** yc */
        mFrustrumMat.m[1] = 0;
        mFrustrumMat.m[5] = (2 * near) / (top - bottom);
        mFrustrumMat.m[9] = (top + bottom) / (top - bottom);
        mFrustrumMat.m[13] = 0;

        /** zc */
        mFrustrumMat.m[2] = 0;
        mFrustrumMat.m[6] = 0;
        mFrustrumMat.m[10] = -(far + near) / (far - near);
        mFrustrumMat.m[14] = -2 * far * near / (far - near);

        /** wc */
        mFrustrumMat.m[3] = 0;
        mFrustrumMat.m[7] = 0;
        mFrustrumMat.m[11] = -1;
        mFrustrumMat.m[15] = 0;

        addAction(mFrustrumUpdated);

        mMode = Mode.PERSPECTIVE;
    }

    /**
     * Create a perspective matrix based on field of view.
     *
     * @param fov  - in degrees
     * @param type - Horizontal, vertical or best fit (based on aspect ratio for least fisheye effect)
     * @param near - near distance
     * @param far  - far distance
     * @param x    - screen width
     * @param y    - screen height
     */
    public void setPerspectiveProjection(float fov, FovType type, float near, float far, int x, int y) {
        float left, right, bottom, top;
        float ratio = (float) x / y;

        mFovDeg = fov;
        float fovRad = (float) Math.toRadians(mFovDeg);

        mInputFovType = type;

        if (mInputFovType == FovType.AUTO_FIT) {
            if (ratio > 1) {
                mFovType = FovType.HORIZONTAL_LOCK;
            } else {
                mFovType = FovType.VERTICAL_LOCK;
            }
        } else {
            mFovType = type;
        }

        if (mFovType == FovType.HORIZONTAL_LOCK) {
            /** fov = 2*tan(right/near) -> right = near * atan(fov/2) */
            right = (float) (near * Math.atan(fovRad / 2));
            left = -right;
            top = right / ratio;
            bottom = -top;
        } else {
            /** fov = 2*tan(top/near) -> top = near * atan(fov/2) */
            top = (float) (near * Math.atan(fovRad / 2));
            bottom = -top;
            right = top * ratio;
            left = -right;
        }

        mViewPort.set(0, 0, x, y);

        setProjectionPerspective(left, right, bottom, top, near, far);
    }

    /**
     * Creates an orthographic projection matrix. All values are specified from origo.
     */
    public void setOrthographicProjection(float left, float right, float bottom, float top,
                                          float near, float far, int width, int height) {
        mLeft = left;
        mRight = right;
        mBottom = bottom;
        mTop = top;
        mNear = near;
        mFar = far;

        /** xc */
        mFrustrumMat.m[0] = 2 / (right - left);
        mFrustrumMat.m[4] = 0;
        mFrustrumMat.m[8] = 0;
        mFrustrumMat.m[12] = -(right + left) / (right - left);

        /** yc */
        mFrustrumMat.m[1] = 0;
        mFrustrumMat.m[5] = 2 / (top - bottom);
        mFrustrumMat.m[9] = 0;
        mFrustrumMat.m[13] = -(top + bottom) / (top - bottom);

        /** zc */
        mFrustrumMat.m[2] = 0;
        mFrustrumMat.m[6] = 0;
        mFrustrumMat.m[10] = -2 / (far - near);
        mFrustrumMat.m[14] = -(far + near) / (far - near);

        /** wc */
        mFrustrumMat.m[3] = 0;
        mFrustrumMat.m[7] = 0;
        mFrustrumMat.m[11] = 0;
        mFrustrumMat.m[15] = 1;

        mViewPort.set(0, 0, width, height);

        addAction(mFrustrumUpdated);

        mMode = Mode.ORTHOGRAPHIC;
    }

    /**
     * Get the projection matrix.
     *
     * @return The projection matrix.
     */
    public Matrix44 getProjectionMatrix() {
        return mFrustrumMat;
    }

    /**
     * Get the inverted projection matrix.
     *
     * @return The inverted projection matrix.
     */
    public Matrix44 getInvProjectionMatrix() {
        return mInvFrustrumMat;
    }

    /**
     * Calculate the GL width at a certain depth.
     */
    public float calcWidthAtDepth(float depth) {
        return depth * ((mRight - mLeft) / mNear);
    }

    /**
     * Calculate the GL height at a certain depth.
     */
    public float calcHeightAtDepth(float depth) {
        return depth * ((mTop - mBottom) / mNear);
    }

    /**
     * Calculate the necessary depth for a given (geometry) width to cover a certain amount of the
     * screen (nearFieldFactor).
     */
    public float calcDepthAtWidth(float nearFieldFactor, float width) {
        return mNear * width / ((mRight - mLeft) * nearFieldFactor);
    }

    /**
     * Update all matrices that are supported.
     */
    private void updateMatrices() {
        mInvFrustrumMat.set(mFrustrumMat);
        mInvFrustrumMat.invert();
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (mMode.equals(Mode.PERSPECTIVE)) {
            b.append("Perspective projection");
            b.append("\nInput fov type " + mInputFovType);
            b.append("\nResulted fov type " + mFovType);
            b.append("\nFov in degrees " + mFovDeg);
            b.append("\nNear = " + mNear);
            b.append("\nFar = " + mFar);
            b.append("\nLeft = " + mLeft);
            b.append("\nRight = " + mRight);
            b.append("\nBottom = " + mBottom);
            b.append("\nTop = " + mTop);
        } else if (mMode.equals(Mode.ORTHOGRAPHIC)) {
            b.append("Orthographic projection\n");
            b.append("\nNear = " + mNear);
            b.append("\nFar = " + mFar);
            b.append("\nLeft = " + mLeft);
            b.append("\nRight = " + mRight);
            b.append("\nBottom = " + mBottom);
            b.append("\nTop = " + mTop);
        } else {
            b.append("UNINITIALIZED");
        }
        return b.toString();
    }
}
