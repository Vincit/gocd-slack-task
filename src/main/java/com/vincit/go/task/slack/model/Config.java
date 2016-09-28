package com.vincit.go.task.slack.model;

import com.google.gson.annotations.SerializedName;
import com.vincit.go.task.slack.config.ConfigProvider;
import com.vincit.go.task.slack.utils.EnvVarReplacer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Config {

    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$[\\w0-9_]+");
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("[a-fA-F0-9]{3}|[a-fA-F0-9]{6}");

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
    @SerializedName("MarkdownInText")
    private Property markdownInText;

    public Config() {
    }

    public Config(Property webhookUrl, Property channel, Property channelType, Property displayName, Property title, Property message, Property iconOrEmoji, Property colorType, Property color, Property markdownInText) {
        this.message = message;
        this.title = title;
        this.iconOrEmoji = iconOrEmoji;
        this.channel = channel;
        this.channelType = channelType;
        this.webhookUrl = webhookUrl;
        this.displayName = displayName;
        this.colorType = colorType;
        this.color = color;
        this.markdownInText = markdownInText;
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

        message.validate(ConfigProvider.MESSAGE, "Message is required", errors);
        title.validate(ConfigProvider.TITLE, "Title is required", errors);
        iconOrEmoji.validate(ConfigProvider.ICON_OR_EMOJI, "Icon or emoji is required", errors);
        channel.validate(ConfigProvider.CHANNEL, "Channel is required", errors);
        channelType.validate(ConfigProvider.CHANNEL_TYPE, "Channel type is required", errors);
        webhookUrl.validate(ConfigProvider.WEBHOOK_URL, "Webhook URL is required", errors);
        displayName.validate(ConfigProvider.DISPLAY_NAME, "Display name is required", errors);
        colorType.validate(ConfigProvider.COLOR_TYPE, "Color type is required", errors);
        color.validate(ConfigProvider.COLOR, "Color is required", errors);

        if (color.isPresent()) {
            boolean isEnvVar = ENV_VAR_PATTERN.matcher(color.getValue()).matches();
            boolean isColorCode = COLOR_CODE_PATTERN.matcher(color.getValue()).matches();

            if (!isColorCode && !isEnvVar) {
                errors.put(ConfigProvider.COLOR, "Color must be given as three/six hexadecimals (without # prefix) or it must be an environment variable");
            }
        }

        if (colorType.isPresent() && colorType.getValue().equalsIgnoreCase(ColorType.CUSTOM.name())) {
            if (!color.isPresent()) {
                errors.put(ConfigProvider.COLOR, "Color must be defined when using custom color");
            }
        }

        return errors;
    }


    public Set<MarkdownField> getMarkdownIns() {
        Set<MarkdownField> markdownIns = new HashSet<>();

        addFieldIfSet(markdownInText, MarkdownField.TEXT, markdownIns);

        return markdownIns;
    }

    private void addFieldIfSet(Property property, MarkdownField field, Set<MarkdownField> ins) {
        if (property != null && property.isPresent() && Boolean.valueOf(property.getValueOr("false"))) {
            ins.add(field);
        }
    }

    public Config replace(EnvVarReplacer envVarReplacer) {
        return new Config(
                webhookUrl.replace(envVarReplacer),
                channel.replace(envVarReplacer),
                channelType,
                displayName.replace(envVarReplacer),
                title.replace(envVarReplacer),
                message.replace(envVarReplacer),
                iconOrEmoji.replace(envVarReplacer),
                colorType,
                color.replace(envVarReplacer),
                markdownInText
        );
    }
}
