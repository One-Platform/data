package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSampleWithIdClass is a Querydsl query type for SampleWithIdClass
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSampleWithIdClass extends EntityPathBase<SampleWithIdClass> {

    private static final long serialVersionUID = -1608899549;

    public static final QSampleWithIdClass sampleWithIdClass = new QSampleWithIdClass("sampleWithIdClass");

    public final NumberPath<Long> first = createNumber("first", Long.class);

    public final BooleanPath isNew = createBoolean("isNew");

    public final NumberPath<Long> second = createNumber("second", Long.class);

    public QSampleWithIdClass(String variable) {
        super(SampleWithIdClass.class, forVariable(variable));
    }

    public QSampleWithIdClass(Path<? extends SampleWithIdClass> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSampleWithIdClass(PathMetadata<?> metadata) {
        super(SampleWithIdClass.class, metadata);
    }

}

