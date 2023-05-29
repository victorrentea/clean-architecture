package victor.training.clean.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Play {
  public static void main(String[] args) {

    List<String> list = new ArrayList<>();

    List<String> unmLisst = Collections.unmodifiableList(list);
    unmLisst.add("a"); // liskov substitution violation: the caller might not expect the exception.
  }
}
