package ddwucom.mobile.recipebook;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class RecipeXmlParser {
    public enum TagType { NONE, NAME, TYPE, CAL, IMAGE, ING, MANUAL };

    final static String TAG_ROW = "row";
    final static String TAG_NAME = "RCP_NM";
    final static  String TAG_TYPE = "RCP_PAT2";
    final static  String TAG_CAL = "INFO_ENG";
    final static  String TAG_IMAGE = "image";
    final static  String TAG_ING = "RCP_PARTS_DTLS";
    final static  String TAG_MANUAL = "MANUAL";

    public RecipeXmlParser() {
    }

    public ArrayList<Recipe> parse(String xml) {
        Log.d("goeun", "parse");
        ArrayList<Recipe> resultList = new ArrayList();
        Recipe dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ROW)) {
                            dto = new Recipe();
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_TYPE)) {
                            if (dto != null) tagType = TagType.TYPE;
                        } else if (parser.getName().equals(TAG_CAL)) {
                            if (dto != null) tagType = TagType.CAL;
                        } else if (parser.getName().equals(TAG_IMAGE)) {
                            if (dto != null) tagType = TagType.IMAGE;
                        } else if (parser.getName().equals(TAG_ING)) {
                            if (dto != null) tagType = TagType.ING;
                        } else if (parser.getName().contains(TAG_MANUAL)) {
                            if (dto != null) tagType = TagType.MANUAL;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ROW)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case NAME:
                                dto.setName(parser.getText());
                                break;
                            case TYPE:
                                dto.setType(parser.getText());
                                break;
                            case CAL:
                                dto.setCal(Integer.valueOf(parser.getText()));
                                break;
                            case IMAGE:
                                dto.setImageLink(parser.getText());
                                break;
                            case ING:
                                dto.setIngredient(parser.getText());
                                break;
                            case MANUAL:
                                dto.getManuals().add(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }
}
