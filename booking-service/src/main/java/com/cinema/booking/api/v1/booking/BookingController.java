package com.cinema.booking.api.v1.booking;

import com.cinema.booking.api.v1.booking.dto.BookingRequest;
import com.cinema.booking.api.v1.booking.dto.BookingResponse;
import com.cinema.booking.api.v1.booking.service.BookingService;
import com.cinema.booking.api.v1.common.dto.ApiResponse;
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
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created successfully", bookingService.create(request)));
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingResponse> getById(@PathVariable Long bookingId) {
        return ApiResponse.success("Booking fetched successfully", bookingService.getById(bookingId));
    }

    @GetMapping
    public ApiResponse<List<BookingResponse>> getAllByUser(@RequestParam Long userId) {
        return ApiResponse.success("Bookings fetched successfully", bookingService.getAllByUser(userId));
    }
}
