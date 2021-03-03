package com.exam.fetchrewards.points.service;

import com.exam.fetchrewards.points.model.UserRewards;
import com.exam.fetchrewards.points.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class PointServiceV2 {

    @Autowired
    UserRepo repo;

    public Map calculateBalancePoints(){
        return repo.findAll().stream().collect(Collectors.groupingBy(UserRewards::getPayerName, Collectors.summingInt(UserRewards::getBalancepoints)));

    }

    public List<UserRewards> getUserBalance() {
        List<UserRewards> rewardsEntries = repo.findAll();
        return rewardsEntries;
    }

    public boolean addPoints(UserRewards user) {
        UserRewards userRewards = repo.save(user);
        return userRewards != null;
    }

    public List<UserRewards> deductPoints(Integer pointToRedeem) {
        List<UserRewards> returnResponse = new ArrayList<>();

        if (pointToRedeem > repo.userPoints()) {
            System.out.println("not enough points");
        } else {

            List<UserRewards> rewardsEntries = repo.findAll().stream().filter(f->f.getBalancepoints() != 0).collect(Collectors.toList());

            for (UserRewards rewards : rewardsEntries) {

                if(!rewards.isAllPointsConsumed()){

                    int pointsPerPartner = rewards.getPoints() + rewards.getPointsredeemed();

                    if (pointsPerPartner < 0) {
                        pointToRedeem = pointToRedeem - pointsPerPartner;

                        for (UserRewards returnReward : returnResponse) {
                            if (returnReward.getPayerName().equalsIgnoreCase(rewards.getPayerName())) {
                                returnReward.setPointsredeemed(returnReward.getPointsredeemed() + Math.abs(pointsPerPartner));
                                rewards.setAllPointsConsumed(true);
                                repo.save(rewards);
                                break;
                            }
                        }
                    } else {
                        pointToRedeem = tallyEntity(pointToRedeem, returnResponse, rewards, pointsPerPartner, repo);
                    }
                    if (pointToRedeem == 0) {
                        break;
                    }
                }

            }
        }

        System.out.println("returnResponse:" + returnResponse.toString());
        for(UserRewards test : returnResponse){
            test.setTime(new Date());
        }
        return returnResponse;
    }

    static Integer tallyEntity(Integer pointToRedeem, List<UserRewards> returnResponse, UserRewards rewards, int pointsPerPartner, UserRepo repo) {
        if (pointToRedeem < pointsPerPartner) {
            pointsPerPartner = pointsPerPartner - pointToRedeem;
            rewards.setPointsredeemed(rewards.getPointsredeemed() + pointToRedeem * -1);
            UserRewards cloneObj = rewards.clone();
            cloneObj.setPointsredeemed(pointToRedeem * -1);
            returnResponse.add(cloneObj);
            if(rewards.getPoints() + rewards.getPointsredeemed() == 0){
                rewards.setAllPointsConsumed(true);
            }
            repo.save(rewards);
            pointToRedeem = 0;
        } else {
            pointToRedeem = pointToRedeem - pointsPerPartner;
            rewards.setPointsredeemed(rewards.getPointsredeemed() + pointsPerPartner * -1);
            UserRewards cloneObj = rewards.clone();
            cloneObj.setPointsredeemed(pointsPerPartner * -1);
            returnResponse.add(cloneObj);
            if(rewards.getPoints() + rewards.getPointsredeemed() == 0){
                rewards.setAllPointsConsumed(true);
            }
            repo.save(rewards);
        }
        return pointToRedeem;
    }
}
