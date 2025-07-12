package sirttas.elementalcraft.api.sync;

import javax.annotation.Nullable;

public abstract class AbstractSynchronizable implements ISynchronizable {

    @Nullable
    private final Runnable syncCallback;

    protected AbstractSynchronizable(@Nullable Runnable syncCallback) {
        this.syncCallback = syncCallback;
    }

    public void markDirty() {
        if (syncCallback != null) {
            syncCallback.run();
        }
    }
}
