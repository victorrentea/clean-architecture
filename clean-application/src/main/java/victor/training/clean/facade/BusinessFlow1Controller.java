package victor.training.clean.facade;

import lombok.experimental.Delegate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessFlow1Controller {
//   Mapper mapper;
   BusinessFlow1Facade facade;
   public void http1() {
      facade.taie();
   }
   public void http2() {

   }
}

class BusinessFlow1Facade  {
   @Delegate
   private TaiePesteService taiePesteService;
   public void http1() {
      taiePesteService.taie();
   }
}

interface ITaiePesteService {
   void taie();
}
interface ITaiePesteSubtireService {
   void taieSubtire();
}
class TaiePesteService implements ITaiePesteService, ITaiePesteSubtireService{
   public void taie() {
privata();
   }
   private void privata() {

   }
   public void taieSubtire() {

   }
}
class PrajestePeste {

}
