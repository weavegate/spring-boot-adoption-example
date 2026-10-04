# Spring Boot adoption example

A small, synthetic Spring Boot seat-reservation service used to check that a
Spring Boot team can adopt the [weavegate](https://github.com/weavegate/weavegate)
CI gate by following its published documentation alone.

`POST /seats/{seatId}/reservations?customer=<name>` reserves an open seat inside
one `@Transactional` method: it reads the seat, checks for an active
reservation, then inserts one. The schema and seed in `db/` are synthetic.

## Build

Requires Java 21.

```bash
./mvnw -B package
```

## weavegate gate

`.github/workflows/weavegate.yml` gates every pull request and push to `main`
with weavegate: one job replays the committed double-booking schedule
`.weavegate/schedules/sch_6f1ffd61cc07.json`, the other explores candidate
schedules for the two sync-points in `ReservationService`.

Two commits in `main`'s history fail this gate on purpose. They came from
[pull request #1](https://github.com/weavegate/spring-boot-adoption-example/pull/1),
which added the gate before fixing the race:

| Commit | Code | Gate |
| --- | --- | --- |
| `563b05d` | Vulnerable, exploration only | Fails with WG001 |
| `df6e07d` | Vulnerable, schedule committed | Both jobs fail with WG001 |
| `8950c8a` | Seat row locked with `FOR UPDATE` | Both jobs pass |

The failures are the evidence that the gate catches the double booking; every
commit from `8950c8a` on passes.
