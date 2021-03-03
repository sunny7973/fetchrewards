package com.exam.fetchrewards.points.service;

import com.exam.fetchrewards.points.model.UserRewards;
import com.exam.fetchrewards.points.repo.UserRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@Log4j2
public class PointService {

    @Autowired
    UserRepo repo;

    public int calculateTotalPoints(){
        return repo.userPoints();
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
        List<UserRewards> listToDeduct = new ArrayList<>();

        if (pointToRedeem > repo.userPoints()) {
            System.out.println("not enough points");
        } else {
            List<UserRewards> rewardsEntries = repo.findAll();
            LinkedHashMap<String, Integer> lhm = new LinkedHashMap();

            for(UserRewards rewTemp : rewardsEntries){
                SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
                String uniqueKey = smf.format(rewTemp.getTime());
                uniqueKey = rewTemp.getPayerName()+uniqueKey;
                if(lhm.containsKey(uniqueKey)){
                    lhm.put(uniqueKey, lhm.get(uniqueKey)+rewTemp.getPoints());
                }else {
                    lhm.put(uniqueKey, rewTemp.getPoints());
                }
            }


            for (UserRewards rewards : rewardsEntries) {
                int points = rewards.getPoints();

                if (points < 0) {
                    pointToRedeem = pointToRedeem - points;

                    listToDeduct.add(rewards);
                    for (UserRewards returnReward : returnResponse) {
                        if (returnReward.getPayerName().equalsIgnoreCase(rewards.getPayerName())) {
                            returnReward.setPointsredeemed(returnReward.getPointsredeemed() + Math.abs(points));
                        }
                    }
                } else pointToRedeem = PointServiceV2.tallyEntity(pointToRedeem, returnResponse, rewards, points, repo);
                if (pointToRedeem == 0) {
                    break;
                }
            }

        }

        System.out.println("returnResponse:" + returnResponse.toString());
        return returnResponse;
    }
}
