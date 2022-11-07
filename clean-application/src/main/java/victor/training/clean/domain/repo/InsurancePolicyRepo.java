package victor.training.clean.domain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.clean.domain.model.InsurancePolicy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface InsurancePolicyRepo extends JpaRepository<InsurancePolicy, Long> {
}


//class WhoLetThoDogsOut {
//    // who implements this interface above??!?!?!?!?!!?
//    public static void main(String[] args) {
//        InsurancePolicyRepo repo = Proxy.newProxyInstance(InsurancePolicyRepo.class.getClassLoader(),
//                new CLass<?>[]{InsurancePolicyRepo.class}, new InvocationHandler() {
//                    @Override
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        return null;
//                    }
//                });
//    }
//}