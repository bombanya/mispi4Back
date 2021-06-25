package com.bombanya.web4.points;

import com.bombanya.web4.mbeans.AvrgTimeBetweenCLicksCounter;
import com.bombanya.web4.mbeans.PointCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final PointsRepository repository;
    private final PointCounter pointCounter;
    private final AvrgTimeBetweenCLicksCounter timeBetweenCLicksCounter;

    @Autowired
    public ApiController(PointsRepository repository, PointCounter pointCounter, AvrgTimeBetweenCLicksCounter timeBetweenCLicksCounter) {
        this.repository = repository;
        this.pointCounter = pointCounter;
        this.timeBetweenCLicksCounter = timeBetweenCLicksCounter;
    }

    @GetMapping(value = "/allpoints/{username}")
    ResponseEntity<List<Point>> getUserPoints(@PathVariable String username, Principal principal){
        if (!principal.getName().equals(username)){
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(repository.findAllByUsername(username), HttpStatus.OK);
    }

    @PostMapping(value = "/newpoint")
    Point createNewPoint(@RequestBody Point newPoint, Principal principal){
        newPoint.setUsername(principal.getName());
        repository.save(newPoint.checkHit());

        pointCounter.incrTotal(principal.getName());
        if (!newPoint.isBoolResult()) pointCounter.incrOutOfArea(principal.getName());

        timeBetweenCLicksCounter.addPoint(principal.getName());

        return newPoint;
    }
}
