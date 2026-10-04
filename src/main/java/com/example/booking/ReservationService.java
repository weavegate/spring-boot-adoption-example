package com.example.booking;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    private final JdbcTemplate jdbc;

    public ReservationService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Reserves an open seat for a customer; returns false when the seat is closed or already held. */
    @Transactional
    public boolean reserve(long seatId, String customer) {
        List<String> open = jdbc.queryForList(
                "SELECT status FROM seat WHERE id = ? AND status = 'OPEN'", String.class, seatId);
        if (open.isEmpty()) {
            return false;
        }

        Integer held = jdbc.queryForObject(
                "SELECT COUNT(*) FROM reservation WHERE seat_id = ? AND status = 'ACTIVE'",
                Integer.class, seatId);
        if (held != null && held > 0) {
            return false;
        }
        jdbc.update("INSERT INTO reservation (seat_id, customer, status) VALUES (?, ?, 'ACTIVE')",
                seatId, customer);
        return true;
    }
}
