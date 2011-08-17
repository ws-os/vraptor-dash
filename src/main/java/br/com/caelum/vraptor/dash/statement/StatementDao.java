package br.com.caelum.vraptor.dash.statement;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import br.com.caelum.vraptor.ioc.Component;

@Component
public class StatementDao {

	private final Session session;

	public StatementDao(Session session) {
		this.session = session;
	}

	public void validate(String hql) {
		try {
			session.createQuery(hql).list();
		} catch (Exception exception) {
			throw new IllegalArgumentException(exception);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> execute(Statement st) {
		List results = createQuery(st.getHql()).list();
		if (!results.isEmpty() && results.get(0).getClass().isArray()) {
			return results;
		}
		List<Object[]> wrappedResults = new ArrayList<Object[]>();
		for (Object o : results) { 
			wrappedResults.add(new Object[] { o });
		}
		return wrappedResults;
	}

	public void save(Statement statement) {
		session.save(statement);
	}

	public void merge(Statement statement) {
		session.merge(statement);
	}

	public Statement load(String id) {
		return (Statement) session.load(Statement.class, id);
	}
	
	public void delete(Statement statement) {
		session.delete(statement);
	}

	@SuppressWarnings("unchecked")
	public List<Statement> all() {
		return createQuery("from DashStatement").setCacheable(true).setMaxResults(100).list();
	}
	
	private Query createQuery(String hql) {
		return session.createQuery(hql).setMaxResults(100);
	}

}
