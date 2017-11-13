/*
 * Copyright (c) 2010-2017 fork3
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR 
 * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package f3.commons.database.jpa.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;

import f3.commons.database.AbstractConnectionFactory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author n3k0nation
 *
 */
public class SessionFactory {
	
	@Getter @Setter(AccessLevel.PROTECTED) private String dialect;
	@Getter @Setter(AccessLevel.PROTECTED) private AbstractConnectionFactory connectionFactory;
	
	@Getter private org.hibernate.SessionFactory sessionFactory;
	
	protected SessionFactory() {
	}
	
	protected void init(List<Class<?>> mappings) {
		final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySetting("hibernate.dialect", dialect)
					.applySetting("hibernate.current_session_context_class", "thread")
					.applySetting(AvailableSettings.CONNECTION_PROVIDER, new F2ConnectionProvider(connectionFactory))
					.build();
		
		final MetadataSources metaSrc = new MetadataSources(serviceRegistry);
		mappings.forEach(metaSrc::addAnnotatedClass);
		final Metadata meta = metaSrc.getMetadataBuilder()
				.applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
				.build();
		
		sessionFactory = meta.getSessionFactoryBuilder()
				.build();
	}
	
	public Session getSession() throws HibernateException {
		Session session = sessionFactory.getCurrentSession();
		if(session == null) {
			session = sessionFactory.openSession();
		}
		return session;
	}
	
	public void shutdown() {
		sessionFactory.close();
	}
	
}
