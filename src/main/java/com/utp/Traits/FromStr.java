package com.utp.Traits;

import io.vavr.control.Validation;

public interface FromStr<T> {
    public Validation<String,T> from_str(String input);
}
