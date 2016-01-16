package com.vincit.go.task.slack;

import com.thoughtworks.go.plugin.api.GoApplicationAccessor;
import com.thoughtworks.go.plugin.api.GoPlugin;
import com.thoughtworks.go.plugin.api.GoPluginIdentifier;
import com.thoughtworks.go.plugin.api.exceptions.UnhandledRequestTypeException;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractTaskPlugin implements GoPlugin {

    protected static final String CONFIGURATION = "configuration";
    protected static final String VALIDATE = "validate";
    protected static final String EXECUTE = "execute";
    protected static final String VIEW = "view";

    private final String extensionId;
    private final List<String> supportedVersions;

    public AbstractTaskPlugin(String extensionId, String... supportedVersions) {
        this.extensionId = extensionId;
        this.supportedVersions = Arrays.asList(supportedVersions);
    }

    @Override
    public void initializeGoApplicationAccessor(GoApplicationAccessor goApplicationAccessor) {
    }

    @Override
    public GoPluginIdentifier pluginIdentifier() {
        return new GoPluginIdentifier(extensionId, supportedVersions);
    }

    @Override
    public GoPluginApiResponse handle(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        if (CONFIGURATION.equals(request.requestName())) {
            return handleGetConfigRequest(request);
        } else if (VALIDATE.equals(request.requestName())) {
            return handleValidation(request);
        } else if (EXECUTE.equals(request.requestName())) {
            return handleTaskExecution(request);
        } else if (VIEW.equals(request.requestName())) {
            return handleTaskView(request);
        }
        throw new UnhandledRequestTypeException(request.requestName());
    }

    protected GoPluginApiResponse handleTaskView(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        throw new UnhandledRequestTypeException(VIEW);
    }

    protected GoPluginApiResponse handleTaskExecution(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        throw new UnhandledRequestTypeException(EXECUTE);
    }

    protected GoPluginApiResponse handleValidation(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        throw new UnhandledRequestTypeException(VALIDATE);
    }

    protected GoPluginApiResponse handleGetConfigRequest(GoPluginApiRequest request) throws UnhandledRequestTypeException {
        throw new UnhandledRequestTypeException(CONFIGURATION);
    }

}
