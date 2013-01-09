package org.springframework.data.jpa.repository.support;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QJpaEntityInformationSupportUnitTests_NamedUser is a Querydsl query type for NamedUser
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QJpaEntityInformationSupportUnitTests_NamedUser extends EntityPathBase<JpaEntityInformationSupportUnitTests.NamedUser> {

    private static final long serialVersionUID = 555294021;

    public static final QJpaEntityInformationSupportUnitTests_NamedUser namedUser = new QJpaEntityInformationSupportUnitTests_NamedUser("namedUser");

    public QJpaEntityInformationSupportUnitTests_NamedUser(String variable) {
        super(JpaEntityInformationSupportUnitTests.NamedUser.class, forVariable(variable));
    }

    public QJpaEntityInformationSupportUnitTests_NamedUser(Path<? extends JpaEntityInformationSupportUnitTests.NamedUser> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QJpaEntityInformationSupportUnitTests_NamedUser(PathMetadata<?> metadata) {
        super(JpaEntityInformationSupportUnitTests.NamedUser.class, metadata);
    }

}

