package com.lightbc.databasej.service;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.lightbc.databasej.entity.SaveSql;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 持久化数据信息
 */
@Data
@State(name = "DataBaseJPersistentService", storages = {@Storage("DataBaseJ-Settings.xml")})
public class DataBaseJPersistentService implements PersistentStateComponent<DataBaseJPersistentService> {
    private List<SaveSql> saveSqlList;

    public DataBaseJPersistentService() {
        init();
    }

    private void init() {
        this.saveSqlList = new ArrayList<>();
    }

    public static DataBaseJPersistentService getInstance() {
        return ServiceManager.getService(DataBaseJPersistentService.class);
    }

    @Nullable
    @Override
    public DataBaseJPersistentService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull DataBaseJPersistentService dataBaseJPersistentService) {
        XmlSerializerUtil.copyBean(dataBaseJPersistentService, this);
    }
}
