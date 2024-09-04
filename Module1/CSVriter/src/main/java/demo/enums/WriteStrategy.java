package demo.enums;

/*
 * Enum для выделения двух вариантов записи объектов в файл - создавая новый файл - WRITE, или дописывая старый - APPEND
 */
public enum WriteStrategy {
    WRITE(false), APPEND(true);

    private final boolean appendFlag;

    WriteStrategy(boolean flag) {
        this.appendFlag = flag;
    }

    public boolean get() {
        return appendFlag;
    }

    @Override
    public String toString() {
        return String.valueOf(appendFlag);
    }
}