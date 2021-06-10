package com.bombanya.web4.mispi;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ManagedResource
public class PointCounter {

    private final ConcurrentHashMap<String, UserPoints> userToPoints = new ConcurrentHashMap<>();

    @ManagedOperation
    public void incrTotal(String username){
        if (!userToPoints.containsKey(username)) userToPoints.put(username, new UserPoints());
        userToPoints.get(username).incrTotal();
    }

    @ManagedOperation
    public void incrOutOfArea(String username){
        if (!userToPoints.containsKey(username)) userToPoints.put(username, new UserPoints());
        userToPoints.get(username).incrOutOfArea();
    }

    @ManagedOperation
    public long getTotalPoints(String username) {
        if (!userToPoints.containsKey(username)) return -1;
        return userToPoints.get(username).getTotalPoints();
    }

    @ManagedOperation
    public long getPointOutOfArea(String username) {
        if (!userToPoints.containsKey(username)) return -1;
        return userToPoints.get(username).getPointOutOfArea();
    }


    private static class UserPoints{
        private final AtomicLong totalPoints = new AtomicLong();
        private final AtomicLong pointOutOfArea = new AtomicLong();

        public void incrTotal(){
            totalPoints.incrementAndGet();
        }

        public void incrOutOfArea(){
            pointOutOfArea.incrementAndGet();
        }

        public long getTotalPoints() {
            return totalPoints.get();
        }

        public long getPointOutOfArea() {
            return pointOutOfArea.get();
        }
    }
}
