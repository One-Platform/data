package com.sinosoft.one.data.jpa.repository.query;

import com.sinosoft.one.data.jade.annotation.SQL;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-8-10
 * Time: 上午10:30
 * To change this template use File | Settings | File Templates.
 */
public class OneJadeQueryMethod extends QueryMethod {
    private Method method;
    /**
     * Creates a new {@link org.springframework.data.repository.query.QueryMethod} from the given parameters. Looks up the correct query to use for following
     * invocations of the method given.
     *
     * @param method   must not be {@literal null}
     * @param metadata must not be {@literal null}
     */
    public OneJadeQueryMethod(Method method, RepositoryMetadata metadata) {
        super(method, metadata);
        this.method = method;
    }

    String getAnnotatedSQL() {
        String sql = getAnnotationValue("value", String.class);
        return StringUtils.hasText(sql) ? sql : null;
    }

    private <T> T getAnnotationValue(String attribute, Class<T> type) {
        SQL annotation = method.getAnnotation(SQL.class);
        Object value = annotation == null ? AnnotationUtils.getDefaultValue(SQL.class, attribute) : AnnotationUtils
                .getValue(annotation, attribute);

        return type.cast(value);
    }

    public Method getMethod() {
        return method;
    }

	public String getSqlQueryName() {
		return method.getDeclaringClass().toString().split(" ")[1]+ "." + method.getName();
	}

}
