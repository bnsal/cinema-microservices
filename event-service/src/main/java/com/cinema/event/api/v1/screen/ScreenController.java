package com.cinema.event.api.v1.screen;

import com.cinema.event.api.v1.common.dto.ApiResponse;
import com.cinema.event.api.v1.screen.dto.ScreenRequest;
import com.cinema.event.api.v1.screen.dto.ScreenResponse;
import com.cinema.event.api.v1.screen.service.ScreenService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/theaters/{theaterId}/screens")
@RequiredArgsConstructor
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping
    public ResponseEntity<ApiResponse<ScreenResponse>> create(
            @PathVariable Long theaterId,
            @Valid @RequestBody ScreenRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Screen created successfully", screenService.create(theaterId, request))
                );
    }

    @GetMapping
    public ApiResponse<List<ScreenResponse>> getAll(@PathVariable Long theaterId) {
        return ApiResponse.success("Screens fetched successfully", screenService.getAll(theaterId));
    }

    @GetMapping("/{screenId}")
    public ApiResponse<ScreenResponse> getById(@PathVariable Long theaterId, @PathVariable Long screenId) {
        return ApiResponse.success("Screen fetched successfully", screenService.getById(theaterId, screenId));
    }
}
