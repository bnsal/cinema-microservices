package com.cinema.event.api.v1.theater;

import com.cinema.event.api.v1.common.dto.ApiResponse;
import com.cinema.event.api.v1.theater.dto.TheaterRequest;
import com.cinema.event.api.v1.theater.dto.TheaterResponse;
import com.cinema.event.api.v1.theater.service.TheaterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @PostMapping
    public ResponseEntity<ApiResponse<TheaterResponse>> create(@Valid @RequestBody TheaterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Theater created successfully", theaterService.create(request))
                );
    }

    @GetMapping
    public ApiResponse<List<TheaterResponse>> getAll(@RequestParam(required = false) String city) {
        return ApiResponse.success("Theaters fetched successfully", theaterService.getAll(city));
    }

    @GetMapping("/{theaterId}")
    public ApiResponse<TheaterResponse> getById(@PathVariable Long theaterId) {
        return ApiResponse.success("Theater fetched successfully", theaterService.getById(theaterId));
    }
}
