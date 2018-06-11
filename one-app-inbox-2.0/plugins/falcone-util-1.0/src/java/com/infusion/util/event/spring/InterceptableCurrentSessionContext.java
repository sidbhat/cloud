package com.infusion.util.event.spring;

import org.hibernate.context.CurrentSessionContext;
import org.hibernate.classic.Session;
import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.orm.hibernate3.SpringSessionContext;
import com.infusion.util.domain.event.hibernate.InterceptableSessionFactory;

/**
 * This class is used by SessionFactoryImpl to locate the current session for a sessionFactory.  We were running into
 * problems where this class was being passed the wrapped SessionFactoryImpl instead of the wrapper IntercepableSessionFactory.
 * <BR>
 * Our spring factory bean will store the wrapper class in a threadlocal variable so it can be picked up here.  There was
 * no other way to get this to work.
 */
public class InterceptableCurrentSessionContext extends SpringSessionContext{
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    public static ThreadLocal<InterceptableSessionFactory> sessionFactoryTL = new ThreadLocal<InterceptableSessionFactory>();

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    public InterceptableCurrentSessionContext(SessionFactoryImplementor sessionFactory) {
        super(sessionFactoryTL.get() != null ? sessionFactoryTL.get() : sessionFactory);
    }
}
