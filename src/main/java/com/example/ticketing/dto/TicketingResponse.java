package com.example.ticketing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketingResponse {
    private Long ticketId;
    private Long rank;
}
