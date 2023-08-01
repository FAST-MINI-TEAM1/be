package fast.mini.be.domain.order;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponse {
	private Long id;
	private String orderType;
	private String startDate;
	private String endDate;
	private String status;
}
