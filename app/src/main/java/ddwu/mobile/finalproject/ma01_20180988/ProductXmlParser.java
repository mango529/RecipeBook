package ddwu.mobile.finalproject.ma01_20180988;

import android.nfc.Tag;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ProductXmlParser {
    private static final String TAG = "ProductXmlParser";

    public enum TagType { NONE, NAME, GOOD_ID, DETAIL };

    final static String TAG_ITEM = "item";
    final static String TAG_NAME = "goodName";
    final static String TAG_GOOD_ID = "goodId";
    final static String TAG_DETAIL = "detailMean";

    public ProductXmlParser() {
    }

    public ArrayList<Product> parse(String xml) {
        ArrayList<Product> resultList = new ArrayList<>();
        Product dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new Product();
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_GOOD_ID)) {
                            if (dto != null) tagType = TagType.GOOD_ID;
                        } else if (parser.getName().equals(TAG_DETAIL)) {
                            if (dto != null) tagType = TagType.DETAIL;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case NAME:
                                Log.d(TAG, "NAME " + parser.getText());
                                dto.setName(parser.getText());
                                break;
                            case GOOD_ID:
                                Log.d(TAG, "GOOD_ID " + Integer.parseInt(parser.getText()));
                                dto.setGoodId(Integer.parseInt(parser.getText()));
                                break;
                            case DETAIL:
                                Log.d(TAG, "DETAIL " + parser.getText());
                                dto.setDetail(parser.getText());
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
