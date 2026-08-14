package com.cinema.event.api.v1.seat;

import com.cinema.event.api.v1.common.dto.ApiResponse;
import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/screens/{screenId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @PostMapping
    public ResponseEntity<ApiResponse<List<SeatResponse>>> generate(@PathVariable Long screenId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Seats generated successfully", seatService.generate(screenId))
                );
    }

    @GetMapping
    public ApiResponse<List<SeatResponse>> getAll(@PathVariable Long screenId) {
        return ApiResponse.success("Seats fetched successfully", seatService.getAll(screenId));
    }
}
