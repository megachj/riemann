package com.sunset.java.generics;

import com.sunset.java.generics.TypeErasure.MyOptional;
import com.sunset.java.generics.TypeErasure.MyOptionalPerson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sunset.java.generics.GenericType.*;

public class GenericTypeTest {

    @Test
    public void genericTypeInheritance() {
        Integer i = 10;
        Number n = i;

        List<Integer> integers = new ArrayList<>();
        // List<Number> numbers = integers; // compile error: List<Integer> - List<Number> 다른 타입(상속 관계 x)

        ArrayList<Integer> arrayList = new ArrayList<>();
        List<Integer> integers1 = arrayList;
        // List<Number> numbers = arrayList; // compile error
    }

    @Test
    public void rawType() {
        List<GenericType.Developer> developers = Arrays.asList(new Developer(), new Developer());
        List list = developers;
        List<Developer> developers1 = list; // unchecked cast 경고 발생.
        list.forEach(o -> {
            ((Developer)o).hello();
            ((Person)o).hello();
        });
    }

    @Test
    public void typeErasure() {
        MyOptional<String> stringMyOptional = MyOptional.ofNullable("hello");
        System.out.println(stringMyOptional.getTypeName()); // java.lang.Object 출력

        MyOptionalPerson<Person> optionalPerson = MyOptionalPerson.ofNullable(new Person());
        System.out.println(optionalPerson.getTypeName()); // Person 출력

        MyOptionalPerson<Developer> optionalDeveloper = MyOptionalPerson.ofNullable(new Developer());
        System.out.println(optionalDeveloper.getTypeName()); // Person 출력
    }

    @Test
    public void typeInference() {
        emptyMethod(1, Arrays.asList(1, 2, 3));
        GenericType.<Integer>emptyMethod(1, Arrays.asList(1, 2, 3)); // 타입 추론을 잘 하지 못할 경우, 타입을 줄 수 있다.
    }

    @Test
    public void bounded() {
        Integer[] integers = new Integer[] {1, 2, 3, 4, 5, 6, 7};
        System.out.println(countGreaterThan(integers, 3));

        String[] strings = new String[] {"a", "b", "c", "d", "e"};
        System.out.println(countGreaterThan(strings, "b"));
    }

    @Test
    public void wildcard() {
        printList(Arrays.asList(1, 2, 3));
        printList2(Arrays.asList(1, 2, 3));

        List<Integer> integers = Arrays.asList(1, 2, 3);
        // printList(integers); // compile error: List<Object> List<Integer> 다른 타입
        printList2(integers);
    }
}
