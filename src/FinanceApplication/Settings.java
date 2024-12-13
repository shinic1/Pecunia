package FinanceApplication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Settings {
    private static Settings instance;

    private final StringProperty currency;
    private final BooleanProperty isLightMode;
    private final BooleanProperty notificationsEnabled;
    
    private Settings() {
        this.currency = new SimpleStringProperty("USD");
        this.isLightMode = new SimpleBooleanProperty(true);
        this.notificationsEnabled = new SimpleBooleanProperty(true);
    }

    public static Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public BooleanProperty isLightModeProperty() {
        return isLightMode;
    }

    public BooleanProperty notificationsEnabledProperty() {
        return notificationsEnabled;
    }
    
    public String getCurrency() {
        return currency.get();
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public boolean isLightMode() {
        return isLightMode.get();
    }

    public void setLightMode(boolean isLightMode) {
        this.isLightMode.set(isLightMode);
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled.get();
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled.set(notificationsEnabled);
    }
}
