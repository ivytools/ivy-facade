package com.procippus.ivy.graphics;
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
import java.io.Serializable;

/**
 * This is a derived work, with no one to actual cite.
 * Example based on inner class found at: http://www.exampledepot.com/egs/java.awt/Pie.html
 * 
 * 
 * @author Procippus, LLC
 * @author Ryan McGuinness  <i>[ryan@procippus.com]</i>
 */
public class PieValue implements Serializable {
	private static final long serialVersionUID = -1635165974214061436L;
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