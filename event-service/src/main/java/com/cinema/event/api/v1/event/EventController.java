package com.cinema.event.api.v1.event;

import com.cinema.event.api.v1.common.dto.ApiResponse;
import com.cinema.event.api.v1.event.dto.EventRequest;
import com.cinema.event.api.v1.event.dto.EventResponse;
import com.cinema.event.api.v1.event.entities.EventType;
import com.cinema.event.api.v1.event.service.EventService;
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
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> create(@Valid @RequestBody EventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success("Event created successfully", eventService.create(request))
                );
    }

    @GetMapping
    public ApiResponse<List<EventResponse>> getAll(@RequestParam(required = false) EventType type) {
        return ApiResponse.success("Events fetched successfully", eventService.getAll(type));
    }

    @GetMapping("/{eventId}")
    public ApiResponse<EventResponse> getById(@PathVariable Long eventId) {
        return ApiResponse.success("Event fetched successfully", eventService.getById(eventId));
    }
}
