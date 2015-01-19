package tmz.dao;

import tmz.model.InventTable;

public interface InventTableDAO {
    void persistScu(InventTable inventTable);

    InventTable findScuByCompetitor(String id); //поиск товара по продавцу-конкуренту

    InventTable findScu(InventTable inventTable); // поиск товара по артикулу

    void updateScu(InventTable inventTable);

    void deleteScu(InventTable inventTable);
}
