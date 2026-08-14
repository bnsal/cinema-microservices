package com.cinema.event.api.v1.show.service.impl;

import com.cinema.event.api.v1.event.entities.Event;
import com.cinema.event.api.v1.event.entities.EventType;
import com.cinema.event.api.v1.event.exceptions.EventNotFoundException;
import com.cinema.event.api.v1.event.repository.EventRepository;
import com.cinema.event.api.v1.screen.entities.Screen;
import com.cinema.event.api.v1.screen.exceptions.ScreenNotFoundException;
import com.cinema.event.api.v1.screen.repository.ScreenRepository;
import com.cinema.event.api.v1.seat.dto.SeatResponse;
import com.cinema.event.api.v1.seat.service.SeatService;
import com.cinema.event.api.v1.show.dto.MovieShowRequest;
import com.cinema.event.api.v1.show.dto.MovieShowResponse;
import com.cinema.event.api.v1.show.entities.MovieShow;
import com.cinema.event.api.v1.show.exceptions.EventIsNotAMovieException;
import com.cinema.event.api.v1.show.exceptions.MovieShowNotFoundException;
import com.cinema.event.api.v1.show.exceptions.ShowTimeConflictException;
import com.cinema.event.api.v1.show.mapper.MovieShowMapper;
import com.cinema.event.api.v1.show.repository.MovieShowRepository;
import com.cinema.event.api.v1.show.service.MovieShowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieShowServiceImpl implements MovieShowService {

    private final MovieShowRepository movieShowRepository;
    private final EventRepository eventRepository;
    private final ScreenRepository screenRepository;
    private final MovieShowMapper movieShowMapper;
    private final SeatService seatService;

    @Override
    @Transactional
    public MovieShowResponse create(MovieShowRequest request) {
        Event event = findEvent(request.eventId());
        if (event.getType() != EventType.MOVIE) {
            throw new EventIsNotAMovieException(event.getId(), event.getType());
        }

        Screen screen = findScreen(request.screenId());
        MovieShow movieShow = movieShowMapper.toEntity(request, event, screen);
        validateNoOverlap(movieShow);

        return movieShowMapper.toResponse(movieShowRepository.save(movieShow));
    }

    private void validateNoOverlap(MovieShow newShow) {
        LocalDateTime newStart = newShow.startsAt();
        LocalDateTime newEnd = newShow.endsAt();

        boolean conflict = movieShowRepository.findByScreenIdAndShowDateBetween(
                        newShow.getScreen().getId(),
                        newShow.getShowDate().minusDays(1),
                        newShow.getShowDate().plusDays(1))
                .stream()
                .anyMatch(
                        existing ->
                        existing.startsAt().isBefore(newEnd)
                                && existing.endsAt().isAfter(newStart)
                );

        if (conflict) {
            throw new ShowTimeConflictException(
                    newShow.getScreen().getId(), newShow.getShowDate(), newShow.getStartTime());
        }
    }



    @Override
    public List<MovieShowResponse> getAll(Long eventId, LocalDate showDate) {
        return movieShowRepository.findByEventIdAndShowDateOrderByStartTimeAsc(eventId, showDate).stream()
                .map(movieShowMapper::toResponse)
                .toList();
    }

    @Override
    public MovieShowResponse getById(Long showId) {
        return movieShowMapper.toResponse(findById(showId));
    }

    @Override
    public List<SeatResponse> getSeats(Long showId) {
        MovieShow movieShow = findById(showId);
        return seatService.getAll(movieShow.getScreen().getId());
    }



    private MovieShow findById(Long showId) {
        return movieShowRepository.findById(showId)
                .orElseThrow(() -> new MovieShowNotFoundException(showId));
    }

    private Event findEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
    }

    private Screen findScreen(Long screenId) {
        return screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(screenId));
    }
}
