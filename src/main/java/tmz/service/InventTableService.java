package tmz.service;

import tmz.model.InventTable;

public interface InventTableService {
    void persistScu(InventTable inventTable);

    InventTable findScuByCompetitor(String id);

    InventTable findScu(InventTable inventTable);

    void updateScu(InventTable inventTable);

    void deleteScu(InventTable inventTable);
}
