package com.sweetlab.sweetride.game.state;

import com.sweetlab.sweetride.game.BaseEntity;

import junit.framework.TestCase;

/**
 * Test state machine.
 */
public class StateMachineTest extends TestCase {

    private Peter mPeter;
    private Hunting mHunting;
    private Eating mEating;
    private Thinking mThinking;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mHunting = new Hunting();
        mEating = new Eating();
        mThinking = new Thinking();
        mPeter = new Peter();
    }

    public void testSetCurrentState() throws Exception {
        assertNotNull(mPeter.getStateMachine().getCurrentState());
        assertEquals(Eating.class, mPeter.getStateMachine().getCurrentState().getClass());
        mPeter.getStateMachine().setCurrentState(new Hunting());
        assertEquals(Hunting.class, mPeter.getStateMachine().getCurrentState().getClass());
    }

    public void testSetGlobalState() throws Exception {
        assertNotNull(mPeter.getStateMachine().getGlobalState());
        assertEquals(Thinking.class, mPeter.getStateMachine().getGlobalState().getClass());
        mPeter.getStateMachine().setGlobalState(new Hunting());
        assertEquals(Hunting.class, mPeter.getStateMachine().getGlobalState().getClass());
    }

    public void testUpdate() throws Exception {
        assertEquals(Eating.class, mPeter.getStateMachine().getCurrentState().getClass());
        mPeter.update(0);
        assertEquals(Hunting.class, mPeter.getStateMachine().getCurrentState().getClass());
    }

    public void testUpdate2() throws Exception {
        assertEquals(0, mEating.mEnterCount);
        assertEquals(0, mEating.mExecuteCount);
        assertEquals(0, mEating.mExitCount);

        assertEquals(0, mThinking.mEnterCount);
        assertEquals(0, mThinking.mExecuteCount);
        assertEquals(0, mThinking.mExitCount);

        assertEquals(0, mHunting.mEnterCount);
        assertEquals(0, mHunting.mExecuteCount);
        assertEquals(0, mHunting.mExitCount);

        mPeter.update(0);

        assertEquals(0, mEating.mEnterCount);
        assertEquals(1, mEating.mExecuteCount);
        assertEquals(1, mEating.mExitCount);

        assertEquals(0, mThinking.mEnterCount);
        assertEquals(1, mThinking.mExecuteCount);
        assertEquals(0, mThinking.mExitCount);

        assertEquals(1, mHunting.mEnterCount);
        assertEquals(0, mHunting.mExecuteCount);
        assertEquals(0, mHunting.mExitCount);

        mPeter.update(0);

        assertEquals(1, mEating.mEnterCount);
        assertEquals(1, mEating.mExecuteCount);
        assertEquals(1, mEating.mExitCount);

        assertEquals(0, mThinking.mEnterCount);
        assertEquals(2, mThinking.mExecuteCount);
        assertEquals(0, mThinking.mExitCount);

        assertEquals(1, mHunting.mEnterCount);
        assertEquals(1, mHunting.mExecuteCount);
        assertEquals(1, mHunting.mExitCount);
    }

    public void testGetOwner() throws Exception {
        assertEquals(Peter.class, mPeter.getStateMachine().getOwner().getClass());
    }

    public void testChangeState() throws Exception {
        assertNotNull(mPeter.getStateMachine().getCurrentState());
        assertEquals(Eating.class, mPeter.getStateMachine().getCurrentState().getClass());
        mPeter.getStateMachine().changeState(new Hunting());
        assertEquals(Hunting.class, mPeter.getStateMachine().getCurrentState().getClass());
    }

    public void testGetCurrentState() throws Exception {
        assertEquals(Eating.class, mPeter.getStateMachine().getCurrentState().getClass());
    }

    public void testGetGlobalState() throws Exception {
        assertEquals(Thinking.class, mPeter.getStateMachine().getGlobalState().getClass());
    }

    private class Peter extends BaseEntity {
        private StateMachine<Peter> mStateMachine = new StateMachine<>(this);

        public Peter() {
            mStateMachine.setCurrentState(mEating);
            mStateMachine.setGlobalState(mThinking);
        }

        @Override
        public void update(float dt) {
            mStateMachine.update(dt);
        }

        public StateMachine<Peter> getStateMachine() {
            return mStateMachine;
        }
    }

    private class Hunting implements State<Peter> {
        public int mEnterCount;
        public int mExecuteCount;
        public int mExitCount;

        @Override
        public void enter(Peter owner) {
            mEnterCount++;
        }

        @Override
        public void execute(Peter owner, float dt) {
            mExecuteCount++;
            owner.getStateMachine().changeState(mEating);
        }

        @Override
        public void exit(Peter owner) {
            mExitCount++;
        }
    }

    private class Eating implements State<Peter> {
        public int mEnterCount;
        public int mExecuteCount;
        public int mExitCount;

        @Override
        public void enter(Peter owner) {
            mEnterCount++;
        }

        @Override
        public void execute(Peter owner, float dt) {
            mExecuteCount++;
            owner.getStateMachine().changeState(mHunting);
        }

        @Override
        public void exit(Peter owner) {
            mExitCount++;
        }
    }

    private class Thinking implements State<Peter> {
        public int mEnterCount;
        public int mExecuteCount;
        public int mExitCount;

        @Override
        public void enter(Peter owner) {
            mEnterCount++;
        }

        @Override
        public void execute(Peter owner, float dt) {
            mExecuteCount++;
        }

        @Override
        public void exit(Peter owner) {
            mExitCount++;
        }
    }
}