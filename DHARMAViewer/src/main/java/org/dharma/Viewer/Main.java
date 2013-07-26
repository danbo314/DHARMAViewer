package org.dharma.Viewer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class Main extends Activity{

    // UI Fields
    private GLSurfaceView mRendererView;
    private Renderer mRenderer;
    private Hashtable<String,Model> models;
    private final int NONE = 0;
    private final int ROTATE = 1;
    private final int ZOOM = 2;
    private final float TOUCH_SCALE_FACTOR = 200.0f;
    private final float DRAG_SCALE_FACTOR = 591145.0f;
    private float ZOOM_SCALE_FACTOR = 1.8f;
    private int myView;
    private int mode;
    private boolean isInfoOpen;
    private boolean isSettsOpen;
    private float mPreviousX;
    private float mPreviousY;
    private String currentModelName;
    private Model currentModel;
    private ScaleGestureDetector sgd;
    private GestureDetector dtgl;
    private AssetManager mAssets;
    private ArrayList<String> modelNames;
    private ProgressDialog pd;
    private Context mContext;
    private Spinner modelChoices;
    private View popupView;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Setup View
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAssets = this.getAssets();
        mRenderer = new Renderer();
        models = new Hashtable<String,Model>();
        myView = 0;
        isInfoOpen = false;
        isSettsOpen = false;
        currentModelName = null;
        modelNames = new ArrayList<String>();
        mContext = this;
        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

        // Initialize scale and gesture detectors for scaling, rotating, panning, and resetting view
        sgd = new ScaleGestureDetector(this, new ScaleListener());
        dtgl = new GestureDetector(this, new DoubleTapListener());

        // Setup UI
        mRendererView = (GLSurfaceView)findViewById( R.id.renderer );
        mRendererView.setRenderer(mRenderer);
        mRendererView.requestFocus();
        mRendererView.setFocusableInTouchMode(true);

        // Set touch listener for RenderView
        mRendererView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                // Gesture and scale detectors begin listening
                sgd.onTouchEvent(e);
                dtgl.onTouchEvent(e);

                // Store current model position
                float x = e.getX();
                float y = e.getY();

                // Detect screen gesture and figure out what is happening
                switch (e.getAction() & MotionEvent.ACTION_MASK) {
                    // Catch pointer movement along screen
                    case MotionEvent.ACTION_MOVE:
                        // If there are 3 pointers down, pan model across screen
                        // I stuck with 3 pointers because 2 gets very jerky with ScaleGestureDetector below
                        // Change 3 to 2 in this if statement to change number of pointers for pan
                        if(e.getPointerCount() == 3)
                        {
                            if(mRenderer != null)
                            {
                                // Calculate distance moved with dampening based on number of points in model
                                // The more points a model has, the slower it moves -> needs to be dampened less
                                float dx = (x - mPreviousX) / (DRAG_SCALE_FACTOR / (float)currentModel.numPoints());
                                float dy = (y - mPreviousY) / (DRAG_SCALE_FACTOR / (float)currentModel.numPoints());
                                // Update model position in Renderer
                                mRenderer.xPos += dx;
                                mRenderer.yPos -= dy;
                            }
                        }
                        // Catch if there is a different number of pointers down (not 3)
                        else
                        {
                            // See what mode we are in
                            switch (mode)
                            {
                                // If we are in rotate mode, drop in
                                case ROTATE:
                                    if(mRenderer != null)
                                    {
                                        // calculate how far pointer has moved in each direction for rotation angle
                                        float dx = (x - mPreviousX) / TOUCH_SCALE_FACTOR;
                                        float dy = (y - mPreviousY) / TOUCH_SCALE_FACTOR;
                                        // update rotation angles in Renderer
                                        mRenderer.rotX += dx;
                                        mRenderer.rotY += dy;
                                        // Create directional Rotation Quaternions
                                        Quaternion rotX = new Quaternion(new Angle(dx, Angle.Type.RADIAN),new Vector3f(0,1,0));
                                        Quaternion rotY = new Quaternion(new Angle(dy, Angle.Type.RADIAN),new Vector3f(1,0,0));
                                        // Calculate the overall Quaternion rotation vector
                                        Quaternion thisRot = Quaternion.Multiply(rotX,rotY);
                                        // Update Quaternions in Renderer
                                        mRenderer.newRot = Quaternion.Multiply(thisRot,mRenderer.oldRot);
                                        mRenderer.oldRot = new Quaternion(mRenderer.newRot);
                                    }
                                    break;
                            }
                        }
                        // Force Renderer to redraw
                        v.invalidate();
                        break;
                    // Update mode when first pointer is placed on screen to Rotate
                    case MotionEvent.ACTION_DOWN:
                        mode = ROTATE;
                        break;
                    // Update mode to Zoom when second pointer is placed
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = ZOOM;
                        break;
                    // Drop out of zoom and rotate modes when pointer is lifted
                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        break;
                    // Drop out of zoom and rotate modes when second pointer is lifted
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                    // Drop out of zoom and rotate modes when an action is cancelled
                    case MotionEvent.ACTION_CANCEL:
                        mode = NONE;
                        break;
                }
                // Store location for calculations on next gesture
                mPreviousX = x;
                mPreviousY = y;

                return true;
            }
        });

        // Initialize immage buttons for info and settings pop ups
        final ImageView settings = (ImageView)findViewById(R.id.toSettingsButton);
        final ImageView info = (ImageView)findViewById(R.id.toInfoButton);

        // Initialize views for settings popups
        final View settingsDash = layoutInflater.inflate(R.layout.settings2, null);
        final PopupWindow settingsPopUp = new PopupWindow(settingsDash, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Initialize image links for DHARMA and Visarray websites within settings popo up
        ImageView dharmaLink = (ImageView)settingsDash.findViewById(R.id.dharmaLink);
        ImageView visarrayLink = (ImageView)settingsDash.findViewById(R.id.vaLink);
        // Initialize spinner for different model choices within settings pop up
        modelChoices = (Spinner)settingsDash.findViewById(R.id.modelSpinner);

        // Set click listener on DHARMA logo to go to website
        dharmaLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.dharma3d.org/")));
            }
        });

        // Set click listener on Visarray logo to go to website
        visarrayLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.visarray.com/")));
            }
        });

        // Set click listener on model selection spinner and change model in renderer on selection
        modelChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object modelChoice = parent.getItemAtPosition(pos);
                currentModelName = modelChoice.toString();
                currentModel = models.get(modelChoice.toString());
                mRenderer.currentModel =  currentModel;

                // Update the info in the info pop up with the current models information
                TextView infoTitle = (TextView)popupView.findViewById(R.id.infoTitle);
                TextView infoData = (TextView)popupView.findViewById(R.id.infoWriteUp);
                infoTitle.setText(currentModelName);
                infoData.setText("Points in this model: " + currentModel.numPoints());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Create radio group for different model views (points, surface, texture)
        final RadioGroup viewSelector = (RadioGroup)settingsDash.findViewById(R.id.viewSelector);

        // Change the view and the Renderer view when a different radio button is selected
        viewSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                myView = i;
                mRenderer.currentView = i;
            }
        });

        // Set a listener for the "Done Editing" button in the settings pop up to close the pop up
        Button settingsButton = (Button)settingsDash.findViewById(R.id.updateButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingsPopUp.dismiss();
                isSettsOpen = false;
            }
        });

        // Set a listener for the arrow button to open up the settings pop up
        settings.setOnClickListener(new ImageView.OnClickListener(){
            public void onClick(View v){
                if(!isSettsOpen){
                    viewSelector.check(myView);
                    settingsPopUp.showAtLocation(mRendererView,Gravity.CENTER,0,0);
                    isSettsOpen = true;
                }
            }
        });

        // Set a listener for the info button to open the info pop up
        info.setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View v) {
                if (!isInfoOpen) {
                    popupWindow.showAtLocation(info, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    isInfoOpen = true;
                }
                if(popupView != null){
                    // Tap on info box when it is showing to hide it
                    popupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popupWindow.dismiss();
                            isInfoOpen = false;
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Create application instance
        final DHARMApp myApp = (DHARMApp)getApplication();

        // Create AsyncTask to load models in the background and show progress dialog to warn user that app is loading
        AsyncTask<Void,Void,Void> loader = new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute()
            {
                pd = new ProgressDialog(mContext);
                pd.setIndeterminate(true);
                pd.setCancelable(false);
                pd.setTitle("Loading Models");
                pd.setMessage("Please Wait...");
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                loadModels(myApp);
                return null;
            }

            @Override
            protected void onPostExecute(Void v)
            {
                pd.dismiss();
            }
        };

        // If the models have not been loaded previously in app, execute AsyncTask
        if(!myApp.getLoadState())
            loader.execute();
    }

    // Function to load models from Assets folder
    private void loadModels(DHARMApp app){
        try {
            // Look at all files in the base asset directory
            for( String file: mAssets.list("") ){
                // Test if they are xml descriptor files
                if( file.contains(".xml") ){
                    Log.i("Load", "Model File: " + file);

                    for( Model m : DharmaXmlParser.Parse( mAssets, file ) ){
                        Log.i("Model", "Title: " + m.Title);
                        Log.i("Model", "Radius: " + m.Radius);
                        Log.i("Model", "View: " + m.View);
                        Log.i("Model", "Center: " + m.Center);
                        Log.i("Model", "Path: " + m.Path);
                        Log.i("Model", "Number of Points:" + m.numPoints());

                        // double check that models aren't being doubly loaded
                        if(!modelNames.contains(m.Title)){
                            models.put(m.Title,m);
                            modelNames.add(m.Title);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Load - Read/Write to File:",e.toString());
        }

        // If there is no currentModel, set the default model
        if(currentModelName == null)
            currentModelName = "Arch Of Septimis";
        // If there is no default view, set the default view
        if(myView == 0)
            myView = R.id.radioPointsView;

        currentModel = models.get(currentModelName);

        // Update model and view in Renderer
        mRenderer.currentModel = currentModel;
        mRenderer.currentView = myView;

        // Create spinner items from list of model names
        ArrayAdapter<String> modelsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,modelNames);
        modelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelChoices.setAdapter(modelsAdapter);

        // Initialize the info pop up
        popupView = layoutInflater.inflate(R.layout.info, null);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Update the info in the info pop up with the current models information
        TextView infoTitle = (TextView)popupView.findViewById(R.id.infoTitle);
        TextView infoData = (TextView)popupView.findViewById(R.id.infoWriteUp);
        infoTitle.setText(currentModelName);
        infoData.setText("Points in this model: " + currentModel.numPoints());

        // Notify the application that the models have been loaded so that we do not load again
        app.setLoadState(true);
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // ScaleListener that detects a two-fingered scale gesture on screen
    @TargetApi(Build.VERSION_CODES.FROYO)
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Get's scale factore from detected gesture
            ZOOM_SCALE_FACTOR *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            ZOOM_SCALE_FACTOR = Math.max(0.1f, Math.min(ZOOM_SCALE_FACTOR, 5.0f));
            // Update Renderer's scale factor
            mRenderer.scaleZ = ZOOM_SCALE_FACTOR;
            return true;
        }
    }

    // DoubleTapListener that detects a double tap
    private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            // Resets Renderer and model params when a double tap is detected
            mRenderer.reset = true;
            return true;
        }
    }
}