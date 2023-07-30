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
public class SlackExecutorTimingDecorator extends SlackExecutorDecorator{
    private SlackExecutorInterface component;
    public SlackExecutorTimingDecorator(SlackExecutorInterface component) {
        super(component);
    }
    @Override
    public void sendMessage(TaskSlackMessage message) throws IOException {
        long startTime=System.currentTimeMillis();
        super.sendMessage(message);
        long endTime=System.currentTimeMillis();
        System.out.println("Tiempo de Ejecución de SlackExecutor"+(endTime - startTime)+ "ms");
    }
}
