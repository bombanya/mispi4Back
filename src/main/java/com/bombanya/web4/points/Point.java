package com.bombanya.web4.points;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Entity
@Data
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double x;
    private Double y;
    private Double r;
    private String username;
    private String timeOfRequest;
    private String workTime;
    private String result;
    private boolean boolResult;

    public Point checkHit(){
        long start = System.nanoTime();

        timeOfRequest = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)
                .withLocale(new Locale("ru", "RU"))
                .format(LocalDateTime.now());

        if ((x >= 0 && y >= 0 && y <= -1/2.0 * x + r / 2) ||
                (x >= 0 && y <= 0 && x <= r && y >= -r) ||
                (x <= 0 && y >= 0 && x*x + y*y <= r*r / 4)) {
            result = "The point hit the area";
            boolResult = true;
        } else {
            result = "The point didn't hit the area";
            boolResult = false;
        }



        double doubleWorkTime = ((double) (System.nanoTime() - start)) / Math.pow(10, 9);
        workTime = String.format("%.2g", doubleWorkTime);

        return this;
    }
}
