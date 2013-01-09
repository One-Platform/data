package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QSpecialUser is a Querydsl query type for SpecialUser
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSpecialUser extends EntityPathBase<SpecialUser> {

    private static final long serialVersionUID = 1533893466;

    private static final PathInits INITS = PathInits.DIRECT;

    public static final QSpecialUser specialUser = new QSpecialUser("specialUser");

    public final QUser _super;

    //inherited
    public final BooleanPath active;

    //inherited
    public final NumberPath<Integer> age;

    //inherited
    public final SetPath<User, QUser> colleagues;

    //inherited
    public final DateTimePath<java.util.Date> createdAt;

    //inherited
    public final StringPath emailAddress;

    //inherited
    public final StringPath firstname;

    //inherited
    public final NumberPath<Integer> id;

    //inherited
    public final StringPath lastname;

    // inherited
    public final QUser manager;

    //inherited
    public final SetPath<Role, SimplePath<Role>> roles;

    public QSpecialUser(String variable) {
        this(SpecialUser.class, forVariable(variable), INITS);
    }

    public QSpecialUser(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSpecialUser(PathMetadata<?> metadata, PathInits inits) {
        this(SpecialUser.class, metadata, inits);
    }

    public QSpecialUser(Class<? extends SpecialUser> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new QUser(type, metadata, inits);
        this.active = _super.active;
        this.age = _super.age;
        this.colleagues = _super.colleagues;
        this.createdAt = _super.createdAt;
        this.emailAddress = _super.emailAddress;
        this.firstname = _super.firstname;
        this.id = _super.id;
        this.lastname = _super.lastname;
        this.manager = _super.manager;
        this.roles = _super.roles;
    }

}

