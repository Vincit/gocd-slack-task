/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vincit.go.task.slack.executor;

import java.io.IOException;

/**
 *
 * @author alexc
 */
public interface SlackExecutorInterface {
    void sendMessage(TaskSlackMessage message) throws IOException;
}
