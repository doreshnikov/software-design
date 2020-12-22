package profiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PackageAspect {

    public PackageAspect() {
    }

    private final StatCollector collector = StatCollector.getInstance();
    private final String packageName = "package " + StatCollector.getPackageName();

    @Pointcut("execution(!static * *(..)) && !within(profiler..*)")
    public void profileFilter() {
    }

    @Around("profileFilter()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (!signature.getDeclaringType().getPackage().toString().startsWith(packageName)) {
            return joinPoint.proceed();
        }
        collector.push(signature);
        try {
            Object returnValue = joinPoint.proceed();
            collector.pop(signature);
            return returnValue;
        } catch (Throwable e) {
            collector.fail(signature);
            return null;
        }
    }

}
