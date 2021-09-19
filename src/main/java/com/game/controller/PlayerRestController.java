package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PlayerRestController {

    final
    PlayerService playerService;

    public PlayerRestController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity<Player> getById(@PathVariable Long id) {
        if (Utilities.isIdNotValid(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Player> player = this.playerService.getById(id);

        return player.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @DeleteMapping(value = "/rest/players/{id}")
    public ResponseEntity<Player> delete(@PathVariable Long id) {
        if (Utilities.isIdNotValid(id))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Player> player = this.playerService.getById(id);

        if (player.isPresent()) {
            this.playerService.delete(player.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/rest/players")
    public ResponseEntity<List<Player>> getAllby(@RequestParam(required = false) String name, @RequestParam(required = false) String title,
                                                 @RequestParam(required = false) Race race, @RequestParam(required = false) Profession profession,
                                                 @RequestParam(required = false) Long after, @RequestParam(required = false) Long before,
                                                 @RequestParam(required = false) Boolean banned, @RequestParam(required = false) Integer minExperience,
                                                 @RequestParam(required = false) Integer maxExperience, @RequestParam(required = false) Integer minLevel,
                                                 @RequestParam(required = false) Integer maxLevel, @RequestParam(required = false) PlayerOrder order,
                                                 @RequestParam(required = false, defaultValue = "0") Integer pageNumber, @RequestParam(required = false, defaultValue = "3") Integer pageSize) {

        List<Player> players = this.playerService.getAllBy(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        if (pageNumber != 0)
            players = players.stream().skip(pageSize).collect(Collectors.toList());
        players = players.stream().limit(pageSize).collect(Collectors.toList());
        if (order != null) {
            players = players.stream().sorted((o1, o2) -> {
                switch (order) {
                    case NAME:
                        return o1.getName().compareTo(o2.getName());
                    case EXPERIENCE:
                        return o1.getExperience().compareTo(o2.getExperience());
                    case BIRTHDAY:
                        return o1.getBirthday().compareTo(o2.getBirthday());
                    case LEVEL:
                        return o1.getLevel().compareTo(o2.getLevel());
                }
                return o1.getId().compareTo(o2.getId());
            }).collect(Collectors.toList());
        }
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/players/count")
    public ResponseEntity<Integer> count(@RequestParam(required = false) String name, @RequestParam(required = false) String title,
                                         @RequestParam(required = false) Race race, @RequestParam(required = false) Profession profession,
                                         @RequestParam(required = false) Long after, @RequestParam(required = false) Long before,
                                         @RequestParam(required = false) Boolean banned, @RequestParam(required = false) Integer minExperience,
                                         @RequestParam(required = false) Integer maxExperience, @RequestParam(required = false) Integer minLevel,
                                         @RequestParam(required = false) Integer maxLevel) {
        List<Player> players = this.playerService.getAllBy(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel);
        int count = players.size();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @PostMapping(value = "/rest/players", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> create(@RequestBody Player player) {
        if (Utilities.isNotValidCreatedPlayer(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        if (player.getBanned() == null)
            player.setBanned(false);
        playerService.createOrUpdate(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    //to do
    @PostMapping(value = "/rest/players/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody Player player) {

        if (Utilities.isIdNotValid(id) || Utilities.isNotValidUpdatedPlayer(player))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String name = player.getName();
        String title = player.getTitle();
        Race race = player.getRace();
        Profession profession = player.getProfession();
        Date birthday = player.getBirthday();
        Integer experience = player.getExperience();
        Boolean banned = player.getBanned();

        Optional<Player> playerOptional = playerService.getById(id);
        Player playerFromDB;

        if (playerOptional.isPresent()) {
            playerFromDB = playerOptional.get();
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (name != null)
            playerFromDB.setName(name);
        if (title != null)
            playerFromDB.setTitle(title);
        if (race != null)
            playerFromDB.setRace(race);
        if (profession != null)
            playerFromDB.setProfession(profession);
        if (birthday != null)
            playerFromDB.setBirthday(birthday);
        if (experience != null)
            playerFromDB.setExperience(experience);
        if (banned != null)
            playerFromDB.setBanned(banned);

        playerService.createOrUpdate(playerFromDB);
        return new ResponseEntity<>(playerFromDB, HttpStatus.OK);
    }
}
