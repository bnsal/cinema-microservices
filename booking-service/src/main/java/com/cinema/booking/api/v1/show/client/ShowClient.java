package com.cinema.booking.api.v1.show.client;

import com.cinema.booking.api.v1.common.dto.ApiResponse;
import com.cinema.booking.api.v1.show.client.dto.MovieShowClientResponse;
import com.cinema.booking.api.v1.show.client.dto.SeatClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "eventService", url = "${services.event.url}")
public interface ShowClient {

    @GetMapping("/api/v1/movie-shows/{showId}")
    ApiResponse<MovieShowClientResponse> getShowById(@PathVariable Long showId);

    @GetMapping("/api/v1/movie-shows/{showId}/seats")
    ApiResponse<List<SeatClientResponse>> getShowSeats(@PathVariable Long showId);
}
