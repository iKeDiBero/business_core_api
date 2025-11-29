package com.kedibero.business_core_api.repository;

import com.kedibero.business_core_api.entity.SupportTicket;
import com.kedibero.business_core_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    
    List<SupportTicket> findByUserOrderByCreatedAtDesc(User user);
    
    List<SupportTicket> findByUserAndStatusOrderByCreatedAtDesc(User user, String status);
    
    @Query("SELECT t FROM SupportTicket t WHERE t.status = 'pending' AND t.scheduledResolutionAt <= :now")
    List<SupportTicket> findTicketsToResolve(@Param("now") LocalDateTime now);
    
    @Query("SELECT t FROM SupportTicket t WHERE t.user = :user AND t.product.id = :productId ORDER BY t.createdAt DESC")
    List<SupportTicket> findByUserAndProductId(@Param("user") User user, @Param("productId") Long productId);
}
