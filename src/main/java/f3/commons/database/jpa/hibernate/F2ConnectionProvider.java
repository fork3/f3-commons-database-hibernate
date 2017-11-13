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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;

import f3.commons.database.AbstractConnectionFactory;
import lombok.RequiredArgsConstructor;

/**
 * @author n3k0nation
 *
 */
@RequiredArgsConstructor
class F2ConnectionProvider implements ConnectionProvider, Configurable, Stoppable {
	private static final long serialVersionUID = 2643406330684533670L;
	
	private final AbstractConnectionFactory acf;
	
	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return ConnectionProvider.class.equals(unwrapType)
				|| F2ConnectionProvider.class.isAssignableFrom(unwrapType)
				|| DataSource.class.isAssignableFrom(unwrapType);
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		if(ConnectionProvider.class.equals(unwrapType) || F2ConnectionProvider.class.equals(unwrapType)) {
			return (T) this;
		}
		
		if(DataSource.class.isAssignableFrom(unwrapType)) {
			return (T) acf.getDataSource();
		}
		
		throw new UnknownUnwrapTypeException(unwrapType);
	}

	@Override
	public void stop() {
		acf.shutdown();
	}

	@Override
	public void configure(Map configurationValues) {
	}

	@Override
	public Connection getConnection() throws SQLException {
		return acf.getConnection();
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

}
