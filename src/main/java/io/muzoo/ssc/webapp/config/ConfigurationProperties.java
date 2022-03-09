package io.muzoo.ssc.webapp.config;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationProperties {

    private String databaseDriverClassName;
    private String databaseConnectionUrl;
    private String databaseUsername;
    private String databasePassword;

}