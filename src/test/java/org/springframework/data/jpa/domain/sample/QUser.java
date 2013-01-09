package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1918844245;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QUser user = new QUser("user");

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final SetPath<User, QUser> colleagues = this.<User, QUser>createSet("colleagues", User.class, QUser.class);

    public final DateTimePath<java.util.Date> createdAt = createDateTime("createdAt", java.util.Date.class);

    public final StringPath emailAddress = createString("emailAddress");

    public final StringPath firstname = createString("firstname");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath lastname = createString("lastname");

    public final QUser manager;

    public final SetPath<Role, SimplePath<Role>> roles = this.<Role, SimplePath<Role>>createSet("roles", Role.class, SimplePath.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QUser(PathMetadata<?> metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.manager = inits.isInitialized("manager") ? new QUser(forProperty("manager"), inits.get("manager")) : null;
    }

}

