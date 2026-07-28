package com.kahu.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Arrays;
import java.util.List;

/**
 * Carga y valida las variables de entorno del archivo .env una sola vez.
 */
public final class EnvConfig {

    private static final EnvConfig INSTANCE = new EnvConfig();

    private final Dotenv dotenv;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;
    private final int appPort;
    private final boolean showSql;
    private final String jwtSecret;
    private final long jwtExpirationHours;
    private final String appEnv;
    private final List<String> corsOrigins;

    private EnvConfig() {
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.dbUrl = requireNonBlank("DB_URL");
        this.dbUser = requireNonBlank("DB_USER");
        this.dbPassword = require("DB_PASSWORD");
        this.appPort = Integer.parseInt(dotenv.get("APP_PORT", "8080"));
        this.showSql = Boolean.parseBoolean(dotenv.get("SHOW_SQL", "false"));
        this.jwtSecret = requireNonBlank("JWT_SECRET");
        this.jwtExpirationHours = Long.parseLong(dotenv.get("JWT_EXPIRATION_HOURS", "24"));
        this.appEnv = dotenv.get("APP_ENV", "development");
        String corsRaw = dotenv.get("CORS_ORIGINS", "");
        this.corsOrigins = corsRaw.isBlank()
                ? List.of()
                : Arrays.stream(corsRaw.split(",")).map(String::trim).filter(s -> !s.isBlank()).toList();
    }

    public static EnvConfig get() {
        return INSTANCE;
    }

    private String require(String key) {
        String value = dotenv.get(key);
        if (value == null) {
            throw new IllegalStateException(
                    "Variable de entorno obligatoria no definida: " + key + ". Revisa tu archivo .env");
        }
        return value;
    }

    private String requireNonBlank(String key) {
        String value = require(key);
        if (value.isBlank()) {
            throw new IllegalStateException(
                    "Variable de entorno no puede estar vacia: " + key + ". Revisa tu archivo .env");
        }
        return value;
    }

    public boolean isProduction() {
        return "production".equalsIgnoreCase(appEnv);
    }

    public String getDbUrl() { return dbUrl; }
    public String getDbUser() { return dbUser; }
    public String getDbPassword() { return dbPassword; }
    public int getAppPort() { return appPort; }
    public boolean isShowSql() { return showSql; }
    public String getJwtSecret() { return jwtSecret; }
    public long getJwtExpirationHours() { return jwtExpirationHours; }
    public String getAppEnv() { return appEnv; }
    public List<String> getCorsOrigins() { return corsOrigins; }
}
