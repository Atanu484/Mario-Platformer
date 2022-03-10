
package com.nortal.platformer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Game {

    public static void main(String[] args) {
        Game game = new Game("platforms.csv");
        game.run();
    }

    private Integer points = 500;
    private Integer jumps = 0;
    private List<com.nortal.platformer.Platform> platforms;
    private com.nortal.platformer.Platform activePlatform;

    public Game(String gameFile) {
        platforms = readPlatforms(gameFile);
        activePlatform = platforms.get(0);
    }

    public Integer getJumps() {
        return jumps;
    }

    public void run() {
        com.nortal.platformer.Platform nextPlatform = findNextPlatform(activePlatform);
        assert nextPlatform != null;
        jumpController(nextPlatform);

        System.out.println(jumps);
    }

    private void jumpController(com.nortal.platformer.Platform nextPlatform) {
        if(nextPlatform.getCost() <= points) {
            points -= nextPlatform.getCost();
            jumpTo(nextPlatform);
            nextPlatform = findNextPlatform(activePlatform);
            if(null == nextPlatform) {
                return;
            }
            jumpController(nextPlatform);
        } else {
            com.nortal.platformer.Platform previousPlatfortorm = findPreviousPlatform(activePlatform);
            assert previousPlatfortorm != null;
            executeBackInTime(nextPlatform, previousPlatfortorm);
        }
    }

    private void executeBackInTime(com.nortal.platformer.Platform targetPlatform, com.nortal.platformer.Platform nextPlatform) {
        points += nextPlatform.getCost();
        jumpTo(nextPlatform);

        //If platform is back to reached platform call jump controller back
        if(Objects.equals(targetPlatform.getIndex(), Objects.requireNonNull(findNextPlatform(activePlatform)).getIndex())) {
            jumpController(targetPlatform);
        } else {
            // Decide Going Forward or Backward
            if(isPointGapReachable(targetPlatform)) {
                executeBackInTime(targetPlatform, Objects.requireNonNull(findNextPlatform(nextPlatform)));
            }
            else {
                if(findPreviousPlatform(nextPlatform) != null) {
                    executeBackInTime(targetPlatform, Objects.requireNonNull(findPreviousPlatform(nextPlatform)));
                } else {
                    executeBackInTime(targetPlatform, Objects.requireNonNull(findNextPlatform(nextPlatform)));
                }
            }
        }
    }

    private boolean isPointGapReachable(com.nortal.platformer.Platform targetPlatform) {
        Integer earnablePoints = 0;
        com.nortal.platformer.Platform nextPlatform = findNextPlatform(activePlatform);
        while(true) {
            assert nextPlatform != null;
            if (!(nextPlatform.getIndex() < targetPlatform.getIndex())) break;
            earnablePoints += nextPlatform.getCost();
            nextPlatform = findNextPlatform(nextPlatform);
        }

        return earnablePoints + points > targetPlatform.getCost();
    }

    private com.nortal.platformer.Platform findNextPlatform(com.nortal.platformer.Platform platform) {
        if(platform.getIndex() + 1 == platforms.size())
            return null;
        return platforms.get(platform.getIndex() + 1);
    }

    private com.nortal.platformer.Platform findPreviousPlatform(com.nortal.platformer.Platform platform) {
        if(platform.getIndex() == 0)
            return null;
        return platforms.get(platform.getIndex() - 1);
    }

    /**
     * Reads platforms from csv file and returns the as list.
     * @return platforms - Platforms as list
     */
    private List<com.nortal.platformer.Platform> readPlatforms(String gameFile){
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        this.getClass().getResourceAsStream("/" + gameFile))));
        String line = "";
        List<com.nortal.platformer.Platform> platforms = new ArrayList<>();
        while (true) {
            try {
                if ((line = bufferedReader.readLine()) == null) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] column = line.split(", ");
            if (column[0].equals("index") && column[1].equals("cost")) {
                continue;
            }
            com.nortal.platformer.Platform platform = new com.nortal.platformer.Platform(Integer.parseInt(column[0]), Integer.parseInt(column[1]));
            platforms.add(platform);
        }
        return platforms;
    }

    /**
     * Invoke this function to jump to next platform.
     * @param platform - Platform that you are going to jump to.
     */
    public void jumpTo(com.nortal.platformer.Platform platform) {
        activePlatform = platform;
        ++jumps;
        System.out.println("Jumped to next platform id:" + activePlatform.getIndex() + " Points: " + points +
                " Jumps: " + getJumps());
    }
}