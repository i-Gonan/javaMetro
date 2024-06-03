public class train {
    private String destination, nowLocation, arrivalTime, target_Stn, direction, trainID;

    public train(String tID, String dest, String nowstn, String arTime, String target, String Dir){
        this.trainID = tID;
        this.destination = dest;
        this.nowLocation = nowstn;
        this.arrivalTime = arTime;
        this.target_Stn = target;
        this.direction = Dir;
    }

    public void showTrain(){
        System.out.println("------------------------------");
        System.out.println("열차번호 : " + (this.trainID.length() == 2 ? ("00" + this.trainID) : (this.trainID.length() == 3) ? ("0" + this.trainID) : this.trainID) + "호");
        System.out.println("종 착 역 : " + this.destination + "역(" + this.direction + ")");
        System.out.println("현재위치 : " + this.nowLocation + "역");
        System.out.println("도착시간 : " + (this.nowLocation.equals(this.target_Stn) ? "열차가 이미 도착했습니다" : arrivalTime));
        System.out.println("------------------------------");
    }
}
