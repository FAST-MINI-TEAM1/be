package fast.mini.be.domain.admin;

import fast.mini.be.domain.approveDate.ApproveDate;
import fast.mini.be.domain.order.Order;
import fast.mini.be.domain.order.OrderRepository;
import fast.mini.be.domain.order.OrderStatus;
import fast.mini.be.global.erros.exception.Exception400;
import fast.mini.be.global.erros.exception.Exception404;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AdminServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    AdminService adminService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void ok_order_update_to_REJECT() {
        // given
        Long id = 1L;
        String status = "반려";

        // when
        adminService.orderUpdate(new AdminRequest.OrderUpdateDTO(id, status));

        // then
        Order order = orderRepository.findById(id).get();
        assertEquals(OrderStatus.REJECT.name(), order.getStatus().name(), "요청 상태가 REJECT 여야한다.");
    }

    @Test
    public void ok_order_update_to_APPROVE() {
        // given
        Long id = 1L; // start: '2023-07-15', end: '2023-07-20'
        String status = "승인";

        // when
        adminService.orderUpdate(new AdminRequest.OrderUpdateDTO(id, status));

        // then
        Order order = orderRepository.findById(id).get();
        assertEquals(OrderStatus.APPROVE.name(), order.getStatus().name(), "요청 상태가 APPROVE 여야한다.");

        TypedQuery<ApproveDate> query = em.createQuery("SELECT ad FROM ApproveDate ad where ad.order.id = :id", ApproveDate.class);
        query.setParameter("id", order.getId());
        List<ApproveDate> approveDateList = query.getResultList();
        assertEquals(approveDateList.size(), 6, "승인된 날짜는 5일이다.");
        assertEquals(approveDateList.get(0).getDate(), LocalDateTime.of(2023, 7, 15, 0, 0));
        assertEquals(approveDateList.get(1).getDate(), LocalDateTime.of(2023, 7, 16, 0, 0));
        assertEquals(approveDateList.get(2).getDate(), LocalDateTime.of(2023, 7, 17, 0, 0));
        assertEquals(approveDateList.get(3).getDate(), LocalDateTime.of(2023, 7, 18, 0, 0));
        assertEquals(approveDateList.get(4).getDate(), LocalDateTime.of(2023, 7, 19, 0, 0));
        assertEquals(approveDateList.get(5).getDate(), LocalDateTime.of(2023, 7, 20, 0, 0));
    }

    @Test
    public void fail_order_update_wrong_orderId() {
        // given
        Long id = 500L;
        String status = "승인";

        // when
        // then
        assertThrows(Exception404.class, () -> {
            adminService.orderUpdate(new AdminRequest.OrderUpdateDTO(id, status));
        });
    }

    @Test
    public void fail_order_update_wrong_status() {
        // given
        Long id = 1L;
        String status = "삭제";

        // when
        // then
        assertThrows(Exception400.class, () -> {
            adminService.orderUpdate(new AdminRequest.OrderUpdateDTO(id, status));
        });
    }

    @Test
    public void ok_orderListByStatus_WAIT_first_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(0, 4);
        String status = "wait";

        // when
        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 0, "현재 페이지는 0번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.OrderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");

    }

    @Test
    public void ok_orderListByStatus_WAIT_next_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(1, 4);
        String status = "wait";

        // when
        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 1, "현재 페이지는 1번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.OrderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
    }

    @Test
    public void ok_orderListByStatus_NOT_WAIT_first_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(0, 4);
        String status = "complete";

        // when
        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 0, "현재 페이지는 0번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.OrderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertNotEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
    }

    @Test
    public void ok_orderListByStatus_NOT_WAIT_next_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(1, 4);
        String status = "complete";

        // when
        Page<AdminResponse.OrderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 1, "현재 페이지는 1번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.OrderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertNotEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
    }

    @Test
    public void ok_monthlyUserTotal_annual() {
        // given
        String orderType = "annual";
        int year = 2023;
        AdminRequest.MonthlyUserTotalDTO monthlyUserTotalDTO = new AdminRequest.MonthlyUserTotalDTO(orderType, year);

        // when
        List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotalDTOList = adminService.monthlyUserTotal(monthlyUserTotalDTO);

        // then
        AdminResponse.MonthlyUserTotalDTO userTotalDTO = monthlyUserTotalDTOList.get(1);// id=2인 유저
        assertEquals(userTotalDTO.getMonth().getJan(), 6L, "1월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getFeb(), 0L, "2월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getMar(), 0L, "3월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getApr(), 0L, "4월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getMay(), 5L, "5월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getJun(), 0L, "6월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getJul(), 0L, "7월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getAug(), 0L, "8월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getSept(), 0L, "9월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getOct(), 0L, "10월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getNov(), 4L, "11월 연차 사용 내역");
        assertEquals(userTotalDTO.getMonth().getDec(), 0L, "12월 연차 사용 내역");
        assertEquals(userTotalDTO.getTotal(), 15L, "total 연차 사용 내역");
    }

    @Test
    public void ok_monthlyUserTotal_duty() {
        // given
        String orderType = "duty";
        int year = 2023;
        AdminRequest.MonthlyUserTotalDTO monthlyUserTotalDTO = new AdminRequest.MonthlyUserTotalDTO(orderType, year);

        // when
        List<AdminResponse.MonthlyUserTotalDTO> monthlyUserTotalDTOList = adminService.monthlyUserTotal(monthlyUserTotalDTO);

        // then
        AdminResponse.MonthlyUserTotalDTO userTotalDTO = monthlyUserTotalDTOList.get(1);// id=2인 유저
        assertEquals(userTotalDTO.getMonth().getJan(), 1L, "1월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getFeb(), 0L, "2월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getMar(), 0L, "3월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getApr(), 0L, "4월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getMay(), 0L, "5월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getJun(), 0L, "6월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getJul(), 0L, "7월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getAug(), 1L, "8월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getSept(), 0L, "9월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getOct(), 0L, "10월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getNov(), 0L, "11월 당직 사용 내역");
        assertEquals(userTotalDTO.getMonth().getDec(), 0L, "12월 당직 사용 내역");
        assertEquals(userTotalDTO.getTotal(), 2L, "total 당직 사용 내역");
    }

    @Test
    public void ok_dailyOrderList_duty() {
        // given
        String orderType = "duty";
        int year = 2023;
        int month = 8;
        AdminRequest.DailyOrderListDTO dailyOrderListDTO = new AdminRequest.DailyOrderListDTO(orderType, year, month);

        // when
        List<AdminResponse.DailyOrderDTO> dailyOrderDTOList = adminService.dailyOrderList(dailyOrderListDTO);

        // then
        assertEquals(dailyOrderDTOList.size(), 2, "년월이 일치하는 당직 승인 개수");
        dailyOrderDTOList.forEach(
                o -> assertTrue(o.getDate().matches("^2023-08-(0[1-9]|[1-2][0-9]|3[0-1])$"))
        );
    }

    @Test
    public void ok_userSearch_name() {
        // given
        String name = "박지훈";
        String empNo = null;
        AdminRequest.UserSearchDTO requestDTO = new AdminRequest.UserSearchDTO(name, empNo);

        // when
        AdminResponse.UserSearchDTO userSearchDTO = adminService.userSearch(requestDTO);

        // then
        assertEquals(userSearchDTO.getId(), 1, "유저 아이디");
        assertEquals(userSearchDTO.getEmpName(), "박지훈", "유저 이름");
        assertEquals(userSearchDTO.getEmpNo(), "20200001", "유저 사원번호");
        // 더미 데이터에 createdAt 지정한 값이 아닌 현재 시간이 들어가는 오류로 확인 불가
//        assertEquals(userSearchDTO.getCreatedAt(), "2020-11-29", "유저 입사일");
    }

    @Test
    public void ok_userSearch_empno() {
        // given
        String name = null;
        String empNo = "20200001";
        AdminRequest.UserSearchDTO requestDTO = new AdminRequest.UserSearchDTO(name, empNo);

        // when
        AdminResponse.UserSearchDTO userSearchDTO = adminService.userSearch(requestDTO);

        // then
        assertEquals(userSearchDTO.getId(), 1, "유저 아이디");
        assertEquals(userSearchDTO.getEmpName(), "박지훈", "유저 이름");
        assertEquals(userSearchDTO.getEmpNo(), "20200001", "유저 사원번호");
        // 더미 데이터에 createdAt 지정한 값이 아닌 현재 시간이 들어가는 오류로 확인 불가
//        assertEquals(userSearchDTO.getCreatedAt(), "2020-11-29", "유저 입사일");
    }

    @Test
    public void fail_userSearch_empno() {
        // given
        String name = null;
        String empNo = "11110001";
        AdminRequest.UserSearchDTO requestDTO = new AdminRequest.UserSearchDTO(name, empNo);

        // when
        // then
        assertThrows(Exception404.class, () -> {
            adminService.userSearch(requestDTO);
        });
    }
}