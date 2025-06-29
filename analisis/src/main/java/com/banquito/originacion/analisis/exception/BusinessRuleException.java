package com.banquito.originacion.analisis.exception;

public class BusinessRuleException extends RuntimeException {

    private final String rule;
    private final String detail;

    public BusinessRuleException(String rule, String detail) {
        super();
        this.rule = rule;
        this.detail = detail;
    }

    @Override
    public String getMessage() {
        return "Regla de negocio violada: " + this.rule + ". Detalle: " + this.detail;
    }

    public String getRule() {
        return rule;
    }

    public String getDetail() {
        return detail;
    }
} 