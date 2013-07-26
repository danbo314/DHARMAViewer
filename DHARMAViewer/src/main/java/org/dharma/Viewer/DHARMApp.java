package org.dharma.Viewer;

import android.app.Application;

/**
 * Created by Dan Bolivar on 7/25/13.
 */

// Custom Application class that stores whether or not models have been loaded
// so that they only get loaded once per application run.  Preserves memory and avoids crash.
public class DHARMApp extends Application {

    private boolean haveModelsLoaded;

    public DHARMApp() {
        // this method fires only once per application start.
        // getApplicationContext returns null here
        haveModelsLoaded = false;
    }

    public void setLoadState(boolean b){
        haveModelsLoaded = b;
    }

    public boolean getLoadState()
    {
        return haveModelsLoaded;
    }
}
