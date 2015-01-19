package tmz.service;

import tmz.model.PricesCompetitors;

public interface PricesCompetitorsService {
    void persistPrices(PricesCompetitors pricesCompetitors);

    PricesCompetitors findPricesByCompetitor(String id);

    PricesCompetitors findPrice(Integer id);

    void updatePrices(PricesCompetitors pricesCompetitors);

    void deletePrices(PricesCompetitors pricesCompetitors);
}
