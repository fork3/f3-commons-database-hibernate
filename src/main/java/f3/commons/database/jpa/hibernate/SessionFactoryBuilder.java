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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import f3.commons.IBuilder;
import f3.commons.classpath.ClasspathScanner;
import f3.commons.database.AbstractConnectionFactory;
import lombok.Getter;

/**
 * @author n3k0nation
 *
 */
public class SessionFactoryBuilder implements IBuilder<SessionFactory> {
	public static SessionFactoryBuilder builder() {
		return new SessionFactoryBuilder();
	}
	
	@Getter private AbstractConnectionFactory connectionFactory;
	@Getter private String dialect;
	private List<Class<?>> mappings = new ArrayList<>();
	
	private SessionFactoryBuilder() {
	}
	
	public SessionFactoryBuilder setConnectionFactory(AbstractConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		return this;
	}
	
	public SessionFactoryBuilder setDialect(String dialect) {
		this.dialect = dialect;
		return this;
	}
	
	public SessionFactoryBuilder addMapping(Class<?> clazz) {
		mappings.add(clazz);
		return this;
	}
	
	public SessionFactoryBuilder addPackageMapping(String _package) {
		mappings.addAll(ClasspathScanner.getClasses((pkg, name) -> pkg.startsWith(_package), ClasspathScanner.getClassPathURL())
				.stream()
				.filter(cls -> cls.isAnnotationPresent(Entity.class))
				.collect(Collectors.toList())
		);
		return this;
	}
	

	@Override
	public SessionFactory build() {
		if(connectionFactory == null) {
			throw new RuntimeException("Connection factory is null!");
		}
		
		if(dialect == null || dialect.isEmpty()) {
			throw new RuntimeException("Invalid dialect!");
		}
		
		final SessionFactory manager = new SessionFactory();
		manager.setDialect(dialect);
		manager.setConnectionFactory(connectionFactory);
		manager.init(mappings);
		return manager;
	}
	
	
	
}
