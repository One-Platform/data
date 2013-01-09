package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSampleEntityPK is a Querydsl query type for SampleEntityPK
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QSampleEntityPK extends BeanPath<SampleEntityPK> {

    private static final long serialVersionUID = -712868366;

    public static final QSampleEntityPK sampleEntityPK = new QSampleEntityPK("sampleEntityPK");

    public final StringPath first = createString("first");

    public final StringPath second = createString("second");

    public QSampleEntityPK(String variable) {
        super(SampleEntityPK.class, forVariable(variable));
    }

    public QSampleEntityPK(Path<? extends SampleEntityPK> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QSampleEntityPK(PathMetadata<?> metadata) {
        super(SampleEntityPK.class, metadata);
    }

}

