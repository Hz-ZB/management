package cn.edu.sdu.java.server.controllers;

import cn.edu.sdu.java.server.payload.request.DataRequest;
import cn.edu.sdu.java.server.payload.response.DataResponse;
import cn.edu.sdu.java.server.payload.response.OptionItemList;
import cn.edu.sdu.java.server.services.HonourService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/*
联结前后端
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/honour")
public class HonourController {
    private final HonourService honourService;
    public HonourController(HonourService honourService){
        this.honourService=honourService;
    }

    @PostMapping("/getPersonItemOptionList")
    public OptionItemList getPersonItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        return honourService.getPersonItemOptionList(dataRequest);
    }

    @PostMapping("/getHonourItemOptionList")
    public OptionItemList getHonourItemOptionList(@Valid @RequestBody DataRequest dataRequest) {
        return honourService.getHonourItemOptionList(dataRequest);
    }

    @PostMapping("/getHonourList")
    public DataResponse getHonourList(@Valid @RequestBody DataRequest dataRequest) {
        return honourService.getHonourList(dataRequest);
    }
    @PostMapping("/honourSave")
    public DataResponse honourSave(@Valid @RequestBody DataRequest dataRequest) {
        return honourService.honourSave(dataRequest);
    }
    @PostMapping("/honourDelete")
    public DataResponse honourDelete(@Valid @RequestBody DataRequest dataRequest) {
        return honourService.honourDelete(dataRequest);
    }
}
