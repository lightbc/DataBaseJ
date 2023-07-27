package com.lightbc.databasej.actions;

import com.intellij.openapi.actionSystem.DefaultActionGroup;

public class ContextGroup extends DefaultActionGroup {

    public ContextGroup() {
        super(new SaveSqlAction("Save Sql"), new UseSqlAction("Use Sql"), new DeleteSqlAction("Del Sql"));
    }

}
