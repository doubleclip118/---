package Capstone.Capstone.service;

import Capstone.Capstone.domain.AWSCloudInfo;
import Capstone.Capstone.domain.AzureCloudInfo;
import Capstone.Capstone.domain.User;
import Capstone.Capstone.controller.dto.AWSInfoRequest;
import Capstone.Capstone.controller.dto.AWSInfoResponse;
import Capstone.Capstone.controller.dto.AzureInfoRequest;
import Capstone.Capstone.controller.dto.AzureInfoResponse;
import Capstone.Capstone.controller.dto.UserRequest;
import Capstone.Capstone.controller.dto.UserResponse;
import Capstone.Capstone.repository.AWSCloudInfoRepository;
import Capstone.Capstone.repository.AzureCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import Capstone.Capstone.utils.error.AWSCloudInfoNotFoundException;
import Capstone.Capstone.utils.error.AzureCloudInfoNotFoundException;
import Capstone.Capstone.utils.error.UserNotFoundException;
import Capstone.Capstone.utils.error.UserRegistrationException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final AWSCloudInfoRepository awsCloudInfoRepository;
    private final AzureCloudInfoRepository azureCloudInfoRepository;

    public UserService(UserRepository userRepository, AWSCloudInfoRepository awsCloudInfoRepository,
        AzureCloudInfoRepository azureCloudInfoRepository) {
        this.userRepository = userRepository;
        this.awsCloudInfoRepository = awsCloudInfoRepository;
        this.azureCloudInfoRepository = azureCloudInfoRepository;
    }


    public UserResponse registerUser(UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            throw new UserRegistrationException("user already exists");
        }
        User savedUser = userRepository.save(userRequest.UserconvertToEntity(userRequest));
        log.info("register User service");
        return savedUser.UserconvertToDTO(savedUser);

    }

    public UserResponse userLogin(UserRequest userRequest){
        Optional<User> existingUser = userRepository.findByUsername(userRequest.getUsername());
        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("user not found");
        }
        log.info("Login User service");
        return existingUser.get().UserconvertToDTO(existingUser.get());
    }
    @Transactional
    public AWSInfoResponse createAWSInfo(AWSInfoRequest awsInfoRequest){
        User user = userRepository.findById(awsInfoRequest.getUserId()).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        AWSCloudInfo awsCloudInfo = new AWSCloudInfo();
        awsCloudInfo.setDriverName(awsInfoRequest.getDriverName());
        awsCloudInfo.setProviderName(awsInfoRequest.getProviderName());
        awsCloudInfo.setDriverLibFileName(awsInfoRequest.getDriverLibFileName());
        awsCloudInfo.setCredentialName(awsInfoRequest.getCredentialName());
        awsCloudInfo.setCredentialAccessKey(awsInfoRequest.getCredentialAccessKey());
        awsCloudInfo.setCredentialAccessKeyVal(awsInfoRequest.getCredentialAccessKeyVal());
        awsCloudInfo.setCredentialSecretKey(awsInfoRequest.getCredentialSecretKey());
        awsCloudInfo.setCredentialSecretKeyVal(awsInfoRequest.getCredentialSecretKeyVal());
        awsCloudInfo.setRegionName(awsInfoRequest.getRegionName());
        awsCloudInfo.setRegionKey(awsInfoRequest.getRegionKey());
        awsCloudInfo.setRegionValue(awsInfoRequest.getRegionValue());
        awsCloudInfo.setZoneKey(awsInfoRequest.getZoneKey());
        awsCloudInfo.setZoneValue(awsInfoRequest.getZoneValue());
        awsCloudInfo.setConfigName(awsInfoRequest.getConfigName());

        awsCloudInfo.setUser(user);
        user.setAwsCloudInfo(awsCloudInfo);

        AWSCloudInfo savedAWSCloudInfo = awsCloudInfoRepository.save(awsCloudInfo);
        log.info("AWS INFO CREATE service");
        return new AWSInfoResponse(
            awsInfoRequest.getUserId(),
            savedAWSCloudInfo.getDriverName(),
            savedAWSCloudInfo.getProviderName(),
            savedAWSCloudInfo.getDriverLibFileName(),
            savedAWSCloudInfo.getCredentialName(),
            savedAWSCloudInfo.getCredentialAccessKey(),
            savedAWSCloudInfo.getCredentialAccessKeyVal(),
            savedAWSCloudInfo.getCredentialSecretKey(),
            savedAWSCloudInfo.getCredentialSecretKeyVal(),
            savedAWSCloudInfo.getRegionName(),
            savedAWSCloudInfo.getRegionKey(),
            savedAWSCloudInfo.getRegionValue(),
            savedAWSCloudInfo.getZoneKey(),
            savedAWSCloudInfo.getZoneValue(),
            savedAWSCloudInfo.getConfigName()
        );
    }
    @Transactional
    public AzureInfoResponse createAzureInfo(AzureInfoRequest azureInfoRequest) {
        User user = userRepository.findById(azureInfoRequest.getUserId()).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        AzureCloudInfo azureCloudInfo = new AzureCloudInfo();
        azureCloudInfo.setDriverName(azureInfoRequest.getDriverName());
        azureCloudInfo.setProviderName(azureInfoRequest.getProviderName());
        azureCloudInfo.setDriverLibFileName(azureInfoRequest.getDriverLibFileName());
        azureCloudInfo.setCredentialName(azureInfoRequest.getCredentialName());
        azureCloudInfo.setClientIdkey(azureInfoRequest.getClientIdkey());
        azureCloudInfo.setClientIdValue(azureInfoRequest.getClientIdValue());
        azureCloudInfo.setClientSecretKey(azureInfoRequest.getClientSecretKey());
        azureCloudInfo.setClientSecretValue(azureInfoRequest.getClientSecretValue());
        azureCloudInfo.setTenantIdKey(azureInfoRequest.getTenantIdKey());
        azureCloudInfo.setTenantIdValue(azureInfoRequest.getTenantIdValue());
        azureCloudInfo.setRegionName(azureInfoRequest.getRegionName());
        azureCloudInfo.setRegionKey(azureInfoRequest.getRegionKey());
        azureCloudInfo.setRegionValue(azureInfoRequest.getRegionValue());
        azureCloudInfo.setZoneKey(azureInfoRequest.getZoneKey());
        azureCloudInfo.setZoneValue(azureInfoRequest.getZoneValue());
        azureCloudInfo.setConfigName(azureInfoRequest.getConfigName());

        azureCloudInfo.setUser(user);
        user.setAzureCloudInfo(azureCloudInfo);

        AzureCloudInfo savedAzureCloudInfo = azureCloudInfoRepository.save(azureCloudInfo);
        log.info("AZURE INFO CREATE service");
        return new AzureInfoResponse(
            azureInfoRequest.getUserId(),
            savedAzureCloudInfo.getDriverName(),
            savedAzureCloudInfo.getProviderName(),
            savedAzureCloudInfo.getDriverLibFileName(),
            savedAzureCloudInfo.getCredentialName(),
            savedAzureCloudInfo.getClientIdkey(),
            savedAzureCloudInfo.getClientIdValue(),
            savedAzureCloudInfo.getClientSecretKey(),
            savedAzureCloudInfo.getClientSecretValue(),
            savedAzureCloudInfo.getTenantIdKey(),
            savedAzureCloudInfo.getTenantIdValue(),
            savedAzureCloudInfo.getRegionName(),
            savedAzureCloudInfo.getRegionKey(),
            savedAzureCloudInfo.getRegionValue(),
            savedAzureCloudInfo.getZoneKey(),
            savedAzureCloudInfo.getZoneValue(),
            savedAzureCloudInfo.getConfigName()
        );
    }

    public AWSInfoResponse getAWSInfo(Long id){
        User user = userRepository.findByUserIdWithAWSCloudInfo(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        if (user.getAwsCloudInfo() == null) {
            throw new AWSCloudInfoNotFoundException("AWS INFO Not Found");
        }
        return getAwsInfoResponse(user);
    }


    public AzureInfoResponse getAzureInfo(Long id){
        User user = userRepository.findByUserIdWithAzureCloudInfo(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        if (user.getAzureCloudInfo() == null) {
            throw new AzureCloudInfoNotFoundException("Azure INFO Not Found");
        }
        return getAzureInfoResponse(user);
    }

    @Transactional
    public String deleteAWSInfo(Long id){
        User user = userRepository.findByUserIdWithAWSCloudInfo(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        awsCloudInfoRepository.deleteById(user.getAwsCloudInfo().getId());
        user.removeAwsCloudInfo();

        return "성공";
    }
    @Transactional
    public String deleteAzureInfo(Long id){
        User user = userRepository.findByUserIdWithAzureCloudInfo(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        azureCloudInfoRepository.deleteById(user.getAzureCloudInfo().getId());
        user.removeAzureCloudInfo();
        return "성공";
    }
    @Transactional
    public AWSInfoResponse changeAWSInfo(Long id , AWSInfoRequest awsInfoRequest){
        User user = userRepository.findById( id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        user.getAwsCloudInfo().updateAWSInfo(awsInfoRequest);
        userRepository.save(user);
        return getAwsInfoResponse(user);
    }
    @Transactional
    public AzureInfoResponse changeAzureInfo(Long id, AzureInfoRequest azureInfoRequest){
        User user = userRepository.findById(id).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        user.getAzureCloudInfo().updateAzureInfo(azureInfoRequest);
        userRepository.save(user);
        return getAzureInfoResponse(user);
    }

    private AWSInfoResponse getAwsInfoResponse(User user) {
        return new AWSInfoResponse(
            user.getId(),
            user.getAwsCloudInfo().getDriverName(),
            user.getAwsCloudInfo().getProviderName(),
            user.getAwsCloudInfo().getDriverLibFileName(),
            user.getAwsCloudInfo().getCredentialName(),
            user.getAwsCloudInfo().getCredentialAccessKey(),
            user.getAwsCloudInfo().getCredentialAccessKeyVal(),
            user.getAwsCloudInfo().getCredentialSecretKey(),
            user.getAwsCloudInfo().getCredentialSecretKeyVal(),
            user.getAwsCloudInfo().getRegionName(),
            user.getAwsCloudInfo().getRegionKey(),
            user.getAwsCloudInfo().getRegionValue(),
            user.getAwsCloudInfo().getZoneKey(),
            user.getAwsCloudInfo().getZoneValue(),
            user.getAwsCloudInfo().getConfigName()
        );
    }

    private AzureInfoResponse getAzureInfoResponse(User user) {
        return new AzureInfoResponse(
            user.getId(),
            user.getAzureCloudInfo().getDriverName(),
            user.getAzureCloudInfo().getProviderName(),
            user.getAzureCloudInfo().getDriverLibFileName(),
            user.getAzureCloudInfo().getCredentialName(),
            user.getAzureCloudInfo().getClientIdkey(),
            user.getAzureCloudInfo().getClientIdValue(),
            user.getAzureCloudInfo().getClientSecretKey(),
            user.getAzureCloudInfo().getClientSecretValue(),
            user.getAzureCloudInfo().getTenantIdKey(),
            user.getAzureCloudInfo().getTenantIdValue(),
            user.getAzureCloudInfo().getRegionName(),
            user.getAzureCloudInfo().getRegionKey(),
            user.getAzureCloudInfo().getRegionValue(),
            user.getAzureCloudInfo().getZoneKey(),
            user.getAzureCloudInfo().getZoneValue(),
            user.getAzureCloudInfo().getConfigName()
        );
    }

}
