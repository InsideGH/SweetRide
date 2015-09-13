package com.sweetlab.sweetride.logger;

import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.Subscription;
import rx.subjects.PublishSubject;

public class ControllableLog {
    private final Observer<String> mObserver = new Observer<String>() {
        @Override
        public void onCompleted() {
            mLogMessage.log("completed");
        }

        @Override
        public void onError(Throwable e) {
            mLogMessage.log(e.toString());
        }

        @Override
        public void onNext(String s) {
            mLogMessage.log(s);
        }
    };

    private final PublishSubject<String> mSubject = PublishSubject.create();

    private final LogMessage mLogMessage;

    /**
     * The current subscription.
     */
    private Subscription mSubscription;

    /**
     * Constructor. Creates a category that is disabled by default.
     *
     * @param tag        The adb log tag.
     * @param messageTag The message log tag.
     */
    public ControllableLog(String tag, Enum messageTag) {
        mLogMessage = new LogMessage(tag, messageTag);
    }

    /**
     * Log a message.
     *
     * @param message The message.
     */
    public void log(String message) {
        mSubject.onNext(message);
    }

    /**
     * Enable logging with interval.
     *
     * @param period Period of sampling.
     */
    public void enableWithSample(long period) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mSubject.sample(period, TimeUnit.MILLISECONDS).subscribe(mObserver);
    }

    public String getMessageTag() {
        return mLogMessage.getMessageTag();
    }

    /**
     * Enable or disable logging.
     *
     * @param enable True or false.
     */
    public void setEnable(boolean enable) {
        if (enable) {
            enable();
        } else {
            disable();
        }
    }

    /**
     * Enable logging.
     */
    private void enable() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mSubject.subscribe(mObserver);
    }

    /**
     * Disable logging.
     */
    private void disable() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
