package cn.edu.ctbu.gmsbackend.common;

import cn.edu.ctbu.gmsbackend.common.ApiResponse;
import cn.edu.ctbu.gmsbackend.repository.OfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermController {

    private final OfferingRepository offeringRepository;

    @GetMapping
    public ApiResponse<List<String>> terms() {
        return ApiResponse.ok(offeringRepository.findDistinctTerms());
    }
}
