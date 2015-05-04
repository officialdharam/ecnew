package com.ec.dao;

import java.util.List;

public interface ExportDAO {

    public List<List<? extends Object>> exportDatabase();
}
