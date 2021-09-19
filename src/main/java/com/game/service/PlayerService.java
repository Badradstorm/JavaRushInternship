package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.List;
import java.util.Optional;

public interface PlayerService {

    void createOrUpdate(Player player);

    void delete(Player player);

    Optional<Player> getById(long id);

    List<Player> getAllBy(String name, String title, Race race, Profession profession,
                          Long after, Long before, Boolean banned, Integer minExperience,
                          Integer maxExperience, Integer minLevel, Integer maxLevel);
}
