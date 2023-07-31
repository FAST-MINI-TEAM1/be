package fast.mini.be.domain.admin;

import fast.mini.be.domain.admin.AdminResponse.orderByStatusDTO;
import fast.mini.be.domain.approveDate.ApproveDate;
import fast.mini.be.domain.approveDate.ApproveDateRepository;
import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.order.OrderRepository;
import fast.mini.be.domain.order.OrderStatus;
import fast.mini.be.global.erros.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static fast.mini.be.domain.admin.AdminRequest.orderUpdateDTO;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final OrderRepository orderRepository;
    private final ApproveDateRepository approveDateRepository;

    public void orderUpdate(orderUpdateDTO orderUpdateDTO) {
        Order orderPS = orderRepository.findById(orderUpdateDTO.getId())
                .orElseThrow(() -> new Exception404("결재 요청을 찾을 수 없습니다."));

        OrderStatus status = orderUpdateDTO.getStatus();
        orderPS.statusUpdate(status);

        if (status == OrderStatus.APPROVE) {
            List<LocalDateTime> dateList = orderPS.getDateList();

            for (LocalDateTime date : dateList) {
                ApproveDate approveDate = ApproveDate.builder()
                        .order(orderPS)
                        .user(orderPS.getUser())
                        .date(date)
                        .build();
                approveDateRepository.save(approveDate);
            }
        }
    }

    public Page<orderByStatusDTO> orderListByStatus(String status, Pageable pageable){
        Page<Order> orderList;
        if(OrderStatus.WAIT.name().equals(status.toUpperCase())){
            orderList= orderRepository.findAllByStatus(OrderStatus.WAIT, pageable);
        }else{
            orderList= orderRepository.findAllByStatusNot(OrderStatus.WAIT, pageable);
        }

        Page<orderByStatusDTO> orderDTOList = orderByStatusDTO.fromEntityList(orderList);
        return orderDTOList;
    }
}
