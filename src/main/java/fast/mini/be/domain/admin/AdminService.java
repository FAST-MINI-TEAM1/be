package fast.mini.be.domain.admin;

import fast.mini.be.domain.admin.AdminResponse.OrderByStatusDTO;
import fast.mini.be.domain.approveDate.ApproveDate;
import fast.mini.be.domain.approveDate.ApproveDateRepository;
import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.order.OrderRepository;
import fast.mini.be.domain.order.OrderStatus;
import fast.mini.be.domain.user.Role;
import fast.mini.be.domain.user.User;
import fast.mini.be.domain.user.repository.UserRepository;
import fast.mini.be.global.erros.exception.Exception404;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fast.mini.be.domain.admin.AdminRequest.orderUpdateDTO;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final OrderRepository orderRepository;
    private final ApproveDateRepository approveDateRepository;
    private final UserRepository userRepository;

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
  
    public Page<OrderByStatusDTO> orderListByStatus(String status, Pageable pageable){
        Page<Order> orderList;
        if(OrderStatus.WAIT.name().equals(status.toUpperCase())){
            orderList= orderRepository.findAllByStatus(OrderStatus.WAIT, pageable);
        }else{
            orderList= orderRepository.findAllByStatusNot(OrderStatus.WAIT, pageable);
        }

        Page<OrderByStatusDTO> orderDTOList = OrderByStatusDTO.fromEntityList(orderList);
        return orderDTOList;
    }

    public List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotal(AdminRequest.MonthlyUserTotalDTO monthlyUserTotalDTO){
        // 모든 사원에 대한 사용 대장을 구한다
        List<User> users = userRepository.findAllByRole(Role.USER)
                .orElseThrow(()->{return new Exception404("사용자를 찾을 수 없습니다.");});

        List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotalDTOList = new ArrayList<>();
        for(User user : users){
            // 0으로 초기화된 월별 카운트 DTO
            AdminResponse.MonthCountDTO monthCountDTO = new AdminResponse.MonthCountDTO();

            // 해당 사원이 승인된 요청을 가지는지 찾고 월별 카운트를 갱신한다
            Optional<List<ApproveDate>> approveDateList = approveDateRepository.findApproveDatesForUserByOrderTypeInYear(
                            user.getId(), monthlyUserTotalDTO.getOrderType(), monthlyUserTotalDTO.getYear());
            approveDateList.ifPresent(monthCountDTO::count);

            // 해당 사원의 사용 대장을 추가한다
            monthlyUserTotalDTOList.add(new AdminResponse.MonthlyUserTotalDTO(user,monthCountDTO));
        }

        return monthlyUserTotalDTOList;
    }
}
