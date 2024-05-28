public class train {
    private String Line, destination, nowLocation, arrivalTime;
    private int trainID;

    public train(int tID, String Line, String dest, String nowstn, String arTime){
        this.trainID = tID;
        this.Line = Line;
        this.destination = dest;
        this.nowLocation = nowstn;
        this.arrivalTime = arTime;
    }

    public void showTrain(){
        System.out.println("********************");
        System.out.println("열차번호 : " + this.trainID);
        System.out.println("행 선 지 : " + this.destination);
        System.out.println("현재위치 : " + this.nowLocation);
        System.out.println("도착시간 : " + this.arrivalTime + "초 후 도착 예정");
        System.out.println("********************");
    }
}
