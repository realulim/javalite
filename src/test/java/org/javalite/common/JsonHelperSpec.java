package org.javalite.common;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.javalite.test.jspec.JSpec.$;
import static org.javalite.test.jspec.JSpec.a;

/**
 * @author Igor Polevoy on 5/26/16.
 */
public class JsonHelperSpec {

    @Test
    public void shouldConvertObject2JSON(){
        class Person {
            String firstName, lastName;
            public Person(String firstName, String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }

            public String getFirstName() {
                return firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }
        }
        a(JsonHelper.toJsonString(new Person("John", "Smith"))).shouldBeEqual("{\"firstName\":\"John\",\"lastName\":\"Smith\"}");
    }

    @Test
    public void shouldConvertArray2List(){
        List l = JsonHelper.toList("[1, 2]");
        $(l.size()).shouldBeEqual(2);
        $(l.get(0)).shouldBeEqual(1);
        $(l.get(1)).shouldBeEqual(2);
    }

    @Test
    public void shouldConvertMap2Map(){
        Map m = JsonHelper.toMap("{ \"name\" : \"John\", \"age\": 22 }");
        $(m.size()).shouldBeEqual(2);
        $(m.get("name")).shouldBeEqual("John");
        $(m.get("age")).shouldBeEqual(22);
    }

    @Test
    public void shouldConvertMaps2Maps(){
        Map[] maps = JsonHelper.toMaps("[{ \"name\" : \"John\", \"age\": 22 },{ \"name\" : \"Samantha\", \"age\": 21 }]");
        $(maps.length).shouldBeEqual(2);
        $(maps[0].get("name")).shouldBeEqual("John");
        $(maps[0].get("age")).shouldBeEqual(22);
        $(maps[1].get("name")).shouldBeEqual("Samantha");
        $(maps[1].get("age")).shouldBeEqual(21);
    }
}
