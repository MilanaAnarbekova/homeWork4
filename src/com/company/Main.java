package com.company;

import java.util.Random;

/*Добавить n-го игрока, Golem, который имеет увеличенную жизнь но слабый удар.
 Может принимать на себя 1/5 часть урона исходящего от босса по другим игрокам.
Добавить n-го игрока, Lucky, имеет шанс уклонения от ударов босса.
Добавить n-го игрока, Berserk, блокирует часть удара босса по себе и прибавляет заблокированный урон к своему урону и возвращает его боссу
Добавить n-го игрока, Thor, удар по боссу имеет шанс оглушить босса на 1 раунд, вследствие чего босс пропускает 1 раунд и не наносит урон героям.
 // random.nextBoolean(); - true, false*/

public class Main {
    public static int bosHitt = 50;
    public static int roundNumber = 1;
    public static int bossHealth = 17000;
    public static int bossDamage = 50;
    public static int medic = 30;
    public static String bossDefence = "";
    public static String[] heroesAttackType = {
            "Physical", "Magical", "Kinetic", "Medic", "Berserk", "Thor", "Lucky", "Golem"};
    public static int[] heroesHealth = {340, 270, 250, 300, 400, 350, 280, 450};
    public static int[] heroesDamage = {15, 20, 25, 0, 30, 27, 18, 10};

    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            round();
        }
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0, 1, 2
        bossDefence = heroesAttackType[randomIndex];
        System.out.println("Boss choose " + bossDefence);
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        /* if (heroesHealth[0] <= 0 && heroesHealth[1] <= 0
                && heroesHealth[2] <= 0) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;*/
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void round() {
        System.out.println("ROUND: " + roundNumber);
        chooseBossDefence();
        bossHits();
        heroesHit();
        medic();
        berserk();
        thor();
        lucky();
        golem();
        printStatistics();
        berserkHit();
        bossHit();
        roundNumber++;
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                if (heroesAttackType[i] == bossDefence) {
                    Random random = new Random();
                    int coeff = random.nextInt(3); // 0,1,2,3,4,5,6,7,8
                    if (bossHealth - heroesDamage[i] * coeff < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth = bossHealth - heroesDamage[i] * coeff;
                    }
                    System.out.println("Critical Damage "
                            + heroesDamage[i] * coeff);
                } else {
                    if (bossHealth - heroesDamage[i] < 0) {
                        bossHealth = 0;
                    } else {
                        bossHealth = bossHealth - heroesDamage[i];
                    }
                }
            }
        }
    }


    public static void bossHits() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    public static void medic() {
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] < 100 && heroesHealth[3] > 0 && heroesHealth[i] > 0) {
                heroesHealth[i] = heroesHealth[i] + medic;
                System.out.println("medic hills " + medic);
            }
        }
    }

    public static void berserk() {
        Random random = new Random();
        int berserkBlock = random.nextInt(30) + 1;
        if (heroesHealth[4] > 0) {
            heroesHealth[4] = (heroesHealth[4] - bossDamage) + berserkBlock;
            heroesDamage[4] = heroesDamage[4] + berserkBlock;
            System.out.println(" berserk attack " + berserkBlock);
        }

        if (heroesHealth[4] < 0) {
            heroesHealth[4] = 0;
        }
    }

    public static void berserkHit() {
        heroesDamage[4] = 30;
    }

    public static void thor() {
        Random random = new Random();
        boolean num = random.nextBoolean();
        if (num && heroesHealth[5] > 0) {
            bossDamage = 0;
            System.out.println("Boss stunned");
        } else if (!(num) && heroesHealth[5] > 0) {
            System.out.println("Boss  not stunned");
        }
    }

    public static void bossHit() {
        bossDamage = bosHitt;
    }

    public static void lucky() {
        Random random = new Random();
        boolean luckySave = random.nextBoolean();
        if (heroesHealth[6] > 0) {
            if (luckySave) {
                heroesHealth[6] = heroesHealth[6] + bossDamage;
                if (heroesHealth[6] > 150) {
                    heroesHealth[6] = heroesHealth[6];
                }
                System.out.println("Lucky evaded");
            } else if (!(luckySave)) {
                heroesHealth[6] = heroesHealth[6];
                System.out.println("Lucky not evaded");
            }
        }
    }

    public static void golem() {
        int golemTake = bossDamage / 5;
        int aLiveHeroes = 0;
        if (heroesHealth[7] > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (i == 7) {
                } else if (heroesHealth[i] > 0) {
                    heroesHealth[i] += 1;
                    heroesHealth[i] += golemTake;
                    aLiveHeroes++;
                }
            }

        heroesHealth[7] -= golemTake * aLiveHeroes;
        System.out.println("Golem take " + (golemTake * aLiveHeroes));
    }
    }

    public static void printStatistics() {
        System.out.println("________________");
        System.out.println("Boss Health: " + bossHealth +
                "; Boss Damage: " + bossDamage);
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(
                    heroesAttackType[i] + " Health: "
                            + heroesHealth[i] +
                            "; " + heroesAttackType[i] + " Damage: "
                            + heroesDamage[i]);
        }
        System.out.println("________________");
    }
}
