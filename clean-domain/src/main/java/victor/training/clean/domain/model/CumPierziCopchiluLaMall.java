package victor.training.clean.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

public class CumPierziCopchiluLaMall {
  public static void main(String[] args) {
    Set<Copil> puiiMei = new HashSet<>();

    Copil copil = new Copil("Emma-Simona", "xyz");
    puiiMei.add(copil);
    System.out.println(puiiMei.contains(copil));
    System.out.println(puiiMei.size());

    copil.setName("Emma");//hibernate ii pune @Id cand fac .save(e)
    System.out.println("Imi recunosc copilul: " + puiiMei.contains(copil));

    puiiMei.add(copil);
    System.out.println(puiiMei.size());
  }
}
@Data // in scarba
  @AllArgsConstructor
class Copil {
  private String name;
  private String cnp;
}
