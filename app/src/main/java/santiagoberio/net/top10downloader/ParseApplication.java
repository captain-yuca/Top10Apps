package santiagoberio.net.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by antonio.santiago on 6/15/2016.
 */
public class ParseApplication {
    private String xmlData;
    private ArrayList<Application> applications;

    public ParseApplication(String xmlData) {
        this.xmlData = xmlData;
        applications = new ArrayList<>();

    }

    public ArrayList<Application> getApplications() {
        return applications;
    }


    public boolean process() {
        boolean status = true;
        Application currentRecord = null;
        boolean inEntry = true;
        String textValue = "";

        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.xmlData));
            int eventType = xpp.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){
                String tagNAme = xpp.getName();
                switch(eventType){
                    case XmlPullParser.START_TAG:
                        //Log.d("ParseApplications", "Starting Tag for " + tagNAme);
                        if(tagNAme.equalsIgnoreCase("entry")){
                            inEntry = true;
                            currentRecord = new Application();
                            break;
                       }
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                       //Log.d("Parse Applications", " Ending tag for " + tagNAme);
                        if(inEntry){
                            if(tagNAme.equalsIgnoreCase(" entry")){
                                applications.add(currentRecord);
                                inEntry = false;

                            }else if(tagNAme.equalsIgnoreCase(" name")){
                                currentRecord.setName(textValue);
                            }else if(tagNAme.equalsIgnoreCase(" artist")){
                                currentRecord.setArtist(textValue);
                            }else if(tagNAme.equalsIgnoreCase(" releaseDate")) {
                                currentRecord.setReleaseDate(textValue);
                            }
                        }
                        break;

                    default:
                        //Nothing else to do.
                }
                eventType = xpp.next();

            }

            return true;

        }catch(Exception e){
            status = false;
            e.printStackTrace();

        }

        for(Application app:  applications){
            Log.d("ParseApplications", " ***************");
            Log.d("ParseApplications", " Name " + app.getName());
            Log.d("ParseApplications", " Artist " + app.getArtist());
            Log.d("ParseApplications", " Release Date " + app.getReleaseDate());


        }






        return true;
    }
}

