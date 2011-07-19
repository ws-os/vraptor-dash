package br.com.caelum.vraptor.dash.statement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class StatementDao {

	private final Session session;

	public StatementDao(Session session) {
		this.session = session;
	}

	public void valida(String hql) {
		try {
			session.createQuery(hql).list();
		} catch (Exception exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> execute(Statement st) {
		List<Object[]> results = session.createQuery(st.getHql()).list();
		if (!results.isEmpty()) {
			Object object = results.get(0);
			if (object.getClass().isArray()) {
				return results;
			}
		}
		List<Object[]> novos = new ArrayList<Object[]>();
		for (Object o : results) {
			novos.add(new Object[] { o });
		}
		return novos;
	}

	public void save(Statement statement) {
		session.save(statement);
	}

	public void merge(Statement statement) {
		session.merge(statement);
	}

	public Statement load(String idStatement) {
		return (Statement) session.load( Statement.class, idStatement);
	}

	@SuppressWarnings("unchecked")
	public List<Statement> all() {
		return session.createQuery("from Statement").setCacheable(true).list();
	}

}
