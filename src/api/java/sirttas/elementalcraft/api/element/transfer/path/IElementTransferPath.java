package sirttas.elementalcraft.api.element.transfer.path;

public interface IElementTransferPath {

    boolean isValid();

    void transfer();

    default void renderDebugPath() {}
}
