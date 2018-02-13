import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.platform.suite.api.SelectClasses;

@RunWith(JUnitPlatform.class)
@SelectClasses({QSOTest.class, LogbookTest.class})
public class AllTests {

}