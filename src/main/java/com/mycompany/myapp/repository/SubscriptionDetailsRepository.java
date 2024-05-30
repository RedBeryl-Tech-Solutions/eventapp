
package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SubscriptionDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;

import java.math.BigInteger;
import java.util.List;
/**
 * Spring Data JPA repository for the SubscriptionDetails entity.
 */
@Repository
public interface SubscriptionDetailsRepository extends JpaRepository<SubscriptionDetails, Long> {

    //@Query("SELECT sd, DATEDIFF(sd.subscriptionExpiryDate, :currentDate) AS daysUntilExpiry FROM SubscriptionDetails sd")
//List<SubscriptionDetails> findExpiringSubscriptionsWithDays(@Param("currentDate") LocalDate currentDate);
//@Query(value = "SELECT sd.*, DATEDIFF(sd.subscription_expiry_date, CURDATE()) AS days_until_expiry " +
//    "FROM subscription_details sd", nativeQuery = true)
    @Query(value = "select * from subscription_details\n" +
        "where  subscription_expiry_date > CURRENT_DATE\n" +
        "and date(subscription_expiry_date) - CURRENT_DATE < notification_before_expiry\n" +
        "and notification_mute_flag = false", nativeQuery = true)
    List<SubscriptionDetails> findExpiringSubscriptionsWithDays();


    @Query("SELECT sd FROM SubscriptionDetails sd LEFT JOIN FETCH sd.category")
    List<SubscriptionDetails> findAll1();


}



