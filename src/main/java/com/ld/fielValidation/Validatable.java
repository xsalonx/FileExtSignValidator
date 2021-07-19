package com.ld.fielValidation;

import java.util.Optional;

public interface Validatable {
    boolean canValidate();
    Optional<Boolean> validate();

}
