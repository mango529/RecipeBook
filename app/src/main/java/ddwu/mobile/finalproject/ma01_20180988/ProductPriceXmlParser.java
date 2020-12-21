package ddwu.mobile.finalproject.ma01_20180988;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ProductPriceXmlParser {
    private static final String TAG = "ProductPriceXmlParser";

    public enum TagType { NONE, ENTP_ID, PRICE };

    final static String TAG_ITEM = "item";
    final static String TAG_ENTP_ID = "entpId";
    final static String TAG_PRICE = "goodPrice";

    public ProductPriceXmlParser() {
    }

    public ArrayList<Store> parse(String xml) {
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
                        }  else if (parser.getName().equals(TAG_ENTP_ID)) {
                            if (dto != null) tagType = TagType.ENTP_ID;
                        } else if (parser.getName().equals(TAG_PRICE)) {
                            if (dto != null) tagType = TagType.PRICE;
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
                            case ENTP_ID:
                                dto.setId(Integer.parseInt(parser.getText()));
                                break;
                            case PRICE:
                                dto.setGoodPrice(Integer.parseInt(parser.getText()));
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
