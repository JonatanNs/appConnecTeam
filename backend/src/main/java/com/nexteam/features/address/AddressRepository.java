package com.nexteam.features.address;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {
    void deleteByPublicId(UUID publicId);

    Optional<Address> findByPublicId(UUID publicId);

    @Query("""
            SELECT a FROM Address a
            WHERE (:street IS NULL OR LOWER(a.street) LIKE LOWER(CONCAT('%', :street, '%')))
            AND (:city IS NULL OR LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%')))
            AND (:country IS NULL OR LOWER(a.country) LIKE LOWER(CONCAT('%', :country, '%')))
            AND (:zipcode IS NULL OR a.zipcode LIKE CONCAT('%', :zipcode, '%'))
            """)
    Page<Address> search(
            @Param("street") String street,
            @Param("city") String city,
            @Param("country") String country,
            @Param("zipcode") String zipcode,
            Pageable pageable
    );
}
