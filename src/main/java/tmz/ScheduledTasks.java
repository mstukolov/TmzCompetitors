package tmz;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
public class ScheduledTasks {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedDelay = 1800000000)
    public void reportCurrentTime() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
        String[] arguments = new String[] {"123"};
        try {
            App.main(arguments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
