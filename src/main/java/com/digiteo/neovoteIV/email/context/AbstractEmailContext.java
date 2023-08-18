package com.digiteo.neovoteIV.email.context;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractEmailContext {

    private String from;
    private String to;
    private String subject;
    private String email;
    private String attachment;
    private String fromDisplayName;
    private String emailLanguage;
    private String displayName;
    private String templateLocation;
    private Map<String, Object> context;

    public AbstractEmailContext(){
        this.context = new HashMap<>();
    }

    public <T> void init(T context){
        //here goes the base URL and context(admin entity instance)
    }

    public <T> void initVoter(T context){
        //here goes the base URL and context(voter entity instance)
    }

    public void put(String key, Object value){
        this.context.put(key, value);
    }
}
