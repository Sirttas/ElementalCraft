package sirttas.elementalcraft.api.element.transfer.path;

public class InvalidElementTransferPath implements IElementTransferPath {

    public static final InvalidElementTransferPath INSTANCE = new InvalidElementTransferPath();

    private InvalidElementTransferPath() {}

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void transfer() {
        // nothing to do
    }
}
