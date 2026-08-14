package com.cinema.booking.api.v1.booking.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "show_id", nullable = false)
    private Long showId;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "discount_amount", nullable = false)
    private Integer discountAmount;

    @Column(name = "final_amount", nullable = false)
    private Integer finalAmount;

    @Column(name = "payment_reference", nullable = false, length = 50)
    private String paymentReference;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedSeat> bookedSeats = new ArrayList<>();

    public void addSeat(Long seatId) {
        BookedSeat bookedSeat = new BookedSeat();
        bookedSeat.setBooking(this);
        bookedSeat.setShowId(showId);
        bookedSeat.setSeatId(seatId);
        bookedSeats.add(bookedSeat);
    }
}
