package com.gustavoavila.coopvote.api.controller.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {
    private Integer status;
    private String title;
    private String detail;
    private List<Field> fieldsErrors;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setFieldsErrors(List<Field> fieldsErrors) {
        this.fieldsErrors = fieldsErrors;
    }

    public List<Field> getFieldsErrors() {
        return fieldsErrors;
    }

    public static class Field {
        private String name;
        private String message;

        public Field(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public String getName() {
            return name;
        }

        public String getMessage() {
            return message;
        }
    }
}
