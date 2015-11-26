package br.com.tcc.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import br.com.tcc.model.vo.DataValor;
import br.com.tcc.modelo.Medida;
import br.com.tcc.repository.filter.MedidaFilter;

public class MedidaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;
	Integer mesAtual = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
	String dataAtual = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());

	@SuppressWarnings("unchecked")
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDias) {
		// pegar numero de dias correto, pois o numeroDias aparece sempre com um
		// dia a mais.
		numeroDias -= 1;

		Session session = entityManager.unwrap(Session.class);

		// Data Inicial = Hoje
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, numeroDias * -1);

		// Criando um Mapa vazio
		Map<Date, BigDecimal> resultado = criarMapaVazio(numeroDias, dataInicial);

		Criteria criteria = session.createCriteria(Medida.class);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.sqlGroupProjection("date(hora) as data", "date(hora)", new String[] { "hora" },
						new Type[] { StandardBasicTypes.DATE }))
				.add(Projections.avg("valor").as("media"))).add(Restrictions.ge("hora", dataInicial.getTime()));

		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class))
				.list();

		for (DataValor dataValor : valoresPorData) {
			resultado.put(dataValor.getData(), dataValor.getValor());

		}

		return resultado;

	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDias, Calendar horaInicial) {

		// Clonando, senão a dataInciail.add não retorna nada
		horaInicial = (Calendar) horaInicial.clone();
		Map<Date, BigDecimal> mapaInicial = new TreeMap<>();

		for (int i = 0; i <= numeroDias; i++) {
			mapaInicial.put(horaInicial.getTime(), BigDecimal.ZERO);
			horaInicial.add(Calendar.DAY_OF_WEEK, 1);
		}

		return mapaInicial;
	}

	@SuppressWarnings("unchecked")
	public List<Medida> filtrados(MedidaFilter filtro) {
		Session session = this.entityManager.unwrap(Session.class);

		Criteria criteria = session.createCriteria(Medida.class);

		if (filtro.getDataCriacaoDe() != null) {
			criteria.add(Restrictions.ge("data", filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			criteria.add(Restrictions.le("data", filtro.getDataCriacaoAte()));
		}
		if (filtro.getData() != null) {
			criteria.add(Restrictions.eq("data", filtro.getData()));
		}

		return criteria.addOrder(Order.asc("id")).list();
	}

	public Medida porId(Long id) {
		return this.entityManager.find(Medida.class, id);
	}

	public List<Medida> listarTodos() {
		// TODO Auto-generated method stub
		return this.entityManager.createQuery("from Medida", Medida.class).getResultList();
	}

	public List<Medida> porData() {
		return this.entityManager.createQuery("select m from Medida m where m.data = '2015-10-30'", Medida.class)
				.getResultList();
	}

	public List<Medida> dataAtual() {
		String dataAtual = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
		return this.entityManager.createQuery("select m from Medida m where m.data = '" + dataAtual + "'", Medida.class)
				.getResultList();
	}

	public List<Medida> data(String data) {
		data = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
		return this.entityManager.createQuery("select m from Medida m where m.data = '" + data + "'", Medida.class)
				.getResultList();
	}

	public List<Medida> periodo(String dataInicial, String dataFinal) {
		dataInicial = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
		dataFinal = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
		return this.entityManager.createQuery(
				"select m from Medida m where m.data >= '" + dataInicial + "' and m.data <= '" + dataFinal + "'",
				Medida.class).getResultList();
	}

	public List<Medida> mesAtual() {
		return this.entityManager
				.createQuery("select m from Medida m where month(m.data) = '" + mesAtual + "'", Medida.class)
				.getResultList();
	}

	public Float maxMedidaMes() {
		// Integer mes = Integer.parseInt(mesAtual);
		GregorianCalendar atual = new GregorianCalendar();
		Date mesAtual = new Date(atual.getTime().getMonth());
		Session session = entityManager.unwrap(Session.class);
		Criteria c = session.createCriteria(Medida.class);
		c.setProjection(Projections.max("valor"));
		return (Float) c.uniqueResult();
	}

	public Float minMedidaMes() {
		GregorianCalendar atual = new GregorianCalendar();
		Date dataAtual = new Date(atual.getTime().getYear(), atual.getTime().getMonth(), atual.getTime().getDate());
		Session session = entityManager.unwrap(Session.class);
		Criteria c = session.createCriteria(Medida.class);
		c.setProjection(Projections.min("valor")).add(Restrictions.eq("data", dataAtual));
		return (Float) c.uniqueResult();
	}

	public Float maxMedidaDia() {
		GregorianCalendar atual = new GregorianCalendar();
		Date dataAtual = new Date(atual.getTime().getYear(), atual.getTime().getMonth(), atual.getTime().getDate());
		Session session = entityManager.unwrap(Session.class);
		Criteria c = session.createCriteria(Medida.class);
		c.setProjection(Projections.max("valor")).add(Restrictions.eq("data", dataAtual));
		return (Float) c.uniqueResult();
	}

	public Float minMedidaDia() {
		GregorianCalendar atual = new GregorianCalendar();
		Date dataAtual = new Date(atual.getTime().getYear(), atual.getTime().getMonth(), atual.getTime().getDate());
		Session session = entityManager.unwrap(Session.class);
		Criteria c = session.createCriteria(Medida.class);
		c.setProjection(Projections.min("valor")).add(Restrictions.eq("data", dataAtual));
		return (Float) c.uniqueResult();
	}

	public Float temperaturaAtual() {
		Session session = entityManager.unwrap(Session.class);
		Object lista = entityManager
				.createQuery("select m.valor from Medida as m where m.id in(select max(id) from Medida)")
				.getSingleResult();
		return (Float) lista;
	}

}
