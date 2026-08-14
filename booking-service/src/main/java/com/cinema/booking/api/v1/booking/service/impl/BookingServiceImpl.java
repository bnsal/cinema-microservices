package com.cinema.booking.api.v1.booking.service.impl;

import com.cinema.booking.api.v1.booking.dto.BookingRequest;
import com.cinema.booking.api.v1.booking.dto.BookingResponse;
import com.cinema.booking.api.v1.booking.entities.BookedSeat;
import com.cinema.booking.api.v1.booking.entities.Booking;
import com.cinema.booking.api.v1.booking.exceptions.BookingNotFoundException;
import com.cinema.booking.api.v1.booking.exceptions.SeatAlreadyBookedException;
import com.cinema.booking.api.v1.booking.mapper.BookingMapper;
import com.cinema.booking.api.v1.booking.repository.BookedSeatRepository;
import com.cinema.booking.api.v1.booking.repository.BookingRepository;
import com.cinema.booking.api.v1.booking.service.BookingService;
import com.cinema.booking.api.v1.notification.NotificationService;
import com.cinema.booking.api.v1.payment.PaymentService;
import com.cinema.booking.api.v1.reservation.exceptions.ReservationExpiredException;
import com.cinema.booking.api.v1.reservation.model.Reservation;
import com.cinema.booking.api.v1.reservation.repository.ReservationCache;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookedSeatRepository bookedSeatRepository;
    private final BookingMapper bookingMapper;
    private final ReservationCache reservationCache;
    private final PaymentService paymentService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public BookingResponse create(BookingRequest request) {
        Reservation reservation = reservationCache.find(request.reservationId())
                .orElseThrow(() -> new ReservationExpiredException(request.reservationId()));

        //validating seats again
        validateSeatsAreFree(reservation);

        String paymentReference = paymentService.pay(reservation.userId(), reservation.finalAmount());
        Booking booking = bookingMapper.toEntity(reservation, paymentReference);
        bookingRepository.save(booking);

        reservationCache.delete(reservation);
        notificationService.bookingConfirmed(booking);

        return bookingMapper.toResponse(booking);
    }

    @Override
    public BookingResponse getById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        return bookingMapper.toResponse(booking);
    }

    @Override
    public List<BookingResponse> getAllByUser(Long userId) {
        return bookingRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    private void validateSeatsAreFree(Reservation reservation) {
        List<Long> alreadyBooked = bookedSeatRepository
                .findByShowIdAndSeatIdIn(reservation.showId(), reservation.seatIds()).stream()
                .map(BookedSeat::getSeatId)
                .toList();

        if (!alreadyBooked.isEmpty()) {
            throw new SeatAlreadyBookedException(alreadyBooked);
        }
    }

}
