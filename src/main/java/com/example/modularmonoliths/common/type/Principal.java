package com.example.modularmonoliths.common.type;

import lombok.NonNull;
import lombok.experimental.Accessors;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Value;

@Value(staticConstructor = "of")
@Accessors(fluent = true)
public class Principal {
	
	@NonNull
	String stringValue;
	
}
