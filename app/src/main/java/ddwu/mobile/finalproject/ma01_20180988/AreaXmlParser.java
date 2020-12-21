package ddwu.mobile.finalproject.ma01_20180988;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class AreaXmlParser {
    public enum TagType { NONE, NAME, CODE };

    final static String TAG_NAME = "codeName";
    final static  String TAG_CODE = "code";

    int sec = 1;

    public AreaXmlParser() {}

    public ArrayList<Area> parse(String xml) {
        ArrayList<Area> resultList = new ArrayList<>();
        Area area = null;
        Area detailArea = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_NAME)) {
                            tagType = TagType.NAME;
                        } else if (parser.getName().equals(TAG_CODE)) {
                            tagType = TagType.CODE;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        String text = parser.getText();
                        String secStr;
                        switch (tagType) {
                            case CODE:
                                secStr = text.substring(2, 4);
                                if (Integer.parseInt(secStr) == sec) {
                                    if (area != null) {
                                        resultList.add(area);
                                    }
                                    area = new Area();
                                    area.setCode(text);
                                }
                                else {
                                    detailArea = new Area();
                                    detailArea.setCode(text);
                                }
                                break;
                            case NAME:
                                if (detailArea == null) {
                                    area.setName(text);
                                    sec++;
                                }
                                else {
                                    detailArea.setName(text);
                                    area.getDetailAreas().add(detailArea);
                                    detailArea = null;
                                }
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
