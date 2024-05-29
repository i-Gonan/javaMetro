public class train {
    private String destination, nowLocation;
    private int trainID, arrivalTime;
    private String target_Stn;

    public train(int tID, String dest, String nowstn, int arTime, String target){
        this.trainID = tID;
        this.destination = dest;
        this.nowLocation = nowstn;
        this.arrivalTime = arTime;
        this.target_Stn = target;
    }

    public void showTrain(){
        System.out.println("------------------------------");
        System.out.println("열차번호 : " + this.trainID);
        System.out.println("종 착 역 : " + this.destination + "역");
        System.out.println("현재위치 : " + this.nowLocation + "역");
        System.out.println("도착시간 : " + (this.nowLocation.equals(this.target_Stn) ? "열차가 이미 도착했습니다" : (arrivalTime / 60) + "분 " + (arrivalTime % 60) + "초 후 도착 예정"));
        System.out.println("------------------------------");
    }
}
