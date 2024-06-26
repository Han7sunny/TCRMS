package com.kutca.tcrms.participantapplication.repository;

import com.kutca.tcrms.participantapplication.entity.ParticipantApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantApplicationRepository extends JpaRepository<ParticipantApplication, Long> {
    Boolean existsByParticipant_ParticipantId(Long participant_id);
    Boolean existsByParticipant_ParticipantIdAndEvent_EventId(Long participant_id, Long event_id);
    List<ParticipantApplication> findAllByParticipant_ParticipantId(Long participant_id);
    Optional<ParticipantApplication> findByParticipant_ParticipantIdAndEvent_EventId(Long participant_id, Long eventId);
    Boolean existsByParticipant_ParticipantIdAndEvent_EventIdBetween(Long participant_id, Long startEvent, Long endEvent);
    List<ParticipantApplication> findAllByParticipant_ParticipantIdAndAndEvent_EventId(Long participant_id, Long event_id);
    List<ParticipantApplication> findAllByParticipant_ParticipantIdAndAndEvent_EventIdAndIs2ndCancelTrue(Long participant_id, Long event_id);
    List<ParticipantApplication> findAllByParticipant_ParticipantIdAndEvent_EventIdBetween(Long participant_id, Long startEvent, Long endEvent);
    int countAllByParticipant_ParticipantIdAndEvent_EventIdBetween(Long participant_id, Long startEvent, Long endEvent);
    int countAllByParticipant_ParticipantIdAndEvent_EventIdBetweenAndIs2ndCancelTrue(Long participant_id, Long startEvent, Long endEvent);
//    int countAllByParticipant_ParticipantIdAndEvent_EventId(Long participant_id, Long event_id);
    Optional<ParticipantApplication> findTopByEvent_EventId(Long event_id);
    Boolean existsByEventTeamNumberAndIndexInTeam(int event_team_number, String index_in_team);
}
