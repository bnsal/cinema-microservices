package com.cinema.booking.api.v1.seat.service.impl;

import com.cinema.booking.api.v1.booking.entities.BookedSeat;
import com.cinema.booking.api.v1.booking.repository.BookedSeatRepository;
import com.cinema.booking.api.v1.reservation.repository.ReservationCache;
import com.cinema.booking.api.v1.seat.dto.SeatStatus;
import com.cinema.booking.api.v1.seat.dto.ShowSeatResponse;
import com.cinema.booking.api.v1.seat.service.ShowSeatService;
import com.cinema.booking.api.v1.show.client.dto.SeatClientResponse;
import com.cinema.booking.api.v1.show.client.gateway.ShowGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowGateway showGateway;
    private final BookedSeatRepository bookedSeatRepository;
    private final ReservationCache reservationCache;

    @Override
    public List<ShowSeatResponse> getShowSeats(Long showId) {
        List<SeatClientResponse> seats = showGateway.findSeats(showId);
        List<Long> seatIds = seats.stream().map(SeatClientResponse::id).toList();

        Set<Long> bookedSeatIds = bookedSeatRepository.findByShowId(showId).stream()
                .map(BookedSeat::getSeatId)
                .collect(Collectors.toSet());
        Set<Long> reservedSeatIds = reservationCache.findHeldSeatIds(showId, seatIds);

        List<ShowSeatResponse> showSeats = new ArrayList<>();
        for (SeatClientResponse seat : seats) {
            SeatStatus status = statusOf(seat.id(), bookedSeatIds, reservedSeatIds);
            showSeats.add(new ShowSeatResponse(seat.id(), seat.seatCode(), status));
        }
        return showSeats;
    }

    private SeatStatus statusOf(Long seatId, Set<Long> bookedSeatIds, Set<Long> reservedSeatIds) {
        if (bookedSeatIds.contains(seatId)) {
            return SeatStatus.BOOKED;
        }
        return reservedSeatIds.contains(seatId) ? SeatStatus.RESERVED : SeatStatus.AVAILABLE;
    }
}
