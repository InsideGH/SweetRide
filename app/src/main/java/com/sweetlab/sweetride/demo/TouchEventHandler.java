package com.sweetlab.sweetride.demo;

import android.view.MotionEvent;

import com.sweetlab.sweetride.camera.Camera;
import com.sweetlab.sweetride.intersect.BoundingBox;
import com.sweetlab.sweetride.intersect.Intersect;
import com.sweetlab.sweetride.intersect.Ray;
import com.sweetlab.sweetride.touch.TouchToRay;

import java.util.List;

/**
 * Given a motion event, camera, bounding box this class will
 * call the (supplied) touch listeners with onDown, onUp, onMove and
 * onCancel.
 */
public class TouchEventHandler {
    /**
     * Invalid action index.
     */
    private static final int INVALID_ACTION_INDEX = -1;

    /**
     * Invalid pointer index.
     */
    private static final int INVALID_POINTER_INDEX = -1;

    /**
     * World space ray generator.
     */
    private TouchToRay mTouchToRay;

    /**
     * Intersect.
     */
    private final Intersect mIntersect = new Intersect();

    /**
     * The action index.
     */
    private int mActionIndex = -1;

    /**
     * The pointer id.
     */
    private int mPointerId = -1;

    /**
     * Constructor.
     *
     * @param screenWidth  Screen width.
     * @param screenHeight Screen height.
     */
    public TouchEventHandler(int screenWidth, int screenHeight) {
        mTouchToRay = new TouchToRay(screenWidth, screenHeight);
    }

    /**
     * Call this to test motion event against tracked geometry.
     *
     * @param event The motion event.
     * @return True if handled, false otherwise.
     */
    public boolean onTouchEvent(MotionEvent event, Camera camera, BoundingBox box, List<OnTouchListener> listeners) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_CANCEL: {
                if (mActionIndex != INVALID_ACTION_INDEX) {
                    mActionIndex = INVALID_ACTION_INDEX;
                    mPointerId = INVALID_POINTER_INDEX;
                    for (OnTouchListener listener : listeners) {
                        listener.onCancel();
                    }
                }
                return false;
            }

            case MotionEvent.ACTION_UP: {
                int actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                if (mPointerId == pointerId) {
                    final int x = (int) event.getX(actionIndex);
                    final int y = (int) event.getY(actionIndex);
                    final Ray ray = mTouchToRay.getRay(camera, x, y);
                    mActionIndex = INVALID_ACTION_INDEX;
                    mPointerId = INVALID_POINTER_INDEX;
                    for (OnTouchListener listener : listeners) {
                        if (listener.onUp(ray, x, y)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                if (mActionIndex == event.getActionIndex()) {
                    final int x = (int) event.getX(mActionIndex);
                    final int y = (int) event.getY(mActionIndex);
                    final Ray ray = mTouchToRay.getRay(camera, x, y);
                    mActionIndex = INVALID_ACTION_INDEX;
                    mPointerId = INVALID_POINTER_INDEX;
                    for (OnTouchListener listener : listeners) {
                        if (listener.onUp(ray, x, y)) {
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                int actionIndex = event.getActionIndex();
                final int x = (int) event.getX(actionIndex);
                final int y = (int) event.getY(actionIndex);
                final Ray ray = mTouchToRay.getRay(camera, x, y);
                if (mIntersect.intersects(ray, box)) {
                    mActionIndex = actionIndex;
                    mPointerId = event.getPointerId(mActionIndex);
                    for (OnTouchListener listener : listeners) {
                        if (listener.onDown(ray, x, y)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    if (mPointerId == event.getPointerId(i)) {
                        final int x = (int) event.getX(i);
                        final int y = (int) event.getY(i);
                        final Ray ray = mTouchToRay.getRay(camera, x, y);
                        final boolean isHit = mIntersect.intersects(ray, box);
                        for (OnTouchListener listener : listeners) {
                            if (listener.onMove(ray, isHit, x, y)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
        }
        return false;
    }
}
