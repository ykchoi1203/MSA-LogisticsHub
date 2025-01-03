package com.logistcshub.user.application.config;

import com.github.ksuid.Ksuid;
import com.logistcshub.user.application.client.HubClient;
import com.logistcshub.user.application.client.dto.HubResponseDto;
import com.logistcshub.user.application.dtos.HubDto;
import com.logistcshub.user.common.response.SuccessResponse;
import com.logistcshub.user.domain.model.deliveryManager.DeliveryManager;
import com.logistcshub.user.domain.model.deliveryManager.DeliveryManagerType;
import com.logistcshub.user.domain.model.deliveryManager.HubManager;
import com.logistcshub.user.domain.model.user.User;
import com.logistcshub.user.domain.model.user.UserRoleEnum;
import com.logistcshub.user.infrastructure.repository.DeliveryManagerRepository;
import com.logistcshub.user.infrastructure.repository.HubManagerRepository;
import com.logistcshub.user.infrastructure.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.logistcshub.user.domain.model.deliveryManager.DeliveryManagerType.COMPANY_PIC;
import static com.logistcshub.user.domain.model.deliveryManager.DeliveryManagerType.HUB_PIC;

@Component
@RequiredArgsConstructor
public class UserInserter {
    private final UserRepository userRepository;
    private final HubManagerRepository hubManagerRepository;
    private final HubClient hubClient;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryManagerRepository deliveryManagerRepository;

    @PostConstruct
    @Transactional
    public void init() {
        int i = 1;
        String[] slackIds = {"chldudrms94", "U085DQ6BA02", "U08574P7CEA", "U085DNWN1SN"};
        UserRoleEnum[] roles = UserRoleEnum.values();
        int roleIdx = 0;

        User user = userRepository.findById(200L).orElse(null);

        if(user != null) {
            return;
        }

        List<User> saveUsers = new ArrayList<>();
        //saveUsers.add(User.create("user" + i, passwordEncoder.encode( "password") + i, "useremail" + i + "@naver.com", "010-0000-0000", "chldudrms94" ,UserRoleEnum.MASTER ));

        // 유저 등록
        while(i <= 250) {
            saveUsers.add(User.create("user" + i, passwordEncoder.encode( "password" + i), "useremail" + i + "@naver.com", "010-0000-0000", slackIds[i % 4] , roles[roleIdx]));

            if(i == 2 || i == 4 || i == 230) {
                roleIdx++;
            }
            i++;
        }

        saveUsers = userRepository.saveAll(saveUsers);

        List<User> deliveryManagers = saveUsers.stream().filter(saveuser -> saveuser.getRole().equals(UserRoleEnum.DELIVERY_MANAGER)).toList();

        List<User> hubManagers = saveUsers.stream().filter(saveuser -> saveuser.getRole().equals(UserRoleEnum.HUB_MANAGER)).toList();

        List<HubResponseDto> hubList  = hubClient.getAllHubs("MASTER").getBody().data();

        List<HubManager> saveManagers = new ArrayList<>();
        List<DeliveryManager> saveDeliveryManagers = new ArrayList<>();
        List<DeliveryManager> saveHubDeliveryManagers = new ArrayList<>();

        int idx = 0;
        int deliveryManagerIdx = 0;
        int hubManagerIdx = 0;

        for (HubResponseDto hubDto : hubList) {
            saveManagers.add(HubManager.create(hubManagers.get(hubManagerIdx), hubDto.id()));
            hubManagerIdx++;
            if(idx < 10) {
                saveHubDeliveryManagers.add(DeliveryManager.from(Ksuid.newKsuid().toString(), deliveryManagers.get(deliveryManagerIdx).getId(),null,  HUB_PIC));

                deliveryManagerIdx++;
                idx++;
            }
            for(int j = 0; j < 10; j++) {
                saveDeliveryManagers.add(DeliveryManager.from(Ksuid.newKsuid().toString(), deliveryManagers.get(deliveryManagerIdx).getId(), hubDto.id(), COMPANY_PIC));
                deliveryManagerIdx++;
            }
        }

        deliveryManagerRepository.saveAll(saveDeliveryManagers);
        deliveryManagerRepository.saveAll(saveHubDeliveryManagers);

        hubManagerRepository.saveAll(saveManagers);

    }
}