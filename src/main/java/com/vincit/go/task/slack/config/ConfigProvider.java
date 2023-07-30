package com.vincit.go.task.slack.config;

import com.vincit.go.task.slack.model.ChannelType;
import com.vincit.go.task.slack.model.ColorType;
import com.vincit.go.task.slack.utils.Required;
import com.vincit.go.task.slack.utils.Secure;

import java.util.HashMap;
import java.util.Map;

import static com.vincit.go.task.slack.config.FieldUtils.createField;

public class ConfigProvider {
    private static ConfigProvider instance;
    public static final String CHANNEL = "Channel";
    public static final String CHANNEL_TYPE = "ChannelType";
    public static final String MESSAGE = "Message";
    public static final String TITLE = "Title";
    public static final String ICON_OR_EMOJI = "IconOrEmoji";
    public static final String WEBHOOK_URL = "WebhookUrl";
    public static final String DISPLAY_NAME = "DisplayName";
    public static final String COLOR = "Color";
    public static final String COLOR_TYPE = "ColorType";
    public static final String MARKDOWN_IN_TEXT = "MarkdownInText";
    public static final String FAIL_ON_ERROR = "FailOnError";

    private static Map<String, HashMap<String, Object>> fieldConfig = new HashMap<>();

    static {
        fieldConfig.put(WEBHOOK_URL, createField("", Secure.NO, Required.YES));
        fieldConfig.put(CHANNEL, createField("", Secure.NO, Required.YES));
        fieldConfig.put(CHANNEL_TYPE, createField(ChannelType.CHANNEL.getDisplayValue(), Secure.NO, Required.YES));
        fieldConfig.put(TITLE, createField("", Secure.NO, Required.NO));
        fieldConfig.put(ICON_OR_EMOJI, createField("", Secure.NO, Required.NO));
        fieldConfig.put(MESSAGE, createField("", Secure.NO, Required.NO));
        fieldConfig.put(DISPLAY_NAME, createField("", Secure.NO, Required.NO));
        fieldConfig.put(COLOR_TYPE, createField(ColorType.NONE.getDisplayValue(), Secure.NO, Required.YES));
        fieldConfig.put(COLOR, createField("", Secure.NO, Required.NO));
        fieldConfig.put(MARKDOWN_IN_TEXT, createField("", Secure.NO, Required.NO));
        fieldConfig.put(FAIL_ON_ERROR, createField("", Secure.NO, Required.NO));
    }

        
    
    private ConfigProvider(){
        
    }
    
    public static ConfigProvider getInstance(){
        if(instance==null){
            instance=new ConfigProvider();
        }
        return instance;
    }
    
    
    
        public static Map<String, ?> getFieldConfig() {
        return fieldConfig;
    }

}
