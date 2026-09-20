package org.kwn.suricata.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
// check: https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web.basic.domain-class-converter
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfiguration {
}
