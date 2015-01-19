package tmz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tmz.dao.PricesCompetitorsDAO;
import tmz.model.PricesCompetitors;

@Service("pricesCompetitorsService")
public class PricesCompetitorsServiceImpl implements PricesCompetitorsService{

    @Autowired
    PricesCompetitorsDAO pricesCompetitorsDAO;

    @Override
    @Transactional
    public void persistPrices(PricesCompetitors pricesCompetitors) {
        pricesCompetitorsDAO.persistPrices(pricesCompetitors);
    }

    @Override
    @Transactional
    public PricesCompetitors findPricesByCompetitor(String id) {
        return pricesCompetitorsDAO.findPricesByCompetitor(id);
    }

    @Override
    public PricesCompetitors findPrice(Integer id) {
        return pricesCompetitorsDAO.findPrice(id);
    }

    @Override
    @Transactional
    public void updatePrices(PricesCompetitors pricesCompetitors) {
        pricesCompetitorsDAO.updatePrices(pricesCompetitors);
    }

    @Override
    @Transactional
    public void deletePrices(PricesCompetitors pricesCompetitors) {
        pricesCompetitorsDAO.deletePrices(pricesCompetitors);
    }
}
