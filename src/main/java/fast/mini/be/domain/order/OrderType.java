package fast.mini.be.domain.order;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum OrderType {
    ANNUAL("연차"),
    DUTY("당직");

    private final String label;

    OrderType(String label) {
        this.label = label;
    }

    public static OrderType findByLabel(String label){
        return Arrays.stream(OrderType.values())
                .filter(o->o.getLabel().equals(label))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
