import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Descriptor:
 * Author: sherlock
 * Date: 2019-07-28 6:10 PM
 */
public class Test {

    public static void main(String[] args) {

        System.out.println(Runtime.getRuntime().availableProcessors());
        String str1 = "hello";

        String f1 = String.format("%08d", 23);
        System.out.println(f1);
        System.out.println(String.format("%0"+ (8 - "Apple".length())+"d%s",0 ,"Apple"));


    }
}
