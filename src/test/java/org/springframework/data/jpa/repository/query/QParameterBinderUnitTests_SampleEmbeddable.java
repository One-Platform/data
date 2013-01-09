package org.springframework.data.jpa.repository.query;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QParameterBinderUnitTests_SampleEmbeddable is a Querydsl query type for SampleEmbeddable
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QParameterBinderUnitTests_SampleEmbeddable extends BeanPath<ParameterBinderUnitTests.SampleEmbeddable> {

    private static final long serialVersionUID = -2123541703;

    public static final QParameterBinderUnitTests_SampleEmbeddable sampleEmbeddable = new QParameterBinderUnitTests_SampleEmbeddable("sampleEmbeddable");

    public final StringPath bar = createString("bar");

    public final StringPath foo = createString("foo");

    public QParameterBinderUnitTests_SampleEmbeddable(String variable) {
        super(ParameterBinderUnitTests.SampleEmbeddable.class, forVariable(variable));
    }

    public QParameterBinderUnitTests_SampleEmbeddable(Path<? extends ParameterBinderUnitTests.SampleEmbeddable> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QParameterBinderUnitTests_SampleEmbeddable(PathMetadata<?> metadata) {
        super(ParameterBinderUnitTests.SampleEmbeddable.class, metadata);
    }

}

