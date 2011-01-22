package com.procippus.ivy.model;
/*
 * 
 * Copyright 2011 Procippus, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.awt.Color;

/**
 * @author Procippus, LLC
 * @author Ryan McGuinness
 */
public class PieValue {
    double value;
    Color color;
    public PieValue(double value, Color color) {
        this.value = value;
        this.color = color;
    }
	public double getValue() {
		return value;
	}
	public Color getColor() {
		return color;
	}
}