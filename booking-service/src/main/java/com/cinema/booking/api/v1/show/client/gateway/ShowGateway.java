package com.cinema.booking.api.v1.show.client.gateway;

import com.cinema.booking.api.v1.show.client.ShowClient;
import com.cinema.booking.api.v1.show.client.dto.MovieShowClientResponse;
import com.cinema.booking.api.v1.show.client.dto.SeatClientResponse;
import com.cinema.booking.api.v1.show.client.exceptions.EventServiceUnavailableException;
import com.cinema.booking.api.v1.show.client.exceptions.ShowNotFoundException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowGateway {

    private final ShowClient showClient;

    @CircuitBreaker(name = "eventService", fallbackMethod = "findShowFallback")
    public MovieShowClientResponse findShow(Long showId) {
        try {
            return showClient.getShowById(showId).data();
        } catch (FeignException.NotFound exception) {
            throw new ShowNotFoundException(showId);
        }
    }

    @CircuitBreaker(name = "eventService", fallbackMethod = "findSeatsFallback")
    public List<SeatClientResponse> findSeats(Long showId) {
        try {
            return showClient.getShowSeats(showId).data();
        } catch (FeignException.NotFound exception) {
            throw new ShowNotFoundException(showId);
        }
    }

    private MovieShowClientResponse findShowFallback(Long showId, Throwable cause) {
        throw translate(cause);
    }

    private List<SeatClientResponse> findSeatsFallback(Long showId, Throwable cause) {
        throw translate(cause);
    }

    private RuntimeException translate(Throwable cause) {
        if (cause instanceof ShowNotFoundException notFoundException) {
            return notFoundException;
        }
        return new EventServiceUnavailableException(cause);
    }
}
