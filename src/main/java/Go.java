import service.Job;


public class Go {
    public static void main(String[] args) throws InterruptedException {
        Job job = new Job();
        while (true){
            job.job();
            Thread.sleep(600000);
        }
    }
}
