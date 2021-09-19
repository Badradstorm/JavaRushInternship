package com.game.repository;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PlayerRepositoryImpl implements PlayerRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Player> findAllBy(String name, String title, Race race, Profession profession,
                                  Long after, Long before, Boolean banned, Integer minExperience,
                                  Integer maxExperience, Integer minLevel, Integer maxLevel) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
        Root<Player> root = criteriaQuery.from(Player.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
        }
        if (title != null) {
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
        }
        if (race != null) {
            predicates.add(criteriaBuilder.equal(root.get("race"), race));
        }
        if (profession != null) {
            predicates.add(criteriaBuilder.equal(root.get("profession"), profession));
        }
        if (after != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("birthday"), new Date(after)));
        }
        if (before != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("birthday"), new Date(before)));
        }
        if (banned != null) {
            predicates.add(criteriaBuilder.equal(root.get("banned"), banned));
        }
        if (minExperience != null) {
            predicates.add(criteriaBuilder.ge(root.get("experience"), minExperience));
        }
        if (maxExperience != null) {
            predicates.add(criteriaBuilder.le(root.get("experience"), maxExperience));
        }
        if (minLevel != null) {
            predicates.add(criteriaBuilder.ge(root.get("level"), minLevel));
        }
        if (maxLevel != null) {
            predicates.add(criteriaBuilder.le(root.get("level"), maxLevel));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
