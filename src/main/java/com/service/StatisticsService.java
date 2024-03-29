package com.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.client.CountyClient;
import com.domain.dto.CountyDTO;
import com.domain.dto.CountyStatisticsDTO;
import com.domain.entity.RecycleRequestEntity;
import com.domain.entity.UserEntity;
import com.domain.enums.RoleEnum;
import com.domain.enums.StatusEnum;
import com.repo.RecycleRequestRepository;
import com.repo.UserRepository;
import com.repo.VoucherRepository;

@Service
public class StatisticsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private RecycleRequestRepository recycleRequestRepository;
    @Autowired
    private CountyClient countyClient;

    public List<CountyStatisticsDTO> getCountyStatistics() {
        List<CountyDTO> countyDTOs = countyClient.getCounties();
        Map<String, Long> clientsCount = getUsersCountForEachCounty();
        Map<String, Long> vouchersCount = getVouchersCountForEachCounty();
        Map<String, Double> quantityCount = getQuantityRecycledForEachCounty();
        return countyDTOs.stream()
                .map(countyDTO -> {
                    String countyAbbreviation = countyDTO.getCountyAbbreviation();
                    CountyStatisticsDTO countyStatisticsDTO = new CountyStatisticsDTO(countyDTO);
                    countyStatisticsDTO.setNoClients(clientsCount.get(countyAbbreviation) == null ? 0 : clientsCount.get(countyAbbreviation));
                    countyStatisticsDTO.setNoVouchers(vouchersCount.get(countyAbbreviation) == null ? 0 : vouchersCount.get(countyAbbreviation));
                    countyStatisticsDTO.setQuantity(quantityCount.get(countyAbbreviation) == null ? 0.0 : quantityCount.get(countyAbbreviation));
                    return countyStatisticsDTO;
                })
                .collect(Collectors.toList());
    }

    private Map<String, Long> getUsersCountForEachCounty() {
        return userRepository.findAllByRole(RoleEnum.CLIENT).stream()
                .collect(Collectors.groupingBy(UserEntity::getCounty, Collectors.counting()));
    }

    private Map<String, Long> getVouchersCountForEachCounty() {
        return voucherRepository.findAllByClientIdNotNull().stream()
                .collect(Collectors.groupingBy(voucherEntity -> voucherEntity.getClient().getCounty(), Collectors.counting()));
    }

    private Map<String, Double> getQuantityRecycledForEachCounty() {
        return recycleRequestRepository.findByStatus(StatusEnum.CONFIRMED).stream()
                .collect(Collectors.groupingBy(recycleRequestEntity -> recycleRequestEntity.getClient().getCounty(), Collectors.summingDouble(RecycleRequestEntity::getQuantity)));
    }
}
