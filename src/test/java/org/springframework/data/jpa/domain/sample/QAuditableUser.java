package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAuditableUser is a Querydsl query type for AuditableUser
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAuditableUser extends EntityPathBase<AuditableUser> {

    private static final long serialVersionUID = 805245654;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QAuditableUser auditableUser = new QAuditableUser("auditableUser");

    public final org.springframework.data.jpa.domain.QAbstractAuditable _super = new org.springframework.data.jpa.domain.QAbstractAuditable(this);

    public final QAuditableUser createdBy;

    //inherited
    public final DateTimePath<java.util.Date> createdDate = _super.createdDate;

    public final StringPath firstname = createString("firstname");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QAuditableUser lastModifiedBy;

    //inherited
    public final DateTimePath<java.util.Date> lastModifiedDate = _super.lastModifiedDate;

    public final SetPath<AuditableRole, QAuditableRole> roles = this.<AuditableRole, QAuditableRole>createSet("roles", AuditableRole.class, QAuditableRole.class);

    public QAuditableUser(String variable) {
        this(AuditableUser.class, forVariable(variable), INITS);
    }

    public QAuditableUser(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QAuditableUser(PathMetadata<?> metadata, PathInits inits) {
        this(AuditableUser.class, metadata, inits);
    }

    public QAuditableUser(Class<? extends AuditableUser> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.createdBy = inits.isInitialized("createdBy") ? new QAuditableUser(forProperty("createdBy"), inits.get("createdBy")) : null;
        this.lastModifiedBy = inits.isInitialized("lastModifiedBy") ? new QAuditableUser(forProperty("lastModifiedBy"), inits.get("lastModifiedBy")) : null;
    }

}

