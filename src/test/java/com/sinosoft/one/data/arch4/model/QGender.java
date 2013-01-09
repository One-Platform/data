package com.sinosoft.one.data.arch4.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QGender is a Querydsl query type for Gender
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QGender extends EntityPathBase<Gender> {

    private static final long serialVersionUID = 2029662962;

    public static final QGender gender = new QGender("gender");

    public final StringPath id = createString("id");

    public final StringPath name = createString("name");

    public QGender(String variable) {
        super(Gender.class, forVariable(variable));
    }

    public QGender(Path<? extends Gender> entity) {
        super(entity.getType(), entity.getMetadata());
    }

    public QGender(PathMetadata<?> metadata) {
        super(Gender.class, metadata);
    }

}

