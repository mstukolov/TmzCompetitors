package tmz.dao;


import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tmz.model.InventTable;

@Transactional
@Repository("InventTableDAO")
public class InventTableDAOImpl implements InventTableDAO{

    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public void persistScu(InventTable inventTable) {
        sessionFactory.getCurrentSession().persist(inventTable);
    }

    @Override
    public InventTable findScuByCompetitor(String id) {
        return (InventTable) sessionFactory.getCurrentSession().get(InventTable.class, id);
    }

    @Override
    public InventTable findScu(InventTable inventTable) {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(InventTable.class);
        cr.add(Restrictions.eq("scu", inventTable.getScu()));
        if(cr.list().size() > 0) {
            return (InventTable) cr.list().get(0);
        }else{return null;}
        //return (InventTable) sessionFactory.getCurrentSession().get(InventTable.class, inventTable.getRecid());
    }

    @Override
    public void updateScu(InventTable inventTable) {
        sessionFactory.getCurrentSession().update(inventTable);
    }

    @Override
    public void deleteScu(InventTable inventTable) {
        sessionFactory.getCurrentSession().delete(inventTable);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
