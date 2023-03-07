package com.digiteo.neovoteIV.model.jpa.data;

import lombok.Getter;

@Getter
public enum ElectionStatus {

    NOT_INIT("NO INICIADO"), IN_PROGRESS("EN PROGRESO"), FINISHED("FINALIZADO"), SUSPENDED("SUSPENDIDO");

    private String code;

    private ElectionStatus(String code){
        this.code = code;
    }

}
