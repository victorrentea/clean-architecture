package victor.training.clean.domain.service;

public class Base {
}

class Sub2 extends Base {

}
class StringUtils {

  protected static String removeSpaces(String s) {
    return s.toLowerCase().replace("\\s+", "");
  }
}