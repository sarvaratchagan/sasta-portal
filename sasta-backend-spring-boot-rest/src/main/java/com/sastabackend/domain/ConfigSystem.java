package com.sastabackend.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

/**
 * Created by SARVA on 13/Nov/2015.
 */
@Entity(name = "config_system")
public class ConfigSystem implements java.io.Serializable {

    @Id
    @NotNull
    @Column(name = "name", nullable = false)
    private java.lang.String name;
    private java.lang.String value;
    private java.lang.String label;

    private Boolean allow_edit;
    private java.sql.Timestamp created_date;
    private java.sql.Timestamp modified_date;
    private Long create_by;
    private Long modify_by;

    public ConfigSystem(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getAllowEdit() {
        return allow_edit;
    }

    public void setAllowEdit(Boolean allow_edit) {
        this.allow_edit = allow_edit;
    }

    public Timestamp getCreatedDate() {
        return created_date;
    }

    public void setCreatedDate(Timestamp created_date) {
        this.created_date = created_date;
    }

    public Timestamp getModifiedDate() {
        return modified_date;
    }

    public void setModifiedDate(Timestamp modified_date) {
        this.modified_date = modified_date;
    }

    public Long getCreateBy() {
        return create_by;
    }

    public void setCreateBy(Long create_by) {
        this.create_by = create_by;
    }

    public Long getModifyBy() {
        return modify_by;
    }

    public void setModifyBy(Long modify_by) {
        this.modify_by = modify_by;
    }

    @Override
    public String toString() {
        return "ConfigSystem{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", allow_edit=" + allow_edit +
                ", created_date=" + created_date +
                ", modified_date=" + modified_date +
                ", create_by=" + create_by +
                ", modify_by=" + modify_by +
                '}';
    }
}
