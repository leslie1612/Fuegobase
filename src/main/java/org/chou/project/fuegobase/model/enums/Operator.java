package org.chou.project.fuegobase.model.enums;

import lombok.Getter;

@Getter
public enum Operator {
    EQUAL("="),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_OR_EQUAL(">="),
    LESS_THAN_OR_EQUAL("<="),
    CONTAINS("CONTAINS");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

}
