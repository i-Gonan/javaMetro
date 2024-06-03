public class train {
    private String destination, nowLocation, arrivalTime, target_Stn, direction, trainCompany, trainTel;
    private int trainID;

    public train(int tID, String dest, String nowstn, String arTime, String target, String Dir){
        this.trainID = tID;
        this.destination = dest;
        this.nowLocation = nowstn;
        this.arrivalTime = arTime;
        this.target_Stn = target;
        this.direction = Dir;
        trainco();
    }

    private void trainco(){
        if(this.arrivalTime.contains("[")){
            this.trainCompany = "한국철도공사(Korail)";
            this.trainTel = "1544-7788";
        }
        else{
            this.trainCompany = "서울교통공사(SeoulMetro)";
            this.trainTel = "1577-1234";
        }
    }

    public void showTrain(){
        System.out.println("------------------------------");
        System.out.println("열차번호 : " + this.trainID + "호");
        System.out.println("종 착 역 : " + this.destination + "역(" + this.direction + ")");
        System.out.println("현재위치 : " + this.nowLocation + "역");
        System.out.println("도착시간 : " + (this.nowLocation.equals(this.target_Stn) ? "열차가 이미 도착했습니다" : arrivalTime));
        System.out.println("이 열차는 " + this.trainCompany + " 차량입니다. 유사시 연락처는 " + this.trainTel + " 입니다.");
        System.out.println("------------------------------");
    }
}
