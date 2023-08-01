package fast.mini.be.domain.admin;

import fast.mini.be.domain.order.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class AdminRequest {

    @Getter
    @Setter
    public static class orderUpdateDTO{
        @NotEmpty
        Long id;
        @NotEmpty
        OrderStatus status;

        public orderUpdateDTO(Long id, String status) {
            this.id = id;
            this.status = OrderStatus.findByLabel(status);
        }
    }
}
