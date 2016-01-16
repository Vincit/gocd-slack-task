package com.vincit.go.task.slack.model;

import com.google.gson.annotations.SerializedName;
import com.vincit.go.task.slack.SlackTaskPlugin;

import java.util.HashMap;
import java.util.Map;

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

    public Config(Property message, Property title, Property iconOrEmoji, Property channel, Property channelType, Property webhookUrl, Property displayName, Property colorType, Property color) {
        this.message = message;
        this.title = title;
        this.iconOrEmoji = iconOrEmoji;
        this.channel = channel;
        this.channelType = channelType;
        this.webhookUrl = webhookUrl;
        this.displayName = displayName;
        this.colorType = colorType;
        this.color = color;
    }

    private String getValueOr(Property property, String value) {
        if (property != null) {
            return property.getValueOr(value);
        } else {
            return value;
        }
    }

    public String getMessage() {
        return getValueOr(message, "");
    }

    public String getTitle() {
        return getValueOr(title, "");
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
        return getValueOr(displayName, "");
    }

    public ColorType getColorType() {
        String value = getValueOr(colorType, null);
        if (value != null) {
            return ColorType.valueOf(value.toUpperCase());
        } else {
            return null;
        }
    }

    public String getCustomColor() {
        return getValueOr(color, null);
    }

    public String getColor() {
        ColorType colorType = getColorType();
        if (colorType == null) {
            return null;
        }
        
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
            return ChannelType.valueOf(value.toUpperCase());
        } else {
            return null;
        }
    }

    public Map<String, String> validate() {
        Map<String, String> errors = new HashMap<>();

        message.validate(SlackTaskPlugin.MESSAGE, "Message is required", errors);
        title.validate(SlackTaskPlugin.TITLE, "Title is required", errors);
        iconOrEmoji.validate(SlackTaskPlugin.ICON_OR_EMOJI, "Icon or emoji is required", errors);
        channel.validate(SlackTaskPlugin.CHANNEL, "Channel is required", errors);
        channelType.validate(SlackTaskPlugin.CHANNEL_TYPE, "Channel type is required", errors);
        webhookUrl.validate(SlackTaskPlugin.WEBHOOK_URL, "Webhook URL is required", errors);
        displayName.validate(SlackTaskPlugin.DISPLAY_NAME, "Display name is required", errors);
        colorType.validate(SlackTaskPlugin.COLOR_TYPE, "Color type is required", errors);
        color.validate(SlackTaskPlugin.COLOR, "Color is required", errors);

        if (color.isPresent()) {
            if (!color.getValue().matches("[a-fA-F0-9]{6}")) {
                errors.put(SlackTaskPlugin.COLOR, "Color must be given as six hexadecimals (without # prefix)");
            }
        }

        if (colorType.isPresent() && colorType.getValue().equalsIgnoreCase(ColorType.CUSTOM.name())) {
            if (!color.isPresent()) {
                errors.put(SlackTaskPlugin.COLOR, "Color must be defined when using custom color");
            }
        }

        return errors;
    }


}
