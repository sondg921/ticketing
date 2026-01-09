package com.example.ticketing.controller;

import com.example.ticketing.service.TicketingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TicketingController {
    private final TicketingService ticketingService;

    @PostMapping("/ticketing")
    public Long issueTicket(@RequestBody TicketingRequest request){
        return ticketingService.issueTicket(request.getUserId(), request.getConcertId());
    }

    @Data
    static class TicketingRequest{
        private Long userId;
        private Long concertId;
    }
}
