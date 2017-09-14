package com.procippus.ivy;

import nu.xom.Element;

public interface XMLAdapter<T> {
    T fromElement(Element e);

    Element toElement(T fromClass);
}
