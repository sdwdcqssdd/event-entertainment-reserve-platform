package com.team20.backend.dto;

import java.util.Date;
import java.util.List;

/**
 * EventQuery
 用于搜索时的查询条件
 实际上没有相关的表，只是用于查询
 */
public class EventQuery {
    private List<String> organizerNames;

    private List<Integer> OrganizerIds;
    private List<Integer> categoryIds;

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<Integer> getOrganizerIds() {
        return OrganizerIds;
    }

    public void setOrganizerIds(List<Integer> organizerIds) {
        OrganizerIds = organizerIds;
    }

    private List<Date> dates;

    public List<Integer> getVenueIds() {
        return venueIds;
    }

    public void setVenueIds(List<Integer> venueIds) {
        this.venueIds = venueIds;
    }

    private List<Integer> venueIds;

    public EventQuery() {
    }

    public List<String> getOrganizerNames() {
        return organizerNames;
    }

    public void setOrganizerNames(List<String> organizerNames) {
        this.organizerNames = organizerNames;
    }

    public List<Date> getDates() {
        return dates;
    }

    public void setDates(List<Date> dates) {
        this.dates = dates;
    }

    public List<String> getVenueNames() {
        return venueNames;
    }

    public void setVenueNames(List<String> venueNames) {
        this.venueNames = venueNames;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private List<String> venueNames;
    private String keyword;


}