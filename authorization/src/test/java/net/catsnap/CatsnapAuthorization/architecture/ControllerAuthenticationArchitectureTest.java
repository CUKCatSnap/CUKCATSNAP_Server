package net.catsnap.CatsnapAuthorization.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import net.catsnap.shared.auth.Authentication;
import org.springframework.web.bind.annotation.RestController;

/**
 * 컨트롤러 인증 아키텍처 테스트
 * <p>
 * 모든 컨트롤러 메서드가 Authentication 메타 어노테이션을 가져야 함을 검증합니다.
 * 테스트 코드 내의 컨트롤러는 검증 대상에서 제외됩니다.
 * </p>
 */
@AnalyzeClasses(
    packages = "net.catsnap.CatsnapAuthorization",
    importOptions = {ImportOption.DoNotIncludeTests.class}
)
class ControllerAuthenticationArchitectureTest {

    /**
     * 모든 컨트롤러의 public 메서드는 Authentication 메타 어노테이션을 가져야 합니다.
     * <p>
     * Authentication 메타 어노테이션을 가진 어노테이션:
     * <ul>
     *   <li>@Admin</li>
     *   <li>@AnyUser</li>
     *   <li>@LoginUser</li>
     *   <li>@LoginPhotographer</li>
     *   <li>@LonginModel</li>
     * </ul>
     * </p>
     */
    @ArchTest
    static final ArchRule 모든_컨트롤러_메서드는_Authentication_어노테이션을_가져야_한다 =
        methods()
            .that().areDeclaredInClassesThat().areAnnotatedWith(RestController.class)
            .and().arePublic()
            .and().areDeclaredInClassesThat().resideInAPackage("..presentation..")
            .should(haveAuthenticationMetaAnnotation())
            .because("모든 API 엔드포인트는 명시적인 권한 정의가 필요합니다");

    /**
     * Authentication 메타 어노테이션을 가지고 있는지 확인하는 커스텀 조건
     */
    private static ArchCondition<JavaMethod> haveAuthenticationMetaAnnotation() {
        return new ArchCondition<>("have @Authentication meta-annotation") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean hasAuthenticationAnnotation = method.getAnnotations().stream()
                    .anyMatch(
                        ControllerAuthenticationArchitectureTest::hasAuthenticationMetaAnnotation);

                if (!hasAuthenticationAnnotation) {
                    String message = String.format(
                        "메서드 %s.%s()는 @Authentication 메타 어노테이션이 필요합니다. " +
                            "(@Admin, @AnyUser, @LoginUser, @LoginPhotographer, @LonginModel 중 하나를 사용하세요)",
                        method.getOwner().getSimpleName(),
                        method.getName()
                    );
                    events.add(SimpleConditionEvent.violated(method, message));
                }
            }
        };
    }

    /**
     * 어노테이션이 Authentication 메타 어노테이션을 가지고 있는지 확인
     */
    private static boolean hasAuthenticationMetaAnnotation(JavaAnnotation<?> annotation) {
        return annotation.getRawType().isAnnotatedWith(Authentication.class);
    }
}
