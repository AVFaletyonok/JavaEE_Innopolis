package org.example.repository;

import org.example.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для хранения данных клиентов.
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Boolean existsByPassportNumber(String passportNumber);
    Boolean existsByPhoneNumber(String phoneNumber);
    Optional<ClientEntity> findByPassportNumber(String passportNumber);
    Optional<ClientEntity> findByPhoneNumber(String phoneNumber);
}
