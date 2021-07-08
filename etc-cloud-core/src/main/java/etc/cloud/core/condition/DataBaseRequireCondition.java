package etc.cloud.core.condition;

import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.commons.toolkit.VersionUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * @author lm_xu
 */
public class DataBaseRequireCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String projectName = VersionUtils.getProjectLogoInfo().getRight();
        return Arrays.asList(CommonConstants.SPRING_BEAN_FILTER)
                .stream()
                .anyMatch(bean->!projectName.contains(bean));
    }
}
