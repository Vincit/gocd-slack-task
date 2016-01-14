package com.vincit.go.task.slack.model;

import com.google.gson.annotations.SerializedName;

public class Config {

    @SerializedName("Message")
    private Property message;
    @SerializedName("Title")
    private Property title;
    @SerializedName("IconOrEmoji")
    private Property iconOrEmoji;
    @SerializedName("Channel")
    private Property channel;
    @SerializedName("ChannelType")
    private Property channelType;
    @SerializedName("WebhookUrl")
    private Property webhookUrl;
    @SerializedName("DisplayName")
    private Property displayName;
    @SerializedName("ColorType")
    private Property colorType;
    @SerializedName("Color")
    private Property color;

    public Config() {
    }

    private String getValueOr(Property property, String value) {
        if (property != null) {
            return property.getValueOr(value);
        } else {
            return value;
        }
    }

    public String getMessage() {
        return getValueOr(message, null);
    }

    public String getTitle() {
        return getValueOr(title, null);
    }

    public String getIconOrEmoji() {
        return getValueOr(iconOrEmoji, null);
    }

    public String getChannel() {
        return getValueOr(channel, null);
    }

    public String getWebhookUrl() {
        return getValueOr(webhookUrl, null);
    }

    public String getDisplayName() {
        return getValueOr(displayName, null);
    }

    public ColorType getColorType() {
        String value = getValueOr(colorType, null);
        if (value != null) {
            return ColorType.valueOf(value);
        } else {
            return null;
        }
    }

    public String getCustomColor() {
        return getValueOr(color, null);
    }

    public String getColor() {
        ColorType colorType = getColorType();
        switch (colorType) {
            case NONE: return null;
            case GOOD:
            case WARNING:
            case DANGER:
                return colorType.name().toLowerCase();
            case CUSTOM:
                return getCustomColor();
            default:
                throw new IllegalArgumentException("Invalid color mode");
        }
    }

    public ChannelType getChannelType() {
        String value = getValueOr(channelType, null);
        if (value != null) {
            return ChannelType.valueOf(value);
        } else {
            return null;
        }
    }
}
