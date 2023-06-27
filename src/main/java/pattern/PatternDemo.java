package pattern;

import com.example.OgnlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternDemo {

    public static void main(String[] args) {
        //matchesDemo();
        //matchIf();
//        test();
        ifCondition();
    }

    // 匹配一个字符串，它是一个以字母开头，后面跟着零个或多个字母、数字或下划线的标识符
    public static void matchesDemo(){
        String input = "my_variable123";
        boolean isMatch = input.matches("[a-zA-Z][a-zA-Z0-9_]*");
        System.out.println(isMatch);  // 输出 true
    }

    // 通过java正则表达式，用来提取出一段字符串中的<if></if>结构，如果test=a>b，那么会有问题
    public static void matchIf(){
        String input = "这是一段<if test=\"true\">满足条件的</if>测试字符串。";
        String regex = "<if[^>]*>.*?</if>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();//等价于matcher.group(0)
            System.out.println(match);
        }
    }

    public static void test(){
        // String input = "Here is an example with <if condition=\"a > b\">some text</if> inside an if statement.";
        String input = "Here is an example with <if condition=\"true\">some text</if> inside an if statement.";
        Pattern pattern = Pattern.compile("<if[^>]*>.*?</if>");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String ifStatement = matcher.group(0);
            String ifContent = ifStatement.replaceAll("<[^>]*>", "").trim();
            System.out.println(ifContent);
        }
    }

    public static void ifCondition(){
        String input = "Here is an example with <if condition=\"a > b\">some text</if> inside an if statement.";
        Pattern pattern = Pattern.compile("<if\\s+condition=\"(.*?)\">(.*?)<\\/if>");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println(matcher.group(0));
            String condition = matcher.group(1);
            String ifContent = matcher.group(2).trim();
            System.out.println("Condition: " + condition);
            System.out.println("Content: " + ifContent);
        }
    }


}
