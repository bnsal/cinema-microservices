package com.cinema.event.api.v1.seat.service.impl;

import com.cinema.event.api.v1.screen.entities.Screen;
import com.cinema.event.api.v1.screen.exceptions.ScreenNotFoundException;
import com.cinema.event.api.v1.screen.repository.ScreenRepository;
import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.seat.entities.Seat;
import com.cinema.event.api.v1.seat.exceptions.SeatsAlreadyGeneratedException;
import com.cinema.event.api.v1.seat.mapper.SeatMapper;
import com.cinema.event.api.v1.seat.repository.SeatRepository;
import com.cinema.event.api.v1.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeatServiceImpl implements SeatService {

    private static final char FIRST_ROW_LABEL = 'A';

    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;


    @Override
    @Transactional
    public List<SeatResponse> generate(Long screenId) {
        Screen screen = findScreen(screenId);
        if (seatRepository.existsByScreenId(screenId)) {
            throw new SeatsAlreadyGeneratedException(screenId);
        }

        List<Seat> seats = new ArrayList<>();
        for (int row = 0; row < screen.getTotalRows(); row++) {
            for (int seatNumber = 1; seatNumber <= screen.getSeatsPerRow(); seatNumber++) {

                Seat seat = Seat.builder()
                        .screen(screen)
                        .rowLabel(String.valueOf((char) (FIRST_ROW_LABEL + row)))
                        .seatNumber(seatNumber)
                        .build();
                seats.add(seat);
            }
        }

        return seatRepository.saveAll(seats)
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public List<SeatResponse> getAll(Long screenId) {
        findScreen(screenId);
        return seatRepository.findByScreenIdOrderByRowLabelAscSeatNumberAsc(screenId).stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    private Screen findScreen(Long screenId) {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(screenId));
    }
}
