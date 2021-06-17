package victor.training.clean.quotation.infra;


import victor.training.clean.quotation.entity.CustomerVO;

public class CustomerServiceClient implements victor.training.clean.quotation.service.ExternalCustomerProvider {
   @Override
   public CustomerVO getCustomerById(long id) {
//      new RestTemplate(...)
      return null;
   }
}
