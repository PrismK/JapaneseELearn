package com.prismk.japaneseelearn.utils;

import android.util.Xml;

import com.prismk.japaneseelearn.bean.ArticleData;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * create by Nevermore
 * 2019/3/20
 * 你不愿回忆的过去已经死亡，就让黑夜把它永远埋葬
 */
public class ArticleXmlParser {



    public static List<ArticleData> parser(InputStream in) {
        List<ArticleData> articleDataList = new ArrayList<>();
        XmlPullParser parser = Xml.newPullParser();
        ArticleData articleData = null;
        try {
            parser.setInput(in,"utf-8");
            int eventType = parser.getEventType();
            while (eventType!=XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")){
                            articleData = new ArticleData();
                        }else if (parser.getName().equals("title")){
                            articleData.setTitle(parser.nextText());
                        }else if (parser.getName().equals("context")){
                            articleData.setContext(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")){
                            articleDataList.add(articleData);
                        }
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articleDataList;
    }
}
