package cn.xisun.spring.cloud;

import java.time.LocalDate;

/**
 * @author WangDesong
 * @version 1.0
 * @date 2023/3/7 19:40
 * @description
 */
public class Main {
    public static final String START_TIME_ENDING = " 00:00:00";

    public static void main(String[] args) {
        LocalDate today = LocalDate.now();

        String startTime = today.toString().concat(START_TIME_ENDING);
        System.out.println(startTime);
        String endTime = today.plusDays(1).toString().concat(START_TIME_ENDING);
        System.out.println(endTime);
    }
}
