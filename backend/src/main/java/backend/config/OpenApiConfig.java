package backend.config;

import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.models.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class OpenApiConfig {

    @Bean
    public OperationCustomizer operationIdCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            // 核心逻辑：使用“控制器类名 + 方法名”作为operationId
            // 例如：ShotMaterialController.update
            String controllerName = handlerMethod.getBeanType().getSimpleName().toLowerCase().replace("controller", "");
            String methodName =  StringUtils.capitalize(handlerMethod.getMethod().getName());
            String customOperationId = controllerName + methodName;

            // 将自定义的operationId设置到模型中去
            operation.setOperationId(customOperationId);
            return operation;
        };
    }
}