/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vincit.go.task.slack.executor;

import java.io.IOException;

/**
 *
 * @author alexc
 */
public class SlackExecutorDecorator implements SlackExecutorInterface{

    
    private SlackExecutorInterface component;
    
    public SlackExecutorDecorator(SlackExecutorInterface component){
        this.component=component;
    }
    @Override
    public void sendMessage(TaskSlackMessage message) throws IOException {
        component.sendMessage(message);
    }
    
    
}
