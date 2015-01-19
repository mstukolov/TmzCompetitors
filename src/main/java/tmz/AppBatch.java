package tmz;

import org.springframework.boot.SpringApplication;

import java.io.IOException;

/**
 * Created by stukolov_m on 16.01.2015.
 */
public class AppBatch {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(ScheduledTasks.class, args);

    }
}
