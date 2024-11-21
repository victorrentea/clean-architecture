## Title
We manage transactions using custom aspects

## Status
Accepted

## Context
In Jonas transactions were done using AOP declared in XML files:

```
  <aop:config>
    <aop:pointcut id="slmBrManagerPointcut"
                  expression="execution(* com.francetelecom.malimagp.alarm.backend.business.slm.SlmBusinessRuleManager.*(..))" />
    <aop:advisor advice-ref="alarmTxAdviceRWRequired" pointcut-ref="slmBrManagerPointcut" />
    <aop:advisor advice-ref="coreReplicaTxAdviceRORequired" pointcut-ref="slmBrManagerPointcut" />
  </aop:config>
```
The initial solution was to decomission aspects and to use @Transactional on classes/methods.
The issues with this solution were:

## Cons
1. The transactions are hard to track
2. Impossible to mix with modern @Transactional-annotation-based tx management. When we extracted @Entities and some DAOs in external library(jar) in another package and @Transactional annotation was ignored

## Decision
1. We use AOP using @Around+@Aspect class based configuration
2. All transactions for one module must be in the same class
3. We use transaction template to manually create the transactions
4. Risk: renaming the class will break the transaction

```
    @Around("execution(* com.orange.malima.mgp.webservices.deviceinfo.quartz.iface.IDisBusinessForQuartzService.*(..))")
    public Object applyTransactionForDisBusinessForQuartzService(ProceedingJoinPoint pjp) throws Throwable {
        return handleCoreTxAdviceRWRequired(coreTransactionManager, pjp);
    }
```

```
    public static TransactionTemplate coreTxAdviceRWRequired(HibernateTransactionManager coreTransactionManager) {
        TransactionTemplate definition = new TransactionTemplate(coreTransactionManager);
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        definition.setReadOnly(false);
        return definition;
    }
```

## Consequences
Positive:
- standardization
- transactions are grouped and easier to find, track and create
- AOPs are easier to migrate from the monolith

Negative:
- manual configuration instead of automatic one from spring

## Compliance
Manually through MR.