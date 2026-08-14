package com.cinema.booking.api.v1.reservation;

import com.cinema.booking.api.v1.common.dto.ApiResponse;
import com.cinema.booking.api.v1.reservation.dto.ReservationRequest;
import com.cinema.booking.api.v1.reservation.dto.ReservationResponse;
import com.cinema.booking.api.v1.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(
            @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Seats reserved successfully", reservationService.reserve(request))
                );
    }

    @GetMapping("/{reservationId}")
    public ApiResponse<ReservationResponse> getById(@PathVariable String reservationId) {
        return ApiResponse.success("Reservation fetched successfully", reservationService.getById(reservationId));
    }
}
