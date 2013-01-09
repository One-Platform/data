package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSampleEntity is a Querydsl query type for SampleEntity
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSampleEntity extends EntityPathBase<SampleEntity> {

    private static final long serialVersionUID = 1286407607;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QSampleEntity sampleEntity = new QSampleEntity("sampleEntity");

    public final QSampleEntityPK id;

    public QSampleEntity(String variable) {
        this(SampleEntity.class, forVariable(variable), INITS);
    }

    public QSampleEntity(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSampleEntity(PathMetadata<?> metadata, PathInits inits) {
        this(SampleEntity.class, metadata, inits);
    }

    public QSampleEntity(Class<? extends SampleEntity> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QSampleEntityPK(forProperty("id")) : null;
    }

}

