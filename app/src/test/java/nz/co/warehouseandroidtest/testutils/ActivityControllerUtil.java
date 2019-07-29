package nz.co.warehouseandroidtest.testutils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;

public class ActivityControllerUtil<T extends Activity> {

    private Class<T> klass;
    private Intent intent;
    private ActivityController<T> controller;

    public static <T extends Activity> ActivityControllerUtil<T> of(Class<T> klass) {
        return new ActivityControllerUtil<>(klass);
    }

    public static <T extends Activity> ActivityControllerUtil<T> of(Class<T> klass, Intent intent) {
        return new ActivityControllerUtil<>(klass, intent);
    }

    private ActivityControllerUtil(Class<T> klass) {
        this(klass, null);
    }

    private ActivityControllerUtil(Class<T> klass, Intent intent) {
        this.klass = klass;
        this.intent = intent;
    }

    /**
     * Instantiate activity without continuing to its lifecycle methods.
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> instantiate() {
        if (controller == null) {
            controller = intent == null ?
                    Robolectric.buildActivity(klass) :
                    Robolectric.buildActivity(klass, intent);
        }
        return this;
    }

    private void instantiate(Intent intent) {
        this.intent = intent;
        instantiate();
    }

    /**
     * Instantiate activity and pass the {@link Intent intent} without continuing to its lifecycle methods.
     * @param intent Intent to be passed to activity
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> instantiateWithIntent(Intent intent) {
        instantiate(intent);
        return this;
    }

    /**
     * Instantiate activity with null {@link Bundle savedInstanceState} and
     * continue to its lifecycle methods:
     *      {@link Activity#onCreate(Bundle)}
     *      {@link Activity#onStart()}
     *      {@link Activity#onPostCreate(Bundle)}
     *      {@link Activity#onResume()}
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> setup() {
        instantiate();
        controller.setup();
        return this;
    }

    /**
     * Instantiate activity with specified {@link Bundle savedInstanceState} and
     * continue to its lifecycle methods:
     *      {@link Activity#onCreate(Bundle)}
     *      {@link Activity#onStart()}
     *      {@link Activity#onPostCreate(Bundle)}
     *      {@link Activity#onRestoreInstanceState(Bundle)}
     *      {@link Activity#onResume()}
     * @param savedInstanceState Instance state to be passed to onCreate
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> setup(Bundle savedInstanceState) {
        instantiate();
        controller.setup(savedInstanceState);
        return this;
    }

    /**
     * Instantiate activity and pass the {@link Intent intent} with null {@link Bundle savedInstanceState}
     * and continue to its lifecycle methods:
     *      {@link Activity#onCreate(Bundle)}
     *      {@link Activity#onStart()}
     *      {@link Activity#onPostCreate(Bundle)}
     *      {@link Activity#onResume()}
     * @param intent Instance state to be passed to onCreate
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> setupWithIntent(Intent intent) {
        instantiate(intent);
        controller.setup();
        return this;
    }

    /**
     * Instantiate activity and pass the {@link Intent intent} with specified {@link Bundle savedInstanceState}
     * and continue to its lifecycle methods:
     *      {@link Activity#onCreate(Bundle)}
     *      {@link Activity#onStart()}
     *      {@link Activity#onPostCreate(Bundle)}
     *      {@link Activity#onRestoreInstanceState(Bundle)}
     *      {@link Activity#onResume()}
     * @param intent Instance state to be passed to onCreate
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> setupWithIntent(Intent intent, Bundle savedInstanceState) {
        instantiate(intent);
        controller.setup(savedInstanceState);
        return this;
    }

    /**
     * Deinitialize activity if it has been created previously by calling its lifecycle methods:
     *      {@link Activity#onPause()}
     *      {@link Activity#onStop()}
     *      {@link Activity#onDestroy()}
     * @return this instance of ActivityControllerUtil
     */
    public ActivityControllerUtil<T> tearDown() {
        if (controller != null) {
            controller.pause()
                    .stop()
                    .destroy();
        }
        return this;
    }

    /**
     * Get instance of the activity if it has been instantiated previously, otherwise throws exception
     * @return this instance of ActivityControllerUtil
     * @throws IllegalStateException if the activity has not been instantiated
     */
    public T get() throws IllegalStateException {
        checkNullController();
        return controller.get();
    }

    public ActivityControllerUtil<T> configurationChange(Configuration newConfiguration) {
        checkNullController();
        controller.configurationChange(newConfiguration);
        return this;
    }

    public ActivityControllerUtil<T> create() {
        instantiate();
        controller.create();
        return this;
    }

    public ActivityControllerUtil<T> create(Bundle bundle) {
        instantiate();
        controller.create(bundle);
        return this;
    }

    public ActivityControllerUtil<T> restoreInstanceState(Bundle bundle) throws IllegalStateException {
        checkNullController();
        controller.restoreInstanceState(bundle);
        return this;
    }

    public ActivityControllerUtil<T> postCreate(Bundle bundle) throws IllegalStateException {
        checkNullController();
        controller.postCreate(bundle);
        return this;
    }

    public ActivityControllerUtil<T> start() throws IllegalStateException {
        checkNullController();
        controller.start();
        return this;
    }

    public ActivityControllerUtil<T> restart() throws IllegalStateException {
        checkNullController();
        controller.restart();
        return this;
    }

    public ActivityControllerUtil<T> resume() throws IllegalStateException {
        checkNullController();
        controller.resume();
        return this;
    }

    public ActivityControllerUtil<T> postResume() throws IllegalStateException {
        checkNullController();
        controller.postResume();
        return this;
    }

    public ActivityControllerUtil<T> newIntent(Intent intent) throws IllegalStateException {
        checkNullController();
        controller.newIntent(intent);
        return this;
    }

    public ActivityControllerUtil<T> saveInstanceState(Bundle outState) throws IllegalStateException {
        checkNullController();
        controller.saveInstanceState(outState);
        return this;
    }

    public ActivityControllerUtil<T> visible() throws IllegalStateException {
        checkNullController();
        controller.visible();
        return this;
    }

    public ActivityControllerUtil<T> pause() throws IllegalStateException {
        checkNullController();
        controller.pause();
        return this;
    }

    public ActivityControllerUtil<T> userLeaving() throws IllegalStateException {
        checkNullController();
        controller.userLeaving();
        return this;
    }

    public ActivityControllerUtil<T> stop() throws IllegalStateException {
        checkNullController();
        controller.stop();
        return this;
    }

    public ActivityControllerUtil<T> destroy() throws IllegalStateException {
        checkNullController();
        controller.destroy();
        controller = null;
        return this;
    }

    private void checkNullController() throws IllegalStateException {
        if (controller == null) {
            throw new IllegalStateException("Activity has not been instantiated. To instantiate it, please call one of this methods prior calling this method: \n" +
                    "       ActivityControllerUtil#instantiate()\n" +
                    "       ActivityControllerUtil#instantiateWithIntent(Intent)\n" +
                    "       ActivityControllerUtil#setup()\n" +
                    "       ActivityControllerUtil#setup(Bundle)\n" +
                    "       ActivityControllerUtil#setupWithIntent(Intent)\n" +
                    "       ActivityControllerUtil#setupWithIntent(Intent, Bundle)\n");
        }
    }
}
