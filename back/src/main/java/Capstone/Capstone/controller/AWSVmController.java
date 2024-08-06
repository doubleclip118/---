package Capstone.Capstone.controller;


import Capstone.Capstone.controller.dto.VmInfoDTO;
import Capstone.Capstone.controller.dto.VmInfoResponse;
import Capstone.Capstone.controller.dto.VmResponse;
import Capstone.Capstone.controller.dto.VmcreateResponse;
import Capstone.Capstone.service.AWSVmInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/vm/aws")
public class AWSVmController {
    private final AWSVmInfoService awsVmInfoService;

    public AWSVmController(AWSVmInfoService awsVmInfoService) {
        this.awsVmInfoService = awsVmInfoService;
    }

    @PostMapping()
    public ResponseEntity<VmInfoResponse> postVmInfo(@RequestBody VmInfoDTO vmInfoDTO){
        VmInfoResponse awsVmInfo = awsVmInfoService.createAWSVmInfo(vmInfoDTO);
        return ResponseEntity.ok(awsVmInfo);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInfo(@PathVariable("id")Long vmid){
        String vmInfoDTO = awsVmInfoService.deleteAWSVmInfo(vmid);
        return ResponseEntity.ok(vmInfoDTO);
    }

    @PostMapping("/con/{id}")
    public ResponseEntity<VmcreateResponse> createVm(@PathVariable("id")Long id){
        VmcreateResponse vm = awsVmInfoService.createVm(id);
        log.info("컨트롤러로 들어옴");
        return ResponseEntity.ok(vm);
    }

    @DeleteMapping("/con/{id}")
    public ResponseEntity<String> deleteVm(@PathVariable("id")Long id){
        awsVmInfoService.createVm(id);
        return ResponseEntity.ok("성공");
    }
}
