package com.pokhyl.lifeboat_roles;


import com.pokhyl.lifeboat_roles.model.Person;
import com.pokhyl.lifeboat_roles.model.PersonRelation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RelationGenerator {

    private static Random random = new Random();

    public static Map<Person, PersonRelation> generate(List<Person> personList) {
        Map<Person, PersonRelation> result = new HashMap<>();

        List<Person> availableFriends = new ArrayList<>(personList);
        List<Person> availableEnemy = new ArrayList<>(personList);

        for (int i = 0; i < personList.size() - 1; i++) {
            Person current = personList.get(i);
            boolean isCurrentInFriendList = availableFriends.remove(current);
            Person friend = availableFriends.get(random.nextInt(availableFriends.size()));
            availableFriends.remove(friend);
            if (isCurrentInFriendList) {
                availableFriends.add(current);
            }

            boolean isCurrentInEnemyList = availableEnemy.remove(current);
            boolean isFriendInEnemyList = availableEnemy.remove(friend);
            if (availableEnemy.isEmpty()) {
                return generate(personList);
            }
            Person enemy = availableEnemy.get(random.nextInt(availableEnemy.size()));
            availableEnemy.remove(enemy);
            if (isCurrentInEnemyList) {
                availableEnemy.add(current);
            }
            if (isFriendInEnemyList) {
                availableEnemy.add(friend);
            }

            result.put(current, PersonRelation.create(friend, enemy));
        }
        Person current = personList.get(personList.size() - 1);
        if (current != availableFriends.get(0) && current != availableEnemy.get(0) && availableFriends.get(0) != availableEnemy.get(0)) {
            result.put(current, PersonRelation.create(availableFriends.get(0), availableEnemy.get(0)));
            return result;
        } else {
            return generate(personList);
        }
    }
}
