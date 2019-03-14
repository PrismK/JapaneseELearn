package com.prismk.japaneseelearn.managers;

import org.greenrobot.eventbus.EventBus;

public class EventBusMananger {
    private static EventBus eventBus=EventBus.getDefault();

    public static void register(Object object){
        eventBus.register(object);
    }
    public static void unregister(Object object){
        eventBus.unregister(object);
    }
    public static void post(Object object){
        eventBus.post(object);
    }
}
