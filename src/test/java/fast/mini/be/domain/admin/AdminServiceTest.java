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
        adminService.orderUpdate(new AdminRequest.orderUpdateDTO(id, status));

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
        adminService.orderUpdate(new AdminRequest.orderUpdateDTO(id, status));

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
            adminService.orderUpdate(new AdminRequest.orderUpdateDTO(id, status));
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
            adminService.orderUpdate(new AdminRequest.orderUpdateDTO(id, status));
        });
    }

    @Test
    public void ok_orderListByStatus_WAIT_first_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(0,4);
        String status = "wait";

        // when
        Page<AdminResponse.orderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 0, "현재 페이지는 0번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.orderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");

    }

    @Test
    public void ok_orderListByStatus_WAIT_next_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(1,4);
        String status = "wait";

        // when
        Page<AdminResponse.orderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 1, "현재 페이지는 1번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.orderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
        assertEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT이며 label(한글)로 반환 한다");
    }

    @Test
    public void ok_orderListByStatus_NOT_WAIT_first_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(0,4);
        String status = "complete";

        // when
        Page<AdminResponse.orderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 0, "현재 페이지는 0번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.orderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertNotEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
    }

    @Test
    public void ok_orderListByStatus_NOT_WAIT_next_page() {
        // given
        Pageable pageable = (Pageable) PageRequest.of(1,4);
        String status = "complete";

        // when
        Page<AdminResponse.orderByStatusDTO> orderListByStatusDTO = adminService.orderListByStatus(status, pageable);

        // then
        assertEquals(orderListByStatusDTO.getNumber(), 1, "현재 페이지는 1번째이다");
        assertEquals(orderListByStatusDTO.getNumberOfElements(), 4, "한 페이지에 데이터는 4개이다.");

        List<AdminResponse.orderByStatusDTO> content = orderListByStatusDTO.getContent();
        assertNotEquals(content.get(0).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(1).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(2).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
        assertNotEquals(content.get(3).getStatus(), OrderStatus.WAIT.getLabel(), "요청 상태는 WAIT가 아니며 label(한글)로 반환 한다");
    }
}