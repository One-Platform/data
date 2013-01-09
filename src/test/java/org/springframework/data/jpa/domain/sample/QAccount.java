package org.springframework.data.jpa.domain.sample;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = 1539069027;

    public static final QAccount account = new QAccount("account");

    public final org.springframework.data.jpa.domain.QAbstractPersistable _super = new org.springframework.data.jpa.domain.QAbstractPersistable(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QAccount(String variable) {
        super(Account.class, forVariable(variable));
    }

    public QAccount(Path<? extends Account> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QAccount(PathMetadata<?> metadata) {
        super(Account.class, metadata);
    }

}

