package com.cinema.event.api.v1.show;

import com.cinema.event.api.v1.common.dto.ApiResponse;
import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.show.dto.MovieShowRequest;
import com.cinema.event.api.v1.show.dto.MovieShowResponse;
import com.cinema.event.api.v1.show.service.MovieShowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movie-shows")
@RequiredArgsConstructor
public class MovieShowController {

    private final MovieShowService movieShowService;

    @PostMapping
    public ResponseEntity<ApiResponse<MovieShowResponse>> create(@Valid @RequestBody MovieShowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Movie show created successfully", movieShowService.create(request))
                );
    }

    @GetMapping
    public ApiResponse<List<MovieShowResponse>> getAll(
            @RequestParam Long eventId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.success("Movie shows fetched successfully", movieShowService.getAll(eventId, date));
    }

    @GetMapping("/{showId}")
    public ApiResponse<MovieShowResponse> getById(@PathVariable Long showId) {
        return ApiResponse.success("Movie show fetched successfully", movieShowService.getById(showId));
    }

    @GetMapping("/{showId}/seats")
    public ApiResponse<List<SeatResponse>> getSeats(@PathVariable Long showId) {
        return ApiResponse.success("Show seats fetched successfully", movieShowService.getSeats(showId));
    }
}
