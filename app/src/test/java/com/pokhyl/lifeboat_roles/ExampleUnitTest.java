package com.pokhyl.lifeboat_roles;

import com.pokhyl.lifeboat_roles.model.Person;
import com.pokhyl.lifeboat_roles.model.PersonRelation;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        for (int i = 0; i < 128; i++) {
            List<Person> personList = Arrays.asList(Person.values());
            Map<Person, PersonRelation> relationMap = RelationGenerator.generate(personList);

            assertEquals(personList.size(), relationMap.keySet().size());
            assertTrue(relationMap.keySet().containsAll(personList));

            for (Map.Entry<Person, PersonRelation> entry : relationMap.entrySet()) {
                Assert.assertNotEquals(entry.getKey(), entry.getValue().friend());
                Assert.assertNotEquals(entry.getKey(), entry.getValue().enemy());
                Assert.assertNotEquals(entry.getValue().friend(), entry.getValue().enemy());
            }

            Set<Person> friends = new HashSet<>();
            Set<Person> enemies = new HashSet<>();
            for (PersonRelation relation : relationMap.values()) {
                friends.add(relation.friend());
                enemies.add(relation.enemy());
            }
            assertEquals(personList.size(), friends.size());
            assertEquals(personList.size(), enemies.size());
        }


    }
}