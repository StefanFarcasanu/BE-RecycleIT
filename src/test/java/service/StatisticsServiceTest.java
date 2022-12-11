package service;

import com.client.CountyClient;
import com.domain.dto.CountyDTO;
import com.domain.dto.CountyStatisticsDTO;
import com.domain.entity.RecycleRequestEntity;
import com.domain.entity.UserEntity;
import com.domain.entity.VoucherEntity;
import com.domain.enums.RoleEnum;
import com.domain.enums.StatusEnum;
import com.repo.RecycleRequestRepository;
import com.repo.UserRepository;
import com.repo.VoucherRepository;
import com.service.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatisticsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private VoucherRepository voucherRepository;
    @Mock
    private RecycleRequestRepository recycleRequestRepository;
    @Mock
    CountyClient countyClient;
    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    public void getCountyStatistics_normalFlow() {
        UserEntity userEntity1 = new UserEntity(1, "firstname", "lastname", "email", "pass", "Cluj", "city", RoleEnum.CLIENT);
        UserEntity userEntity3 = new UserEntity(3, "firstname", "lastname", "email", "pass", "Bihor", "city", RoleEnum.CLIENT);
        UserEntity userEntity4 = new UserEntity(4, "firstname", "lastname", "email", "pass", "Bihor", "city", RoleEnum.CLIENT);
        when(userRepository.findAllByRole(RoleEnum.CLIENT)).thenReturn(Arrays.asList(userEntity1, userEntity3, userEntity4));
        VoucherEntity voucherEntity1 = new VoucherEntity(); voucherEntity1.setClient(userEntity1);
        VoucherEntity voucherEntity2 = new VoucherEntity(); voucherEntity2.setClient(userEntity1);
        VoucherEntity voucherEntity3 = new VoucherEntity(); voucherEntity3.setClient(userEntity3);
        when(voucherRepository.findAll()).thenReturn(Arrays.asList(voucherEntity1, voucherEntity2, voucherEntity3));
        RecycleRequestEntity recycleRequestEntity1 = new RecycleRequestEntity(); recycleRequestEntity1.setClient(userEntity1); recycleRequestEntity1.setQuantity(100.0);
        RecycleRequestEntity recycleRequestEntity2 = new RecycleRequestEntity(); recycleRequestEntity2.setClient(userEntity3); recycleRequestEntity2.setQuantity(200.0);
        RecycleRequestEntity recycleRequestEntity3 = new RecycleRequestEntity(); recycleRequestEntity3.setClient(userEntity3); recycleRequestEntity3.setQuantity(300.0);
        when(recycleRequestRepository.findByStatus(StatusEnum.CONFIRMED)).thenReturn(Arrays.asList(recycleRequestEntity1, recycleRequestEntity2, recycleRequestEntity3));
        when(countyClient.getCounties()).thenReturn(Arrays.asList(new CountyDTO("CJ", "Cluj"), new CountyDTO("BH", "Bihor")));
        List<CountyStatisticsDTO> result = statisticsService.getCountyStatistics();
        CountyStatisticsDTO clujStatistics = result.stream().filter(statsDTO -> statsDTO.getCountyName().equals("Cluj")).findFirst().orElse(null);
        assertNotNull(clujStatistics);
        assertEquals(1, clujStatistics.getNoClients());
        assertEquals(2, clujStatistics.getNoVouchers());
        assertEquals(100.0, clujStatistics.getQuantity());
        CountyStatisticsDTO bihorStatistics = result.stream().filter(statsDTO -> statsDTO.getCountyName().equals("Bihor")).findFirst().orElse(null);
        assertNotNull(bihorStatistics);
        assertEquals(2, bihorStatistics.getNoClients());
        assertEquals(1, bihorStatistics.getNoVouchers());
        assertEquals(500.0, bihorStatistics.getQuantity());
    }
}
