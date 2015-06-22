import org.junit.Test;
import org.lecture.compiler.testframework.AbstractTest;

public class HugoTest extends AbstractTest {

  @Test
  public void testBlubb() {
    Object o = super.createObject("Hugo");
    super.executeMethod(o,"sayHi");
  }
}