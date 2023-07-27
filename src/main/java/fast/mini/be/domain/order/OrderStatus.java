package fast.mini.be.domain.order;

import lombok.Getter;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Getter
public enum OrderStatus {
    WAIT("대기"),
    REJECT("반려"),
    APPROVE("승인");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public static OrderStatus findByLabel(String label){
        return Arrays.stream(OrderStatus.values())
                .filter(o->o.getLabel().equals(label))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
