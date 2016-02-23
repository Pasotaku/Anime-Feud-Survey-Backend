package com.pasotaku.main;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class AnimeFeudConfiguration extends Configuration {
    @NotEmpty
    private String template;

    @NotEmpty
    private String defaultName = "Stranger";
    
    @NotEmpty
    private String applicationName;
    
    @JsonProperty
    public String getTemplate() {
        return template;
    }
    
    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }
    
    @JsonProperty
	public String getApplicationName() {
		return applicationName;
	}

    @JsonProperty
    public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}