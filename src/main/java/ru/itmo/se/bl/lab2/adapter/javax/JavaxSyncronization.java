package ru.itmo.se.bl.lab2.adapter.javax;

import javax.transaction.Synchronization;
import java.util.Objects;

public class JavaxSyncronization implements Synchronization {
    private final jakarta.transaction.Synchronization synchronization;

    public JavaxSyncronization(jakarta.transaction.Synchronization synchronization) {
        Objects.nonNull(synchronization);

        this.synchronization = synchronization;
    }

    @Override
    public void beforeCompletion() {
        synchronization.beforeCompletion();
    }

    @Override
    public void afterCompletion(int status) {
        synchronization.afterCompletion(status);
    }
}
