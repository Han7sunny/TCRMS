package com.kutca.tcrms.participant.repository;

import com.kutca.tcrms.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    List<Participant> findAllByUser_UserId(Long user_id);

    Optional<Participant> findByUser_UserIdAndNameAndIdentityNumber(Long user_id, String name, String identity_name);

    Optional<Participant> findByUser_UserIdAndNameAndPhoneNumber(Long user_id, String name, String phone_number);
}
