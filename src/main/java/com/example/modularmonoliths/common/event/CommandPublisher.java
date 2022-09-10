/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2022.
 */

package com.example.modularmonoliths.common.event;

public interface CommandPublisher {

    void publish(Command command);

}
