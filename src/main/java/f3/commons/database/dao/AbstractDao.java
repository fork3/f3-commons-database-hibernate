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
package f3.commons.database.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;

import f3.commons.database.jpa.hibernate.CloseableTransaction;

/**
 * @author n3k0nation
 *
 */
public abstract class AbstractDao<Entity, Id extends Serializable> implements CrudDao<Entity, Id> {
	
	protected abstract Session getSession();
	protected abstract Class<Entity> getEntityClass();

	@Override
	public Entity findById(Id id) {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			return session.get(getEntityClass(), id);
		}
	}

	@Override
	public void save(Entity entity) {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			session.save(entity);
		}
	}
	
	@Override
	public void saveOrUpdate(Entity entity) {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			session.saveOrUpdate(entity);
		}
	}

	@Override
	public void update(Entity entity) {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			session.update(entity);
		}
	}

	@Override
	public void delete(Entity entity) {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			session.delete(entity);
		}
	}

	@Override
	public List<Entity> findAll() {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			return session.createQuery("SELECT a FROM " + getEntityClass().getSimpleName() + " a").list();
		}
	}

	@Override
	public void deleteAll() {
		try(Session session = getSession();
				CloseableTransaction trx = CloseableTransaction.begin(session)) {
			session.createQuery("DELETE FROM " + getEntityClass().getSimpleName()).executeUpdate();
		}
	}

}
