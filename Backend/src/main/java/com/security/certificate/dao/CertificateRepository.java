package com.security.certificate.dao;

import com.security.certificate.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
    @Query("select c from Certificate c left outer join User u on c.user.id = u.id where c.user.email = :email and c.validFrom <= current_timestamp and c.validTo >= current_timestamp ")
    List<Certificate> findValidCertificates(String email);

    @Query("select c from Certificate c where c.serialNumber = :serialNumber and c.validFrom <= current_timestamp and c.validTo >= current_timestamp")
    Certificate findValidCertificateBySerialNumber(String serialNumber);
}