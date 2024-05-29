import java.util.ArrayList;

public class metroTimestamp {
    private String stnName, stnLine;
    private ArrayList<train> timeStampList_UP = new ArrayList<train>();
    private ArrayList<train> timeStampList_DOWN = new ArrayList<train>();

    public metroTimestamp(String stnName, String stnLine){
        this.stnName = stnName;
        this.stnLine = stnLine;
    }

    public void addTimestamp_UP(int tID, String dest, String nowstn, String arTime, String target){ //상행열차 시간 추가
        timeStampList_UP.add(new train(tID, dest, nowstn, arTime, target));
    }

    public void addTimestamp_DOWN(int tID, String dest, String nowstn, String arTime, String target){ //하행열차 시간 추가
        timeStampList_DOWN.add(new train(tID, dest, nowstn, arTime, target));
    }

    public String getStnName(){
        return this.stnName;
    }

    public String getStnLine(){
        return this.stnLine;
    }

    public void showUPStamp(){
        for(train t : timeStampList_UP){
            t.showTrain();
        }
    }

    public void showDOWNStamp(){
        for(train t : timeStampList_DOWN){
            t.showTrain();
        }
    }
}
