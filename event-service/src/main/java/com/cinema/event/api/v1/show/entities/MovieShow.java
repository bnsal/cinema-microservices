package com.cinema.event.api.v1.show.entities;

import com.cinema.event.api.v1.event.entities.Event;
import com.cinema.event.api.v1.screen.entities.Screen;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
        name = "movie_shows",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_show_screen_slot",
                columnNames = {"screen_id", "show_date", "start_time"}
        ),
        indexes = @Index(name = "idx_show_event_date", columnList = "event_id, show_date")
)
@Getter
@Setter
@NoArgsConstructor
public class MovieShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "show_date", nullable = false)
    private LocalDate showDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "base_price", nullable = false)
    private Integer basePrice;

    public LocalDateTime startsAt() {
        return LocalDateTime.of(showDate, startTime);
    }

    public LocalDateTime endsAt() {
        return startsAt().plusMinutes(event.getDurationMinutes());
    }
}
