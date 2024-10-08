
import java.util.ArrayList;

public class metroTimestamp {
    private String stnName, stnLine;
    private ArrayList<train> timeStampList_UP = new ArrayList<train>();
    private ArrayList<train> timeStampList_DOWN = new ArrayList<train>();

    public metroTimestamp(String stnName, String stnLine){
        this.stnName = stnName;
        this.stnLine = stnLine;
    }

    public void addTimestamp_UP(String tID, String dest, String nowstn, String arTime, String target, String Direction){ //상행열차 시간 추가
        timeStampList_UP.add(new train(tID, dest, nowstn, arTime, target, Direction));
    }

    public void addTimestamp_DOWN(String tID, String dest, String nowstn, String arTime, String target, String Direction){ //하행열차 시간 추가
        timeStampList_DOWN.add(new train(tID, dest, nowstn, arTime, target, Direction));
    }

    public String getStnName(){
        return this.stnName;
    }

    public String getStnLine(){
        return this.stnLine;
    }

    public void showUPStamp(){
        if(timeStampList_UP.size() == 0){
            System.out.println("현재 " + this.stnName + "역의 상행열차 정보가 없습니다.");
        } else {
            for(train t : timeStampList_UP){
                t.showTrain();
            }
            System.out.println("------------------------------\n");
        }
    }

    public void showDOWNStamp(){
        if(timeStampList_DOWN.size() == 0){
            System.out.println("현재 " + this.stnName + "역의 하행열차 정보가 없습니다.");
        } else {
            for (train t : timeStampList_DOWN) {
                t.showTrain();
            }
            System.out.println("------------------------------\n");
        }
    }
}
