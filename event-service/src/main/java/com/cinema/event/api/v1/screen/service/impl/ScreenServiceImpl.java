package com.cinema.event.api.v1.screen.service.impl;

import com.cinema.event.api.v1.screen.dto.ScreenRequest;
import com.cinema.event.api.v1.screen.dto.ScreenResponse;
import com.cinema.event.api.v1.screen.entities.Screen;
import com.cinema.event.api.v1.screen.exceptions.ScreenAlreadyExistsException;
import com.cinema.event.api.v1.screen.exceptions.ScreenNotFoundException;
import com.cinema.event.api.v1.screen.mapper.ScreenMapper;
import com.cinema.event.api.v1.screen.repository.ScreenRepository;
import com.cinema.event.api.v1.screen.service.ScreenService;
import com.cinema.event.api.v1.theater.entities.Theater;
import com.cinema.event.api.v1.theater.exceptions.TheaterNotFoundException;
import com.cinema.event.api.v1.theater.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScreenServiceImpl implements ScreenService {

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;
    private final ScreenMapper screenMapper;

    @Override
    @Transactional
    public ScreenResponse create(Long theaterId, ScreenRequest request) {
        Theater theater = findTheater(theaterId);
        if (screenRepository.existsByTheaterIdAndNameIgnoreCase(theaterId, request.name())) {
            throw new ScreenAlreadyExistsException(theaterId, request.name());
        }

        Screen screen = screenMapper.toEntity(request, theater);
        screen = screenRepository.save(screen);
        return screenMapper.toResponse(screen);
    }

    @Override
    public List<ScreenResponse> getAll(Long theaterId) {
        findTheater(theaterId);
        return screenRepository.findByTheaterIdOrderByNameAsc(theaterId).stream()
                .map(screenMapper::toResponse)
                .toList();
    }

    @Override
    public ScreenResponse getById(Long theaterId, Long screenId) {
        Screen screen = screenRepository.findByTheaterIdAndId(theaterId, screenId)
                .orElseThrow(
                        () -> new ScreenNotFoundException(screenId)
                );
        return screenMapper.toResponse(screen);
    }

    private Theater findTheater(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new TheaterNotFoundException(theaterId));
    }
}
