package tmz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tmz.dao.InventTableDAO;
import tmz.dao.PricesCompetitorsDAO;
import tmz.model.InventTable;
import tmz.model.PricesCompetitors;

@Service("inventTableService")
public class InventTableServiceImpl implements InventTableService{

    @Autowired
    InventTableDAO inventTableDAO;

    @Override
    @Transactional
    public void persistScu(InventTable _args) {
        inventTableDAO.persistScu(_args);
    }

    @Override
    public InventTable findScuByCompetitor(String _args) {
       return inventTableDAO.findScuByCompetitor(_args);
    }

    @Override
    public InventTable findScu(InventTable _args) {
        return inventTableDAO.findScu(_args);
    }

    @Override
    public void updateScu(InventTable _args) {
        inventTableDAO.updateScu(_args);
    }

    @Override
    public void deleteScu(InventTable _args) {
        inventTableDAO.deleteScu(_args);
    }


}
