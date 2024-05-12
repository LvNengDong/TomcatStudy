package geek.tomcat.test;

import java.text.MessageFormat;

/**
 * @Author lnd
 * @Description
 * @Date 2024/5/10 14:49
 */
public class StringFormatTest {
    public static void main(String[] args) {
        String name = "John";
        int age = 25;
        String formattedString = MessageFormat.format("My name is {0} and I am {1} years old.", name, age);
        System.out.println(formattedString); // 输出：My name is John and I am 25 years old.
    }
}
