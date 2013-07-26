package org.dharma.Viewer;

import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Date: 5/19/13
 * Author: James Sweet
 */
public class DharmaXmlParser {
    public static List<Model> Parse(AssetManager assets, String file) throws XmlPullParserException, IOException {
        InputStream in = assets.open(file);
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return ParseModel(assets, parser);
        } finally {
            in.close();
        }
    }

    // Model Functions
    private static List<Model> ParseModel(AssetManager assets, XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Model> models = new ArrayList<Model>();

        parser.require(XmlPullParser.START_TAG, null, "data");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (parser.getName().equals("model")) {
                models.add(readModel(assets, parser));
            } else {
                skip(parser);
            }
        }
        return models;
    }

    private static Model readModel(AssetManager assets, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "model");

        Model retVal = new Model(assets);

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                retVal.Title = readTitle(parser);
            } else if ( name.equals("radius") ) {
                retVal.Radius = readRadius(parser);
            } else if ( name.equals("view") ) {
                retVal.View = readView(parser);
            } else if ( name.equals("center") ) {
                retVal.Center = readCenter(parser);
            } else if ( name.equals("packagepath") ) {
                retVal.Path = readPath(parser);
            } else if ( name.equals("container") ) {
                readContainer(retVal, parser);
            } else if ( name.equals("cloud") ) {
                readCloud(retVal, null, 1.0f, parser);
            } else {
                skip(parser);
            }
        }

        return retVal;
    }

    private static String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private static float readRadius(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "radius");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "radius");
        return Float.parseFloat(data);
    }

    private static float[] readView(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "view");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "view");

        float retVal[] = new float[4];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static float[] readCenter(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "center");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "center");

        float retVal[] = new float[3];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static String readPath(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "packagepath");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "packagepath");
        return title;
    }

    // Contaner Functions
    public static void readContainer( Model model, XmlPullParser parser ) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "container");

        float[] Transformation = new float[16];
        float Scale = 1.0f;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("transformmatrix")) {
                Transformation = readTransformMatrix(parser);
            } else if ( name.equals("pointscale") ) {
                Scale = readPointScale(parser);
            } else if ( name.equals("mesh") ) {
                readMesh(model, Transformation, Scale, parser);
            } else if ( name.equals("cloud") ) {
                readCloud(model, Transformation, Scale, parser);
            } else {
                skip(parser);
            }
        }
    }

    // Mesh Functions
    /*
    <mesh>
				<numberpoints>227</numberpoints>
				<numberindexes>1053</numberindexes>
				<texture>ArchOfSeptimis-18-30.jpg</texture>
				<points>ArchOfSeptimis-18-30.dat</points>
				<indexes>ArchOfSeptimis-18-30-index.dat</indexes>
			</mesh>
     */

    private static void readMesh( Model model, float[] transform, float scale, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "mesh");

        float[] Transformation = transform;
        float Scale = scale;
        int Points = 0;
        int Indicies = 0;
        String PointsPath = "";
        String IndexPath = "";
        String TexturePath = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("transformmatrix")) {
                Transformation = readTransformMatrix(parser);
            } else if ( name.equals("numberpoints") ) {
                Points = readInt("numberpoints", parser);
            } else if ( name.equals("numberindexes") ) {
                Indicies = readInt("numberindexes", parser);
            } else if ( name.equals("points") ) {
                PointsPath = readString("points", parser);
            } else if ( name.equals("indexes") ) {
                IndexPath = readString("indexes", parser);
            } else if (name.equals("texture") ) {
                TexturePath = readString("texture", parser);
            } else {
                skip(parser);
            }
        }

        model.addMesh(Transformation, PointsPath, Points, IndexPath, Indicies, TexturePath);
    }

    // Cloud Functions
    private static void readCloud( Model model, float[] transform, float scale, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "cloud");

        float[] Transformation = transform;
        float Scale = scale;
        int Points = 0;
        String Path = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("transformmatrix")) {
                Transformation = readTransformMatrix(parser);
            } else if ( name.equals("pointscale") ) {
                Scale = readPointScale(parser);
            } else if ( name.equals("numbercolorpoints") ) {
                Points = readCloudPoints(parser);
            } else if ( name.equals("colorpoints") ) {
                Path = readCloudPath(parser);
            } else {
                skip(parser);
            }
        }

        model.addCloud(Transformation, Scale, Points, Path);
    }

    private static float[] readTransformMatrix(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "transformmatrix");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "transformmatrix");

        float retVal[] = new float[16];

        int position = 0;
        for( String v : data.split(", ") ){
            retVal[position] = Float.parseFloat(v);
            position += 1;
        }

        return retVal;
    }

    private static float readPointScale(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "pointscale");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "pointscale");

        return Float.parseFloat(data);
    }

    private static int readCloudPoints(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "numbercolorpoints");
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "numbercolorpoints");

        return Integer.parseInt(data);
    }

    private static String readCloudPath(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "colorpoints");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "colorpoints");
        return title;
    }

    // Helper Functions
    private static int readInt(String tag, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);

        return Integer.parseInt(data);
    }

    private static String readString(String tag, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String data = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);

        return data;
    }

    private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
