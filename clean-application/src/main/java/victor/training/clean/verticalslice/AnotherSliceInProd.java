package victor.training.clean.verticalslice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnotherSliceInProd {
  private final GetCustomerByIdUseCase uc;

  public void method() {
    GetCustomerByIdUseCase.GetCustomerByIdResponse r = uc.findById(1);
  }
}
