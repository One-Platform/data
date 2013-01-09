package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuditableRole is a Querydsl query type for AuditableRole
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAuditableRole extends EntityPathBase<AuditableRole> {

    private static final long serialVersionUID = 805152641;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QAuditableRole auditableRole = new QAuditableRole("auditableRole");

    public final org.springframework.data.jpa.domain.QAbstractAuditable _super = new org.springframework.data.jpa.domain.QAbstractAuditable(this);

    public final QAuditableUser createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QAuditableUser lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath name = createString("name");

    public QAuditableRole(String variable) {
        this(AuditableRole.class, forVariable(variable), INITS);
    }

    public QAuditableRole(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAuditableRole(PathMetadata<?> metadata, PathInits inits) {
        this(AuditableRole.class, metadata, inits);
    }

    public QAuditableRole(Class<? extends AuditableRole> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new QAuditableUser(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.lastModifiedBy = inits.isInitialized("lastModifiedBy") ? new QAuditableUser(forProperty("lastModifiedBy"), inits.get("lastModifiedBy")) : null;
    }

}

