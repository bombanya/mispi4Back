package com.bombanya.web4.mbeans;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ManagedResource
public class PointCounter implements NotificationPublisherAware {

    private final ConcurrentHashMap<String, UserPoints> userToPoints = new ConcurrentHashMap<>();
    private NotificationPublisher notificationPublisher;
    private final AtomicLong totalPointsAllUsers = new AtomicLong();
    private final AtomicLong pointOutOfAreaAllUsers = new AtomicLong();

    @ManagedOperation
    public void incrTotal(String username){
        if (!userToPoints.containsKey(username)) userToPoints.put(username, new UserPoints());
        long currentUserPoints = userToPoints.get(username).incrTotal();
        if (currentUserPoints % 15 == 0){
            notificationPublisher.sendNotification(new Notification("The number of user points is a multiple of 15",
                    this, userToPoints.get(username).getSequenceNumber(),
                    "User " + username + " hit " + currentUserPoints + " points"));
        }

        totalPointsAllUsers.incrementAndGet();
    }

    @ManagedOperation
    public void incrOutOfArea(String username){
        if (!userToPoints.containsKey(username)) userToPoints.put(username, new UserPoints());
        userToPoints.get(username).incrOutOfArea();

        pointOutOfAreaAllUsers.incrementAndGet();
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

    @ManagedAttribute
    public long getTotalPointsAllUsers() {
        return totalPointsAllUsers.get();
    }

    @ManagedAttribute
    public long getPointOutOfAreaAllUsers() {
        return pointOutOfAreaAllUsers.get();
    }

    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    private static class UserPoints{
        private final AtomicLong totalPoints = new AtomicLong();
        private final AtomicLong pointOutOfArea = new AtomicLong();
        private final AtomicLong sequenceNumber = new AtomicLong();

        public long incrTotal(){
            return totalPoints.incrementAndGet();
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

        public long getSequenceNumber(){
            return sequenceNumber.incrementAndGet();
        }
    }
}
