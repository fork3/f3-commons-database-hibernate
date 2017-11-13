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

import java.io.Closeable;

import javax.persistence.RollbackException;

import org.hibernate.Session;
import org.hibernate.Transaction;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @deprecated use hibernate configuration for commit transaction on close.
 * @author n3k0nation
 *
 */
@Deprecated
@RequiredArgsConstructor
public class CloseableTransaction implements Transaction, Closeable, AutoCloseable {
	public static CloseableTransaction begin(Session session) {
		return new CloseableTransaction(session.beginTransaction());
	}
	
	@Delegate private final Transaction transaction;
	
	@Override
	public void close() throws RollbackException {
		if(transaction.isActive()) {
			transaction.commit();
		}
	}
}
