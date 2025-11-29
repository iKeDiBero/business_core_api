package com.kedibero.business_core_api.service;

import com.kedibero.business_core_api.dto.TicketRequest;
import com.kedibero.business_core_api.dto.TicketResponse;
import com.kedibero.business_core_api.entity.Product;
import com.kedibero.business_core_api.entity.SupportTicket;
import com.kedibero.business_core_api.entity.User;
import com.kedibero.business_core_api.repository.ProductRepository;
import com.kedibero.business_core_api.repository.SupportTicketRepository;
import com.kedibero.business_core_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupportTicketService {
    
    @Autowired
    private SupportTicketRepository ticketRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    @Transactional
    public TicketResponse createTicket(TicketRequest request) {
        User user = getCurrentUser();
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(user);
        ticket.setProduct(product);
        ticket.setSubject(request.getSubject());
        ticket.setDescription(request.getDescription());
        ticket.setStatus("pending");
        
        ticket = ticketRepository.save(ticket);
        
        return mapToResponse(ticket);
    }
    
    public List<TicketResponse> getMyTickets() {
        User user = getCurrentUser();
        // Primero verificar y resolver tickets pendientes
        resolveExpiredTickets();
        
        List<SupportTicket> tickets = ticketRepository.findByUserOrderByCreatedAtDesc(user);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    public List<TicketResponse> getMyTicketsByStatus(String status) {
        User user = getCurrentUser();
        // Primero verificar y resolver tickets pendientes
        resolveExpiredTickets();
        
        List<SupportTicket> tickets = ticketRepository.findByUserAndStatusOrderByCreatedAtDesc(user, status);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    
    public TicketResponse getTicketById(Long id) {
        User user = getCurrentUser();
        SupportTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado"));
        
        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("No tienes permiso para ver este ticket");
        }
        
        // Verificar si debe resolverse
        checkAndResolveTicket(ticket);
        
        return mapToResponse(ticket);
    }
    
    @Transactional
    public void checkAndResolveTicket(SupportTicket ticket) {
        if ("pending".equals(ticket.getStatus()) && 
            ticket.getScheduledResolutionAt() != null &&
            LocalDateTime.now().isAfter(ticket.getScheduledResolutionAt())) {
            
            ticket.setStatus("resolved");
            ticket.setResolvedAt(LocalDateTime.now());
            ticketRepository.save(ticket);
        }
    }
    
    // Tarea programada que se ejecuta cada minuto para resolver tickets
    @Scheduled(fixedRate = 60000) // cada 60 segundos
    @Transactional
    public void resolveExpiredTickets() {
        LocalDateTime now = LocalDateTime.now();
        List<SupportTicket> ticketsToResolve = ticketRepository.findTicketsToResolve(now);
        
        for (SupportTicket ticket : ticketsToResolve) {
            ticket.setStatus("resolved");
            ticket.setResolvedAt(now);
            ticketRepository.save(ticket);
        }
    }
    
    private TicketResponse mapToResponse(SupportTicket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setProductId(ticket.getProduct().getId());
        response.setProductName(ticket.getProduct().getName());
        response.setProductSku(ticket.getProduct().getSku());
        response.setSubject(ticket.getSubject());
        response.setDescription(ticket.getDescription());
        response.setStatus(ticket.getStatus());
        response.setStatusLabel(getStatusLabel(ticket.getStatus()));
        response.setCreatedAt(ticket.getCreatedAt());
        response.setResolvedAt(ticket.getResolvedAt());
        response.setScheduledResolutionAt(ticket.getScheduledResolutionAt());
        
        // Calcular segundos restantes si está pendiente
        if ("pending".equals(ticket.getStatus()) && ticket.getScheduledResolutionAt() != null) {
            long remaining = ChronoUnit.SECONDS.between(LocalDateTime.now(), ticket.getScheduledResolutionAt());
            response.setRemainingSeconds(Math.max(0, remaining));
        } else {
            response.setRemainingSeconds(0L);
        }
        
        return response;
    }
    
    private String getStatusLabel(String status) {
        switch (status) {
            case "pending":
                return "Pendiente";
            case "in_progress":
                return "En Progreso";
            case "resolved":
                return "Resuelto";
            default:
                return status;
        }
    }
}
