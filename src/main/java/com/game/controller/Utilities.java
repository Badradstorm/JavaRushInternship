package com.game.controller;

import com.game.entity.Player;

import java.util.Date;

public class Utilities {

    protected static boolean isIdNotValid(Long id) {
        return id <= 0;
    }

    protected static boolean isNameNotValid(Player player) {
        String name = player.getName();
        if (name != null)
            return name.equals("") || name.length() > 12;
        return false;
    }

    protected static boolean isTitleNotValid(Player player) {
        String title = player.getTitle();
        if (title != null)
            return title.length() > 30;
        return false;
    }

    protected static boolean isBirthdayNotValid(Player player) {
        Date birthday = player.getBirthday();
        if (birthday != null)
            return birthday.getTime() < 0;
        return false;
    }

    protected static boolean isExperienceNotValid(Player player) {
        Integer experience = player.getExperience();
        if (experience != null)
            return experience < 0 || experience > 10000000;
        return false;
    }

    protected static boolean isNotValidCreatedPlayer(Player player) {
        return player == null || isAnyParamEqualNull(player) || isAnyParamNotValid(player);
    }

    private static boolean isAnyParamNotValid(Player player) {
        return isNameNotValid(player) || isTitleNotValid(player) || isBirthdayNotValid(player) || isExperienceNotValid(player);
    }

    private static boolean isAnyParamEqualNull(Player player) {
        boolean nullName = player.getName() == null;
        boolean nullTitle = player.getTitle() == null;
        boolean nullBirthday = player.getBirthday() == null;
        boolean nullExperience = player.getExperience() == null;
        boolean nullRace = player.getRace() == null;
        boolean nullProfession = player.getProfession() == null;
        return nullName || nullTitle || nullBirthday || nullExperience || nullRace || nullProfession;
    }

    public static boolean isNotValidUpdatedPlayer(Player player) {
        if (player != null)
            return isAnyParamNotValid(player);
        return false;
    }
}
