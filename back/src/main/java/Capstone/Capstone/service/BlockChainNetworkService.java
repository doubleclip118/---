package Capstone.Capstone.service;

import Capstone.Capstone.controller.dto.BlockChainNetworkRequest;
import Capstone.Capstone.controller.dto.BlockChainNetworkResponse;
import Capstone.Capstone.domain.AWSVmInfo;
import Capstone.Capstone.repository.AWSVmInfoRepository;
import Capstone.Capstone.repository.AzureVmInfoRepository;
import Capstone.Capstone.repository.OpenstackCloudInfoRepository;
import Capstone.Capstone.repository.UserRepository;
import com.jcraft.jsch.Session;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BlockChainNetworkService {

    private static final Logger logger = LoggerFactory.getLogger(BlockChainNetworkService.class);

    private final SSHConnector sshConnector;
    private final AWSVmInfoRepository awsVmInfoRepository;
    private final AzureVmInfoRepository azureVmInfoRepository;
    private final OpenstackCloudInfoRepository openstackCloudInfoRepository;
    private final UserRepository userRepository;

    public BlockChainNetworkService(SSHConnector sshConnector,
        AWSVmInfoRepository awsVmInfoRepository,
        AzureVmInfoRepository azureVmInfoRepository,
        OpenstackCloudInfoRepository openstackCloudInfoRepository,
        UserRepository userRepository) {
        this.sshConnector = sshConnector;
        this.awsVmInfoRepository = awsVmInfoRepository;
        this.azureVmInfoRepository = azureVmInfoRepository;
        this.openstackCloudInfoRepository = openstackCloudInfoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void connectToEC2Instance(Long vmId) {
        logger.info("Attempting to connect to EC2 instance with ID: {}", vmId);
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));

        String privateKey = vmInfo.getSecretkey();
        String ipAddress = vmInfo.getIp();

        Session session = null;
        try {
            session = sshConnector.connectToEC2(privateKey, ipAddress);
            logger.info("Successfully connected to EC2 instance: {}", ipAddress);

            String command = "ls -al";
            String result = sshConnector.executeCommand(session, command);
            logger.info("Command result: {}", result);

        } catch (Exception e) {
            logger.error("Failed to connect to EC2 instance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to connect to EC2 instance", e);
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    @Transactional
    public void sftpToEC2Instance(Long vmId) {
        logger.info("Attempting SFTP to EC2 instance with ID: {}", vmId);
        AWSVmInfo vmInfo = awsVmInfoRepository.findById(vmId)
            .orElseThrow(() -> new RuntimeException("VM not found with ID: " + vmId));

        String privateKey = vmInfo.getSecretkey();
        String ipAddress = vmInfo.getIp();

        Session session = null;
        try {
            session = sshConnector.connectToEC2(privateKey, ipAddress);
            logger.info("Successfully connected to EC2 instance: {}", ipAddress);

            sshConnector.sendPemKeyViaSftp(session, privateKey);
            logger.info("Successfully sent PEM key via SFTP");

        } catch (Exception e) {
            logger.error("Failed SFTP to EC2 instance: {}", e.getMessage(), e);
            throw new RuntimeException("Failed SFTP to EC2 instance", e);
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    @Transactional
    public BlockChainNetworkResponse executeStartupScript(BlockChainNetworkRequest network) {
        logger.info("Executing startup script for network: {}", network.getNetworkName());
        try {
            setupCAVM(network);
            setupORG1VM(network);

            logger.info("Startup script executed successfully for network: {}", network.getNetworkName());
            return new BlockChainNetworkResponse(network.getUserId(), network.getNetworkName());
        } catch (Exception e) {
            logger.error("Failed to execute startup script for network: {}", network.getNetworkName(), e);
            throw new RuntimeException("Failed to execute startup script", e);
        }
    }

    private void setupCAVM(BlockChainNetworkRequest network) throws Exception {
        logger.info("Setting up CA VM for network: {}", network.getNetworkName());
        Session session = null;
        try {
            session = sshConnector.connectToEC2(network.getCaSecretKey(), network.getCaIP());

            executeCommand(session, "curl -L -o $PWD/startup-ca.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-ca.sh");
            executeCommand(session, "chmod +x startup-ca.sh");
            executeCommand(session, "./startup-ca.sh " + network.getCaIP());

            logger.info("CA VM setup completed for network: {}", network.getNetworkName());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    private void setupORG1VM(BlockChainNetworkRequest network) throws Exception {
        logger.info("Setting up ORG1 VM for network: {}", network.getNetworkName());
        Session session = null;
        try {
            session = sshConnector.connectToEC2(network.getCaSecretKey(), network.getOrgIP());

            sshConnector.sendPemKeyViaSftp(session, network.getCaSecretKey());
            executeCommand(session, "chmod 400 temp.pem");

            executeCommand(session, "curl -L -o $PWD/startup-org1.sh https://raw.githubusercontent.com/okcdbu/kkoejoejoe-script-vm/main/startup-org1.sh");
            executeCommand(session, "chmod +x startup-org1.sh");
            executeCommand(session, "./startup-org1.sh " + network.getCaIP());

            logger.info("ORG1 VM setup completed for network: {}", network.getNetworkName());
        } finally {
            sshConnector.disconnectFromEC2(session);
        }
    }

    private void executeCommand(Session session, String command) throws Exception {
        logger.debug("Executing command: {}", command);
        String result = sshConnector.executeCommand(session, command);
        logger.debug("Command result: {}", result);
    }
}
