package ConfHandler.repositories;


import ConfHandler.model.dto.ConferenceInfoDto;
import ConfHandler.model.entity.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ConferenceRepository extends JpaRepository<Conference,UUID> {

    @Query("select new ConfHandler.model.dto.ConferenceInfoDto( " +
            "conference.name, " +
            "conference.dateStart," +
            "conference.dateEnd" +
            ") from Conference conference " +
            "order by conference.id desc " +
            "limit 1 ")
    Optional<ConferenceInfoDto> getConferenceInfo();
}
