package com.sweetlab.sweetride.tryouts.stars;

import android.test.AndroidTestCase;
import android.util.Log;

import com.sweetlab.sweetride.math.Camera;
import com.sweetlab.sweetride.math.Matrix33;
import com.sweetlab.sweetride.math.Matrix44;
import com.sweetlab.sweetride.math.Vec3;

public class TransformTryoutTest extends AndroidTestCase {

    public void test1() {
        Matrix44 mat = new Matrix44();
        mat.rotate(45, 0, 1, 0);
        Log.d("Peter100", "rot 45, 0, 1, 0 \n" + mat);
        mat.translate(33, 44, 55);
        Log.d("Peter100", "trans 33, 44 ,55 \n" + mat);

        Matrix44 mat2 = new Matrix44();
        mat2.rotate(-45, 0, 1, 0);
        mat.mult(mat2);
        Log.d("Peter100", "anti rot \n" + mat);

    }

    public void test2() {
        Matrix44 mat = new Matrix44();
        mat.translate(33, 44, 55);
        Log.d("Peter100", "trans 33, 44 ,55 \n" + mat);
        mat.rotate(45, 0, 1, 0);
        Log.d("Peter100", "rot 45, 0, 1, 0 \n" + mat);

        Matrix44 mat2 = new Matrix44();
        mat2.rotate(-45, 0, 1, 0);
        mat.mult(mat2);
        Log.d("Peter100", "anti rot \n" + mat);
    }

    public void test3() {
        Vec3 camPos = new Vec3(0,0,3);
        Vec3 camLookPos = new Vec3(-3, 0, 0);

        Camera camera = new Camera();
        camera.lookAt(camPos, camLookPos);
        Log.d("Peter100", "camera \n" + camera.getViewMatrix());

        Matrix33 tmp = new Matrix33();
        camera.getViewMatrix().getRotation(tmp);

        Vec3 pos = new Vec3();
        camera.getViewMatrix().get3x1Translation(pos);
        pos.transform(tmp);

        Log.d("Peter100", "pos = " + pos);
//        assertEquals(camPos.x, pos.x, EPS);
//        assertEquals(camPos.y, pos.y, EPS);
//        assertEquals(camPos.z, pos.z, EPS);
    }
}
