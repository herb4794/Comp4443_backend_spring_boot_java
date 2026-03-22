package comp4443.booking_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import comp4443.booking_app.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

}
