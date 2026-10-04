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
