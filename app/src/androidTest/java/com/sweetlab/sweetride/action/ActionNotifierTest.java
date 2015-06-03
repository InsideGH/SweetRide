package com.sweetlab.sweetride.action;

import android.test.AndroidTestCase;

/**
 * Test of the action notifier.
 */
public class ActionNotifierTest extends AndroidTestCase {

    public void testConnectNotifier() throws Exception {
        NotifierA notifierA = new NotifierA();
        NotifierB notifierB = new NotifierB();
        NotifierC notifierC = new NotifierC();

        /**
         * Connect empty notifiers.
         */
        notifierA.connectNotifier(notifierB);

        /**
         * Add two actions to a notifier that is not connected.
         */
        notifierC.addAction(notifierC.mCreateAction);
        notifierC.addAction(notifierC.mLoadAction);

        /**
         * Connect the notifier that has 2 actions.
         */
        notifierB.connectNotifier(notifierC);

        /**
         * The actions have now been reported.
         */
        assertTrue(notifierA.hasActions());
        assertTrue(notifierB.hasActions());
        assertTrue(notifierC.hasActions());
        assertEquals(2, notifierA.getActionCount());
        assertEquals(2, notifierB.getActionCount());
        assertEquals(2, notifierC.getActionCount());
        assertEquals(notifierC.mCreateAction, notifierA.getAction(0));
        assertEquals(notifierC.mCreateAction, notifierB.getAction(0));
        assertEquals(notifierC.mCreateAction, notifierC.getAction(0));
        assertEquals(notifierC.mLoadAction, notifierA.getAction(1));
        assertEquals(notifierC.mLoadAction, notifierB.getAction(1));
        assertEquals(notifierC.mLoadAction, notifierC.getAction(1));

        /**
         * The the action source and type.
         */
        Action createAction = notifierA.getAction(0);
        ActionNotifier source = createAction.getSource();
        ActionType type = createAction.getType();
        assertEquals(ActionType.CREATE, type);
        assertEquals(source, notifierC);

        /**
         * Remove this action.
         */
        createAction.remove();

        /**
         * Now only the load action is left.
         */
        assertTrue(notifierA.hasActions());
        assertTrue(notifierB.hasActions());
        assertTrue(notifierC.hasActions());
        assertEquals(1, notifierA.getActionCount());
        assertEquals(1, notifierB.getActionCount());
        assertEquals(1, notifierC.getActionCount());
        assertEquals(notifierC.mLoadAction, notifierA.getAction(0));
        assertEquals(notifierC.mLoadAction, notifierB.getAction(0));
        assertEquals(notifierC.mLoadAction, notifierC.getAction(0));

        /**
         * Now do a bit of special thing by removing the load action from the middle only.
         */
        Action loadAction = notifierA.getAction(0);
        notifierB.removeAction(loadAction);

        assertFalse(notifierA.hasActions());
        assertFalse(notifierB.hasActions());
        assertTrue(notifierC.hasActions());
        assertEquals(0, notifierA.getActionCount());
        assertEquals(0, notifierB.getActionCount());
        assertEquals(1, notifierC.getActionCount());
        assertEquals(notifierC.mLoadAction, notifierC.getAction(0));

        /**
         * Now remove the action from everywhere.
         */
        loadAction.remove();

        assertFalse(notifierA.hasActions());
        assertFalse(notifierB.hasActions());
        assertFalse(notifierC.hasActions());
    }

    public void testDisconnectNotifier() throws Exception {
        NotifierA notifierA = new NotifierA();
        NotifierB notifierB = new NotifierB();
        NotifierC notifierC = new NotifierC();

        /**
         * Add some actions to all notifiers.
         */
        notifierA.addAction(notifierA.mCreateAction);
        notifierB.addAction(notifierB.mCreateAction);
        notifierC.addAction(notifierC.mCreateAction);
        notifierC.addAction(notifierC.mLoadAction);

        /**
         * Connect together.
         */
        notifierA.connectNotifier(notifierB);
        notifierB.connectNotifier(notifierC);

        /**
         * All connected.
         */
        assertEquals(4, notifierA.getActionCount());
        assertEquals(3, notifierB.getActionCount());
        assertEquals(2, notifierC.getActionCount());

        /**
         * Disconnect B tree from A.
         */
        notifierA.disconnectNotifier(notifierB);
        assertEquals(1, notifierA.getActionCount());
        assertEquals(3, notifierB.getActionCount());
        assertEquals(2, notifierC.getActionCount());

        /**
         * Connect B tree to A again.
         */
        notifierA.connectNotifier(notifierB);
        assertEquals(4, notifierA.getActionCount());
        assertEquals(3, notifierB.getActionCount());
        assertEquals(2, notifierC.getActionCount());

        /**
         * Disconnect C leaf from B.
         */
        notifierB.disconnectNotifier(notifierC);
        assertEquals(2, notifierA.getActionCount());
        assertEquals(1, notifierB.getActionCount());
        assertEquals(2, notifierC.getActionCount());

        /**
         * Remove all 2 actions from C.
         */
        notifierC.getAction(0).remove();
        notifierC.getAction(0).remove();

        /**
         * Connect back C to B.
         */
        notifierB.connectNotifier(notifierC);
        assertEquals(2, notifierA.getActionCount());
        assertEquals(1, notifierB.getActionCount());
        assertEquals(0, notifierC.getActionCount());
    }

    public void testAddAction() throws Exception {
        NotifierA notifierA = new NotifierA();
        assertFalse(notifierA.hasActions());

        /**
         * Add create action.
         */
        notifierA.addAction(notifierA.mCreateAction);
        assertTrue(notifierA.hasActions());
        assertTrue(notifierA.getActionCount() == 1);
        assertEquals(notifierA.mCreateAction, notifierA.getAction(0));

        /**
         * Add load action.
         */
        notifierA.addAction(notifierA.mLoadAction);
        assertTrue(notifierA.getActionCount() == 2);
        assertEquals(notifierA.mLoadAction, notifierA.getAction(1));

        /**
         * This add of create action will shuffle around the actions
         * placing the create action last.
         */
        notifierA.addAction(notifierA.mCreateAction);
        assertTrue(notifierA.getActionCount() == 2);
        assertEquals(notifierA.mLoadAction, notifierA.getAction(0));
        assertEquals(notifierA.mCreateAction, notifierA.getAction(1));
    }

    public void testRemoveAction() throws Exception {
        NotifierA notifierA = new NotifierA();
        notifierA.removeAction(notifierA.mCreateAction);

        /**
         * Add create and load action
         */
        notifierA.addAction(notifierA.mCreateAction);
        notifierA.addAction(notifierA.mLoadAction);
        assertTrue(notifierA.getActionCount() == 2);

        /**
         * Remove create action.
         */
        notifierA.removeAction(notifierA.mCreateAction);

        /**
         * Check that load action is left.
         */
        assertTrue(notifierA.getActionCount() == 1);
        assertEquals(notifierA.mLoadAction, notifierA.getAction(0));
    }

    public void testGetActionCount() throws Exception {
        NotifierA notifierA = new NotifierA();
        assertTrue(notifierA.getActionCount() == 0);
        for (int i = 0; i < 10; i++) {
            notifierA.addAction(notifierA.mCreateAction);
            notifierA.addAction(notifierA.mLoadAction);
        }

        /**
         * Test that there are no duplicates.
         */
        assertTrue(notifierA.getActionCount() == 2);
    }

    public void testGetAction() throws Exception {
        NotifierA notifierA = new NotifierA();
        boolean gotException = false;
        try {
            notifierA.getAction(0);
        } catch (IndexOutOfBoundsException e) {
            gotException = true;
        }
        assertTrue(gotException);

        /**
         * Test that we get back same reference.
         */
        notifierA.addAction(notifierA.mCreateAction);
        Action action = notifierA.getAction(0);
        assertEquals(action, notifierA.mCreateAction);
    }

    public void testHasActions() throws Exception {
        NotifierA notifierA = new NotifierA();
        assertFalse(notifierA.hasActions());
        notifierA.addAction(notifierA.mCreateAction);
        assertTrue(notifierA.hasActions());
        notifierA.removeAction(notifierA.mCreateAction);
        assertFalse(notifierA.hasActions());
    }

    private class NotifierA extends ActionNotifier {
        public Action mCreateAction = new Action(this, ActionType.CREATE);
        public Action mLoadAction = new Action(this, ActionType.LOAD);
    }

    private class NotifierB extends ActionNotifier {
        public Action mCreateAction = new Action(this, ActionType.CREATE);

    }

    private class NotifierC extends ActionNotifier {
        public Action mCreateAction = new Action(this, ActionType.CREATE);
        public Action mLoadAction = new Action(this, ActionType.LOAD);
    }
}