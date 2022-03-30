package victor.training.clean.facade;

import java.util.List;

public class FactoryMethodDeCe {

   public static void main(String[] args) {


//      new Optional<>
      List<Integer> list = List.of(1);
      List<Integer> list2 = List.of(1,2,3,4);
      Long l1 = Long.valueOf(127);
      Long l2 = Long.valueOf(127);

      System.out.println(l1 == l2);
   }
}
