package org.tbk.vishy.web;

import com.github.theborakompanioni.openmrc.web.OpenMrcHttpRequestService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/openmrc")
public class VishyOpenMrcCtrl {

    private final OpenMrcHttpRequestService openMrcService;

    @Autowired
    public VishyOpenMrcCtrl(OpenMrcHttpRequestService openMrcService) {
        this.openMrcService = openMrcService;
    }

    @RequestMapping(value = "/consume", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "payload", dataType = "String", paramType = "body", required = true)
    })
    public ResponseEntity<Void> trackMapping(HttpServletRequest request) {
        try {
            HttpServletResponse response = openMrcService.apply(request);
            return ResponseEntity.status(response.getStatus()).build();
        } catch (RuntimeException | Error e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
