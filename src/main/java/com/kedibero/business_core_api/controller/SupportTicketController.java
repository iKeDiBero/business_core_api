package com.kedibero.business_core_api.controller;

import com.kedibero.business_core_api.dto.ApiResponse;
import com.kedibero.business_core_api.dto.TicketRequest;
import com.kedibero.business_core_api.dto.TicketResponse;
import com.kedibero.business_core_api.service.SupportTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/support")
public class SupportTicketController {
    
    @Autowired
    private SupportTicketService ticketService;
    
    @PostMapping("/tickets")
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@Valid @RequestBody TicketRequest request) {
        TicketResponse ticket = ticketService.createTicket(request);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }
    
    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getMyTickets(
            @RequestParam(required = false) String status) {
        List<TicketResponse> tickets;
        if (status != null && !status.isEmpty()) {
            tickets = ticketService.getMyTicketsByStatus(status);
        } else {
            tickets = ticketService.getMyTickets();
        }
        return ResponseEntity.ok(ApiResponse.success(tickets));
    }
    
    @GetMapping("/tickets/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }
}
