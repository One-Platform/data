package org.springframework.data.jpa.support;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity is a Querydsl query type for SampleEntity
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity extends EntityPathBase<ClasspathScanningPersistenceUnitPostProcessorUnitTests.SampleEntity> {

    private static final long serialVersionUID = 1986192332;

    public static final QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity sampleEntity = new QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity("sampleEntity");

    public QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity(String variable) {
        super(ClasspathScanningPersistenceUnitPostProcessorUnitTests.SampleEntity.class, forVariable(variable));
    }

    public QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity(Path<? extends ClasspathScanningPersistenceUnitPostProcessorUnitTests.SampleEntity> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QClasspathScanningPersistenceUnitPostProcessorUnitTests_SampleEntity(PathMetadata<?> metadata) {
        super(ClasspathScanningPersistenceUnitPostProcessorUnitTests.SampleEntity.class, metadata);
    }

}

