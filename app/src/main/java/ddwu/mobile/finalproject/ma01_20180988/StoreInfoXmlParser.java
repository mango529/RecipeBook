package ddwu.mobile.finalproject.ma01_20180988;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class StoreInfoXmlParser {
    private static final String TAG = "StoreInfoXmlParser";
    public enum TagType { NONE, NAME, ENTP_ID, DETAIL_AREA, ADDRESS, X_MAP, Y_MAP, TEL };

    final static String TAG_ITEM = "iros.openapi.service.vo.entpInfoVO";
    final static String TAG_NAME = "entpName";
    final static String TAG_ENTP_ID = "entpId";
    final static String TAG_DETAIL_AREA = "areaDetailCode";
    final static String TAG_ADDRESS = "plmkAddrBasic";
    final static String TAG_X_MAP = "xMapCoord";
    final static String TAG_Y_MAP = "yMapCoord";
    final static String TAG_TEL = "entpTelno";

    public StoreInfoXmlParser() {
    }

    public ArrayList<Store> parse(String xml, String myArea) {
        ArrayList<Store> resultList = new ArrayList<>();
        Store dto = null;

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
                            dto = new Store();
                        } else if (parser.getName().equals(TAG_NAME)) {
                            if (dto != null) tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_ENTP_ID)) {
                            if (dto != null) tagType = TagType.ENTP_ID;
                        } else if (parser.getName().equals(TAG_DETAIL_AREA)) {
                            if (dto != null) tagType = TagType.DETAIL_AREA;
                        } else if (parser.getName().equals(TAG_ADDRESS)) {
                            if (dto != null) tagType = TagType.ADDRESS;
                        } else if (parser.getName().equals(TAG_X_MAP)) {
                            if (dto != null) tagType = TagType.X_MAP;
                        } else if (parser.getName().contains(TAG_Y_MAP)) {
                            if (dto != null) tagType = TagType.Y_MAP;
                        } else if (parser.getName().contains(TAG_TEL)) {
                            if (dto != null) tagType = TagType.TEL;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch(tagType) {
                            case NAME:
                                dto.setName(parser.getText());
                                break;
                            case ENTP_ID:
                                dto.setId(Integer.parseInt(parser.getText()));
                                break;
                            case DETAIL_AREA:
                                if (!parser.getText().equals(myArea)) {
                                    dto = null;
                                }
                                break;
                            case ADDRESS:
                                dto.setAddress(parser.getText());
                                break;
                            case X_MAP:
                                dto.setLatitude(Double.parseDouble(parser.getText()));
                                break;
                            case Y_MAP:
                                dto.setLongitude(Double.parseDouble(parser.getText()));
                                break;
                            case TEL:
                                dto.setTel(parser.getText());
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
