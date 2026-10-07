
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs JUnit 5 test classes from the command line without Maven or Gradle.
 */
public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("       Running Academic Test Suite (fWCt)        ");
        System.out.println("=================================================");

        Class<?>[] testClasses = new Class<?>[]{
                UserTest.class,
                BuildingTest.class,
                BathroomTest.class,
                FWCtTest.class
        };

        int totalPassed = 0;
        int totalFailed = 0;

        for (Class<?> clazz : testClasses) {
            int passedInClass = 0;
            int failedInClass = 0;

            List<Method> testMethods = new ArrayList<>();

            for (Method m : clazz.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class)) {
                    testMethods.add(m);
                }
            }

            for (Method testMethod : testMethods) {
                try {
                    Object testInstance = clazz.getDeclaredConstructor().newInstance();
                    testMethod.invoke(testInstance);
                    passedInClass++;
                } catch (Throwable t) {
                    failedInClass++;
                    System.err.printf("  [FAIL] %s.%s: %s\n",
                            clazz.getSimpleName(), testMethod.getName(),
                            t.getCause() != null ? t.getCause().getMessage() : t.getMessage());
                }
            }

            if (failedInClass == 0) {
                System.out.printf("[✓] %-24s PASS (%d/%d tests)\n",
                        clazz.getSimpleName(), passedInClass, testMethods.size());
            } else {
                System.out.printf("[✗] %-24s FAIL (%d passed, %d failed)\n",
                        clazz.getSimpleName(), passedInClass, failedInClass);
            }

            totalPassed += passedInClass;
            totalFailed += failedInClass;
        }

        System.out.println("-------------------------------------------------");
        System.out.printf("Result: %d passed, %d failed. (Total: %d tests)\n",
                totalPassed, totalFailed, totalPassed + totalFailed);
        System.out.println("=================================================");

        if (totalFailed > 0) {
            System.exit(1);
        }
    }
}
