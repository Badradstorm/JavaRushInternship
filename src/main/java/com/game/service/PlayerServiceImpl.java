package com.game.service;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    final
    PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void createOrUpdate(Player player) {
        int experience = player.getExperience();
        int level = (int) ((Math.sqrt(2500 + 200 * experience) - 50) / 100);
        int untilNextLevel = 50 * (level + 1) * (level + 2) - experience;
        player.setLevel(level);
        player.setUntilNextLevel(untilNextLevel);
        playerRepository.save(player);
    }

    @Override
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    @Override
    public Optional<Player> getById(long id) {
        return playerRepository.findById(id);
    }

    @Override
    public List<Player> getAllBy(String name, String title, Race race, Profession profession,
                                 Long after, Long before, Boolean banned, Integer minExperience,
                                 Integer maxExperience, Integer minLevel, Integer maxLevel) {
        return playerRepository.findAllBy(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }
}
