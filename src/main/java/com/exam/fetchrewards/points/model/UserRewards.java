package com.exam.fetchrewards.points.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity(name="user")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Data
public class UserRewards implements  Cloneable{

    @Id
    @Column(updatable = false)
    @SequenceGenerator(name="user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @JsonIgnore
    private Long id;
    @JsonProperty("payer_name")
    private String payerName;
    private Integer points;
    @JsonIgnore
    private Integer pointsredeemed = 0 ;
    @JsonFormat(pattern="MM/dd HHa", timezone = "CST")
    @Column(columnDefinition = "DATETIME")
    private Date time;
    @JsonIgnore
    @Formula(value = "points + pointsredeemed")
    private Integer balancepoints =0;
    @JsonIgnore
    boolean allPointsConsumed = false;



    public UserRewards(String payerName, int points, Date time) {
        this.payerName = payerName;
        this.points = points;
        this.time = time;
        this.pointsredeemed = 0;
        this.balancepoints = 0;
        this.allPointsConsumed = false;
    }

    @Override
    public String toString() {
        return "[" + payerName   +
               ", " + pointsredeemed +
                ", now]"+System.lineSeparator();
    }

    @Override
    public UserRewards clone() {
        try {
            return (UserRewards) super.clone();
        } catch (CloneNotSupportedException e) {
            return new UserRewards(this.payerName, this.points, this.time);
        }
    }

}
