package com.teri.alttd.Cache;

import com.teri.alttd.Objects.Pom;
import com.teri.alttd.Queries.PomQueries;

import java.util.HashMap;

public class PomCache {
    private static final HashMap<Integer, Pom> pomGroups = new HashMap<>(); //All admin groups for all guilds that have added admin groups.

    /**
     * Check if a specific group in a specific guild is a pom group.
     * @param pomId the pom to check for.
     * @return if a group is in a specific guild.
     */
    public static boolean inPom(int pomId, long userId){
        return pomGroups.get(pomId).hasUser(userId);
    }

    /**
     * Get the pom with the specified id from the pomGroups map
     * @param pomId id of the pom you want to retrieve
     * @return the pom belonging to the pomId
     */
    public static Pom getPom(int pomId) {
        return pomGroups.get(pomId);
    }

    /**
     * Remove the pom group.
     * @param pomId the id of the pom to remove.
     */
    public static void removePom(int pomId){
        pomGroups.remove(pomId);
        PomQueries.deletePom(pomId);
    }

    /**
     * Add the pom group, it should already be in the database since you can't have the pomId without a database entry.
     * @param pomId group to add.
     */
    public static void addPom(int pomId, Pom pom){
        pomGroups.put(pomId, pom);
    }

    /**
     * Load all pom groups.
     */
    public static void loadPomGroups(){
        pomGroups.putAll(PomQueries.loadAllPoms());
    }
}
