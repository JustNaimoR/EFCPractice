package edu.mod6.linkabbreviationsservice.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public class ShortenLinkIdSequenceRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final static String SEQUENCE_NAME = "shorten_link_id_seq";

    public Long getNextSequenceValue() {
        Long nextValue = (Long) entityManager
                .createNativeQuery("select nextval(:seq_name)")
                .setParameter("seq_name", SEQUENCE_NAME)
                .getSingleResult();
        return nextValue;
    }
}