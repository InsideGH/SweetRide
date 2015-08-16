package com.sweetlab.sweetride.demo.game.assets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Android assets loader using RX java.
 */
public class AssetsLoader {

    /**
     * Android context.
     */
    private final Context mContext;

    /**
     * Constructor.
     *
     * @param context Android context.
     */
    public AssetsLoader(Context context) {
        mContext = context;
    }

    /**
     * Load bitmap asynchronously. Loads in background and notifies subscriber on main thread. This
     * is done by using observeOn main thread and subscribeOn IO thread.
     *
     * @param asset   Bitmap asset.
     * @param options Bitmap options.
     * @return Bitmap observable.
     */
    public Observable<Bitmap> loadBitmapAsync(final int asset, final BitmapFactory.Options options) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(BitmapFactory.decodeResource(mContext.getResources(), asset, options));
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
    }

    /**
     * Load bitmap. Up to caller to decide which scheduler to observeOn and subscribeOn.
     *
     * @param asset   Bitmap asset.
     * @param options Bitmap options.
     * @return Bitmap observable.
     */
    public Observable<Bitmap> loadBitmap(final int asset, final BitmapFactory.Options options) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                subscriber.onNext(BitmapFactory.decodeResource(mContext.getResources(), asset, options));
            }
        });
    }
}
