package fast.mini.be.domain.order;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

	private String orderType;
	private String startAt;
	private String endAt;
	private String reason;
	private Category category;
	private String etc;

}
