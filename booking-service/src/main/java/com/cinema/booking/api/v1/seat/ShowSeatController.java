package com.cinema.booking.api.v1.seat;

import com.cinema.booking.api.v1.common.dto.ApiResponse;
import com.cinema.booking.api.v1.seat.dto.ShowSeatResponse;
import com.cinema.booking.api.v1.seat.service.ShowSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shows/{showId}/seats")
@RequiredArgsConstructor
public class ShowSeatController {

    private final ShowSeatService showSeatService;

    @GetMapping
    public ApiResponse<List<ShowSeatResponse>> getShowSeats(@PathVariable Long showId) {
        return ApiResponse.success("Show seats fetched successfully", showSeatService.getShowSeats(showId));
    }
}
