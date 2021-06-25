package com.bombanya.web4.mbeans;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ManagedResource
public class AvrgTimeBetweenCLicksCounter {

    private final ConcurrentHashMap<String, UserHitTimeData> userToPoints = new ConcurrentHashMap<>();

    @ManagedOperation
    public void addPoint(String username){
        if (!userToPoints.containsKey(username)) userToPoints.put(username, new UserHitTimeData());
        userToPoints.get(username).incrTotalPoints();
    }

    @ManagedOperation
    public long getAverageTime(String username){
        if (!userToPoints.containsKey(username)) return -1;
        return userToPoints.get(username).getAverageTime();
    }

    private static class UserHitTimeData {
        private final AtomicLong totalPoints = new AtomicLong();
        private final long firstClickTime = System.nanoTime();
        private final AtomicLong lastClickTime = new AtomicLong();

        public void incrTotalPoints(){
            totalPoints.incrementAndGet();
            lastClickTime.set(System.nanoTime());
        }

        public long getAverageTime(){
            return (long) (((double)(lastClickTime.get() - firstClickTime)) / totalPoints.get() / Math.pow(10, 9));
        }
    }
}
