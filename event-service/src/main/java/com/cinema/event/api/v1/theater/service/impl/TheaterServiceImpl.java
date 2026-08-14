package com.cinema.event.api.v1.theater.service.impl;

import com.cinema.event.api.v1.theater.dto.TheaterRequest;
import com.cinema.event.api.v1.theater.dto.TheaterResponse;
import com.cinema.event.api.v1.theater.entities.Theater;
import com.cinema.event.api.v1.theater.exceptions.TheaterNotFoundException;
import com.cinema.event.api.v1.theater.mapper.TheaterMapper;
import com.cinema.event.api.v1.theater.repository.TheaterRepository;
import com.cinema.event.api.v1.theater.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TheaterServiceImpl implements TheaterService {

    private final TheaterRepository theaterRepository;
    private final TheaterMapper theaterMapper;

    @Override
    @Transactional
    public TheaterResponse create(TheaterRequest request) {
        Theater theater = theaterMapper.toEntity(request);
        theater = theaterRepository.save(theater);
        return theaterMapper.toResponse(theater);
    }

    @Override
    public List<TheaterResponse> getAll(String city) {
        List<Theater> theaters = city == null
                ? theaterRepository.findAll()
                : theaterRepository.findByCityIgnoreCaseOrderByNameAsc(city);
        return theaters.stream().map(theaterMapper::toResponse).toList();
    }

    @Override
    public TheaterResponse getById(Long theaterId) {
        return theaterMapper.toResponse(findById(theaterId));
    }

    private Theater findById(Long theaterId) {
        return theaterRepository.findById(theaterId)
                .orElseThrow(() -> new TheaterNotFoundException(theaterId));
    }
}
