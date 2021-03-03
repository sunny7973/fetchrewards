package com.exam.fetchrewards.points.repo;

import com.exam.fetchrewards.points.model.UserRewards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.jws.soap.SOAPBinding;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserRewards, Long> {

    @Query(value="SELECT SUM(points) FROM user")
    int userPoints();

    /*@Query(value="SELECT payerName,points FROM user group by payerName")
    List<UserRewards> getAllEntries();*/
}
