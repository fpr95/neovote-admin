package com.digiteo.neovoteIV.model.jpa.data;

import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum ElectionStatus {

    NOT_INIT("NO INICIADO"), IN_PROGRESS("EN PROGRESO"), FINISHED("FINALIZADO"), SUSPENDED("SUSPENDIDO");

    private String code;
    private static final Map<String, ElectionStatus> MAP_CODE_ENUM;

    private ElectionStatus(String code){
        this.code = code;
    }

    // impl from Effective Java
    static {
        Map<String, ElectionStatus> map = new HashMap<String, ElectionStatus>();
        for(ElectionStatus e : ElectionStatus.values()){
            map.put(e.getCode().toUpperCase(), e);
        }
        MAP_CODE_ENUM = Collections.unmodifiableMap(map);
    }

    public static ElectionStatus getEnumFromCode(String code){
        return MAP_CODE_ENUM.get(code.toUpperCase());
    }

}
