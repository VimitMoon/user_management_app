package in.vi.user_management_app.utils;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // load data into country, state and cities tables
        // runner is used to perform the operation only once at the start of the project
        // perform the application logic only once


    }
}
